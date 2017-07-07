package com.csiit.suumerpractic.lukoicat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csiit.suumerpractic.lukoicat.screens.GameScreen;

public class MyGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;

	public GameScreen game;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		game = new GameScreen(this);
		setScreen(game);
	}

	public void render() {
		super.render();
	}
	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}
