package com.me.lightdark.vues;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.me.lightdark.modeles.Case;
import com.me.lightdark.modeles.Monde;
import com.me.lightdark.modeles.Niveau;
import com.me.lightdark.modeles.Perso;
import com.me.lightdark.modeles.Projectile;

public class AfficherMonde {

	private static final float CAM_LARG = 11f;
	private static final float CAM_HAUT = 11f;
	private static final float DUREE_IMAGES = 0.06f;
	
	private Monde monde;
	private OrthographicCamera cam;
	
	private TextureRegion imgCase;
	private TextureRegion imgPerso;
	private TextureRegion imgProjectile;
	
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
		ppuY = (float)height / CAM_LARG;
	}
	
	
	public AfficherMonde( Monde monde, boolean debug) {
		this.monde = monde;
		this.cam =  new OrthographicCamera(CAM_HAUT / 2f,CAM_HAUT / 2f);
		this.cam.position.set(CAM_LARG / 2f, CAM_HAUT / 2f, 0); // ici on place la camera au centre
		this.deboguage = debug;
		
		spriteBatch = new SpriteBatch();
		chargerTextures();
	}


	private void chargerTextures() {
		// TODO mettre a jour avec des atlas
		this.imgPerso = new TextureRegion(new Texture(Gdx.files.internal("images/perso.png")));
		this.imgCase = new TextureRegion(new Texture(Gdx.files.internal("images/herbe_seche.png")));
		this.imgProjectile = new TextureRegion(new Texture(Gdx.files.internal("images/projectile.png")));
		// TODO rajouter pour toutes les cases
		 
	}
	
	public void render() {
		spriteBatch.begin();

		// dessiner strate 1
		drawMap();
		// dessiner strate 2
		drawProjectile();
		// dessiner strate 3
		drawPerso();
		
		spriteBatch.end();
	}

	private void drawMap(){
		Niveau niveau = monde.getNiveau();
		Array<Case> cases=  monde.getAffichable(this.width, this.height);
		for(int i = 0; i<cases.size;i++){
			 Case c = cases.get(i);
			 spriteBatch.draw(this.imgCase, c.getPosition().x * ppuX, c.getPosition().y * ppuY, c.TAILLE * ppuX, c.TAILLE * ppuY);
		}
		/*for(int i = 0; i<monde.niveau.size;i++){
			 Case c = monde.getCase().get(i);
			 spriteBatch.draw(this.imgCase, c.getPosition().x * ppuX, c.getPosition().y * ppuY, c.TAILLE * ppuX, c.TAILLE * ppuY);
		}*/
	}
	
	private void drawPerso(){
		Perso p = monde.getPerso();
		spriteBatch.draw(this.imgPerso, p.getPosition().x * ppuX, p.getPosition().y * ppuY, p.TAILLE * ppuX, p.TAILLE * ppuY);
	}
	
	
	private void drawProjectile(){
		 Array<Projectile> project = monde.getProjectile();
		 for(int i=0;i<project.size;i++){
			 spriteBatch.draw(this.imgProjectile, project.get(i).getPosition().x * ppuX, project.get(i).getPosition().y * ppuY, project.get(i).TAILLE * ppuX, project.get(i).TAILLE * ppuY);
		 }
	}
	

}
