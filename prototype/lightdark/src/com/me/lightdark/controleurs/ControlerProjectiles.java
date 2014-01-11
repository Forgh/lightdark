package com.me.lightdark.controleurs;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.me.lightdark.modeles.Case;
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
	
	private boolean gererCollisionAnimaux(Rectangle p){
		boolean pasTouche = true;
		int i = 0;
		while (i<this.monde.getAnimals().size && pasTouche){
			if (this.monde.getAnimals().get(i).getCadre().overlaps(p)){
				pasTouche = false;
			}
			i++;
		}
		return pasTouche;
		
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
				if(persoRect.overlaps(collision.get(i)) || !gererCollisionAnimaux(p.getCadre())){
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
		while(i< shadowTouched.size && ok){
			if(persoRect.overlaps(shadowTouched.get(i)) && shadowTouched.get(i).equals(p.getCaseCible().getCadre())) {
				System.out.println(lanceur.getEtat());
				p.devientObsolete();
				lanceur.setPosition(new Vector2(shadowTouched.get(i).x + (shadowTouched.get(i).width /2f) - (lanceur.TAILLE / 2f),shadowTouched.get(i).y + (shadowTouched.get(i).height /2f) -  (lanceur.TAILLE / 2f)));
				lanceur.changerEtat(Dark.SHADOWWALKING);
				//on remet en shadowwalking si jamais on touche la case désirée 
				ok=false;
			}
			i++;
		}
		
	

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
		Vector2 vtemp = new Vector2(p.getInitial());
		Vector2 position = p.getPosition();
		Vector2 posInitial = p.getInitial();
		
	
		vtemp.x = (Math.abs(position.x) - Math.abs(posInitial.x));
		vtemp.y = (Math.abs(position.y) - Math.abs(posInitial.y));

		// calcul du rayon par le th�or�me de pythagore
		float rayon = (float) Math.sqrt(Math.pow((double)vtemp.x, 2.0) + Math.pow((double)vtemp.y, 2.0));
		
		if (rayon > p.DISTANCE_MAX){
			//System.out.println("[DEBUG] Distance max atteinte");
			p.devientObsolete();
			if(lanceur.getForm()==Form.SHADOWFORM) {
				//Le grappin revient en "boomerang" s'il est all� trop loin
				Projectile boomerang = monde.lancerBoomerang(p);
				
				
				//Si le retour est quasi arriv�, il devient obsol�te
				if(boomerang.getPosition().dst2(boomerang.getCaseCible().getPosition())<1f){
					boomerang.devientObsolete();
					lanceur.changerEtat(Dark.SHADOWWALKING);
					//On remet en shadowwalking si le grappin revient
				}
				
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
