package com.csiit.suumerpractic.lukoicat;

import com.badlogic.gdx.Game;
import com.csiit.suumerpractic.lukoicat.screens.GameScreen;

public class MyGame extends Game {

	public GameScreen game;
	
	@Override
	public void create () {
		game = new GameScreen();
		setScreen(game);
	}

}
