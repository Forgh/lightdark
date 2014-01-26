package com.me.lightdark.controleurs;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.me.lightdark.modeles.Case;
//import com.me.lightdark.modeles.Case;
import com.me.lightdark.modeles.Dark;
import com.me.lightdark.modeles.Form;
import com.me.lightdark.modeles.Light;
import com.me.lightdark.modeles.Monde;
import com.me.lightdark.modeles.Perso;
import com.me.lightdark.modeles.Perso.Etat;
import com.me.lightdark.modeles.Perso.direction;

public class ControlerPerso {

	private Monde monde;
	private Perso perso;
	private Array<Rectangle> collision;
	private Array<Case> arrives;
	
	private Sound bruitEpee = Gdx.audio.newSound(Gdx.files.internal("sound/epees.wav"));
	private Sound bruitOmbre = Gdx.audio.newSound(Gdx.files.internal("sound/ombre.wav"));
	private Sound bruitProj = Gdx.audio.newSound(Gdx.files.internal("sound/projectile.wav"));

	
	Vector2 directionTir;
	Vector2 cibleTir;
	boolean charged;//Si le tir est chargé ou non
	boolean attacking;//Si le Perso se prépare à attaquer ou non (charge un tir, arme son épée par ex.)
	
	
	enum Touches {
		GAUCHE, DROITE, HAUT, BAS, FEU//, EPEE
	}
	
	static Map<Touches, Boolean> touches = new HashMap<Touches, Boolean>();
	static {
		touches.put(Touches.GAUCHE, false);
		touches.put(Touches.HAUT, false);
		touches.put(Touches.DROITE, false);
		touches.put(Touches.BAS, false);
		touches.put(Touches.FEU, false);
	};
	
	static Touches toucheActuHoz; // ! rustine !
	static Touches toucheActuVer; // ! rustine !
	
