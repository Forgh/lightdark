package com.me.lightdark.modeles;



import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.me.lightdark.modeles.Case.type_case_generique;

public class Niveau {

	private int largeur;
	private int hauteur;
	private Case[][] cases;
	private Rectangle[][] bloquantes;
	private Rectangle[][] ombres;
	private Rectangle[][] light;
	
	private Vector2 posStart;
	
	private Array<Animal> animals = new Array<Animal>();

	public Niveau() {
		// TODO Auto-generated constructor stub
		this.largeur = 13; this.hauteur = 13;
		posStart = new Vector2(0f,0f);
		cases = new Case[largeur][hauteur];
		bloquantes = new Rectangle[largeur][hauteur];
		ombres = new Rectangle[largeur][hauteur];
		light = new Rectangle[largeur][hauteur];
		
		demo6();
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
	
	public Vector2 getCloseShadow( Vector2 v){
		float x =0;
		float y =0;
		for(int i=0;i<largeur;i++){
			for(int j=0;j<hauteur;j++){
				if (Math.abs(i-v.x)<Math.abs(x-v.x) && Math.abs(j-v.y)<Math.abs(y-v.y) && light[i][j] == null){
					x = i;y=j;
				}
			}
		}
		
		return new Vector2(x,y);
	}
	
	public Array<Animal> getAnimals(){
		return this.animals;
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
	
private void demo1(){

		
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
		
		this.animals.add(new Animal(new Vector2(3f,3f)));
		this.animals.get(0).getPath().add(new Vector2(1f,3f));
		this.animals.get(0).getPath().add(new Vector2(5f,3f));
		this.animals.get(0).getPath().add(new Vector2(3f,1f));
		this.animals.get(0).getPath().add(new Vector2(11f,1f));
		this.animals.get(0).getPath().add(new Vector2(11f,11f));
		this.animals.get(0).getPath().add(new Vector2(1f,11f));
		//this.animals.get(0).getPath().add(new Vector2(1f,3f));
		
		this.posStart = new Vector2(1f,1f);
	}

private void demo2(){

createGroundAndBorder(); 

for (int i =3; i<6 ; i++){
createCaseWithShadow(i,11);
}
createCaseWithShadow(3,10);
createCaseWithShadow(5,10);

for (int i=4; i<7; i++){
createCaseWithShadow(i,7);
createCaseWithShadow(i,6);

}

for (int i=6; i>0; i-- ){
createCaseWithShadow(1,i);

}

for (int i =1; i<5; i++){
createCaseWithShadow(i,1);

}

createCaseWithShadow(7,1);

for (int i=2; i<5;i++){ 
createCaseWithShadow(11,i);

}

for (int i= 7; i<1; i++){
createCaseWithShadow(11, i);
}
createCaseWithShadow(10,7);
createCaseWithShadow(10,9);

for(int i=0;i<largeur;i++){
for(int j=0;j<hauteur;j++){
	if(cases[i][j].typeCase== type_case_generique.TERRE)
        light[i][j] = cases[i][j].getCadre();
    }
}
//entrée et sortie
cases[0][hauteur-2].setTypeCase(type_case_generique.OMBRE);
bloquantes[0][hauteur-2]=null;
cases[8][hauteur-1].setTypeCase(type_case_generique.OMBRE);
bloquantes[largeur-5][hauteur-1]=null;

cases[4][hauteur-2].setTypeCase(type_case_generique.OMBRE);
bloquantes[4][hauteur-2]=null;
createCaseWithShadow(11, hauteur-4);
createCaseWithShadow(11, hauteur-6);

this.animals.add(new Animal(new Vector2(4f,hauteur-2f)));//mob en haut
this.animals.add(new Animal(new Vector2(11f,hauteur-5f)));//mob à droite
this.animals.add(new Animal(new Vector2(8f,hauteur-2f)));//souris
this.animals.get(2).getPath().add(new Vector2(8f,hauteur-2f));
this.animals.get(2).getPath().add(new Vector2(8f,hauteur-8f));


}


private void demo3(){

createGroundAndBorder();    

for (int i = 4; i<6; i++){
    for (int j= hauteur-1; j>5; j--)
        createCaseWithShadow(i,j);
}
createCaseWithShadow(2,5);
createCaseWithShadow(3,3);

for (int i=6; i<11; i++)
    createCaseWithShadow(7,i);


for (int i = hauteur-1; i>3; i--)
    createCaseWithShadow(largeur-2,i);

createCaseWithShadow(6,hauteur-1);
createCaseWithShadow(7,hauteur-1);
createCaseWithShadow(9,hauteur-1);
    
for(int i=0;i<largeur;i++){
    for(int j=0;j<hauteur;j++){
        if(cases[i][j].typeCase== type_case_generique.TERRE)
        light[i][j] = cases[i][j].getCadre();
    }
}

//entrée et sortie
cases[0][hauteur-2].setTypeCase(type_case_generique.OMBRE);
cases[8][hauteur-1].setTypeCase(type_case_generique.OMBRE);
bloquantes[0][hauteur-2]=null;
bloquantes[8][hauteur-1]=null;



this.animals.add(new Animal(new Vector2(8f,2f)));
this.animals.get(0).getPath().add(new Vector2(8f,2f));
this.animals.get(0).getPath().add(new Vector2(4f,2f));
}


private void demo4(){

	createGroundAndBorder();
	
	//Entrée et sortie :
	cases[0][hauteur-3].setTypeCase(type_case_generique.OMBRE);
	cases[largeur-1][hauteur-3].setTypeCase(type_case_generique.OMBRE);
	
	//Blocs de décor :
	cases[1][hauteur-5].setTypeCase(type_case_generique.MONTAGNE);
	cases[1][hauteur-6].setTypeCase(type_case_generique.MONTAGNE);
	
	for(int i=1; i<6; i++)
		cases[1][i].setTypeCase(type_case_generique.MONTAGNE);
	for(int i=1; i<6; i++)
		cases[2][i].setTypeCase(type_case_generique.MONTAGNE);
	
	cases[4][1].setTypeCase(type_case_generique.MONTAGNE);
	cases[4][2].setTypeCase(type_case_generique.MONTAGNE);
	cases[5][1].setTypeCase(type_case_generique.MONTAGNE);
	cases[5][2].setTypeCase(type_case_generique.MONTAGNE);
	
	cases[5][6].setTypeCase(type_case_generique.MONTAGNE);
	cases[5][7].setTypeCase(type_case_generique.MONTAGNE);
	
	for(int i=3; i<6; i++)
		cases[i][hauteur-2].setTypeCase(type_case_generique.MONTAGNE);
	for(int i=3; i<6; i++)
		cases[i][hauteur-3].setTypeCase(type_case_generique.MONTAGNE);
	
	for(int i=3; i<hauteur-1; i++)
		cases[7][i].setTypeCase(type_case_generique.MONTAGNE);
	for(int i=5; i<hauteur-3; i++)
		cases[9][i].setTypeCase(type_case_generique.MONTAGNE);
	
	cases[9][hauteur-2].setTypeCase(type_case_generique.MONTAGNE);
	cases[10][5].setTypeCase(type_case_generique.MONTAGNE);
	cases[11][5].setTypeCase(type_case_generique.MONTAGNE);
	cases[8][3].setTypeCase(type_case_generique.MONTAGNE);
	cases[9][3].setTypeCase(type_case_generique.MONTAGNE);
	cases[10][3].setTypeCase(type_case_generique.MONTAGNE);
	cases[10][1].setTypeCase(type_case_generique.MONTAGNE);
	//Coffre épée
	cases[11][1].setTypeCase(type_case_generique.OMBRE);
	
	for(int i=0;i<largeur;i++){
		for(int j=0;j<hauteur;j++){
			if(cases[i][j].typeCase== type_case_generique.TERRE)
			light[i][j] = cases[i][j].getCadre();
			else if(cases[i][j].typeCase== type_case_generique.MONTAGNE)
				bloquantes[i][j]=cases[i][j].getCadre();
		}
	}
	
	this.animals.add(new Animal(new Vector2(11f,2f)));
	this.animals.get(0).getPath().add(new Vector2(11f,2f));//départ
	this.animals.get(0).getPath().add(new Vector2(11f,4f));
	this.animals.get(0).getPath().add(new Vector2(8f,4f));
	this.animals.get(0).getPath().add(new Vector2(8f,hauteur-2f));//arrivée, puis retour
	this.animals.get(0).getPath().add(new Vector2(8f,4f));
	this.animals.get(0).getPath().add(new Vector2(11f,4f));
}

private void demo5(){

	createGroundAndBorder();
	
		
	for(int i=0;i<largeur;i++){
		for(int j=0;j<hauteur;j++){
			if(cases[i][j].typeCase== type_case_generique.TERRE)
			light[i][j] = cases[i][j].getCadre();
		}
	}
	
	//Insérer ici les mobs
	this.animals.add(new Animal(new Vector2(4f,6f)));
	this.animals.add(new Animal(new Vector2(6f,6f)));
	this.animals.add(new Animal(new Vector2(8f,6f)));
	
	//Insérer ici le coffre de l'orbe
	cases[6][8].setTypeCase(type_case_generique.OMBRE);
	
	//L'entrée et la sortie :
	cases[6][0].setTypeCase(type_case_generique.OMBRE);
	cases[6][hauteur-1].setTypeCase(type_case_generique.OMBRE);
	bloquantes[6][0] = null;
	bloquantes[6][hauteur-1] = null;
	
}

public void refresh(){
	//Met à jour les cases bloquantes, ombres et light
	//Ne retire pas (encore) les valeurs anciennement présentes
	for(int i=0;i<largeur;i++){
	    for(int j=0;j<hauteur;j++){
	        if(cases[i][j].typeCase== type_case_generique.TERRE)
	        	light[i][j] = cases[i][j].getCadre();
	        if(cases[i][j].typeCase== type_case_generique.MONTAGNE)
	        	bloquantes[i][j] = cases[i][j].getCadre();
	        if(cases[i][j].typeCase==type_case_generique.OMBRE)
	        	ombres[i][j]=cases[i][j].getCadre();
	    }
	}
}
public void demo6(){
	createGroundAndBorder();
	this.posStart = new Vector2(largeur-1f,2f);
	
	
	for(int i=5; i<largeur-1; i++)
		for(int j=4; j<hauteur-5; j++)
			createCaseWithShadow(i, j);
	
	cases[largeur-1][2].setTypeCase(type_case_generique.OMBRE);
	cases[0][2].setTypeCase(type_case_generique.OMBRE);
	bloquantes[largeur-1][2]=null;
	bloquantes[0][2]=null;
	
	
	for(int i=1; i<largeur-1; i++)
		cases[i][hauteur-2].setTypeCase(type_case_generique.OMBRE);
	
	createCaseWithShadow(3,hauteur-3);
	createCaseWithShadow(3,hauteur-4);
	
	
	//ici les mobs...
	this.animals.add(new Animal(new Vector2(10f,10f)));
	this.animals.get(0).getPath().add(new Vector2(10f,10f));
	this.animals.get(0).getPath().add(new Vector2(10f,8f));
	this.animals.get(0).getPath().add(new Vector2(6f,8f));
	this.animals.get(0).getPath().add(new Vector2(6f,10f));
	
	
	this.animals.add(new Animal(new Vector2(6f,8f)));
	this.animals.get(1).getPath().add(new Vector2(6f,8f));
	this.animals.get(1).getPath().add(new Vector2(6f,10f));
	this.animals.get(1).getPath().add(new Vector2(10f,10f));
	this.animals.get(1).getPath().add(new Vector2(10f,8f));
	//ici la salamandre...
	this.animals.add(new Animal(new Vector2(8f,9f)));
	
	
	refresh();
}

public Vector2 getPosStart() {
	// TODO Auto-generated method stub
	return this.posStart;
}
	
	
	
	
	
}
