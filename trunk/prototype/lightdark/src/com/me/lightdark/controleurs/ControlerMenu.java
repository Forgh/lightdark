package com.me.lightdark.controleurs;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.math.Vector2;
import com.me.lightdark.modeles.Monde;
import com.me.lightdark.modeles.Perso;


public class ControlerMenu {
	private Perso perso;
	private Button orbe;
	private Button pause;
	private Monde monde;
	
	private float transformTime = 10;		//durï¿½e de la transformation (en secondes)
	private float cooldownTime = 40;		//temps de recharge du click ï¿½ la prochaine utilisation
	private boolean orbEnabled = true;		//par dï¿½faut l'orbe est utilisable
	
	public ControlerMenu(Monde monde) {
		this.perso = monde.getPerso();
		this.monde = monde;
		this.orbe = monde.getOrbe();
		this.orbe.setSize(50,50);
		this.orbe.addListener(new ClickListener());
		
		this.pause = monde.getPause();
		this.pause.setSize(64, 32);
		this.pause.addListener(new ClickListener());
	}
	
	public void orbPressed(){
		
		int posX =(int)perso.getPosition().x;//La position de la case sur laquelle est le joueur
		int posY = (int)perso.getPosition().y;
		
		//S'il est sur une ombre et que l'orbe est activé
		if (monde.getNiveau().get(posX, posY).getTypeCase()=="OMBRE" && orbEnabled){
			perso.switchForm();
			
			orbEnabled = false; //désactive l'Orbe
			//-------------------------
			
			Timer.Task transform = new Timer.Task()
			{
			    @Override
			    public void run() {
			    	System.out.println("[DEBUG] Transformation terminée");
			    	//Vector2 back = new Vector2(savePos);
			    	perso.setPosition(monde.getNiveau().getCloseShadow(perso.getPosition() ));
					perso.switchForm();
					System.out.println("[DEBUG] Orbe réactivé" + perso.getPosition().x + " => " + + perso.getPosition().y);
					
			    }
			};
			
			Timer.Task cooldown = new Timer.Task()
			{
			    @Override
			    public void run() {
			    	System.out.println("[DEBUG] Orbe rï¿½activï¿½");
			    	orbEnabled = true; //rï¿½active l'orbe aprï¿½s le cooldown
			    	
			    }
			};
			
			System.out.println("[DEBUG] Orbe utilisé");
			Timer.schedule(transform, transformTime);
			Timer.schedule(cooldown, cooldownTime);
			
		}
		/*Si la transformation est impossible, dire pourquoi
		 * Plus tard, on pourra Implï¿½menter un FeedBack*/
		else if(!orbEnabled)
			System.out.println("[DEBUG] L'orbe doit se recharger !");
		else System.out.println("[DEBUG] Tu n'es pas sur une ombre !");
	
		
		
		
	}

	
	public Button getOrbe(){
		return this.orbe;
	}
	
	public Button getPause(){
		return this.pause;
	}
	
	public boolean isPausePressed(int screenX, int screenY){
		
		screenY = 800-screenY;
	
		if((float)screenX >= pause.getX() && (float)screenX <= (pause.getX() + pause.getWidth())
				&& (float)screenY >= pause.getY() && (float)screenY <= (pause.getY()+ pause.getHeight())){
			return true;
			
		}else return false;
	}
	
	public boolean isOrbPressed(int screenX, int screenY){
		
		screenY = 800-screenY;
	
		if((float)screenX >= orbe.getX() && (float)screenX <= (orbe.getX() + orbe.getWidth())
				&& (float)screenY >= orbe.getY() && (float)screenY <= (orbe.getY()+ orbe.getHeight())){
			return true;
			
		}else return false;
	}
}
