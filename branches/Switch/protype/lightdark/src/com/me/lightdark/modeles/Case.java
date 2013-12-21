package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Case {
	public static final float TAILLE = 1f;
	
	Vector2 position = new Vector2();
	Rectangle cadre = new Rectangle();
	

	
	interface type_case{} 
	
	enum type_case_generique implements type_case {
		TERRE, EAU, VEGETATION, MONTAGNE, CAVERNE
	}
	

	boolean bloquante;
	boolean ombre;
	float coef_friction;
	
	public Case(Vector2 position) {
		this.position = position;
		this.cadre.setX(position.x);
		this.cadre.setY(position.y);
		this.cadre.height = TAILLE;
		this.cadre.width = TAILLE;
		
		this.bloquante = false;
		this.ombre = false;
		this.coef_friction = 1f;
	}
	
	// **** GETTERS ****
	public Vector2 getPosition() {
		return position;
	}

	public Rectangle getCadre() {
		return cadre;
	}
	
	/*
	 *  Les deux attributs sont ombre et bloquante
	 */
	public void setBloquant(boolean b){
		this.bloquante = b;
	}
	
	public void setFriction(float f){
		this.coef_friction = f;
	}
	
	public float getFriction(){
		return this.coef_friction;
	}
	
	public boolean getBloquant(){
		return this.bloquante;
	}
	
	public void setOmbre(boolean b){
		this.ombre = b;
	}
	
	public boolean getOmbre(){
		return this.ombre;
	}
	/*
	public ? getType(){
		return "";
	}
	*/
}
