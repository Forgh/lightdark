package com.me.lightdark.modeles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.lightdark.modeles.Perso.Form;

public class Light extends Perso{
		
		public Light(Vector2 position) {
			super(position);
			super.setForm(Form.LIGHTFORM);
		}
		
		public Form getForm(){
		    return Form.LIGHTFORM;
		}			

}
