package com.me.lightdark.controleurs;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Timer;
import com.me.lightdark.modeles.Animal;
import com.me.lightdark.modeles.Anime;
import com.me.lightdark.modeles.Anime.AnimeType;
import com.me.lightdark.modeles.Case;
import com.me.lightdark.modeles.CompetenceAnimaux;
import com.me.lightdark.modeles.Dark;
import com.me.lightdark.modeles.Form;
import com.me.lightdark.modeles.Monde;
import com.me.lightdark.modeles.Monstre;
import com.me.lightdark.modeles.Perso;
import com.me.lightdark.modeles.Projectile;

public class ControlerProjectiles {

	private Monde monde;
	
	private Perso lanceur;
	private Array<Projectile> projectiles;
	private Array<Projectile> fleches;
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
			for(int y= 0;y<=monde.getNiveau().getHauteur();y++){// Tant qu'on reste dans la taille du monde ...
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
		this.fleches = monde.getFleche();
		for (int i =0 ; i< this.projectiles.size;i++){
			gererCollision(this.projectiles.get(i), delta);
			gererDistance(this.projectiles.get(i));
			this.projectiles.get(i).update(delta);
		}
		for (int i =0 ; i< this.fleches.size;i++){
			gererCollisionFleche(this.fleches.get(i), delta);
			this.fleches.get(i).update(delta);
		}
		gererObsoletes();
	}
	
	private boolean gererCollisionAnimaux(Rectangle p){// si touche un annimal, return false
		boolean pasTouche = true;
		int i = 0;
		while (i<this.monde.getAnime().size && pasTouche){
			if (this.monde.getAnime().get(i).getCadre().overlaps(p) && !this.monde.getAnime().get(i).isTamed()){ //on gere une collision (overlaps entre projectile et l'animal)
				pasTouche = false;
			}
			i++;
		}
		return pasTouche;
		
	}
	
	private boolean estAnimal(Anime a) {
		if(a.getAnimeType() == AnimeType.ANIMAL)
			return true;
		else
			return false;
	}
	
	public void gererCollision(Projectile p, float delta){// HERE ? TODO ameliorer la gestion du grappin
		p.getRapidite().scl(delta); // on travail au ralenti
		
		p.getCadre().x += p.getRapidite().x;
		p.getCadre().y += p.getRapidite().y;
		
		Rectangle persoRect = rectPool.obtain();
		persoRect.set(p.getCadre());
		
		this.chargerCollision();
		persoRect.x += p.getRapidite().x;
		persoRect.y += p.getRapidite().y;
		boolean ok = true;

		int i = 0;
		while(i< monde.getAnime().size && ok){

			if(monde.getAnime().get(i) instanceof Animal && persoRect.overlaps(monde.getAnime().get(i).getCadre()) && monde.getPerso().getForm()==Form.SHADOWFORM) {
				//Si le projectile du perso en ShadowForm rencontre un Animal
				
					
					//lanceur.getPosition().x = monde.getAnime().get(i).getCadre().x;//TODO : placer cash sur le CENTRE de l'animal (+=Animal.TAILLE/4)
	                //lanceur.getPosition().y = monde.getAnime().get(i).getCadre().y;
	                
				p.devientObsolete();
	            lanceur.transit(new Vector2(monde.getAnime().get(i).getCadre().x, monde.getAnime().get(i).getCadre().y));
	            
	            final Anime target = monde.getAnime().get(i);
	            target.setTamer(lanceur);
	            lanceur.setAnimal(target);
	            Timer.Task transiter = new Timer.Task() {

					@Override
					public void run() {
						lanceur.changerEtat(Dark.TAMING);
	                
					target.setTaming(true);//L'animal devient contrôlé
					
					}
				};
				if(!transiter.isScheduled())
					Timer.schedule(transiter, 0.5f);
	                
	                
					

	                //ici test d'une compétence
	               // monde.getAnime().get(i).demarrerCompetence();
					//System.out.println("[DEBUG] Shadow Taming");
					ok=false;
			}
			else if(monde.getAnime().get(i) instanceof Monstre && persoRect.overlaps(monde.getAnime().get(i).getCadre()) && monde.getPerso().getForm()==Form.LIGHTFORM && p.getLanceur()==monde.getPerso()) {
				//Si le perso en LightForm tire sur un monstre (no friendFire chez les mobs)
				p.devientObsolete();
				//System.out.println("[DEBUG0] Shadow Taming");
				ok=false; 

				//System.out.println("[DEBUG] Degat proj");

				((Monstre) monde.getAnime().get(i)).recevoirCoup(lanceur.puissance());
			
			}
			
			i++;
		}
		i=0;
		
		while (i< collision.size && ok){
			if (collision.get(i) != null) {
				
				if(persoRect.overlaps(collision.get(i))){// !gererCollisionAnimaux(p.getCadre())){
					//Si le projectile rencontre une case bloquante
					p.getRapidite().x = 0;
					p.getRapidite().y = 0;
					p.devientObsolete();
					
					//System.out.println("Le tir entre en collision");
					
					if(!(lanceur.getEtat()==Dark.TAMING)){
						lanceur.changerEtat(Dark.IDLE);
					}
					//On remet en shadowwalking si jamais le grappin touche un obstacle ET qu'il n'était pas en taming
					ok = false;
				}
				
				
			}
			i++;
		}
		if(monde.getPerso().getForm()==Form.SHADOWFORM){
			i=0;
			while(i< shadowTouched.size && ok){
				if(persoRect.overlaps(shadowTouched.get(i)) && shadowTouched.get(i).equals(p.getCaseCible().getCadre())) {
					//System.out.println(lanceur.getEtat());
					p.devientObsolete();
					//System.out.println("[DEBUG] Collision ombre détectée");
					
					float destx = (shadowTouched.get(i).x + (shadowTouched.get(i).width /2f) - (lanceur.TAILLE / 2f));
					float desty = (shadowTouched.get(i).y + (shadowTouched.get(i).height /2f) -  (lanceur.TAILLE / 2f));
					
					lanceur.transit(new Vector2(destx, desty));
					//System.out.println("Début de transition");
					
					final Timer.Task afterGrabbing = new Timer.Task() {

						@Override
						public void run() {
							
							lanceur.changerEtat(Dark.IDLE);
							
						
						}
					};
					
					Timer.Task toDarkness = new Timer.Task() {

						@Override
						public void run() {
							
							Timer.schedule(afterGrabbing, 0.5f);
							if(lanceur.getAnimal()!=null){//Désactiver le shadow taming si de retour sur une ombre
								lanceur.getAnimal().setTaming(false);
								lanceur.getAnimal().setTamer(null);
							}
						
						}
					};
					
					
					if(!toDarkness.isScheduled())
						Timer.schedule(toDarkness, 0.5f);
					
					
						//ici test d'une compétence
						//lanceur.getAnimal().stopperCompetence(CompetenceAnimaux.COURRIR);

					ok=false;
				}
				i++;
			}
		}
	

		p.getRapidite().scl(1/delta); // on restore la vitesse
		
	}
	
