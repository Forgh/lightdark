package com.me.lightdark.controleurs;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.lightdark.modeles.Monde;
import com.me.lightdark.modeles.Perso;

public class ControlerMenu {
	private Perso perso;
	private Button orbe;
	private Button pause;
	
	public ControlerMenu(Monde monde) {
		this.perso = monde.getPerso();
		
		this.orbe = monde.getOrbe();
		this.orbe.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				System.out.println("CLICK");
				orbPressed();
			}
		});
		
		this.pause = monde.getPause();
		this.pause.addListener(new ClickListener());
	}
	
	public void orbPressed(){
		perso.switchForm();
	}
	
	public Button getOrbe(){
		return this.orbe;
	}
	
	public Button getPause(){
		return this.pause;
	}
	
}
