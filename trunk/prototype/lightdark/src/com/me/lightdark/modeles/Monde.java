package com.me.lightdark.modeles;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;

public class Monde {

	Niveau niveau;
	
	Perso perso;
	
	Array<Projectile> projectiles = new Array<Projectile>();

	// Getters -----------
	public Array<Projectile> getProjectile() {
		return projectiles;
	}

	public Perso getPerso() {
		return perso;
	}
	
	public Niveau getNiveau(){
		return niveau;
	}

	public Monde() {
		// TODO Charger une map.
		this.perso = new Perso(new Vector2(1,1));
		perso.setForm(Form.SHADOWFORM);
		this.niveau = new Niveau();
		

		//cases.add(new Case(new Vector2(5,1), false));

	}
	
	public void lancerProjectile(Vector2 vect, Vector2 cible){
		//if(perso.getEtat()!=Dark.GRABBING)
			Projectile v = new Projectile(perso,new Vector2(perso.getPosition()),vect);
			projectiles.add( v);
			Case c = this.niveau.get((int) cible.x, (int) cible.y);
			if (c != null){
				v.setCaseCible(c); // on ajoute les coord. de la cible
			}
			
	}
	
	public Array<Case> getAffichable(int espaceH, int espaceV){ // H et V pour eventuellement faire un defilement style java
		int x = (int)perso.getPosition().x - espaceH;
		int y = (int)perso.getPosition().y - espaceV;
		
		// ici on verifie tous les bords
		if (x<0){
			x=0;
		}
		if (y<0){
			y=0;
		}
		
		int x2 = x + 2 * espaceH;
		int y2 = y + 2 * espaceV;
		if (x2>niveau.getLargeur()){
			x2 = niveau.getLargeur() - 1;
		}
		if (y2>niveau.getHauteur()){
			y2 = niveau.getHauteur() - 1;
		}
		
		Array<Case> cases = new Array<Case>();
		for(int i = x; i<=x2; i++){
			for (int j=y;j<=y2;j++){
				Case carre = niveau.get(i, j);
				if (carre != null){
					cases.add(carre);
				}
			}
		}
		
		return cases;
	}
	
	

}
