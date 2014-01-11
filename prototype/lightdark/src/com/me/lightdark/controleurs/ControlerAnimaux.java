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
	
	
	public ControlerAnimaux(Monde monde) {
		// TODO Auto-generated constructor stub
		this.monde = monde;
		this.animaux = this.monde.getAnimals();
		this.collision = new Array<Rectangle>();
		chargerCollision();
		demarrerParcoursAnimaux();
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
	
	public void gererCollision(Animal a, float delta){
		a.getRapidite().scl(delta); // on travail au ralenti
		
		a.getCadre().x += a.getRapidite().x;
		a.getCadre().y += a.getRapidite().y;
		
		Rectangle animaRect = rectPool.obtain();
		animaRect.set(a.getCadre());
		
		this.chargerCollision();
		animaRect.x += a.getRapidite().x;
		animaRect.y += a.getRapidite().y;
		
		int i = 0;
		boolean ok = true;
		while (i< collision.size && ok){
			if (collision.get(i) != null) {
				if(animaRect.overlaps(collision.get(i))){
					a.getRapidite().x = 0;
					a.getRapidite().y = 0;
					
					
					ok = false;
				}
				
				
			}
			i++;
		}
		
		a.getRapidite().scl(1/delta); // on restore la vitesse
	}
	
	public void corrigeDirection(Animal a){
		if (a.getPath().size>0){
			if (a.getPosition().x>a.getPath().get(a.getPathStep()).x){
				a.getRapidite().x = -Animal.VITESSE;
			}else if (a.getPosition().x<a.getPath().get(a.getPathStep()).x){
				a.getRapidite().x = Animal.VITESSE;
			}else{
				a.getRapidite().x = 0f;
			}
			
			if (a.getPosition().y>a.getPath().get(a.getPathStep()).y){
				a.getRapidite().y = -a.VITESSE;
			}else if (a.getPosition().y<a.getPath().get(a.getPathStep()).y){
				a.getRapidite().y = a.VITESSE;
			}else{
				a.getRapidite().y = 0f;
			}
		}else{
			a.getRapidite().x = 0f;
			a.getRapidite().y = 0f;
		}
	}
	
	public void verifDirection(Animal a){
		Vector2 v = a.getLastPosition();
		Vector2 p = a.getPosition();
		
		if ((int)p.x != (int)v.x || (int)p.y != (int)v.y){
			System.out.println(p.x + " :" + v.x + ";" + p.y + " :" + v.y) ;
			corrigeDirection(a);
			a.setLastPosition(new Vector2(a.getPosition()));
		}
	}
	
	public void nextStep(Animal a){
		if (a.getPath().size>0 && a.getPathStep() >= a.getPath().size -1){
			a.setPathStep(0);
		}else if (a.getPath().size>0 && a.getPathStep() < a.getPath().size - 1){
			a.setPathStep(a.getPathStep()+1);
		}
	}
	
	public void gererParcours(Animal a){
		Vector2 v = a.getPath().get(a.getPathStep());
		Vector2 p = a.getPosition();
		//Rectangle r = new Rectangle();
		//r.set(v.x, v.y, 1f, 1f);
		
		if ( Math.abs(p.x - v.x )<0.1f && Math.abs(p.y - v.y )<0.1f){
			
			this.nextStep(a);
		}
		
	}
	public void update(float delta){
		
		for(int i = 0; i<this.animaux.size;i++){
			verifDirection(this.animaux.get(i));
			this.animaux.get(i).update(delta);
			gererParcours(this.animaux.get(i));
		}
	}

}
