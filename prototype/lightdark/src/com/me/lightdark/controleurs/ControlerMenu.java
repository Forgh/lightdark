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
	
	private float transformTime = 10f;		//durée de la transformation (en secondes)
	private float cooldownTime = 40f;		//temps de recharge du click à la prochaine utilisation
	private boolean orbEnabled = true;		//par défaut l'orbe est utilisable
	
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
			final Vector2 savePos = new Vector2(perso.getPosition());//Sauver sa position
			perso.switchForm();
			
			orbEnabled = false; //désactive l'Orbe
			//-------------------------
			
			Timer.Task transform = new Timer.Task()
			{
			    @Override
			    public void run() {
			    	System.out.println("[DEBUG] Transformation terminée");
			    	Vector2 back = new Vector2(savePos);
			    	perso.setPosition(savePos);
					perso.switchForm();
					
			    }
			};
			
			Timer.Task cooldown = new Timer.Task()
			{
			    @Override
			    public void run() {
			    	System.out.println("[DEBUG] Orbe réactivé");
			    	orbEnabled = true; //réactive l'orbe après le cooldown
			    	
			    }
			};
			
			System.out.println("[DEBUG] Orbe utilisé");
			Timer.schedule(transform, transformTime);
			Timer.schedule(cooldown, cooldownTime);
			
		}
		/*Si la transformation est impossible, dire pourquoi
		 * Plus tard, on pourra Implémenter un FeedBack*/
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
		if(screenX >= pause.getOriginX() && screenX <= (pause.getOriginX() + pause.getWidth())
				&& screenY >= pause.getOriginY() && screenY <= (pause.getOriginY()+ pause.getHeight())){
			return true;
		}else return false;
	}
}
