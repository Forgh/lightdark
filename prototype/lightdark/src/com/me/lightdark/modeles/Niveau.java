package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Niveau {

	private int largeur;
	private int hauteur;
	private Case[][] cases;
	private Rectangle[][] bloquantes;
	public Niveau() {
		// TODO Auto-generated constructor stub
		cases = new Case[11][11];
		bloquantes = new Rectangle[11][11];
		demo();
	}
	
	public int getLargeur(){
		return this.largeur;
	}

	public int getHauteur(){
		return this.hauteur;
	}
	
	// normalement peut etre ici des setters
	
	public Case get(int x, int y){
		if (x<0 || y<0 || x>= largeur || y >= hauteur){
			return null;
		}else{
			return cases[x][y];
		}
	}
	
	public Rectangle getCollision(int x, int y){
		if (x<0 || y<0 || x>= largeur || y >= hauteur){
			return null;
		}else{
			return bloquantes[x][y];
		}
	}
	
	public void createCaseWithShadow (int i, int j) {
		cases[i][j] = new Case(new Vector2(i,j));
		bloquantes[i][j] = cases[i][j].getCadre();

	}
	
	
	private void demo(){
		largeur = 11;
		hauteur = 11;
		
		//cases[0][10] = new Case(new Vector2(0,10));
		//cases[10][0] = new Case(new Vector2(10,0));
		
		cases[1][1] = new Case(new Vector2(1,1));
		cases[2][1] = new Case(new Vector2(2,1));
		(cases[2][1]).setFriction(0.1f);
		
		cases[9][9] = new Case(new Vector2(9,9));
		cases[5][5] = new Case(new Vector2(5,5));
		
		bloquantes[1][1] = cases[1][1].getCadre();
		bloquantes[9][9] = cases[9][9].getCadre();
		bloquantes[5][5] = cases[5][5].getCadre();
		/*
		cases.add(new Case(new Vector2(0,10)));
		cases.add(new Case(new Vector2(10,0)));
		cases.add(new Case(new Vector2(10,10)));

		cases.add(new Case(new Vector2(0,0)));
		*/
	}
	
	private void demo2(){
		largeur = 11;
		hauteur = 11;
		

		
		for(int i=7;i<hauteur;i++){
			createCaseWithShadow(0,i);
		}
		
		for(int i=0;i<3;i++){
			createCaseWithShadow(i,2);
		}

		for(int i=0;i<3;i++){
			createCaseWithShadow(4,i);
		}
		
		for(int i=3;i<8;i++){
			createCaseWithShadow(i,5);
		}
		
		for(int i=3;i<6;i++){
			createCaseWithShadow(10,i);
		}
		
		for(int i=7;i<10;i++){
			createCaseWithShadow(i,7);
		}
		
		createCaseWithShadow(4,8);

		
		for(int i=3;i<7;i++){
			createCaseWithShadow(i,10);
		}
		
		createCaseWithShadow(9,9);
		

		
	}
	
}
