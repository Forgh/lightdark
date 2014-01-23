package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Case {
	public static final float TAILLE = 1f;
	
	Vector2 position = new Vector2();
	Rectangle cadre = new Rectangle();
	
	interface type_case{} 
	
	enum type_case_generique implements type_case {
		TERRE, EAU, VEGETATION, MONTAGNE, CAVERNE, OMBRE
	}
	
	type_case_generique typeCase;
	boolean bloquante;
	boolean ombre;
	int hashCode;
	
	public Case(Vector2 position) {
		this.position = position;
		this.cadre.setX(position.x);
		this.cadre.setY(position.y);
		this.cadre.height = TAILLE;
		this.cadre.width = TAILLE;
		this.bloquante = false;
		this.ombre = false;
		this.hashCode=this.hashCode();
	}
	
	
	public void setTypeCase(type_case_generique t) {
		this.typeCase=t;
	}
	// **** GETTERS ****
	public Vector2 getPosition() {
		return position;
	}

	public Rectangle getCadre() {
		return cadre;
	}
	
	public int getHashCode(){
		return hashCode;
	}
	
	/*
	 *  Les deux attributs sont ombre et bloquante
	 */
	public void setBloquant(boolean b){
		this.bloquante = b;
	}
	
	public boolean getBloquant(){
		return this.bloquante;
	}
	
	public String getTypeCase(){
		String s;
		try{
			s = this.typeCase.toString();
		}catch (NullPointerException npe){
			s = new String();
		}
		return s;
	}
	
	public void setOmbre(boolean b){
		this.ombre = b;
	}
	
	public boolean equals(Case c){
		if(this.hashCode == c.getHashCode()){
			return true;
		}
		else return false;
	}
	
	public boolean getOmbre(){
		return this.ombre;
	}
	
	public void action(CompetenceAnimaux ca, Monde m){
		// ici gerer la capacité
	}
	
	public void arrive(){

	}
	
	/*
	public ? getType(){
		return "";
	}
	*/
}
