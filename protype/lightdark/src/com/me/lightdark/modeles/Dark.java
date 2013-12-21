package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Vector2;

public class Dark extends Perso {
	
	public interface Etat{} // ici polymorphisme pour light/shadow
	
	public enum ShadowForm implements Etat {
		IDLE, SHADOWWALKING, GRABBING, DYING, TAMING 
	} //Différenciation entre MARCHANt et SHADOWWALKING : la ShadowForm ne peut marche QUE sur des ombres!
	
	private ShadowForm etat = ShadowForm.IDLE;
	
	public Dark(Vector2 position) {
		super(position);
		super.setForm(Form.SHADOWFORM);
		this.setEtat(ShadowForm.IDLE);
	}

	public ShadowForm getEtat() {
		return etat;
	}

	public void setEtat(ShadowForm etat) {
		this.etat = etat;
	}
	
	
		

}
