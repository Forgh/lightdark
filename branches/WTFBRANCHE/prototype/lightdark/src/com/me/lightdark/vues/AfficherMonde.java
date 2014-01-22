package com.me.lightdark.vues;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.me.lightdark.modeles.Animal;
import com.me.lightdark.modeles.Anime;
import com.me.lightdark.modeles.Anime.AnimeEspece;
import com.me.lightdark.modeles.Case;
import com.me.lightdark.modeles.Dark;
import com.me.lightdark.modeles.Epee;
import com.me.lightdark.modeles.Form;
import com.me.lightdark.modeles.Monde;
import com.me.lightdark.modeles.Niveau;
import com.me.lightdark.modeles.Perso;
import com.me.lightdark.modeles.Projectile;

public class AfficherMonde {

	private float CAM_LARG = 15f;
	private float CAM_HAUT = 13f;
	private static final float DUREE_IMAGES = 0.06f;
	
	private AfficherSideMenu menu;
	private Monde monde;
	private OrthographicCamera cam;
	
	
	private TextureRegion imgSol;
	private TextureRegion imgObstacle;
	private TextureRegion imgOmbre;
	private TextureRegion imgLightForm;
	private TextureRegion imgDarkForm;
	private TextureRegion imgDarkFormTaming;
	private TextureRegion imgProjectile;
	private TextureRegion imgSword;
	private TextureRegion imgAnimal;
	private TextureRegion imgMonstreCube;
	
	private SpriteBatch spriteBatch;
	
	private boolean deboguage = false;
	
	private int width;
	private int height;
	// ppu: Pixel Par Unite
	private float ppuX;	
	private float ppuY;	
	
	public void setSize (int w, int h) {
		this.width = w;
		this.height = h;
		
		ppuX = (float)width / CAM_LARG;
		ppuY = (float)height / CAM_HAUT;
	}
	
	public int getWidth(){
		return this.width;
	}
	public AfficherMonde( Monde monde, boolean debug) {
		this.monde = monde;
		//this.CAM_HAUT = (float) this.monde.getNiveau().getHauteur();
		//this.CAM_LARG = (float) this.monde.getNiveau().getLargeur();
		this.cam =  new OrthographicCamera(CAM_LARG / 2f,CAM_HAUT / 2f);
		this.cam.position.set(CAM_LARG / 2f, CAM_HAUT / 2f, 0); // ici on place la camera au centre
		this.deboguage = debug;
		
		spriteBatch = new SpriteBatch();
		chargerTextures();
	}


	private void chargerTextures() {
		// TODO mettre a jour avec des atlas
		this.imgLightForm = new TextureRegion(new Texture(Gdx.files.internal("images/light.png")));
		this.imgDarkForm = new TextureRegion(new Texture(Gdx.files.internal("images/dark.png")));
		this.imgDarkFormTaming = new TextureRegion(new Texture(Gdx.files.internal("images/taming.png")));
		this.imgSol = new TextureRegion(new Texture(Gdx.files.internal("images/herbe_seche.png")));
		this.imgObstacle = new TextureRegion(new Texture(Gdx.files.internal("images/roche.png")));
		this.imgOmbre= new TextureRegion(new Texture(Gdx.files.internal("images/ombre.png")));
		this.imgProjectile = new TextureRegion(new Texture(Gdx.files.internal("images/projectile.png")));
		this.imgSword = new TextureRegion(new Texture(Gdx.files.internal("images/sword.png")));
		this.imgAnimal = new TextureRegion(new Texture(Gdx.files.internal("images/cat_laptop.png")));
		this.imgMonstreCube = new TextureRegion(new Texture(Gdx.files.internal("images/monstre_cube.png")));
		// TODO rajouter pour toutes les cases
		 
	}
	
	public void render() {
		spriteBatch.begin();

		// dessiner strate 1
		drawMap();
		// dessiner strate 2
		drawProjectile();
		drawSword();
		// dessiner strate 3
		drawAnimals();
		drawPerso();
		
		spriteBatch.end();
	}

	private void drawMap(){
		Niveau niveau = monde.getNiveau();
		Array<Case> cases=  monde.getAffichable(this.width, this.height);
		for(int i = 0; i<cases.size;i++){
			 Case c = cases.get(i);
			 if(c.getTypeCase().equals("TERRE")){
				 spriteBatch.draw(this.imgSol, c.getPosition().x * ppuX, c.getPosition().y * ppuY, c.TAILLE * ppuX, c.TAILLE * ppuY);
			 }
			 else if(c.getTypeCase().equals("MONTAGNE")){
				 spriteBatch.draw(this.imgObstacle, c.getPosition().x * ppuX, c.getPosition().y * ppuY, c.TAILLE * ppuX, c.TAILLE * ppuY);
			 }
			 else if(c.getTypeCase().equals("OMBRE")){
				 spriteBatch.draw(this.imgOmbre, c.getPosition().x * ppuX, c.getPosition().y * ppuY, c.TAILLE * ppuX, c.TAILLE * ppuY);
			 }
		}
		/*for(int i = 0; i<monde.niveau.size;i++){
			 Case c = monde.getCase().get(i);
			 spriteBatch.draw(this.imgCase, c.getPosition().x * ppuX, c.getPosition().y * ppuY, c.TAILLE * ppuX, c.TAILLE * ppuY);
		}*/
	}
	
	private void drawPerso(){
		Perso p = monde.getPerso();
		if(p.getForm()==Form.LIGHTFORM)
			spriteBatch.draw(this.imgLightForm, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
		else if(p.getForm()==Form.SHADOWFORM){
			if(!(p.getEtat()==Dark.TAMING))//ShadowForm classique
				spriteBatch.draw(this.imgDarkForm, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
			else //ShadowForm Taming
				spriteBatch.draw(this.imgDarkFormTaming, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
		}
	}
	
	private void drawAnimals(){
		Array<Anime> project = monde.getAnime();
		 for(int i=0;i<project.size;i++){
			 if (project.get(i).getAnimeEspece() == AnimeEspece.MONSTRE_CUBE){
				 spriteBatch.draw(this.imgMonstreCube, project.get(i).getPosition().x * ppuX, project.get(i).getPosition().y * ppuY, project.get(i).TAILLE * ppuX, project.get(i).TAILLE * ppuY);
			 }else{
				 spriteBatch.draw(this.imgAnimal, project.get(i).getPosition().x * ppuX, project.get(i).getPosition().y * ppuY, project.get(i).TAILLE * ppuX, project.get(i).TAILLE * ppuY);
			 }
			
		 }
	}
	
	
	private void drawProjectile(){
		 Array<Projectile> project = monde.getProjectile();
		 for(int i=0;i<project.size;i++){
			 spriteBatch.draw(this.imgProjectile, project.get(i).getPosition().x * ppuX, project.get(i).getPosition().y * ppuY, project.get(i).TAILLE * ppuX, project.get(i).TAILLE * ppuY);
		 }
	}
	
	private void drawSword(){
		 Array<Epee> sword = monde.getEpee();
		 for(int i=0;i<sword.size;i++){
			 spriteBatch.draw(this.imgSword, sword.get(i).getPosition().x * ppuX, sword.get(i).getPosition().y * ppuY, sword.get(i).TAILLE * ppuX, sword.get(i).TAILLE * ppuY);
		 }
	}

}
