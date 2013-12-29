package com.me.lightdark;

import com.me.lightdark.ecrans.GameScreen;

import com.badlogic.gdx.Game;

public class LightDark extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen());
	}

}