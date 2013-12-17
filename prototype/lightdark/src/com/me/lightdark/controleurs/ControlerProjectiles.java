package com.me.lightdark.controleurs;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.me.lightdark.modeles.Monde;
import com.me.lightdark.modeles.Projectile;

public class ControlerProjectiles {

	private Monde monde;
	
	
	
	private Array<Projectile> projectiles;
	private Array<Rectangle> collision;
	//idem pour les mobs
	
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
	public void update(float delta) {
		this.projectiles = monde.getProjectile();
		
		
		
		for (int i =0 ; i< this.projectiles.size;i++){
			gererCollision(this.projectiles.get(i), delta);
			this.projectiles.get(i).update(delta);
		}
		
		gererObsoletes();
	}
	
	public void gererCollision(Projectile p, float delta){
		p.getRapidite().mul(delta); // on travail au ralenti
		
		p.getCadre().x += p.getRapidite().x;
		p.getCadre().y += p.getRapidite().y;
		
		Rectangle persoRect = rectPool.obtain();
		persoRect.set(p.getCadre());
		
		this.chargerCollision();
		persoRect.x += p.getRapidite().x;
		persoRect.y += p.getRapidite().y;
		
		int i = 0;
		boolean ok = true;
		while (i< collision.size && ok){
			if (collision.get(i) != null && persoRect.overlaps(collision.get(i))){
				p.getRapidite().x = 0;
				p.getRapidite().y = 0;
				p.devientObsolete();
				ok = false;
			}
			i++;
		}
		
		// ici pour les mobs

		p.getRapidite().mul(1/delta); // on restore la vitesse
		
	}
	
	public void gererObsoletes(){
		for (int i =0 ; i< this.projectiles.size;i++){
			if (this.projectiles.get(i).estObsolete()){
				this.projectiles.removeIndex(i); // on attends que le garbage collector s'en occupe
			}
		}
	}
	
	public ControlerProjectiles(Monde monde) {
		// TODO Auto-generated constructor stub
		this.monde = monde;
		this.projectiles = new Array<Projectile>();
		this.collision = new Array<Rectangle>();
	}

}
