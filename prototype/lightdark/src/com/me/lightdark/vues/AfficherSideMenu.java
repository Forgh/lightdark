package com.me.lightdark.vues;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.me.lightdark.modeles.Monde;


public class AfficherSideMenu {

	private Monde monde;
	private TextureRegion imgOrbe;
	private TextureRegion imgPause;
	private TextureRegion imgHealth;
	
	private boolean frameUpState;
	private TextureRegion frameUp;
	private SpriteBatch spriteBatch;
	
	private boolean end_msg_on = false;
	private boolean win_this_game = false;
	
	
	public void setSize (int w, int h) {
		
	}
	
	
	
	public AfficherSideMenu(Monde monde) {
		this.monde = monde;
		
		this.frameUpState  = false;
		
		spriteBatch = new SpriteBatch();
		chargerTextures();
	}

	public boolean isPaused(){
		return this.frameUpState;
	}
	
	private void chargerTextures() {
		this.imgOrbe = new TextureRegion(new Texture("images/orbe.png"));
		this.imgPause = new TextureRegion(new Texture("images/start.png"));
		this.imgHealth = new TextureRegion(new Texture("images/hp.png"));
		this.frameUp  = new TextureRegion(new Texture("images/frameUp.png"));
	}
	
	public void setEndMsgOn(boolean win){
		this.end_msg_on = true;
		this.win_this_game = win;
	}
	
	public void drawEndMsg(boolean win){
		//spriteBatch.begin();
		Color c1 = spriteBatch.getColor();
		spriteBatch.setColor(c1.r, c1.g, c1.b, 0.9f);
		spriteBatch.draw(this.frameUp, 25,100, 800, 600);
		// texte de fin en fonction du score ok pas ok
		
		spriteBatch.setColor(c1);

	}
	
	public void showPauseFrame(){
		this.frameUpState = true;
	}
	
	public void hidePauseFrame(){
		this.frameUpState = false;
	}
	
	
	private void drawFrameUpPause(){
		Color c1 = spriteBatch.getColor();
		spriteBatch.setColor(c1.r, c1.g, c1.b, 0.9f);
		spriteBatch.draw(this.frameUp, 25,100, 800, 600);
		// + toutes les options
		spriteBatch.setColor(c1);
		
	}
	
	public void render() {
		spriteBatch.begin();
		drawOrbe();
		drawPause();
		drawHealth();
		if (this.frameUpState ) drawFrameUpPause();
		if (this.end_msg_on) drawEndMsg(this.win_this_game);
		spriteBatch.end();
	}

	public void updateHealth(){
		//spriteBatch.begin();
		drawHealth();
		//spriteBatch.end();
	}
	
	private void drawPause(){
		Button p = monde.getPause();
		spriteBatch.draw(this.imgPause, p.getX(), p.getY(), 64, 32);
	
	}
	
	private void drawHealth(){
		for(int i=0; i<monde.getPerso().getHealth();i++){
			spriteBatch.draw(this.imgHealth, 900+i*32, 600, 32, 32);
		}
	}
	private void drawOrbe(){
		Button o = monde.getOrbe();
		
		spriteBatch.draw(this.imgOrbe, o.getX(), o.getY(), 50, 50);
	}
	
	
	

}
