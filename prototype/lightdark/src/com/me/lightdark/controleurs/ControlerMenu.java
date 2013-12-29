package com.me.lightdark.controleurs;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.Array;
import com.me.lightdark.modeles.Form;
import com.me.lightdark.modeles.Monde;
import com.me.lightdark.modeles.Perso;

public class ControlerMenu {
	private Perso perso;
	private Button orbe;
	
	public ControlerMenu(Monde monde) {
		this.perso = monde.getPerso();
		this.orbe = monde.getOrbe();

	}
	
	public void orbPressed(){
		perso.switchForm();
	}
	
}
