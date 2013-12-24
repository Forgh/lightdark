package com.me.lightdark.controleurs;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.me.lightdark.modeles.Dark;
import com.me.lightdark.modeles.Form;
import com.me.lightdark.modeles.Monde;
import com.me.lightdark.modeles.Perso;
import com.me.lightdark.modeles.Projectile;

public class ControlerProjectiles {

	private Monde monde;
	
	private Perso lanceur;
	private Array<Projectile> projectiles;
	private Array<Rectangle> collision;
	private Array<Rectangle> shadowTouched;
	
	//idem pour les mobs
	
	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
                return new Rectangle();
        }
};


	private void chargerCollision(){
		collision.clear();
		shadowTouched.clear();
		for(int x = 0;x<=monde.getNiveau().getLargeur();x++){
			for(int y= 0;y<=monde.getNiveau().getHauteur();y++){
				if (monde.getNiveau().getCollision(x, y) != null){ // (x>=dX) && (x<= fX) && (y>=dY) && (y<= fY)){
					collision.add(monde.getNiveau().getCollision(x, y));
				}
				else if (lanceur.getForm() == Form.SHADOWFORM && monde.getNiveau().getCollisionWithShadow(x, y) != null){
					shadowTouched.add(monde.getNiveau().getCollisionWithShadow(x, y));
					
				}
			}
		}
		
	}
	public void update(float delta) {
		this.projectiles = monde.getProjectile();
		
		
		
		
		
		for (int i =0 ; i< this.projectiles.size;i++){
			gererCollision(this.projectiles.get(i), delta);
			gererDistance(this.projectiles.get(i));
			this.projectiles.get(i).update(delta);
		}
		
		gererObsoletes();
	}
	
	public void gererCollision(Projectile p, float delta){
		p.getRapidite().scl(delta); // on travail au ralenti
		
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
			if (collision.get(i) != null) {
				if(persoRect.overlaps(collision.get(i))){
					p.getRapidite().x = 0;
					p.getRapidite().y = 0;
					p.devientObsolete();
					lanceur.changerEtat(Dark.SHADOWWALKING);
					//On remet en shadowwalking si jamais le grappin touche un obstacle
					ok = false;
				}
				
				
			}
			i++;
		}
		i=0;
		while(i< shadowTouched.size){
			if(persoRect.overlaps(shadowTouched.get(i)) && shadowTouched.get(i).equals(p.getCaseCible().getCadre())) {
				System.out.println(lanceur.getEtat());
				lanceur.setPosition(new Vector2(shadowTouched.get(i).x,shadowTouched.get(i).y));
				lanceur.changerEtat(Dark.SHADOWWALKING);
				//on remet en shadowwalking si jamais on touche la case désirée (A FIX)
			}
			i++;
		}
		
		// ici pour les mobs

		p.getRapidite().scl(1/delta); // on restore la vitesse
		
	}
	
	public void gererObsoletes(){
		for (int i =0 ; i< this.projectiles.size;i++){
			if (this.projectiles.get(i).estObsolete()){
				this.projectiles.removeIndex(i); // on attends que le garbage collector s'en occupe
			}
		}
	}
	
	
	/*
	 * Cette methode rend obsolete un projectile qui depasse sa portee
	 */
	public void gererDistance(Projectile p){
		// notre projectile est-il trop loin ?
		Vector2 vtemp = new Vector2();
		Vector2 position = p.getPosition();
		Vector2 posInitial = p.getInitial();
		
		vtemp.x = (Math.abs(position.x) - Math.abs(posInitial.x));
		vtemp.y = (Math.abs(position.y) - Math.abs(posInitial.y));

		if (Math.abs(vtemp.x)>=p.DISTANCE_MAX || Math.abs(vtemp.y)>=p.DISTANCE_MAX){
			p.devientObsolete(); //ben, au moins il ne doit plus aller vers l'infini...
			if(lanceur.getForm()==Form.SHADOWFORM) {
				lanceur.changerEtat(Dark.SHADOWWALKING);
				//On remet en shadowwalking si on atteint le max de distance.
			}
		}
	}
	
	public ControlerProjectiles(Monde monde, Perso lanceur) {
		// TODO Auto-generated constructor stub
		this.monde = monde;
		this.projectiles = new Array<Projectile>();
		this.collision = new Array<Rectangle>();
		this.shadowTouched= new Array<Rectangle>();
		this.lanceur=lanceur;
	}

}
