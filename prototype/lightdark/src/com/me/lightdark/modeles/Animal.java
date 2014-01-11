package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Animal {
	public static final float VITESSE = 2f;	// vitesse par unite de temps sur une unite d'espace
	public static final float TAILLE = 1f; // une demi unite
	
	private Vector2 position = new Vector2();
	private Vector2 rapidite = new Vector2();
	
	private Rectangle cadre = new Rectangle();
	
	private Array<Vector2> path = new Array<Vector2>();
	private int pathStep = 0;
	
	private float tempsAnime = 0;
	
	public Animal(Vector2 position) {
		// TODO Auto-generated constructor stub
		
		this.position = position;
		this.cadre.setPosition(position);
		this.cadre.height = TAILLE;
		this.cadre.width = TAILLE;
	}
	
	
	
	// **** GETTERS ****
		public Vector2 getPosition() {
			return position;
		}
		
		
		public Array<Vector2>  getPath() {
			return this.path;
		}
		
		public int getPathStep(){
			return this.pathStep;
		}
		
		public void setPathStep(int e){
			this.pathStep = e;
		}
		
		
		public Rectangle getCadre() {
			return cadre;
		}
		

		public float temps(){
			return tempsAnime;
		}

		public void update(float delta) {
			tempsAnime += delta;
			position.add(rapidite.cpy().scl(delta)); //?
			
			cadre.setPosition(position);
		}

		public void setPosition(Vector2 v){
			this.position=v;
		}
		



		public Vector2 getRapidite() {
			// TODO Auto-generated method stub
			return this.rapidite;
		}
		
		

}
