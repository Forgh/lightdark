package com.me.lightdark.modeles;



import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.me.lightdark.modeles.Anime.AnimeEspece;
import com.me.lightdark.modeles.Objet.type_objet;
import com.me.lightdark.modeles.type_case_generique;


public class Niveau {

	private int largeur;
	private int hauteur;
	private Case[][] cases;
	private Rectangle[][] bloquantes;
	private Rectangle[][] ombres;
	private Rectangle[][] light;
	
	private Monde monde;
	
	

	private Vector2 posStart;
	private Form formStart;
	
	private String changeLevel;
	private String levelName;
	
	private Array<Anime> animals = new Array<Anime>();
	
	Array<Projectile> projectiles = new Array<Projectile>();
	Array<Projectile> fleches = new Array<Projectile>();
	Array<Objet> objets = new Array<Objet>();

	Array<Epee> sword = new Array<Epee>();

	public Niveau(String niv, Monde m) {
		monde = m;
		this.largeur = 13; this.hauteur = 13;
		posStart = new Vector2(0f,0f);
		cases = new Case[largeur][hauteur];
		bloquantes = new Rectangle[largeur][hauteur];
		ombres = new Rectangle[largeur][hauteur];
		light = new Rectangle[largeur][hauteur];
		levelName = niv;
		unloadNiveau();
		
		if (levelName.equals("demo1")){
			demo1();
		}else if (levelName.equals("demo2")){
			demo2();
		}else if (levelName.equals("demo3")){
			demo3();
		}else if (levelName.equals("demo4")){
			demo4();
		}else if (levelName.equals("demo5")){
			demo5();
		}else if (levelName.equals("demo6")){
			demo6();
		}else{
			demo();
		}
	}
	
	/*public void loadStartLevel(String niv, Monde m){
		Perso p;
		if (niv.equals("demo1")){
			demo1();
		}else if (niv.equals("demo2")){
			demo2();
		}else{
			demo();
		}
		m.setPerso(null);
		p = new Perso(this.getPosStart(), m);
		p.setForm(this.getFormStart());
		m.setPerso(p);
	}*/
	
	public String isLevelChanged(){
		return this.changeLevel;
	}
	
	public Array<Projectile> getProjectile() {
		return projectiles;
	}
	
	public Array<Projectile> getFleche() {
		return fleches;
	} 
	
	public Array<Objet> getObjet() {
		return objets;
	} 
	
	public Array<Epee> getEpee() {
		return sword;
	}
	
	public int getLargeur(){
		return this.largeur;
	}

	public int getHauteur(){
		return this.hauteur;
	}
	
	public boolean isShadow(int i, int j){
		if(this.ombres[i][j]!=null){
			return true;
		}
		else return false;
	}
	
	public String getLevelName(){
		return this.levelName;
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
		Vector2 max = new Vector2(0f, 0f);
		Vector2 index = new Vector2(0f, 0f);
		for(int i=0;i<largeur;i++){
			for(int j=0;j<hauteur;j++){
				index.x = i;
				index.y = j;
				
				if ((index.dst(v)<max.dst(v)) && ombres[i][j] != null){
					max.x = i;
					max.y = j;
				}
			}
		}
		
		return max;
	}
	
	public Form getFormStart(){
		return this.formStart;
	}
	
	public Array<Anime> getAnime(){
		return this.animals;
	}
	
	/*public void setCorrectType(){
		for
	}*/
	
	
	
	private void createObstacle(int i, int j){
		//case avec arbuste
		if(bloquantes[i][j]==null){
			bloquantes[i][j] = cases[i][j].getCadre();
			cases[i][j].setTypeCase(type_case_generique.ARBUSTE_HERBE);
		}
		
		if(j-1<=hauteur && j-1>=0 && ombres[i][j-1]==null && bloquantes[i][j-1]==null){
			ombres[i][j-1]=cases[i][j-1].getCadre();
			cases[i][j-1].setTypeCase(type_case_generique.OMBRE_HERBE_BAS);
			
		}
		if(i-1<=largeur && i-1>=0 && ombres[i-1][j]==null && bloquantes[i-1][j]==null){
			ombres[i-1][j]=cases[i-1][j].getCadre();
			cases[i-1][j].setTypeCase(type_case_generique.OMBRE_HERBE_GAUCHE);
		}
		
		if(i-1<=largeur && i-1>=0 && j-1<=largeur && j-1>=0 && ombres[i-1][j-1]==null && bloquantes[i-1][j-1]==null){
			ombres[i-1][j-1]=cases[i-1][j-1].getCadre();
			cases[i-1][j-1].setTypeCase(type_case_generique.OMBRE_HERBE_COIN);
		}

		
	}
	
