package com.me.lightdark.controleurs;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.me.lightdark.controleurs.ControlerPerso.Touches;
import com.me.lightdark.modeles.Animal;
import com.me.lightdark.modeles.Anime;
import com.me.lightdark.modeles.Anime.AnimeType;
import com.me.lightdark.modeles.Dark;
import com.me.lightdark.modeles.Form;
import com.me.lightdark.modeles.Monde;
import com.me.lightdark.modeles.Monstre;

public class ControlerAnimaux {
	private Monde monde;
	private Array<Anime> animaux;
	
	private Array<Rectangle> collision;
	
	private Map<Anime, Integer> reperer = new HashMap<Anime, Integer>();
	
	final Vector2 vecteurNul = new Vector2(0f,0f);
	
	public ControlerAnimaux(Monde monde) {
		// TODO Auto-generated constructor stub
		this.monde = monde;
		this.animaux = this.monde.getAnime();
		this.collision = new Array<Rectangle>();
		chargerCollision();
		//demarrerParcoursAnimaux();
	}
	
	private void demarrerParcoursAnimaux(){
		for (int i=0;i<this.animaux.size;i++){
			this.nextStep(this.animaux.get(i));
		}
	}
	
	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
                return new Rectangle();
        }
};
	
	private void chargerCollision(){
		collision.clear();
		for(int x = 0;x<=monde.getNiveau().getLargeur();x++){
			for(int y= 0;y<=monde.getNiveau().getHauteur();y++){
				if (monde.getNiveau().getCollision(x, y) != null){ // (x>=dX) && (x<= fX) && (y>=dY) && (y<= fY)){
					collision.add(monde.getNiveau().getCollision(x, y));
				}
			}
		}
		
	}
	
	/*public void estPlusProche(Rectangle a, Rectangle b){
		
	}*/
	
	public void gererCollision(Anime a, float delta){
		a.getRapidite().scl(delta); // on travail au ralenti
		
		a.getCadre().x += a.getRapidite().x;
		a.getCadre().y += a.getRapidite().y;
		
		Rectangle animaRect = rectPool.obtain();
		Rectangle r = a.getCadre();
		animaRect.set(r.x + 0.1f,r.y + 0.1f,r.width - 0.1f,r.height - 0.1f);
		
		this.chargerCollision();
		animaRect.x += a.getRapidite().x;
		animaRect.y += a.getRapidite().y;
		
		int i = 0;
		boolean ok = true;
		while (i< collision.size && ok){
			if (collision.get(i) != null) {
				Rectangle v = collision.get(i);
				Rectangle p = a.getCadre();
				float aprox = 0.1f;
				if (v.overlaps(animaRect)){
					/*
					if (Math.abs(animaRect.x - v.x)<=v.width/2 && Math.abs(animaRect.y - v.y)<=v.height ){
						System.out.println("gauche");
						a.getRapidite().x = 0;
					}
					else if ((Math.abs(animaRect.x - v.x)<=v.width && Math.abs(animaRect.y - v.y)<=v.height  )){
						System.out.println("droite");
						a.getRapidite().x = 0;
					}
					
					if ((Math.abs(animaRect.y - v.y)<=v.height/2 && Math.abs(animaRect.x - v.x)<=v.width )){
						System.out.println("bas");
						a.getRapidite().y = 0;
						
					}else if ((Math.abs(animaRect.y - v.y)<=v.height && Math.abs(animaRect.x - v.x)<=v.width )){
						System.out.println("haut");
						a.getRapidite().y = 0;
					}
					
				*/
					a.getRapidite().x = 0;
					a.getRapidite().y = 0;
					
					 
					 ok = false;
					 /*if ((v.x + .5 * v.width > animaRect.x + .5 * animaRect.width)
			                    && (v.y + .5 * v.height > animaRect.y + .5 * animaRect.height)
			                    || (animaRect.x + .5 * animaRect.width > v.x + .5 * v.width)
			                    && (animaRect.y + .5 * animaRect.height > v.y + .5 * v.height) )
			            {
						 a.getRapidite().x = 0;
						 a.getRapidite().y = 0;
						 ok = false;
			            }*/
					
				}
				
				
			}
			i++;
		}
		
		a.getRapidite().scl(1/delta); // on restore la vitesse
	}
	
	public void corrigeDirection(Anime a){
		
		Vector2 v = new Vector2();
		v.x = -(a.getPosition().x - a.getPath().get(a.getPathStep()).x);
		v.y = -(a.getPosition().y - a.getPath().get(a.getPathStep()).y);
		float angle = (float) Math.atan2(v.y, v.x);
		
		v.x =(float)Math.cos(angle);
		v.y =(float)Math.sin(angle);
		
		a.getRapidite().x = v.x * a.VITESSE;
		a.getRapidite().y = v.y * a.VITESSE;
	}
	
	
	public void nextStep(Anime a){
		if (a.getPath().size>0 && a.getPathStep() >= a.getPath().size -1){
			a.setPathStep(0);
		}else if (a.getPath().size>0 && a.getPathStep() < a.getPath().size - 1){
			a.setPathStep(a.getPathStep()+1);
		}
		
		
	}
	
	public void suivreJoueur(Anime a){
		if (reperer.get(a) == null){
			reperer.put(a,  new Integer(a.getPath().size));
			a.setPathStep(reperer.get(a).intValue());
			a.getPath().add(this.monde.getPerso().getPosition());
			
		}
	}
	 
	public void arretSuivreJoueur(Anime a){
		if (reperer.get(a) != null){
			a.getPath().removeIndex(reperer.get(a));
			reperer.put(a,  null);
		}
	}
	
	public void detecterJoueur(Monstre a){
		Vector2 v = new Vector2(this.monde.getPerso().getPosition());
		if (v.dst(a.getPosition()) < a.DISTANCE_VUE && this.monde.getPerso().getEtat() != null && this.monde.getPerso().getEtat().getClass().equals(Dark.class) && ((Dark) this.monde.getPerso().getEtat()) == Dark.GRABBING ){
			this.suivreJoueur(a);
		}else{
			this.arretSuivreJoueur(a);
		}
	}
	
	public void gererParcours(Anime a){
		
		if(!(a.getPath().size==0)){//n'op�re que si l'Anime a un parcours
			if (a.getPathStep() >= a.getPath().size)
				a.setPathStep(a.getPath().size - 1);
		Vector2 v = a.getPath().get(a.getPathStep());
		Vector2 p = a.getPosition();
		corrigeDirection(a);
		float approx = 0.1f;
		if ( Math.abs(p.x - v.x )<approx && Math.abs(p.y - v.y )<approx){
		
			this.nextStep(a);
		}
		}
		
	}
	
	public double angleProche(Vector2 v, Vector2 a){
		Vector2 w = new Vector2();
		
		w.x = Math.abs(v.x - a.x);
		w.y = Math.abs(v.y - a.y);
		
		double angle = w.angle();
		
		double d = Math.toDegrees(angle);
		if (Math.abs(d-45)<=45){ // proche de zero
			return 0;
		}else if (Math.abs(d-45)>45 && Math.abs(d-45)<=135){ // proche de 90
			return 90;
		}else if (Math.abs(d-45)>135 && Math.abs(d-45)<=225){ // proche de 180
			return 180;
		}else if (Math.abs(d-45)>225 && Math.abs(d-45)<=315){ // proche de 270
			return 270;
		}
		return 0;
	}
	
	public void bruteForce(Anime a, float delta){
		int i =0;
		int d = 0;
		gererCollision(a, delta);
		double e;
		/*e = StrictMath.toRadians(angleProche(a.getPosition(), a.getPath().get(a.getPathStep())));
		a.getRapidite().x = (float) Math.cos(e);
		a.getRapidite().y = (float) Math.sin(e);
		*/
		while (a.getRapidite().equals(vecteurNul) && i < 360 ){
			i = d * 90;
			e = StrictMath.toRadians(i);
			a.getRapidite().x = (float) Math.cos(e);
			a.getRapidite().y = (float) Math.sin(e);
			gererCollision(a, delta);
			d++;
		}
		
	}
	
	public void update(float delta){
		
		for(int i = 0; i<this.animaux.size;i++){
			//if(!(this.animaux.get(i) instanceof Animal) && !this.animaux.get(i).isTamed())
				//bruteForce(this.animaux.get(i),delta);
			this.animaux.get(i).update(delta);
			if ((this.animaux.get(i) instanceof Monstre) && this.animaux.get(i).getAnimeType() == AnimeType.MONSTRE){ //
				detecterJoueur((Monstre) this.animaux.get(i));
			}
			//if(!(this.animaux.get(i) instanceof Animal) && !this.animaux.get(i).isTamed())
				gererParcours(this.animaux.get(i));
			
		}
	}

}
