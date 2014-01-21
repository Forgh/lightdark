package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Vector2;
import com.me.lightdark.modeles.Anime.AnimeType;

public class Monstre extends Anime{
	
	public final int VIE_MAX = 50;
	public final float DISTANCE_VUE = 4f;
	public final float DISTANCE_TIR = 2f;
	
	private int puissance_tir = 20;
	private int vie;
	
	public Monstre(Vector2 position) {
		super(position);
		// TODO Auto-generated constructor stub
	}

	public AnimeType getAnimeType(){
		return AnimeType.MONSTRE;
	}
	
	public void recevoirCoup(int puissance){
		this.vie -= puissance;
		
	}

	@Override
	public void setTaming(boolean t) {
		// TODO Auto-generated method stub
		
	}

}
