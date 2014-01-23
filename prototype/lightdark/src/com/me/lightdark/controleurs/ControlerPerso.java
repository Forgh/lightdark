package com.me.lightdark.controleurs;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
//import com.me.lightdark.modeles.Case;
import com.me.lightdark.modeles.Dark;
import com.me.lightdark.modeles.Form;
import com.me.lightdark.modeles.Light;
import com.me.lightdark.modeles.Monde;
import com.me.lightdark.modeles.Perso;
import com.me.lightdark.modeles.Perso.Etat;

public class ControlerPerso {

	private Monde monde;
	private Perso perso;
	private Array<Rectangle> collision;
	
	Vector2 directionTir;
	Vector2 cibleTir;
	boolean charged;//Si le tir est chargé ou non
	
	enum direction  {
		HAUT, HAUT_GAUCHE, HAUT_DROITE,
		BAS, BAS_GAUCHE, BAS_DROITE,
		GAUCHE, DROITE,
	}
	
	enum Touches {
		GAUCHE, DROITE, HAUT, BAS, FEU, EPEE
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
		this.collision = new Array<Rectangle>();
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

		//float negX = (v.x<0f ? -1f : 1f);
		//float negY = (v.y<0f ? -1f : 1f);
		
		float angle = (float) Math.atan2(v.y, v.x);
		
		v.x =(float)Math.cos(angle);
		v.y =(float)Math.sin(angle);
		
		/*
		 * ï¿½ verif que la precision numerique des float ne tombe pas sur 0.0 
		 */
		directionTir.x = (float) (v.x != 0.0 ? v.x : 0.001); // on evite de passer par zï¿½ro (bloquant)
		directionTir.y =  (float) (v.y != 0.0 ? v.y : 0.001);


		cibleTir = new Vector2(posX, posY);
	}
	
	public void sourisDroitPresse(int x, int y, int w, int h) {
		//NB : Fusionné avec FeuPresse
		//touches.get(touches.put(Touches.EPEE, true));
		
		
		/*float posX = ((  (this.monde.getNiveau().getLargeur() / (float) w) * (float) x));
		float posY = (this.monde.getNiveau().getHauteur() - ((this.monde.getNiveau().getHauteur() / (float) h) * (float) y));
		
		Vector2 v = new Vector2(posX, posY);
		
		v.sub(this.perso.getPosition());

		float negX = (v.x<0f ? -1f : 1f);
		float negY = (v.y<0f ? -1f : 1f);
		
		float angle = (float) v.angle(); //Math.atan2(v.y, v.x);
		
		v.x =(float)Math.cos(angle);
		v.y =(float)Math.sin(angle);
		
		
		 // ï¿½ verif que la precision numerique des float ne tombe pas sur 0.0 
		 
		directionTir.x = (float) (v.x != 0.0 ? v.x : 0.001); // on evite de passer par zï¿½ro (bloquant)
		directionTir.y =  (float) (v.y != 0.0 ? v.y : 0.001);

		cibleTir = new Vector2(posX, posY);
		*/
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
	
	/*@Param les coordonnées x et y et si le tir est chargé*/
	public void feuRelache(int x, int y, boolean charged) {
		touches.get(touches.put(Touches.FEU, false));
		directionTir.x = 0;
		directionTir.y = 0;
		this.charged = charged;
	}
	
	
	
	public void sourisDroitRelache (int x, int y){
		touches.get(touches.put(Touches.EPEE, false));
	
	}
	
	public void update(float delta) {
		gererEntrees();
		gererCollision(delta);

		perso.update(delta);
	}
	
	public void gererCollision(float delta){
		perso.getRapidite().scl(delta); // on travaille au ralenti
		
		
		Rectangle persoRect = rectPool.obtain();
		persoRect.set(perso.getCadre());
		
		this.chargerCollision();
		persoRect.x += perso.getRapidite().x;
		persoRect.y += perso.getRapidite().y;
		
		int i = 0;
		boolean ok = true;
		while (i< collision.size && ok){
			if (collision.get(i) != null && persoRect.overlaps(collision.get(i))){
				perso.getRapidite().x = 0;
				perso.getRapidite().y = 0;
				ok = false;
			}
			i++;
		}

		perso.getRapidite().scl(1/delta); // on restaure la vitesse
		
	}
	
	private void gererEntrees() {
		// ici on modifie l'état du perso
		if (touches.get(Touches.GAUCHE) && toucheActuHoz == Touches.GAUCHE) {
				
				perso.getRapidite().x = -Perso.VITESSE;
			
		}
		if (touches.get(Touches.DROITE) && toucheActuHoz == Touches.DROITE) {
			
				perso.getRapidite().x = Perso.VITESSE;
			
		}
		if (touches.get(Touches.HAUT)  && toucheActuVer == Touches.HAUT) {
			
				perso.getRapidite().y = Perso.VITESSE;
			
		}
		if (touches.get(Touches.BAS)  && toucheActuVer == Touches.BAS) {	
			
				perso.getRapidite().y = -Perso.VITESSE;
			
		}
		
		if (!touches.get(Touches.GAUCHE) && !touches.get(Touches.DROITE)){
			perso.getRapidite().x = 0;
		}
		
		if (!touches.get(Touches.HAUT) && !touches.get(Touches.BAS)){
			perso.getRapidite().y = 0;
		}
		
		if (perso.getRapidite().x != 0f && perso.getRapidite().y != 0f){
			if (perso.getForm() == Form.LIGHTFORM)
				perso.changerEtat(Light.MARCHANT);
		}
		else{
			if (perso.getForm() == Form.LIGHTFORM)
				perso.changerEtat(Light.INACTIF);
		}
		
		if (touches.get(Touches.FEU) && directionTir.x !=0 && directionTir.y !=0 && perso.getEtat()!=Dark.GRABBING){
			/*Gestion des attaques*/
			
			if(perso.getForm()==Form.SHADOWFORM) {
				perso.changerEtat(Dark.GRABBING);
			}
			
			if(perso.getForm()==Form.SHADOWFORM || charged)
			monde.lancerProjectile(new Vector2(directionTir), new Vector2(cibleTir));
			else monde.frapperEpee(new Vector2(directionTir), new Vector2(cibleTir));
			
			directionTir.x = 0;
			directionTir.y = 0;
			
		}
		
		/*
		if ( !touches.get(Touches.GAUCHE) && !touches.get(Touches.DROITE) && !touches.get(Touches.HAUT) && !touches.get(Touches.BAS)){
			Etat etat = perso.getEtat();
			if (etat.getClass().equals(Etat_light.class))
				perso.changerEtat(Etat_light.INACTIF);
			
			perso.getRapidite().x = 0;
			perso.getRapidite().y = 0;
		}*/
		
		//ici cas ou il tire
		/*if ((touches.get(Touches.GAUCHE) && touches.get(Touches.DROITE)) || (touches.get(Touches.HAUT) && touches.get(Touches.BAS)) ||
				(!touches.get(Touches.GAUCHE) && !(touches.get(Touches.DROITE))) ||
						(!touches.get(Touches.HAUT) && !(touches.get(Touches.BAS)))) {
			Etat etat = perso.getEtat();
			if (etat.getClass().equals(Etat_light.class))
				perso.changerEtat(Etat_light.INACTIF);
			
			perso.getRapidite().x = 0;
			perso.getRapidite().y = 0;
					
		}*/
	}
}