	public void unloadNiveau(){
		posStart = new Vector2(0f,0f);
		cases = new Case[largeur][hauteur];
		bloquantes = new Rectangle[largeur][hauteur];
		ombres = new Rectangle[largeur][hauteur];
		light = new Rectangle[largeur][hauteur];
		
		projectiles.clear();
		fleches.clear();
		objets.clear();
		sword.clear();
		animals.clear();
	}
	
	public void createGroundAndBorder(){
		
		for(int i=0;i<largeur;i++){
			for(int j=0;j<hauteur;j++){
				createHerbe(i,j);
			}
		}
		
		for(int i=0;i<largeur;i++){
			setFalaise(i,0);
			if(bloquantes[i][1]==null){
				cases[i][1].setTypeCase(type_case_generique.TRANSITION_HERBE_BAS);
			}
			setFalaise(i,hauteur-1);
			if(bloquantes[i][hauteur-2]==null){
				cases[i][hauteur-2].setTypeCase(type_case_generique.OMBRE_HERBE_BAS);
				ombres[i][hauteur-2] = cases[i][hauteur-2].getCadre();

			}
			
		}
		
		for(int i=0;i<hauteur;i++){
			setFalaise(0,i);
			if(bloquantes[1][i]==null){
				cases[1][i].setTypeCase(type_case_generique.TRANSITION_HERBE_GAUCHE);
			}
			setFalaise(largeur-1,i);
			if(bloquantes[largeur-2][i]==null){
				cases[largeur-2][i].setTypeCase(type_case_generique.OMBRE_HERBE_GAUCHE);
				ombres[largeur-2][i] = cases[largeur-2][i].getCadre();
			}
		}
		
		cases[1][hauteur-2].setTypeCase(type_case_generique.OMBRE_HERBE_BAS);
		setToFullShadow(11,11);
	}
	
	
	
	
	private void setFalaise(int i, int j){
		bloquantes[i][j] = cases[i][j].getCadre();
		cases[i][j].setTypeCase(type_case_generique.FALAISE);
	}
	
	
	/*private void createOmbreCoin(int i, int j){
		cases[i][j] = new Case(new Vector2(i,j));
		cases[i][j].setTypeCase(type_case_generique.TRANSITION_HERBE_COIN);
	}*/
	
	private void createHerbe(int i, int j){
		cases[i][j] = new Case(new Vector2(i,j));
		cases[i][j].setTypeCase(type_case_generique.HERBE);
	}
	
