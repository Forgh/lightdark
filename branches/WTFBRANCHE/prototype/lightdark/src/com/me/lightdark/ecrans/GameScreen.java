package com.me.lightdark.ecrans;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.me.lightdark.controleurs.ControlerAnimaux;
import com.me.lightdark.controleurs.ControlerEpee;
import com.me.lightdark.controleurs.ControlerMenu;
import com.me.lightdark.controleurs.ControlerPerso;
import com.me.lightdark.controleurs.ControlerProjectiles;
import com.me.lightdark.modeles.Dark;
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
	private Sound gameOver = Gdx.audio.newSound(Gdx.files.internal("sound/gameover.wav"));
	private Sound darkness = Gdx.audio.newSound(Gdx.files.internal("sound/darkness.mp3"));
	private Sound chargedSound = Gdx.audio.newSound(Gdx.files.internal("sound/charged.wav"));


	private boolean finish;
	private ControlerAnimaux animaux;
	
	private ControlerMenu menu;
	
	private Timer.Task chargeTime = new Timer.Task()//action à la fin du timer de charge du clic
	{
	    @Override
	    public void run() {
	    	charged = true;	//Si le timer est passé, alors le clic est chargé
	    	chargedSound.play();
	    }
	};
	
	//private boolean gamePaused = false; // en pause ou pas
	
	private int width;
	private int height;
	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.Q){
			control.gauchePresse();
			monde.getPerso().toWalking();
		}
			
		if (keycode == Keys.D){
			control.droitPresse();
			monde.getPerso().toWalking();
		}
			
		if (keycode == Keys.Z){
			control.hautPresse();
			monde.getPerso().toWalking();
		}
			
		if (keycode == Keys.S){
			control.basPresse();
			monde.getPerso().toWalking();
		}
			
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.Q){
			control.gaucheRelache();
			monde.getPerso().toIdle();
		}
			
		if (keycode == Keys.D){
			control.droitRelache();
			monde.getPerso().toIdle();

		}
			
		if (keycode == Keys.Z){
			control.hautRelache();
			monde.getPerso().toIdle();

		}
			
		if (keycode == Keys.S){
			control.basRelache();
			monde.getPerso().toIdle();

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
		if(chargeTime.isScheduled()) chargeTime.cancel();
		Timer.schedule(chargeTime, 1.5f);//lancer le timer
		
		if(screenX<=800){//Si clic dans la zone de jeu
			if (Input.Buttons.LEFT == button)
				control.feuPresse(screenX, screenY, 866, this.height);
		}
		else if(Input.Buttons.LEFT == button && menu.isPausePressed(screenX, screenY)){
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
				//System.out.println("Appui chargé : "+wasCharged);
				//control.feuRelache(screenX, screenY, wasCharged);
				control.feuRelache(screenX, screenY, 866, this.height, wasCharged);
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
		if(!affSideMenu.isPaused() && !this.finish){
			
			control.update(delta);
			
			tirs.update(delta);
			epee.update(delta);
			animaux.update(delta);
		}
		affMonde.render();
		affSideMenu.render();
		
		if(monde.getPerso().getHealth()==0){
			monde.getPerso().setDying();
			darkness.pause(5);
			gameOver.play(0.3f);
			monde.getPerso().refill();
			reload(monde.getNiveau().getLevelName());
			darkness.resume();
			
		}
		
		if (monde.getNiveau().isLevelChanged() != null){
			if (!monde.getNiveau().isLevelChanged().equals("END_GAME")){
				System.out.println(monde.getNiveau().isLevelChanged());
				String s = new String(monde.getNiveau().isLevelChanged());
				this.monde.getPerso().setRapidite(new Vector2(0f,0f));
				reload(s);
			}else{
				affSideMenu.setEndMsgOn(true);
				this.finish = true;
				
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		affMonde.setSize(width, height);
		this.width = width;
		this.height = height;
	}

	public void reload(String niv){
		monde = new Monde(niv);
		affMonde = new AfficherMonde(monde, true);
		affSideMenu = new AfficherSideMenu(monde);
		control = new ControlerPerso(monde);
		tirs = new ControlerProjectiles(monde, monde.getPerso());
		epee = new ControlerEpee(monde, monde.getPerso());
		animaux = new ControlerAnimaux(monde);
		menu = new ControlerMenu(monde);
		affMonde.setSize(width, height);
	}
	@Override
	public void show() {
		reload("demo1");
		//monde.unlockOrb();
		darkness.loop(0.1f);

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
