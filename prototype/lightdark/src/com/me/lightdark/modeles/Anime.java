package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Anime {
	public float VITESSE = 2f;	// vitesse par unite de temps sur une unite d'espace
	public float TAILLE = 1f; // une demi unite
	
	public enum AnimeType {
		INCONNU, MONSTRE, ANIMAL
	}
	
	public enum AnimeEspece { // affichage
		INCONNU, MONSTRE_CUBE, ANIMAL_CHAT
	}
	
	private Vector2 position = new Vector2();
	private Vector2 rapidite = new Vector2();
	
	private Niveau niveau;
	
	private Rectangle cadre = new Rectangle();
	
	private Array<Vector2> path = new Array<Vector2>();
	private int pathStep = 0;
	
	private float tempsAnime = 0;
	
	private AnimeEspece espece;
	
	public Anime(Vector2 position) {
		// TODO Auto-generated constructor stub
		
		this.position = position;
		this.cadre.setPosition(position);
		this.cadre.height = TAILLE;
		this.cadre.width = TAILLE;
		this.setAnimeEspece(AnimeEspece.INCONNU);
	}
	
	public Anime(Vector2 position, Niveau niv){
		this(position);
		niveau = niv;
	}
	
	



		// **** GETTERS ****
		public Vector2 getPosition() {
			return position;
		}
		
		public Niveau getNiveau(){
			return niveau;
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
		
		/*@param la position de l'anime
		 * @return un nouveau cadre de collision pour cet anime*/
		public Rectangle newCadre(Vector2 v){
			Rectangle rect = new Rectangle();
			rect.setPosition(position);
			rect.height = TAILLE;
			rect.width = TAILLE;
			return rect;
		}
		

		public float temps(){
			return tempsAnime;
		}
		
		public void setTemps(float f){
		 tempsAnime = f;
		}
		
		public void setCadre(Rectangle r){
			if(r==null){
			cadre.height=0f;
			cadre.width=0f;
			}
			else cadre = r;
		}

		public void update(float delta) {
			tempsAnime += delta;
			
			position.add(rapidite.cpy().scl(delta));
			
			cadre.setPosition(position);
		}

		public void setPosition(Vector2 v){
			this.position=v;
		}
		

		public AnimeType getAnimeType(){
			return AnimeType.INCONNU;
		}
		
		public AnimeEspece getAnimeEspece(){
			return espece;
		}
		
		public void setAnimeEspece(AnimeEspece ae){
			espece = ae;
		}
		

		public Vector2 getRapidite() {
			return this.rapidite;
		}
		
		public boolean isTamed(){
			return false;
		}
		
		public abstract void setTaming(boolean t);

		public void demarrerCompetence(CompetenceAnimaux ca){
			//ici rien
		}
		
		public void stopperCompetence(CompetenceAnimaux ca){
			// ici rien
		}

		public abstract void setTamer(Perso p);
}
