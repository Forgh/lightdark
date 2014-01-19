package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Vector2;
import com.me.lightdark.modeles.Anime.AnimeType;

public class Monstre extends Anime{

	public Monstre(Vector2 position) {
		super(position);
		// TODO Auto-generated constructor stub
	}

	public AnimeType getAnimeType(){
		return AnimeType.MONSTRE;
	}

}