	public ControlerPerso(Monde monde) {
		this.monde = monde;
		this.perso = monde.getPerso();
		this.attacking = false;
		this.collision = new Array<Rectangle>();
		this.arrives = new Array<Case>();
		this.directionTir = new Vector2();
	
		//ici on charge la map des collisions 
	}
	
	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
                return new Rectangle();
        }
};


	private void chargerCollision(){
		collision.clear();
		for(int x = 0;x<=monde.getNiveau().getLargeur();x++){
			for(int y= 0;y<=monde.getNiveau().getHauteur();y++){
				if (monde.getNiveau().getCollision(x, y) != null){ // (x>=dX) && (x<= fX) && (y>=dY) && (y<= fY)){
					collision.add(monde.getNiveau().getCollision(x, y));
				}
				
				else if (perso.getForm() == Form.SHADOWFORM){
					if(monde.getNiveau().getCollisionWithLight(x, y) != null && !(perso.getEtat()==Dark.TAMING)){ //Si collision joueur-lumière (hors taming)
						collision.add(monde.getNiveau().getCollisionWithLight(x, y));
				}
			}
		}
		for(int i=0;i<monde.getAnime().size;i++){
				if(monde.getAnime().get(i)!=null){
					collision.add(monde.getAnime().get(i).getCadre());
				}
			}
	}
		
	}
	
	/*Définis par défaut les cases qui permettent au personnage d'arriver sur un micro-monde et d'en sortir*/
	private void chargerArrives(){
		arrives.clear();
		for(int x = 0;x<=monde.getNiveau().getLargeur();x++){
			for(int y= 0;y<=monde.getNiveau().getHauteur();y++){
				if (monde.getNiveau().get(x, y) != null){ // (x>=dX) && (x<= fX) && (y>=dY) && (y<= fY)){
					arrives.add(monde.getNiveau().get(x, y));
				}
			}
		}
	}
		


	public void gauchePresse() {
		touches.get(touches.put(Touches.GAUCHE, true));
		toucheActuHoz = Touches.GAUCHE;
	}
	
	public void droitPresse() {
		touches.get(touches.put(Touches.DROITE, true));
		toucheActuHoz = Touches.DROITE;
	}
	
	public void hautPresse() {
		touches.get(touches.put(Touches.HAUT, true));
		toucheActuVer = Touches.HAUT;
	}
	
	public void basPresse() {
		touches.get(touches.put(Touches.BAS, true));
		toucheActuVer = Touches.BAS;
	}
	
	
	public void feuPresse(int x, int y, int w, int h) {
		touches.get(touches.put(Touches.FEU, true));
		
		float posX = ((  (this.monde.getNiveau().getLargeur() / (float) w) * (float) x));
		float posY = (this.monde.getNiveau().getHauteur() - ((this.monde.getNiveau().getHauteur() / (float) h) * (float) y));
				
		Vector2 v = new Vector2(posX, posY);

		v.sub(this.perso.getPosition());

		
		float angle = (float) Math.atan2(v.y, v.x);
		
		v.x =(float)Math.cos(angle);
		v.y =(float)Math.sin(angle);
		
		
		 //ï¿½ verif que la precision numerique des float ne tombe pas sur 0.0 
		 
		directionTir.x = (float) (v.x != 0.0 ? v.x : 0.001); // on evite de passer par zï¿½ro (bloquant)
		directionTir.y =  (float) (v.y != 0.0 ? v.y : 0.001);


		cibleTir = new Vector2(posX, posY);
		
	}
	
	
	
	public void gaucheRelache() {
		touches.get(touches.put(Touches.GAUCHE, false));
	}
	
	public void droitRelache() {
		touches.get(touches.put(Touches.DROITE, false));
	}
	
	public void hautRelache() {
		touches.get(touches.put(Touches.HAUT, false));
	}
	
	public void basRelache() {
		touches.get(touches.put(Touches.BAS, false));
	}
	
	/*@Param les coordonnées x et y, largeur et hauteur du niveau, et si le tir est chargé*/
	public void feuRelache(int x, int y, int w,int h,  boolean charged) {
		
		this.charged=charged;
		
		float posX = ((  (this.monde.getNiveau().getLargeur() / (float) w) * (float) x));
		float posY = (this.monde.getNiveau().getHauteur() - ((this.monde.getNiveau().getHauteur() / (float) h) * (float) y));
				
		Vector2 v = new Vector2(posX, posY);

		v.sub(this.perso.getPosition());

		
		float angle = (float) Math.atan2(v.y, v.x);
		
		v.x =(float)Math.cos(angle);
		v.y =(float)Math.sin(angle);
		
		/*
		 * ï¿½ verif que la precision numerique des float ne tombe pas sur 0.0 
		 */
		directionTir.x = (float) (v.x != 0.0 ? v.x : 0.001); // on evite de passer par zï¿½ro (bloquant)
		directionTir.y =  (float) (v.y != 0.0 ? v.y : 0.001);


		cibleTir = new Vector2(posX, posY);
		
		touches.get(touches.put(Touches.FEU, false));
		
	}
	
	
	
	public void update(float delta) {
		this.perso = monde.getPerso();
		gererEntrees();
		gererArrives(delta);
		gererCollision(delta);
		perso.update(delta);
	}
	
	public void gererCollision(float delta){
		perso.getRapidite().scl(delta); // on travaille au ralenti
		
		float approx = 0f;
		Rectangle persoRect = rectPool.obtain();
		persoRect.set(perso.getCadre().x - approx, perso.getCadre().y - approx, perso.getCadre().height - approx, perso.getCadre().width - approx);
		
		this.chargerCollision();
		persoRect.x += perso.getRapidite().x;
		//persoRect.y += perso.getRapidite().y;
		
		int i = 0;
		boolean ok = true;
		while (i< collision.size && ok){
			if (collision.get(i) != null && persoRect.overlaps(collision.get(i))){
				perso.getRapidite().x=0;
				ok = false;
			}
			i++;
		}

		if (ok && (persoRect.x<0 || persoRect.x>this.monde.getNiveau().getLargeur())){
			perso.getRapidite().x = 0;
		}
		
		persoRect.x = perso.getCadre().x;
		persoRect.y += perso.getRapidite().y;
		
		i = 0;
		boolean ok2 = true;
		while (i< collision.size && ok2){
			if (collision.get(i) != null && persoRect.overlaps(collision.get(i))){
				perso.getRapidite().y=0;
				ok2 = false;
			}
			i++;
		}
		
		if (ok && (persoRect.y<0 || persoRect.y>this.monde.getNiveau().getHauteur())){
			perso.getRapidite().y = 0;
		}
		

		perso.getRapidite().scl(1/delta); // on restaure la vitesse
		
	}
	
	public void gererArrives(float delta){
		perso.getRapidite().scl(delta); // on travaille au ralenti
		
		
		Rectangle persoRect = rectPool.obtain();
		persoRect.set(perso.getCadre());
		
		this.chargerArrives();
		persoRect.x += perso.getRapidite().x;
		persoRect.y += perso.getRapidite().y;
		
		int i = 0;
		boolean ok = true;
		while (i< arrives.size && ok){
			if (arrives.get(i) != null && persoRect.overlaps(arrives.get(i).getCadre())){
				arrives.get(i).arrive();
				//System.out.println("toto" +arrives.get(i).getPosition().x + " : "+  +arrives.get(i).getPosition().y );
				ok = false;
			}
			i++;
		}

		perso.getRapidite().scl(1/delta); // on restaure la vitesse
	}
	
	private void gererEntrees() {
		// ici on modifie l'état du perso
		if (touches.get(Touches.HAUT)  && toucheActuVer == Touches.HAUT) {
				perso.setDirection(direction.HAUT);
				perso.getRapidite().y = Perso.VITESSE;
			
		}
		if (touches.get(Touches.BAS)  && toucheActuVer == Touches.BAS) {	
				perso.setDirection(direction.BAS);
				perso.getRapidite().y = -Perso.VITESSE;
			
		}
		
		if (touches.get(Touches.GAUCHE) && toucheActuHoz == Touches.GAUCHE) {
				perso.setDirection(direction.GAUCHE);
				perso.getRapidite().x = -Perso.VITESSE;
			
		}
		if (touches.get(Touches.DROITE) && toucheActuHoz == Touches.DROITE) {
				perso.setDirection(direction.DROITE);
				perso.getRapidite().x = Perso.VITESSE;
			
		}
		
		
		if (!touches.get(Touches.GAUCHE) && !touches.get(Touches.DROITE)){
			perso.getRapidite().x = 0;
		}
		
		if (!touches.get(Touches.HAUT) && !touches.get(Touches.BAS)){
			perso.getRapidite().y = 0;
		}
		
		if (perso.getRapidite().x != 0f || perso.getRapidite().y != 0f){
			if (perso.getForm() == Form.LIGHTFORM)
					perso.changerEtat(Light.MARCHANT);
		}
		else{
			if (perso.getForm() == Form.LIGHTFORM)
				perso.changerEtat(Light.INACTIF);
		}
		
		if (touches.get(Touches.FEU) && directionTir.x !=0 && directionTir.y !=0 && perso.getEtat()!=Dark.GRABBING){
			//si on appuie sur la touche de feu, que la direction du tir est correcte et que le Shadow-player ne tire pas déjà
			attacking = true;
		}
		
		if(!touches.get(Touches.FEU) && attacking){
			//Si la touche de feu est relâchée après un appui dans le but d'effectuer une attaque
			attacking = false;//reset pour le prochain appui
			
			
			if(perso.getForm()==Form.SHADOWFORM && !charged && perso.getEtat()!=Dark.TAMING) {//Si en shadowForm en tir non chargé et hors taming
				perso.changerEtat(Dark.GRABBING);
				monde.lancerProjectile(new Vector2(directionTir), new Vector2(cibleTir));
				this.bruitOmbre.play();
			}
			
			if(perso.getForm()==Form.SHADOWFORM && !charged && perso.getEtat()==Dark.TAMING) {//Si en shadowForm en tir non chargé et en taming
				//NB : ne pas sortir du Taming
				if(monde.getProjectile().size==0)
					monde.lancerProjectile(new Vector2(directionTir), new Vector2(cibleTir));
					
			}
			
			
			
			if(perso.getForm()==Form.SHADOWFORM && charged && perso.getEtat()==Dark.TAMING) {//Si en shadowForm en tir chargé
				//System.out.println("[DEBUG] Lancement d'un tir chargé depuis l'animal : Compétence à activer");
				perso.getAnimal().demarrerCompetence();
			}
			
			
			if(perso.getForm()==Form.LIGHTFORM && !charged){//Si en LightForm en tir non chargé
				
				monde.frapperEpee(new Vector2(directionTir), new Vector2(cibleTir));
				this.bruitEpee.play();
			}
			if(perso.getForm()==Form.LIGHTFORM && charged){//Si tir en LightForm et tir chargé
				monde.lancerProjectile(new Vector2(directionTir), new Vector2(cibleTir));
				this.bruitProj.play();
			}
			//Finalement, reset les coordonnées du tir pour un prochain appui, le tir est de toute façon déchargé
			directionTir.x = 0;
			directionTir.y = 0;
			charged = false;
			
		}
		
	}
}
