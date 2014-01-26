package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.me.lightdark.modeles.Anime.AnimeType;

public class Animal extends Anime {

	private boolean taming;
	
	private Perso tamer;//le controleur (=null)
	
	private CompetenceAnimaux comptanima;
	
	private Animal me;
	
	public Animal(Vector2 position) {
		super(position);
		this.taming=false;
		tamer=null;
		me = this;
	}
	
	public AnimeType getAnimeType(){
		return AnimeType.ANIMAL;
	}

	public boolean isTamed(){
		return this.taming;
	}
	
	private Timer.Task reinitFrame = new Timer.Task()
	{
	    @Override
	    public void run() {
	    	resetCadre();
	    }
	};

	@Override
	public void setTaming(boolean t) {
		// TODO Auto-generated method stub
		this.taming=t;
	}
	
	@Override
	public void setTamer(Perso p){
		tamer = p;
	}
	
	public void setCompetence( CompetenceAnimaux ca){
		comptanima = ca;
	}
	
	public CompetenceAnimaux getCompetence(){
		return comptanima;
	}
	
	public void resetCadre(){
		super.setCadre(super.newCadre(getPosition()));
	}
	
	@Override
	public void update(float delta) {
		super.setTemps(super.temps()+delta);
		
		if(!taming){
			if(super.getCadre().height==0 && super.getCadre().width==0 && !reinitFrame.isScheduled()){
				Timer.schedule(reinitFrame, 0.3f);
				this.tamer = null;//il n'y a plus de tamer
			}
			if(!reinitFrame.isScheduled() && tamer == null)//ne doit bouger qu'après "s'être remis du taming"
				super.setPosition(super.getPosition().add(super.getRapidite().cpy().scl(delta)));
			
			}
		else {//if taming
		
			if(super.getCadre()!=null)
				super.setCadre(null);
				
			super.setPosition(tamer.getPosition().cpy());//.sub(TAILLE/4, TAILLE/4));
			if(tamer.getEtat()!=Dark.TAMING)
				taming = false;
		}
		
		super.getCadre().setPosition(super.getPosition());
	}

	@Override
	public void demarrerCompetence(CompetenceAnimaux ca) {
		// TODO Auto-generated method stub
		super.demarrerCompetence(comptanima);
		boolean actionTimed = false;
		
		if (comptanima == CompetenceAnimaux.COURRIR){
			tamer.setVitesse(tamer.VITESSE * 4);
			actionTimed = true;
			
			
		}else if (comptanima == CompetenceAnimaux.CHANTER){
			tamer.setTamingDetectable(true);
			actionTimed = true;
			
		}else if (comptanima == CompetenceAnimaux.BRULER){
			tamer.setTamingDetectable(true);
			actionTimed = true;
		}
		
		
		
		
		if (actionTimed){
			Timer.Task fin = new Timer.Task() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (me != null) me.stopperCompetence(me.comptanima);
				}
			};
			Timer.schedule(fin, 3f); // DUREE de l'effet
		}
	}
	
	public void demarrerCompetence() {
		// TODO Auto-generated method stub
		this.demarrerCompetence(this.comptanima);
		
	}

	public void stopperCompetence(CompetenceAnimaux ca) {
		// TODO Auto-generated method stub
		super.stopperCompetence(ca);
		
		try{
			if (ca == CompetenceAnimaux.COURRIR){
				tamer.resetVitesse();
			}else if (comptanima == CompetenceAnimaux.CHANTER){
				tamer.setTamingDetectable(false);
			}else if (comptanima == CompetenceAnimaux.BRULER){
				Case c = tamer.getMonde().getNiveau().get((int)super.getPosition().x, (int)super.getPosition().y);
				c.action(ca, tamer.getMonde());
			}
	}catch (NullPointerException npe){
		
	}finally{
			
		}
		
	}
}
