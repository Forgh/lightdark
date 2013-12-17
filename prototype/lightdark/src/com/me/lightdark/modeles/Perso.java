package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Perso {
	
	public interface Etat{} // ici polymorphisme pour light/shadow
	
	public enum Etat_light implements Etat {
		INACTIF, MARCHANT, TIRANT, MOURRANT
	}
	
	public static final float VITESSE = 4f;	// vitesse par unite de temps sur une unite d'espace
	public static final float TAILLE = 0.5f; // une demi unite
	
	private Vector2 position = new Vector2();
	private Vector2 rapidite = new Vector2();
	
	private Etat etat = Etat_light.INACTIF;
	
	// normalement d'apres le cours on initialise dans le constructeur mais ici �a revient pareil
	private Rectangle cadre = new Rectangle();
	
	private float tempsAnime = 0;
	
	
	public Perso(Vector2 position) {
		this.position = position;
		this.cadre.setPosition(position);
		this.cadre.height = TAILLE;
		this.cadre.width = TAILLE;
	}
	
	
	// **** GETTERS ****
	public Vector2 getPosition() {
		return position;
	}

	public Rectangle getCadre() {
		return cadre;
	}
	
	public void changerEtat(Etat etat){
		this.etat = etat;
	}
	
	public Vector2 getRapidite(){
		return this.rapidite;
	}
	
	public void setRapidite(Vector2 v){
		this.rapidite = v;
	}
	
	public Etat getEtat(){
		return this.etat;
	}


	public float temps(){
		return tempsAnime;
	}

	public void update(float delta) {
		tempsAnime += delta;
		position.add(rapidite.cpy().scl(delta)); //?
		cadre.setPosition(position);
	}
		

}