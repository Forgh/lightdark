package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.me.lightdark.modeles.Anime.AnimeType;

public class Animal extends Anime {

	boolean taming;
	
	public Animal(Vector2 position) {
		super(position);
		this.taming=false;
		// TODO Auto-generated constructor stub
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
}
