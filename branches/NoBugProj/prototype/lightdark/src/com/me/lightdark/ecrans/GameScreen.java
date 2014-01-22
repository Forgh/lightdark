package com.me.lightdark.ecrans;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.me.lightdark.controleurs.ControlerAnimaux;
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
	private boolean charged = false; //tir chargé
	private ControlerEpee epee;

	private ControlerAnimaux animaux;
	
	private ControlerMenu menu;
	
	private Timer.Task chargeTime = new Timer.Task()//action à la fin du timer de charge du clic
	{
	    @Override
	    public void run() {
	    	charged = true;	//Si le timer est passé, alors le clic est chargé
	    }
	};
	
	//private boolean gamePaused = false; // en pause ou pas
	
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
		
		Timer.schedule(chargeTime, 1.5f);//lancer le timer
		if(screenX<=800){
			
			if (Input.Buttons.LEFT == button){
				control.feuPresse(screenX, screenY, 866, this.height);
			}
			if (Input.Buttons.RIGHT == button){
				control.sourisDroitPresse(screenX, screenY, 866, this.height);
			}
		}else if(Input.Buttons.LEFT == button && menu.isPausePressed(screenX, screenY)){
			if(!affSideMenu.isPaused())
				affSideMenu.showPauseFrame();
			else
				affSideMenu.hidePauseFrame();
		}
		else if(Input.Buttons.LEFT == button && menu.isOrbPressed(screenX, screenY) && !affSideMenu.isPaused()){
			menu.orbPressed();
		}
		
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		boolean wasCharged = charged;//sauvegarde de l'état de charge
		if(chargeTime.isScheduled()) chargeTime.cancel();//Si le timer était toujours en cours, l'annuler
		
		if(screenX<=800){
			if (Input.Buttons.LEFT == button){
				System.out.println("Appui chargé : "+wasCharged);
				control.feuRelache(screenX, screenY, wasCharged);
				charged = false;//
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

		if(!affSideMenu.isPaused()){
			control.update(delta);
			
			tirs.update(delta);
			epee.update(delta);
			animaux.update(delta);
		}
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
		animaux = new ControlerAnimaux(monde);
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
