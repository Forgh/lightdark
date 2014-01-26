package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Vector2;
import com.me.lightdark.modeles.Anime.AnimeType;

public class Monstre extends Anime{
	
	public final int VIE_MAX = 50;
	public final float DISTANCE_VUE = 4f;
	public final float DISTANCE_TIR = 20f;
	
	private Vector2 direction_de_base;
	
	private int puissance_tir = 1;
	private int vie;
	
	public Monstre(Vector2 position, Niveau niveau) {
		super(position, niveau);
		this.vie = this.VIE_MAX;
		direction_de_base = new Vector2(0f,0f);// Vector2.Zero;
		// TODO Auto-generated constructor stub
	}

	public AnimeType getAnimeType(){
		return AnimeType.MONSTRE;
	}
	
	public void recevoirCoup(int puissance){
		this.vie -= puissance;
		
	}
	
	public boolean isDead(){
		return this.vie<=0;
	}
	
	public Vector2 getDirectionBase(){
		return direction_de_base;
	}
	
	public void setDirectionBase(Vector2 v){
		direction_de_base = v;
	}
	/*@return true si rien ne bloque le champ de vision entre le monstre et la position donnée, false sinon
	 * @param Vector2 cible, la position de la cible a regarder
	 * Need : avoir vérifié avant que le joueur n'étais pas à portée (plus économe)
	 * HowTo : On regarde tous les xiemes de vecteur mob-cible si la case en question est bloquante*/
	public boolean champDegage(Vector2 cible){
		
		float precision = 50f;//précision avec laquelle on vérifie le vecteur mob-player
		Vector2 trace = new Vector2((cible.cpy().sub(super.getPosition())).div(precision));//Le vecteur mob-->cible divisé par la précision
		Vector2 verif = super.getPosition().cpy();
		
		
		for(int i=0; i<(int)precision; i++){//tous les xièmes
			verif.add(trace);//on progresse de 1/x vers le joueur	
			if(super.getNiveau().getCollision((int)verif.x, (int)verif.y)!=null)//si verif est sur une bloquante
				return false;//alors le mob ne peut pas nous voir
		}
		
		return true;//si on arrive là, c'est que le joueur est visible pour le mob
	}
	

	@Override
	public void setTaming(boolean t) {
	}
	
	@Override
	public void setTamer(Perso p){
	}


}
