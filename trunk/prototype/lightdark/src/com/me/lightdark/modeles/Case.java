package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Case {
	public static final float TAILLE = 1f;
	
	Vector2 position = new Vector2();
	Rectangle cadre = new Rectangle();

	public Case(Vector2 position) {
		this.position = position;
		this.cadre.setX(position.x);
		this.cadre.setY(position.y);
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
	
}
