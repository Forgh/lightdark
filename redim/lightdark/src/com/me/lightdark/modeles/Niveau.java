package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.lightdark.modeles.Case.type_case_generique;

public class Niveau {

	private int largeur;
	private int hauteur;
	private Case[][] cases;
	private Rectangle[][] bloquantes;
	private Rectangle[][] ombres;
	private Rectangle[][] light;

	public Niveau() {
		// TODO Auto-generated constructor stub
		this.largeur = 13; this.hauteur = 13;
		
		cases = new Case[largeur][hauteur];
		bloquantes = new Rectangle[largeur][hauteur];
		ombres = new Rectangle[largeur][hauteur];
		light = new Rectangle[largeur][hauteur];
		
		demo2();
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
	
	public Rectangle getCollisionWithShadow(int x, int y){
		if (x<0 || y<0 || x>= largeur || y >= hauteur){
			return null;
		}else{
			return ombres[x][y];
		}
	}
	
	public Rectangle getCollisionWithLight(int x, int y){
		if (x<0 || y<0 || x>= largeur || y >= hauteur){
			return null;
		}else{
			return light[x][y];
		}
	}
	
	public void createCaseWithShadow (int i, int j) {
		/*if(j-1<=hauteur && j-1>=0 && cases[i][j-1]==null){
			cases[i][j-1]= new Case(new Vector2(i,j-1));
		}
		if(i-1<=largeur && i-1>=0 && cases[i-1][j]==null){
			cases[i-1][j]= new Case(new Vector2(i-1,j));
		}*/
		
		bloquantes[i][j] = cases[i][j].getCadre();
		cases[i][j].setTypeCase(type_case_generique.MONTAGNE);
		
		if(j-1<=hauteur && j-1>=0 && ombres[i][j-1]==null && bloquantes[i][j-1]==null){
			ombres[i][j-1]=cases[i][j-1].getCadre();
			cases[i][j-1].setTypeCase(type_case_generique.OMBRE);
			
		}
		if(i-1<=largeur && i-1>=0 && ombres[i-1][j]==null && bloquantes[i-1][j]==null){
			ombres[i-1][j]=cases[i-1][j].getCadre();
			cases[i-1][j].setTypeCase(type_case_generique.OMBRE);
		}
		
		if(i-1<=largeur && i-1>=0 && j-1<=largeur && j-1>=0 && ombres[i-1][j-1]==null && bloquantes[i-1][j-1]==null){
			ombres[i-1][j-1]=cases[i-1][j-1].getCadre();
			cases[i-1][j-1].setTypeCase(type_case_generique.OMBRE);
		}
	}
	
	public void createGroundAndBorder(){
		
		for(int i=0;i<largeur;i++){
			for(int j=0;j<hauteur;j++){
				cases[i][j] = new Case(new Vector2(i,j));
				cases[i][j].setTypeCase(type_case_generique.TERRE);
			}
		}
		
		for(int i=0;i<largeur;i++){
			bloquantes[i][0] = cases[i][0].getCadre();
			cases[i][0].setTypeCase(type_case_generique.MONTAGNE);
			bloquantes[i][hauteur-1] = cases[i][hauteur-1].getCadre();
			cases[i][hauteur-1].setTypeCase(type_case_generique.MONTAGNE);
		}
		
		for(int i=0;i<hauteur;i++){
			bloquantes[0][i] = cases[0][i].getCadre();
			cases[0][i].setTypeCase(type_case_generique.MONTAGNE);
			bloquantes[largeur-1][i] = cases[largeur-1][i].getCadre();
			cases[largeur-1][i].setTypeCase(type_case_generique.MONTAGNE);
		}
	}
	
	private void demo(){
		largeur = 11;
		hauteur = 11;
		
		//cases[0][10] = new Case(new Vector2(0,10));
		//cases[10][0] = new Case(new Vector2(10,0));
		
		cases[1][1] = new Case(new Vector2(1,1));
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

		
		/*for(int i=0;i<hauteur;i++){
			for(int j=0;j<largeur;j++){
				cases[i][j] = new Case(new Vector2(i,j));
				cases[i][j].setTypeCase(type_case_generique.TERRE);
			}
		}*/
		
		createGroundAndBorder();
		
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
		
		for(int i=0;i<largeur;i++){
			for(int j=0;j<hauteur;j++){
				if(cases[i][j].typeCase== type_case_generique.TERRE)
				light[i][j] = cases[i][j].getCadre();
			}
		}
		
	}
	
}
