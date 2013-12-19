package com.me.lightdark.controleurs;

import java.util.HashMap;
import java.util.Map;




import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.me.lightdark.modeles.Case;
import com.me.lightdark.modeles.Monde;
import com.me.lightdark.modeles.Perso;
import com.me.lightdark.modeles.Perso.Etat;
import com.me.lightdark.modeles.Perso.Etat_light;

public class ControlerPerso {

	private Monde monde;
	private Perso perso;
	private Array<Rectangle> collision;
	
	Vector2 directionTir;
	
	enum direction  {
		HAUT, HAUT_GAUCHE, HAUT_DROITE,
		BAS, BAS_GAUCHE, BAS_DROITE,
		GAUCHE, DROITE,
	}
	
	enum Touches {
		GAUCHE, DROITE, HAUT, BAS, FEU,
	}
	
	static Map<Touches, Boolean> touches = new HashMap<Touches, Boolean>();
	static {
		touches.put(Touches.GAUCHE, false);
		touches.put(Touches.HAUT, false);
		touches.put(Touches.DROITE, false);
		touches.put(Touches.BAS, false);
		touches.put(Touches.FEU, false);
	};
	
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
			}
		}
		
	}

	public void gauchePresse() {
		touches.get(touches.put(Touches.GAUCHE, true));
	}
	
	public void droitPresse() {
		touches.get(touches.put(Touches.DROITE, true));
	}
	
	public void hautPresse() {
		touches.get(touches.put(Touches.HAUT, true));
	}
	
	public void basPresse() {
		touches.get(touches.put(Touches.BAS, true));
	}
	
	
	public void feuPresse(int x, int y, int w, int h) {
		touches.get(touches.put(Touches.FEU, true));
		//soluce temporaire � 1h15 du mat
		
		float fx = (11f/w)*x;
		float fy = 11f - ((11f/h)*y);
		//System.out.println("x: " + fx + "; x1: " + perso.getPosition().x);
		//System.out.println("y: " + fy + "; y1: " + perso.getPosition().y);
		//directionTir.x = (fx-perso.getPosition().x) / Math.abs(fx-perso.getPosition().x);
		directionTir.y = (perso.getPosition().y - fy) / Math.abs(perso.getPosition().y - fy);
		if (fx> perso.getPosition().x){
			directionTir.x = 1;
		}else{
			directionTir.x = -1;
		}
		
		if (fy> perso.getPosition().y){
			directionTir.y = 1;
		}else{
			directionTir.y = -1;
		}
		
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
	
	
	public void feuRelache(int x, int y) {
		touches.get(touches.put(Touches.FEU, false));
		directionTir.x = 0;
		directionTir.y = 0;
	}
	
	
	public void update(float delta) {
		gererEntrees();
		gererCollision(delta);

		perso.update(delta);
	}
	
	public void gererCollision(float delta){
		perso.getRapidite().mul(delta); // on travail au ralenti
		
		perso.getCadre().x += perso.getRapidite().x;
		perso.getCadre().y += perso.getRapidite().y;
		
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

		perso.getRapidite().mul(1/delta); // on restore la vitesse
		
	}
	
	private void gererEntrees() {
		// ici on modifie l'�tat du perso
		if (touches.get(Touches.GAUCHE)) {
			if (!touches.get(Touches.DROITE)){
				perso.getRapidite().x = -Perso.VITESSE;
			}
			//perso.getRapidite().y = 0;
			
		}
		if (touches.get(Touches.DROITE)) {
			if (!touches.get(Touches.GAUCHE)){
				perso.getRapidite().x = Perso.VITESSE;
			}
			//perso.getRapidite().y = 0;
			
		}
		if (touches.get(Touches.HAUT)) {
			if (!touches.get(Touches.BAS)){
				perso.getRapidite().y = Perso.VITESSE;
			}
			//perso.getRapidite().x = 0;
			
		}
		if (touches.get(Touches.BAS)) {	
			if (!touches.get(Touches.HAUT)){
				perso.getRapidite().y = -Perso.VITESSE;
			
			}
			//perso.getRapidite().x = 0;
			
		}
		
		if (!touches.get(Touches.GAUCHE) && !touches.get(Touches.DROITE)){
			perso.getRapidite().x = 0;
		}
		
		if (!touches.get(Touches.HAUT) && !touches.get(Touches.BAS)){
			perso.getRapidite().y = 0;
		}
		
		if (perso.getRapidite().x != 0f && perso.getRapidite().y != 0f){
			Etat etat = perso.getEtat();
			if (etat.getClass().equals(Etat_light.class))
				perso.changerEtat(Etat_light.MARCHANT);
		}else{
			Etat etat = perso.getEtat();
			if (etat.getClass().equals(Etat_light.class))
				perso.changerEtat(Etat_light.INACTIF);
		}
		
		if (touches.get(Touches.FEU) && directionTir.x !=0 && directionTir.y !=0){
			monde.lancerProjectile(new Vector2(directionTir));
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