	public void gererCollisionFleche(Projectile p, float delta){
		p.getRapidite().scl(delta); // on travail au ralenti
		
		p.getCadre().x += p.getRapidite().x;
		p.getCadre().y += p.getRapidite().y;
		
		Rectangle persoRect = rectPool.obtain();
		persoRect.set(p.getCadre());
		
		this.chargerCollision();
		persoRect.x += p.getRapidite().x;
		persoRect.y += p.getRapidite().y;
		boolean ok = true;

		int i = 0;
		if(persoRect.overlaps(monde.getPerso().getCadre())){
			monde.getPerso().healthDown();
			p.getRapidite().x = 0;
			p.getRapidite().y = 0;
			p.devientObsolete();
		}
		while (i< collision.size && ok){
			if (collision.get(i) != null) {
				if(persoRect.overlaps(collision.get(i))){
					p.getRapidite().x = 0;
					p.getRapidite().y = 0;
					p.devientObsolete();
					//On remet en shadowwalking si jamais le grappin touche un obstacle
					ok = false;
				}
				
				
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
		for (int i =0 ; i< this.fleches.size;i++){
			if (this.fleches.get(i).estObsolete()){
				this.fleches.removeIndex(i); // on attends que le garbage collector s'en occupe
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
		
	
		vtemp.x = (Math.abs(position.x - posInitial.x));
		vtemp.y = (Math.abs(position.y - posInitial.y));

		// calcul du rayon par le thï¿½orï¿½me de pythagore
		float rayon = p.getPosition().dst(p.getInitial());//(float) Math.sqrt(Math.pow((double)vtemp.x, 2.0) + Math.pow((double)vtemp.y, 2.0));
		
		if (rayon > p.DISTANCE_MAX){
			//System.out.println("[DEBUG] Distance max atteinte");
			p.devientObsolete();
			if(p.getLanceur()!=null && lanceur.getForm()==Form.SHADOWFORM) {
				//Le grappin revient en "boomerang" s'il est allï¿½ trop loin
				Projectile boomerang = monde.lancerBoomerang(p);
				
				
				//Si le retour est quasi arrivï¿½, il devient obsolï¿½te
				if(boomerang.getPosition().dst2(boomerang.getCaseCible().getPosition())<1f){
					if(!boomerang.getFromTaming())
						lanceur.changerEtat(Dark.IDLE);
					boomerang.devientObsolete();
				}
				
			}
		}
			
	}
	
	
	
	public ControlerProjectiles(Monde monde, Perso lanceur) {
		this.monde = monde;
		this.projectiles = new Array<Projectile>();
		this.fleches = new Array<Projectile>();
		this.collision = new Array<Rectangle>();
		this.shadowTouched= new Array<Rectangle>();
		this.lanceur=lanceur;
	}

}
