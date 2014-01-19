package com.me.lightdark.controleurs;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.me.lightdark.modeles.Animal;
import com.me.lightdark.modeles.Dark;
import com.me.lightdark.modeles.Form;
import com.me.lightdark.modeles.Monde;

public class ControlerAnimaux {
	private Monde monde;
	private Array<Animal> animaux;
	
	private Array<Rectangle> collision;
	
	final Vector2 vecteurNul = new Vector2(0f,0f);
	
	public ControlerAnimaux(Monde monde) {
		// TODO Auto-generated constructor stub
		this.monde = monde;
		this.animaux = this.monde.getAnimals();
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
	
	public void estPlusProche(Rectangle a, Rectangle b){
		
	}
	
	public void gererCollision(Animal a, float delta){
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
	
	public void corrigeDirection(Animal a){
		
		Vector2 v = new Vector2();
		v.x = -(a.getPosition().x - a.getPath().get(a.getPathStep()).x);
		v.y = -(a.getPosition().y - a.getPath().get(a.getPathStep()).y);
		float angle = (float) Math.atan2(v.y, v.x);
		
		v.x =(float)Math.cos(angle);
		v.y =(float)Math.sin(angle);
		
		a.getRapidite().x = v.x * Animal.VITESSE;
		a.getRapidite().y = v.y * Animal.VITESSE;
	}
	
	
	public void nextStep(Animal a){
		if (a.getPath().size>0 && a.getPathStep() >= a.getPath().size -1){
			a.setPathStep(0);
		}else if (a.getPath().size>0 && a.getPathStep() < a.getPath().size - 1){
			a.setPathStep(a.getPathStep()+1);
		}
		
		
	}
	
	public void gererParcours(Animal a){
		if(!(a.getPath().size==0)){//n'opère que si l'animal a un parcour
		Vector2 v = a.getPath().get(a.getPathStep());
		Vector2 p = a.getPosition();
		corrigeDirection(a);
		float approx = 0.1f;
		if ( Math.abs(p.x - v.x )<approx && Math.abs(p.y - v.y )<approx){
		
			this.nextStep(a);
		}
		}
		
	}
	public void bruteForce(Animal a, float delta){
		int i =0;
		int d = 0;
		gererCollision(a, delta);
		while (a.getRapidite().equals(vecteurNul) && i < 360 ){
			i = d * 90;
			double e = StrictMath.toRadians(i);
			a.getRapidite().x = (float) Math.cos(e);
			a.getRapidite().y = (float) Math.sin(e);
			gererCollision(a, delta);
			d++;
		}
		
	}
	
	public void update(float delta){
		
		for(int i = 0; i<this.animaux.size;i++){
			bruteForce(this.animaux.get(i),delta);
			this.animaux.get(i).update(delta);
			gererParcours(this.animaux.get(i));
			
		}
	}

}
