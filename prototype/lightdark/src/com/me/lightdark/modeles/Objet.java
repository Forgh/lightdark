package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.me.lightdark.modeles.Anime.AnimeEspece;

public class Objet {
	private Vector2 position;
	public float TAILLE;
	private Vector2 rapidite = new Vector2();
	
	private Rectangle cadre = new Rectangle();
	
	private Array<Vector2> path = new Array<Vector2>();
	private int pathStep = 0;
	private float tempsAnime;
	
	public enum type_objet {
		OBJET_FEU
	}
	private type_objet t;
	

	public Objet(Vector2 position, float t) {
		// TODO Auto-generated constructor stub
		
		this.position = position;
		this.cadre.setPosition(position);
		this.TAILLE = t;
		this.cadre.height = TAILLE;
		this.cadre.width = TAILLE;
		this.tempsAnime = 0f;
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
		
		public void setTypeCase(type_objet t){
			this.t = t;
		}
		
		public type_objet getTypeCase(){
			return this.t;
		}
		
		public void update(float delta) {
			tempsAnime += delta;
			
			position.add(rapidite.cpy().scl(delta));
			
			cadre.setPosition(position);
		}

}
