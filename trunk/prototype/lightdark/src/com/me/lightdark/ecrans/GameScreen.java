package com.me.lightdark.ecrans;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
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
		
		if(screenX<=800){
				// TODO Auto-generated method stub
			if (Input.Buttons.LEFT == button){
				control.feuPresse(screenX, screenY, 866, this.height);
			}
			if (Input.Buttons.RIGHT == button){
				control.sourisDroitPresse(screenX, screenY, 866, this.height);
			}
		}else if(Input.Buttons.LEFT == button && menu.isPausePressed(screenX, screenY)){
			if(!affSideMenu.isPaused()){
				affSideMenu.showPauseFrame();
			}
			else {
				affSideMenu.hidePauseFrame();
			}
		}
		else if(Input.Buttons.LEFT == button && menu.isOrbPressed(screenX, screenY)){
			menu.orbPressed();
		}
		
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if(screenX<=800){
			if (Input.Buttons.LEFT == button){
				control.feuRelache(screenX, screenY);
			}
			if (Input.Buttons.RIGHT == button){
				control.sourisDroitRelache(screenX, screenY);
			}
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
