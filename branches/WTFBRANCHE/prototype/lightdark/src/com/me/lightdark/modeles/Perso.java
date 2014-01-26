package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Perso {
	
	public interface Etat{} // ici polymorphisme pour light/shadow

	public static final float VITESSE_DEF = 4f;
	public static float VITESSE;	// vitesse par unite de temps sur une unite d'espace
	public static final float TAILLE = 0.9f; // une demi unite
	//public static final int max_health = 75;
	public static final int max_health_light= 3; //vie max light
	public static final int max_health_shadow= 1; //vie max shadow
	
	public final int puissance = 30;
	public final int puissanceMini = 20;
	
	private boolean taming_detectable;
	
	private int health; //niveau de vie actuel
	private int healthLight; //niveau de vie sauvegard� actuel en light
	private int healthShadow; //niveau de vie sauvegard� en shadow

	
	private Vector2 position = new Vector2();
	private Vector2 rapidite = new Vector2();
	private Vector2 transition = null;//La destination de transition  du joueur (si null -> pas de transition en cours)
	
	private Form form;
	
	private Enum<?> etat;
	
	private Anime animal = null;
	
	// normalement d'apres le cours on initialise dans le constructeur mais ici �a revient pareil
	private Rectangle cadre = new Rectangle();
	
	private float tempsAnime = 0;
	
	private Monde monde;
	
	private direction direction;
	private Vector2 cadre_pos;
	private float correctY;
	private float correctX;
	
	public enum direction  {
		HAUT, HAUT_GAUCHE, HAUT_DROITE,
		BAS, BAS_GAUCHE, BAS_DROITE,
		GAUCHE, DROITE,
	}
	
	
	public Perso(Vector2 position, Monde m) {
		this.position = position;
		this.cadre_pos = new Vector2(position);
		this.correctX = (this.TAILLE / 2) - (this.TAILLE / 4);
		this.correctY = (this.TAILLE / 8);
		this.cadre.height = TAILLE/2;
		this.cadre.width = (this.TAILLE/2) + (this.TAILLE / 4);
		this.healthLight=max_health_light;
		this.healthShadow=max_health_shadow;
		this.VITESSE = this.VITESSE_DEF;
		taming_detectable = false;
		this.monde = m;
		this.direction = direction.DROITE;
	}
	
	
	// **** GETTERS ****
	public Monde getMonde(){
		return monde;
	}
	
	public int puissance(){
		return this.puissance;
	}
	public int puissanceMini(){
		return puissanceMini;
	}
	public Vector2 getPosition() {
		return position;
	}

	public Rectangle getCadre() {
		return cadre;
	}
	
	public Form getForm() {
		// TODO Auto-generated method stub
		return form;
	}
	
	public Enum<?> getEtat(){
		return this.etat;
	}
	
	public void setVitesse(float f){
		this.VITESSE = f;
	}
	
	public void resetVitesse(){
		this.VITESSE = this.VITESSE_DEF;
	}
	public <E> void changerEtat(Enum<?> e){
		this.etat = e;
	}
	
	public int getHealth(){
		return this.health;
	}
	
	public Anime getAnimal(){
		return animal;
	}
	
	public void setAnimal(Anime anime){
		animal = anime;
	}
	
	
	public Vector2 getRapidite(){
		return this.rapidite;
	}
	
	public direction getDirection(){
		return this.direction;
	}
	
	public void setDirection(direction d){
		direction = d;
	}
	
	public void setRapidite(Vector2 v){
		this.rapidite = v;
	}
	
	public void setPosition(Vector2 v){
		
		this.position=v;
	}
	
	public boolean isTamingDetectable(){
		return this.taming_detectable;
	}
	
	public void setTamingDetectable(boolean value){
		this.taming_detectable = value;
	}
	public void setForm(Form f){
		this.form=f;
		if(this.form==Form.SHADOWFORM)
			this.health=this.healthShadow;
		else
			this.health=this.healthLight;
	}
	
	public void healthUp(int puissance){
		health++;
	}
	
	public void refill(){
		this.healthLight=max_health_light;
		this.healthShadow=max_health_shadow;
	}
	
	public void healthDown(){
		health--;
		System.out.println("HEALTH DOWN");
	}
	
	
	public float temps(){
		return tempsAnime;
	}

	public void update(float delta) {
		tempsAnime += delta;
		if (transition==null)
			position.add(rapidite.cpy().scl(delta)); //?
		else//Si transistion en cours
			transit();
		cadre.setPosition(position.x + correctX, position.y + correctY);
	}

	public void switchForm (){
		if(this.getForm()==Form.LIGHTFORM){
			this.healthLight=this.health; //on sauvegarde la sant� avant le switch
			this.setForm(Form.SHADOWFORM);
			this.health=healthShadow;
		}
		else{
			this.setForm(Form.LIGHTFORM);
			this.health=healthLight;
		}
		System.out.println(this.getForm());
		this.toIdle();
	}
	
	public void toWalking(){
		if(this.form==Form.LIGHTFORM)
			this.etat=Light.MARCHANT;
		else if (this.getAnimal()==null) {
			this.etat=Dark.SHADOWWALKING;
		}
	}
	
	public void toIdle(){
		if(this.form==Form.LIGHTFORM)
			this.etat=Light.INACTIF;
		else if (this.getAnimal()==null) {
			this.etat=Dark.IDLE;
		}
	}
	
	/*@param la destination du joueur
	 * �tablit les coordonn�es de destination de la transition du joueur
	 * l'attribut transition devient un vecteur d�placement*/
	public void transit(Vector2 destination){
		transition = destination;
		
	}
	
	/*Se charge de d�placer le perso d'un cran vers sa destination de transition
	 * S'il est arriv�, passe transition � null*/
	public void transit(){
		//System.out.println("transition "+position+" --> "+transition);
		Vector2 diff = position.cpy().sub(transition);
		//System.out.println("diff : " +diff);
		if(Math.abs(diff.x)<0.01f && Math.abs(diff.y)<0.01f){//Si arriv�, arr�ter la transition
			System.out.println("Fin de transition");
			
			transition = null;
		}
		else{ 
			Vector2 step = transition.cpy().sub(position).div(10);//le vecteur d�placement, divis� par le nombre d'�tapes
			//System.out.println("Pos Joueur : "+position+", Destination de transition :"+ transition+"\nVecteur deplacement : "+step);
					 
			position.add(step);
		}
	}

}