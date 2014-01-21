package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.me.lightdark.modeles.Anime.AnimeType;

public class Animal extends Anime {

	boolean taming;
	Perso tamer;//le controleur (=null)
	
	public Animal(Vector2 position) {
		super(position);
		this.taming=false;
		tamer=null;
	}
	
	public AnimeType getAnimeType(){
		return AnimeType.ANIMAL;
	}

	public boolean isTamed(){
		return this.taming;
	}

	@Override
	public void setTaming(boolean t) {
		// TODO Auto-generated method stub
		this.taming=t;
	}
	
	@Override
	public void setTamer(Perso p){
		tamer = p;
	}
	
	@Override
	public void update(float delta) {
		super.setTemps(super.temps()+delta);
		
		if(!taming)
			super.setPosition(super.getPosition().add(super.getRapidite().cpy().scl(delta)));
		else {
			super.setPosition(tamer.getPosition().cpy());
			if(tamer.getEtat()!=Dark.TAMING)
				taming = false;
		}
		
		super.getCadre().setPosition(super.getPosition());
	}
}
