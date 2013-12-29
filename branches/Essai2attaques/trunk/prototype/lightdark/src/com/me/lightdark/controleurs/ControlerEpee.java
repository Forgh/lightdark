package com.me.lightdark.controleurs;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.me.lightdark.modeles.Dark;
import com.me.lightdark.modeles.Epee;
import com.me.lightdark.modeles.Form;
import com.me.lightdark.modeles.Monde;
import com.me.lightdark.modeles.Perso;

public class ControlerEpee {

	private Monde monde;
	
	private Perso epeiste;
	private Array<Epee> sword;
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
				
			}
		}
		
	}
	public void update(float delta) {
		this.sword = monde.getEpee();
		
		
		
		
		
		for (int i =0 ; i< this.sword.size;i++){
			gererCollision(this.sword.get(i), delta);
			gererDistance(this.sword.get(i));
			this.sword.get(i).update(delta);
		}
		
		gererObsoletes();
	}
	
	public void gererCollision(Epee p, float delta){
		p.getRapidite().scl(delta); // on travail au ralenti
		
		p.getCadre().x += p.getRapidite().x;
		p.getCadre().y += p.getRapidite().y;
		
		Rectangle persoRect = rectPool.obtain();
		persoRect.set(p.getCadre());
		
		this.chargerCollision();
		persoRect.x += p.getRapidite().x;
		persoRect.y += p.getRapidite().y;
		
		int i = 0;
	////////////////////////////////////??????????????????///////////////////////////
		boolean ok = true;
		while (i< collision.size && ok){
			if (collision.get(i) != null) {
				if(persoRect.overlaps(collision.get(i))){
					p.getRapidite().x = 0;
					p.getRapidite().y = 0;
					p.devientObsolete();
					epeiste.changerEtat(Dark.SHADOWWALKING);
					//On remet en shadowwalking si jamais le grappin touche un obstacle
					ok = false;
				}
				
				
			}
			i++;
		}
		i=0;
		/////////////////////////////////////?????????????????????/////////////////////////
		while(i< shadowTouched.size){
			if(persoRect.overlaps(shadowTouched.get(i)) && shadowTouched.get(i).equals(p.getCaseCible().getCadre())) {
				System.out.println(epeiste.getEtat());
				epeiste.setPosition(new Vector2(shadowTouched.get(i).x + (shadowTouched.get(i).width /2f) - (epeiste.TAILLE / 2f),shadowTouched.get(i).y + (shadowTouched.get(i).height /2f) -  (epeiste.TAILLE / 2f)));
				epeiste.changerEtat(Dark.SHADOWWALKING);
				//on remet en shadowwalking si jamais on touche la case désirée (A FIX)
			}
			i++;
		}
		
		// ici pour les mobs

		p.getRapidite().scl(1/delta); // on restaure la vitesse
		
	}
	
	public void gererObsoletes(){
		for (int i =0 ; i< this.sword.size;i++){
			if (this.sword.get(i).estObsolete()){
				this.sword.removeIndex(i); // on attends que le garbage collector s'en occupe
			}
		}
	}
	
	
	/*
	 * Cette methode rend obsolete un projectile qui depasse sa portee
	 */
	public void gererDistance(Epee p){
		// notre projectile est-il trop loin ?
		Vector2 vtemp = new Vector2(p.getInitial());
		Vector2 position = p.getPosition();
		Vector2 posInitial = p.getInitial();
		
	
		vtemp.x = (Math.abs(position.x) - Math.abs(posInitial.x));
		vtemp.y = (Math.abs(position.y) - Math.abs(posInitial.y));

		// calcul du rayon par le th�or�me de pythagore
		float rayon = (float) Math.sqrt(Math.pow((double)vtemp.x, 2.0) + Math.pow((double)vtemp.y, 2.0));
		
		if (rayon > p.DISTANCE_MAX){
//////////////////////////////////????????????????????????//////////////////////
			p.devientObsolete(); //ben, au moins il ne doit plus aller vers l'infini...
			if(epeiste.getForm()==Form.SHADOWFORM) {
				epeiste.changerEtat(Dark.SHADOWWALKING);
				//On remet en shadowwalking si on atteint le max de distance.
			}
		}
	}
	
	public ControlerEpee(Monde monde, Perso epeiste) {
		// TODO Auto-generated constructor stub
		this.monde = monde;
		this.sword = new Array<Epee>();
		this.collision = new Array<Rectangle>();
		this.shadowTouched= new Array<Rectangle>();
		this.epeiste=epeiste;
	}

}
