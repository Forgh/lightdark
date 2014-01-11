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
		Vector2 v = a.getPath().get(a.getPathStep());
		Vector2 p = a.getPosition();
		//Rectangle r = new Rectangle();
		//r.set(v.x, v.y, 1f, 1f);
		corrigeDirection(a);
		float aprox = 0.1f;
		if ( Math.abs(p.x - v.x )<aprox && Math.abs(p.y - v.y )<aprox){
		
			this.nextStep(a);
		}
		
	}
	public void update(float delta){
		
		for(int i = 0; i<this.animaux.size;i++){
			gererCollision(this.animaux.get(i),delta);
			this.animaux.get(i).update(delta);
			gererParcours(this.animaux.get(i));
		}
	}

}
