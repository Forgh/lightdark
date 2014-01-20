package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Perso {
	
	public interface Etat{} // ici polymorphisme pour light/shadow

	
	public static final float VITESSE = 4f;	// vitesse par unite de temps sur une unite d'espace
	public static final float TAILLE = 0.5f; // une demi unite
	public static final int max_health = 75;
	private int health;
	
	private Vector2 position = new Vector2();
	private Vector2 rapidite = new Vector2();
	
	private Form form;
	
	private Enum<?> etat;
	
	// normalement d'apres le cours on initialise dans le constructeur mais ici �a revient pareil
	private Rectangle cadre = new Rectangle();
	
	private float tempsAnime = 0;
	
	
	public Perso(Vector2 position) {
		this.position = position;
		this.cadre.setPosition(position);
		this.cadre.height = TAILLE;
		this.cadre.width = TAILLE;
		this.health=3;
	}
	
	
	// **** GETTERS ****
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
	
	public <E> void changerEtat(Enum<?> e){
		this.etat = e;
	}
	
	public int getHealth(){
		return this.health;
	}
	
	
	public Vector2 getRapidite(){
		return this.rapidite;
	}
	
	public void setRapidite(Vector2 v){
		this.rapidite = v;
	}
	
	public void setPosition(Vector2 v){
		this.position=v;
	}
	
	public void setForm(Form f){
		this.form=f;
	}
	
	public void healthUp(int puissance){
		health += puissance;
	}
	
	public void healthDown(int puissance){
		health -= puissance;
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
			this.setForm(Form.SHADOWFORM);
		}
		else{
			this.setForm(Form.LIGHTFORM);
		}
		System.out.println(this.getForm());
	}
	
		

}
