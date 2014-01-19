package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Vector2;
import com.me.lightdark.modeles.Anime.AnimeType;

public class Monstre extends Anime{
	
	public final float DISTANCE_VUE = 4f;
	

	public Monstre(Vector2 position) {
		super(position);
		// TODO Auto-generated constructor stub
	}

	public AnimeType getAnimeType(){
		return AnimeType.MONSTRE;
	}

	@Override
	public void setTaming(boolean t) {
		// TODO Auto-generated method stub
		
	}

}