	private void setToFullShadow(int i, int j){
		cases[i][j].setTypeCase(type_case_generique.OMBRE);
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
		this.formStart=Form.SHADOWFORM;
		
		createGroundAndBorder();
		this.posStart = new Vector2(1f,1f);

		for(int i=7;i<hauteur;i++){
			createObstacle(0,i);
		}
		
		for(int i=0;i<3;i++){
			createObstacle(i,2);
		}

		for(int i=0;i<3;i++){
			createObstacle(4,i);
		}
		
		for(int i=2;i<7;i++){
			createObstacle(i,5);
		}
		
		for(int i=2;i<5;i++){
			createObstacle(9,i);
		}
		
		for(int i=6;i<9;i++){
			createObstacle(i,7);
		}
		
		createObstacle(3,8);

		
		for(int i=3;i<7;i++){
			createObstacle(i,10);
		}
		
		createObstacle(9,9);
		
		/*for(int i=0;i<largeur;i++){
			for(int j=0;j<hauteur;j++){
				if(cases[i][j].typeCase== type_case_generique.HERBE)
				light[i][j] = cases[i][j].getCadre();
			}
		}*/
		
		/*this.animals.add(new Animal(new Vector2(4f,3f)));
		this.animals.get(0).getPath().add(new Vector2(1f,3f));
		this.animals.get(0).getPath().add(new Vector2(5f,3f));
		this.animals.get(0).getPath().add(new Vector2(9f,11f));
		this.animals.get(0).getPath().add(new Vector2(11f,1f));
		this.animals.get(0).getPath().add(new Vector2(11f,11f));
		this.animals.get(0).getPath().add(new Vector2(1f,11f));
		//this.animals.get(0).getPath().add(new Vector2(1f,3f));
		this.animals.get(0).VITESSE = 1f;
		((Animal) this.animals.get(0)).setCompetence(CompetenceAnimaux.BRULER);
		cases[11][11] =  new Case(new Vector2(11f,11f)){
			public void action(CompetenceAnimaux ca, Monde m){
				if (ca == CompetenceAnimaux.BRULER){
					m.addObjet(new Objet(new Vector2(11f,11f), 1f));
				}
			}
		};
		cases[11][11].setTypeCase(type_case_generique.HERBE);
		*/
		bloquantes[12][11]=null;
		cases[12][11] =  new Case(new Vector2(12f,11f)){
			public void arrive(){
				unloadNiveau();
				changeLevel = new String("demo2");
				//m.getNiveau().loadStartLevel("demo2", m);
			}
		};
		ombres[12][11]=cases[12][11].getCadre();
		cases[12][11].setTypeCase(type_case_generique.PORTE_DROITE);
		/*
		this.animals.add(new Monstre(new Vector2(3f,6f), this));
		this.animals.get(1).setAnimeEspece(AnimeEspece.MONSTRE_CUBE);
		this.animals.get(1).getPath().add(new Vector2(1f,6f));
		this.animals.get(1).getPath().add(new Vector2(5f,6f));
		this.animals.get(1).VITESSE = 0.5f;
		*/
		//cases[7][2].setTypeCase(type_case_generique.ARBUSTE_HERBE);
		
//		this.formStart = Form.LIGHTFORM;
		
		cases[3][1].setTypeCase(type_case_generique.OMBRE_HERBE_BAS);
		cases[2][9].setTypeCase(type_case_generique.OMBRE_HERBE_GAUCHE);
		cases[2][11].setTypeCase(type_case_generique.OMBRE_HERBE_GAUCHE);

		setToFullShadow(11,11);
		setToFullShadow(2,11);
		refresh();

}

private void demo2(){

	this.formStart=Form.SHADOWFORM;
	this.posStart = new Vector2(0f,11f);
	createGroundAndBorder();
	
	createObstacle(4,10);
	createObstacle(4,11);
	createObstacle(7,10);
	createObstacle(7,11);

	
	
	for (int i=4; i<7; i++){
		createObstacle(i,7);
		createObstacle(i,6);
	
	}
	cases[3][6].setTypeCase(type_case_generique.OMBRE_HERBE_GAUCHE);
	
	for (int i=6; i>0; i-- ){
		createObstacle(1,i);
	
	}
	
	for (int i =1; i<5; i++){
		createObstacle(i,1);
	
	}
	
	
	
	for (int i=2; i<5;i++){ 
		createObstacle(11,i);
	
	}
	
		createObstacle(10,6);
		createObstacle(11,6);
		createObstacle(10,9);
		createObstacle(11,9);
	
	/*for(int i=0;i<largeur;i++){
	for(int j=0;j<hauteur;j++){
		if(cases[i][j].typeCase== type_case_generique.TERRE)
	        light[i][j] = cases[i][j].getCadre();
	    }
	}*/
	//entrï¿½e et sortie
	cases[0][hauteur-2].setTypeCase(type_case_generique.PORTE_GAUCHE);
	bloquantes[0][hauteur-2]=null;
	
	cases[8][hauteur-1] =  new Case(new Vector2(8f,hauteur-1)){
		public void arrive(){
			unloadNiveau();
			changeLevel = new String("demo3");
		}
	};
	
	
	
	//bloquantes[largeur-5][hauteur-1]=null;
	
	
	
	//cases[4][hauteur-2].setTypeCase(type_case_generique.OMBRE);
	bloquantes[5][hauteur-2]=null;
	createObstacle(11, hauteur-4);
	
	
	
	
	//this.animals.add(new Animal(new Vector2(5f,hauteur-2f)));//mob en haut
	this.animals.add(new Monstre(new Vector2(6f,hauteur-2f), this));
	this.animals.get(0).setAnimeEspece(AnimeEspece.MONSTRE_CUBE);

	//this.animals.add(new Animal(new Vector2(11f,hauteur-6f)));//mob ï¿½ droite
	this.animals.add(new Monstre(new Vector2(11f,hauteur-6f), this));
	this.animals.get(1).setAnimeEspece(AnimeEspece.MONSTRE_CUBE);

	this.animals.add(new Animal(new Vector2(8f,hauteur-2f)));//souris
	this.animals.get(2).getPath().add(new Vector2(8f,hauteur-2f));
	this.animals.get(2).getPath().add(new Vector2(8f,hauteur-8f));
	bloquantes[8][hauteur-1]=null;
	cases[8][hauteur-1].setTypeCase(type_case_generique.PORTE_HAUT);
	
	setToFullShadow(3,11);
	setToFullShadow(11,8);
	setToFullShadow(11,11);
	setToFullShadow(10,5);
	setToFullShadow(6,11);


	cases[11][5].setTypeCase(type_case_generique.OMBRE_HERBE_BAS);
	cases[11][1].setTypeCase(type_case_generique.OMBRE_HERBE_BAS);

	refresh();
}


private void demo3(){
	this.formStart=Form.SHADOWFORM;

	createGroundAndBorder();    
	
	for (int i = 4; i<6; i++){
	    for (int j= 6; j<hauteur-1; j++)
	        createObstacle(i,j);
	}
	createObstacle(2,5);
	createObstacle(3,3);
	
	for (int i=6; i<11; i++)
	    createObstacle(7,i);
	
	
	for (int i = 4; i<hauteur-3; i++)
	    createObstacle(largeur-2,i);
	
	createObstacle(6,hauteur-1);
	createObstacle(7,hauteur-1);
	createObstacle(9,hauteur-1);
	    
	/*for(int i=0;i<largeur;i++){
	    for(int j=0;j<hauteur;j++){
	        if(cases[i][j].typeCase== type_case_generique.TERRE)
	        light[i][j] = cases[i][j].getCadre();
	    }
	}*/
	
	//entrï¿½e et sortie
	

	cases[12][hauteur-2] =  new Case(new Vector2(12f, hauteur-2f)){
		public void arrive(){
			unloadNiveau();
			changeLevel = new String("demo4");
		}
	};
	cases[2][0].setTypeCase(type_case_generique.PORTE_BAS);

	cases[12][hauteur-2].setTypeCase(type_case_generique.PORTE_DROITE);
	this.animals.add(new Monstre(new Vector2(11f,hauteur-2f), this));
	this.animals.get(0).setAnimeEspece(AnimeEspece.MONSTRE_CUBE);
	ombres[12][hauteur-2]  = cases[12][hauteur-2].getCadre();
	ombres[2][0] = cases[2][0].getCadre();
	
	bloquantes[2][0]=null;
	bloquantes[12][hauteur-2]=null;
	ombres[12][hauteur-2] = cases[11][hauteur-2].getCadre();
	
	
	
	this.animals.add(new Animal(new Vector2(8f,2f)));
	Animal coq = (Animal)this.animals.get(1);
	coq.setCompetence(CompetenceAnimaux.CHANTER);
	this.animals.get(1).getPath().add(new Vector2(8f,2f));
	this.animals.get(1).getPath().add(new Vector2(4f,2f));
	
	this.posStart = new Vector2(2f,0f);
	

	
	
	setToFullShadow(2,4);
	setToFullShadow(3,11);
	setToFullShadow(6,5);
	cases[6][11].setTypeCase(type_case_generique.OMBRE_HERBE_GAUCHE);

	cases[6][5].setTypeCase(type_case_generique.OMBRE_HERBE_BAS);

	//setToFullShadow(11,2);
	setToFullShadow(11,3);
	setToFullShadow(11,11);
	refresh();
}


private void demo4(){
	this.formStart=Form.LIGHTFORM;

	createGroundAndBorder();
	
	
	
	//Blocs de dï¿½cor :
	createObstacle(1,hauteur-5);
	createObstacle(1,hauteur-6);
	
	for(int i=1; i<6; i++)
		createObstacle(1,i);
	for(int i=1; i<6; i++)
		createObstacle(2,i);
	
	createObstacle(4,1);
	createObstacle(4,2);
	createObstacle(5,1);
	createObstacle(5,2);
	
	createObstacle(5,6);
	createObstacle(5,7);
	
	for(int i=3; i<6; i++)
		createObstacle(i,hauteur-2);
	for(int i=3; i<6; i++)
		createObstacle(i,hauteur-3);
	
	for(int i=3; i<hauteur-1; i++)
		createObstacle(7,i);
	for(int i=5; i<hauteur-3; i++)
		createObstacle(9,i);
	
	createObstacle(9,hauteur-2);
	createObstacle(10,5);
	createObstacle(11,5);
	createObstacle(8,3);
	createObstacle(9,3);
	createObstacle(10,3);
	createObstacle(10,1);
	//Coffre épée
	
	cases[11][1] = new Case(new Vector2(11f, 1f)){
		public void arrive(){
			int posX = (int)monde.getPerso().getPosition().x;
			int posY = (int)monde.getPerso().getPosition().y;
			if(monde.getPerso().getForm()==Form.LIGHTFORM && posX==11 && posY==1){
				//System.out.println("Activation épée");
				monde.unlockSword();
			}
		}
	};
	
	cases[11][1].setTypeCase(type_case_generique.COFFRE_HERBE);
	
	
	
/*	for(int i=0;i<largeur;i++){
		for(int j=0;j<hauteur;j++){
			if(createObstacle(i);j].typeCase== type_case_generique.TERRE)
			light[i][j] = cases[i][j].getCadre();
			else if(cases[i][j].typeCase== type_case_generique.FALAISE)
				bloquantes[i][j]=cases[i][j].getCadre();
		}
	}*/
	
	this.animals.add(new Monstre(new Vector2(11f,2f),this));
	this.animals.get(0).setAnimeEspece(AnimeEspece.MONSTRE_CUBE);

	this.animals.get(0).getPath().add(new Vector2(11f,2f));//dï¿½part
	this.animals.get(0).getPath().add(new Vector2(11f,4f));
	this.animals.get(0).getPath().add(new Vector2(8f,4f));
	this.animals.get(0).getPath().add(new Vector2(8f,hauteur-2f));//arrivï¿½e, puis retour
	this.animals.get(0).getPath().add(new Vector2(8f,4f));
	this.animals.get(0).getPath().add(new Vector2(11f,4f));
	
	this.posStart = new Vector2(0f,10f);
	
	//Entrï¿½e et sortie :
		cases[0][hauteur-3].setTypeCase(type_case_generique.PORTE_GAUCHE);
		bloquantes[0][hauteur-3] = null;
		cases[largeur-1][hauteur-3] =  new Case(new Vector2(hauteur-1,hauteur-3)){
			public void arrive(){
				unloadNiveau();
				changeLevel = new String("demo5");
			}
		};
		cases[largeur-1][hauteur-3].setTypeCase(type_case_generique.PORTE_DROITE);
		ombres[largeur-1][hauteur-3] = cases[largeur-1][hauteur-3].getCadre();
		bloquantes[largeur-1][hauteur-3] = null;
		
		cases[2][10].setTypeCase(type_case_generique.OMBRE_HERBE_GAUCHE);
		cases[2][11].setTypeCase(type_case_generique.OMBRE_HERBE_GAUCHE);
		cases[6][11].setTypeCase(type_case_generique.OMBRE_HERBE_GAUCHE);
		cases[8][10].setTypeCase(type_case_generique.OMBRE_HERBE_GAUCHE);
		cases[8][11].setTypeCase(type_case_generique.OMBRE_HERBE_GAUCHE);


		setToFullShadow(6,9);
		cases[8][9].setTypeCase(type_case_generique.OMBRE_HERBE_GAUCHE);

		setToFullShadow(11,11);
		setToFullShadow(6,5);

		setToFullShadow(2,11);

		cases[11][2].setTypeCase(type_case_generique.OMBRE_HERBE_BAS);

		setToFullShadow(11,4);
		setToFullShadow(9,2);
		refresh();

}

private void demo5(){
	this.formStart=Form.LIGHTFORM;

	createGroundAndBorder();
	
		
	/*for(int i=0;i<largeur;i++){
		for(int j=0;j<hauteur;j++){
			if(cases[i][j].typeCase== type_case_generique.TERRE)
			light[i][j] = cases[i][j].getCadre();
		}
	}*/
	
	//Insï¿½rer ici les mobs
	this.animals.add(new Monstre(new Vector2(4f,6f),this));
	this.animals.get(0).setAnimeEspece(AnimeEspece.MONSTRE_CUBE);

	this.animals.add(new Monstre(new Vector2(6f,6f),this));
	this.animals.get(1).setAnimeEspece(AnimeEspece.MONSTRE_CUBE);

	this.animals.add(new Monstre(new Vector2(8f,6f),this));
	this.animals.get(2).setAnimeEspece(AnimeEspece.MONSTRE_CUBE);

	
	this.cases[6][8] = new Case(new Vector2(6f, 8f)){
		public void arrive(){
			int posX = (int)monde.getPerso().getPosition().x;
			int posY = (int)monde.getPerso().getPosition().y;
			if(monde.getPerso().getForm()==Form.LIGHTFORM && posX==6 && posY==8)
				//System.out.println("Activation de l'Orbe");
				monde.unlockOrb();
		}
	};
	
	//Insï¿½rer ici le coffre de l'orbe
	cases[6][8].setTypeCase(type_case_generique.COFFRE_HERBE);
	
	
	
	//L'entrï¿½e et la sortie :
	ombres[6][0] = cases[6][0].getCadre();
	cases[6][0].setTypeCase(type_case_generique.PORTE_BAS);
	
	cases[6][hauteur-1] =  new Case(new Vector2(6f,hauteur-1)){
		public void arrive(){
			unloadNiveau();
			changeLevel = new String("demo6");
		}
	};
	ombres[6][hauteur-1] = cases[6][hauteur-1].getCadre();
	cases[6][hauteur-1].setTypeCase(type_case_generique.PORTE_HAUT);
	
	bloquantes[6][0] = null;
	bloquantes[6][hauteur-1] = null;
	
	this.posStart = new Vector2(6f,0f);
	refresh();

}

public void refresh(){
	//Met à jour les cases bloquantes, ombres et light
	//Ne retire pas (encore) les valeurs anciennement présentes
	for(int i=0;i<largeur;i++){
	    for(int j=0;j<hauteur;j++){
	        if(cases[i][j].typeCase == type_case_generique.HERBE || cases[i][j].typeCase == type_case_generique.TRANSITION_HERBE_BAS || cases[i][j].typeCase == type_case_generique.TRANSITION_HERBE_DROITE || cases[i][j].typeCase == type_case_generique.TRANSITION_HERBE_GAUCHE || cases[i][j].typeCase == type_case_generique.TRANSITION_HERBE_HAUT)
	        	light[i][j] = cases[i][j].getCadre();
	        if(cases[i][j].typeCase== type_case_generique.FALAISE)
	        	bloquantes[i][j] = cases[i][j].getCadre();
	        if(cases[i][j].typeCase==type_case_generique.OMBRE || cases[i][j].typeCase==type_case_generique.OMBRE_HERBE_BAS || cases[i][j].typeCase==type_case_generique.OMBRE_HERBE_GAUCHE || cases[i][j].typeCase==type_case_generique.OMBRE_HERBE_COIN)
	        	ombres[i][j]=cases[i][j].getCadre();
	        
	    }
	}
	
	//on évite les conflits : si 2 types sont sur la même case, on met l'ombre à null
	for(int i=0;i<largeur;i++){
	    for(int j=0;j<hauteur;j++){
	    	if (ombres[i][j] != null && light[i][j] != null){
	    		ombres[i][j] = null;
	    	}else if (ombres[i][j] != null && bloquantes[i][j] != null){
	    		ombres[i][j] = null;
	    	}
	        
	    }
	}
}
public void demo6(){
	this.formStart=Form.SHADOWFORM;
	createGroundAndBorder();
	
	
		
	
	
	for(int i=5; i<largeur-1; i++)
		for(int j=4; j<hauteur-5; j++)
			createObstacle(i, j);
	
	cases[largeur-1][2] =  new Case(new Vector2(largeur-1f,2f)){
		public void arrive(){
			unloadNiveau();
			changeLevel = new String("END_GAME");
		}
	};
	cases[largeur-1][2].setTypeCase(type_case_generique.OMBRE);
	cases[0][2].setTypeCase(type_case_generique.OMBRE);
	bloquantes[largeur-1][2]=null;
	bloquantes[0][2]=null;
	
	for(int j=8; j<11; j++){
		cases[5][j]=new Case(new Vector2(5f, j));
		bloquantes[5][j] = cases[5][j].getCadre();
		cases[5][j].setTypeCase(type_case_generique.GRILLE_HERBE);
	}
	cases[5][11].setTypeCase(type_case_generique.GRILLE_HERBE_OMBRE);
	ombres[5][11]=null;
	
	for(int i=1; i<largeur-1; i++)
		cases[i][hauteur-2].setTypeCase(type_case_generique.OMBRE);
	
	createObstacle(3,hauteur-3);
	createObstacle(3,hauteur-4);
	
	cases[largeur-3][1] = new Case(new Vector2(largeur-3f, 1f)){
		public void  action(CompetenceAnimaux ca, Monde m){
			if (ca == CompetenceAnimaux.BRULER){
				Objet o =new Objet(new Vector2(largeur-3f, 1f), 1f);
				o.setTypeCase(type_objet.OBJET_FEU);
				m.addObjet(o);
				
			}
		}
	};
	cases[largeur-3][1].setTypeCase(type_case_generique.POT_HERBE);
	
	
	
	//ici les mobs...
	this.animals.add(new Monstre(new Vector2(10f,10f), this));
	this.animals.get(0).getPath().add(new Vector2(10f,10f));
	this.animals.get(0).getPath().add(new Vector2(10f,8f));
	this.animals.get(0).getPath().add(new Vector2(6f,8f));
	this.animals.get(0).getPath().add(new Vector2(6f,10f));
	
	
	this.animals.add(new Monstre(new Vector2(6f,8f), this));
	this.animals.get(1).getPath().add(new Vector2(6f,8f));
	this.animals.get(1).getPath().add(new Vector2(6f,10f));
	this.animals.get(1).getPath().add(new Vector2(10f,10f));
	this.animals.get(1).getPath().add(new Vector2(10f,8f));
	
	animals.get(0).setAnimeEspece(AnimeEspece.MONSTRE_CUBE);
	animals.get(1).setAnimeEspece(AnimeEspece.MONSTRE_CUBE);
	//ici la salamandre...
	Animal a = new Animal(new Vector2(8f,9f));
	this.animals.add(a);
	a.setCompetence(CompetenceAnimaux.BRULER);
	animals.get(2).setAnimeEspece(AnimeEspece.SALAMANDRE);
	Animal salamandre = (Animal)animals.get(2);
	salamandre.setCompetence(CompetenceAnimaux.BRULER);
	
	salamandre.getPath().add(new Vector2(9f,9f));
	salamandre.getPath().add(new Vector2(7f,9f));
	
	
	this.posStart = new Vector2(0f,2f);
	refresh();
}


public Vector2 getPosStart() {
	// TODO Auto-generated method stub
	return this.posStart;
}
	
	
	
	
	
}
