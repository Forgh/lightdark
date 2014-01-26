package com.me.lightdark.modeles;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class Monde {

	Niveau niveau;
	
	private Perso perso;
	
	Array<Projectile> projectiles;
	Array<Projectile> fleches;
	Array<Objet> objets;

	Array<Epee> sword;

	static boolean debloquerEpee = false;
	static boolean debloquerOrbe = false;

	private Button pause;
	private Button orbe;
	private boolean orbEnabled = true;		//par dï¿½faut l'orbe est utilisable

	// Getters -----------
	public Array<Projectile> getProjectile() {
		return projectiles;
	}
	
	public Array<Projectile> getFleche() {
		return fleches;
	} 
	
	public Array<Objet> getObjet() {
		return objets;
	} 
	
	public Array<Anime> getAnime() {
		return this.niveau.getAnime();
	}
	
	public boolean hasUnlockedSword(){
		return this.debloquerEpee;
	}
	
	public boolean hasUnlockedOrb(){
		return this.debloquerOrbe;
	}
	
	public void unlockSword(){
		this.debloquerEpee=true;
	}
	
	public void unlockOrb(){
		this.debloquerOrbe=true;
	}
	
	public Array<Epee> getEpee() {
		return sword;
	}
	
	public boolean isOrbEnabled(){
		return this.orbEnabled;
	}

	public void switchOrb(boolean t){
		this.orbEnabled=t;
	}
	
	public Perso getPerso() {
		return perso;
	}
	
	public void setPerso(Perso p) {
		perso = p;
	}
	
	public Niveau getNiveau(){
		return niveau;
	}

	public Monde(String niv) {
		// TODO Charger une map.
		
		this.niveau = new Niveau(niv, this);
		
		projectiles = this.niveau.getProjectile();
		fleches = this.niveau.getFleche();
		objets = this.niveau.getObjet();
		sword = this.niveau.getEpee();
		
		this.perso = new Perso(niveau.getPosStart(), this);
		this.perso.setForm(niveau.getFormStart());
		
		//niveau.loadStartLevel("demo1", this);
		
		this.perso.setTamingDetectable(false);
		
		this.pause = new Button();
		
		this.pause.setX(930);
		this.pause.setY(700);
		
		this.orbe = new Button();
		
		this.orbe.setX(872);
		this.orbe.setY(400);
		

		//cases.add(new Case(new Vector2(5,1), false));

	}
	
	public void lancerProjectile(Vector2 vect, Vector2 cible){
		
			Projectile v = new Projectile(perso,(new Vector2(perso.getPosition() )),vect);
			projectiles.add(v);
			Case c = this.niveau.get((int) cible.x, (int) cible.y);
			if (c != null){
				v.setCaseCible(c); // on ajoute les coord. de la cible
			}
			
	}
	
	
	public void lancerProjectileParMonstre(Vector2 monstre, Vector2 cible){
		//if(perso.getEtat()!=Dark.GRABBING)
		
		Projectile v = new Projectile((new Vector2(monstre)),cible.cpy().sub(monstre));//départ projectile puis direction
		fleches.add(v);
		//System.out.println("[DEBUG] ajout d'une flèche");
		Case c = this.niveau.get((int) cible.x, (int) cible.y);
		if (c != null){
			v.setCaseCible(c); // on ajoute les coord. de la cible
		}
			
	}
	public void frapperEpee(Vector2 vect, Vector2 cible){

		if(perso.getForm() != Form.SHADOWFORM && hasUnlockedSword()){
			Epee s = new Epee(perso,new Vector2(perso.getPosition()),vect);
			sword.add(s);
		}
					
	}
	
	/*
	 * Se charge de lancer dans la map un grappin-retour venant du grappin source
	 * @return Projectile boomerang le projectile de retour, pour pouvoir le suivre puis l'éliminer
	 */
	public Projectile lancerBoomerang(Projectile source){
		Projectile boomerang = new Projectile(source);
		projectiles.add(boomerang);
		boomerang.setCaseCible(new Case (perso.getPosition()));
		return boomerang;
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
	
	public void addObjet(Objet o){
		this.objets.add(o);
	}

	public Button getPause() { 
		return pause;
	}

	public void setPause(Button pause) {
		this.pause = pause;
	}

	public Button getOrbe() {
		return orbe;
	}

	public void setOrbe(Button orbe) {
		this.orbe = orbe;
	}
	
	public void setLvl(String l) {
		this.niveau= new Niveau(l, this);
		//System.out.println("l");
	}
	

}
