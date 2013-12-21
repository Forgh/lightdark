package com.me.lightdark;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "lightdark - alpha";
 
		cfg.useGL20 = false;
		cfg.width = 800;
		cfg.height = 800;
		new LwjglApplication(new LightDark(), cfg);
	}
}
  