package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Projectile {

	public static final float VITESSE = 5f;	// vitesse par unite de temps sur une unite d'espace
	public static final float TAILLE = 0.2f; // 2/10 unite
	
	public static final float DISTANCE_MAX = 2.5f; // distance max de la portee du projectile
	
	private Vector2 position = new Vector2();
	private Vector2 rapidite = new Vector2();
	private Vector2 posInitial = new Vector2();
	private Perso lanceur;
	private Case case_cible;
	private boolean obsolete;// = false;
	
	
	// normalement d'apres le cours on initialise dans le constructeur mais ici �a revient pareil
	Rectangle cadre = new Rectangle();
	
	float tempsAnime = 0;
	
	
	public Projectile(Perso lanceur, Vector2 position, Vector2 direction) {
		this.lanceur=lanceur;
		this.position = position;
		this.posInitial = new Vector2(position);
		this.rapidite = direction;
		this.cadre.height = TAILLE;
		this.cadre.width = TAILLE;
		cadre.setPosition(lanceur.getPosition());

		this.obsolete = false;
	}
	
	
	// **** GETTERS ****
	public Vector2 getPosition() {
		return position;
	}
	
	public Vector2 getInitial() {
		return posInitial;
	}
	
	public Perso getLanceur(){
		return lanceur;
	}

	public Rectangle getCadre() {
		return cadre;
	}

	public boolean estObsolete(){
		return this.obsolete;
	}
	
	public void devientObsolete( ){
		this.obsolete = true;
	}
	
	public Vector2 getRapidite(){
		return this.rapidite;
	}
	
	public void setRapidite(Vector2 v){
		this.rapidite = v;
	}
	
	public void setCaseCible(Case c){
		this.case_cible = c;
	}
	
	public Case getCaseCible(){
		return this.case_cible;
	}
	

	public float temps(){
		return tempsAnime;
	}

	public void update(float delta) {
		tempsAnime += delta;
		position.add(rapidite.cpy().scl(delta * VITESSE)); //?
		cadre.setPosition(position);
		//System.out.println("ok");
		
	}
		
}
