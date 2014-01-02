package com.me.lightdark.ecrans;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.me.lightdark.controleurs.ControlerEpee;
import com.me.lightdark.controleurs.ControlerMenu;
import com.me.lightdark.controleurs.ControlerPerso;
import com.me.lightdark.controleurs.ControlerProjectiles;
import com.me.lightdark.modeles.Monde;
import com.me.lightdark.vues.AfficherMonde;
import com.me.lightdark.vues.AfficherSideMenu;

public class GameScreen extends Stage implements Screen, InputProcessor{

	private static final int VIRTUAL_WIDTH = 800;
    private static final int VIRTUAL_HEIGHT = 800;
    private static final float ASPECT_RATIO =
        (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;
    private Rectangle viewport;
    
	private Monde monde;
	private AfficherMonde affMonde;
	private AfficherSideMenu affSideMenu;
	
	private ControlerPerso control;
	private ControlerProjectiles tirs;
	private ControlerEpee epee;

	private ControlerMenu menu;
	
	private int width;
	private int height;
	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.Q){
			control.gauchePresse();
		}
			
		if (keycode == Keys.D){
			control.droitPresse();
		}
			
		if (keycode == Keys.Z){
			control.hautPresse();
		}
			
		if (keycode == Keys.S){
			control.basPresse();
		}
			
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.Q){
			control.gaucheRelache();
		}
			
		if (keycode == Keys.D){
			control.droitRelache();
		}
			
		if (keycode == Keys.Z){
			control.hautRelache();
		}
			
		if (keycode == Keys.S){
			control.basRelache();
		}
			
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if (Input.Buttons.LEFT == button){
			control.feuPresse(screenX, screenY, this.width, this.height);
		}
		if (Input.Buttons.RIGHT == button){
			control.sourisDroitPresse(screenX, screenY, this.width, this.height);
		}
		if(menu.getOrbe().getClickListener().isPressed()){
			System.out.println("GameScreen");
			menu.orbPressed();
			//TODO update les control pour le passage à l'autre forme
			//control.update(delta);
		}
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if (Input.Buttons.LEFT == button){
			control.feuRelache(screenX, screenY);
		}
		if (Input.Buttons.RIGHT == button){
			control.sourisDroitRelache(screenX, screenY);
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		this.affMonde.getCam().update();
		this.affMonde.getCam().apply(Gdx.gl10);
 
        // set viewport
        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
                          (int) viewport.width, (int) viewport.height);
        
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		control.update(delta);
		
		tirs.update(delta);
		epee.update(delta);

		affMonde.render();
		affSideMenu.render();
	}

	@Override
	public void resize(int width, int height) {
		// calculate new viewport
        float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);
        if(aspectRatio > ASPECT_RATIO)
        {
            scale = (float)height/(float)VIRTUAL_HEIGHT;
            crop.x = (width - VIRTUAL_WIDTH*scale)/2f;
        }
        else if(aspectRatio < ASPECT_RATIO)
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
            crop.y = (height - VIRTUAL_HEIGHT*scale)/2f;
        }
        else
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
        }
 
        float w = (float)VIRTUAL_WIDTH*scale;
        float h = (float)VIRTUAL_HEIGHT*scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
        
		affMonde.setSize(width, height);
		this.width = width;
		this.height = height;
	}

	@Override
	public void show() {
		monde = new Monde();
		affMonde = new AfficherMonde(monde, true);
		affSideMenu = new AfficherSideMenu(monde);
		control = new ControlerPerso(monde);
		tirs = new ControlerProjectiles(monde, monde.getPerso());
		epee = new ControlerEpee(monde, monde.getPerso());
		menu = new ControlerMenu(monde);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
	}
	
}
