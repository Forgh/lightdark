package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Perso {
	
	public interface Etat{} // ici polymorphisme pour light/shadow

	public static final float VITESSE_DEF = 4f;
	public static float VITESSE;	// vitesse par unite de temps sur une unite d'espace
	public static final float TAILLE = 1f; // une demi unite
	//public static final int max_health = 75;
	public static final int max_health_light= 3; //vie max light
	public static final int max_health_shadow= 1; //vie max shadow
	
	public final int puissance = 30;
	public final int puissanceMini = 20;
	
	private boolean taming_detectable;
	
	private int health; //niveau de vie actuel
	private int healthLight; //niveau de vie sauvegardé actuel en light
	private int healthShadow; //niveau de vie sauvegardé en shadow

	
	private Vector2 position = new Vector2();
	private Vector2 rapidite = new Vector2();
	
	private Form form;
	
	private Enum<?> etat;
	
	private Anime animal = null;
	
	// normalement d'apres le cours on initialise dans le constructeur mais ici ï¿½a revient pareil
	private Rectangle cadre = new Rectangle();
	
	private float tempsAnime = 0;
	
	private Monde monde;
	
	private direction direction;
	
	public enum direction  {
		HAUT, HAUT_GAUCHE, HAUT_DROITE,
		BAS, BAS_GAUCHE, BAS_DROITE,
		GAUCHE, DROITE,
	}
	
	
	public Perso(Vector2 position, Monde m) {
		this.position = position;
		this.cadre.setPosition(position);
		this.cadre.height = TAILLE/2;
		this.cadre.width = TAILLE;
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
		position.add(rapidite.cpy().scl(delta)); //?
		cadre.setPosition(position);
	}

	public void switchForm (){
		if(this.getForm()==Form.LIGHTFORM){
			this.healthLight=this.health; //on sauvegarde la santé avant le switch
			this.setForm(Form.SHADOWFORM);
			this.health=healthShadow;
		}
		else{
			this.setForm(Form.LIGHTFORM);
			this.health=healthLight;
		}
		System.out.println(this.getForm());
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

}
