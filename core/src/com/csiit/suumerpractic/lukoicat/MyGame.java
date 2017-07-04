package com.csiit.suumerpractic.lukoicat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csiit.suumerpractic.lukoicat.controller.WorldController;
import com.csiit.suumerpractic.lukoicat.model.World;
import com.csiit.suumerpractic.lukoicat.screens.GameScreen;
import com.csiit.suumerpractic.lukoicat.view.WorldRenderer;

public class MyGame extends Game {
	//SpriteBatch batch;
	//Texture img;
	public GameScreen game;
	
	@Override
	public void create () {
		game = new GameScreen();
		setScreen(game);
	}

	@Override
	public void resize(int width, int height) {

	}

	//@Override
	//public void render () {
	//	game.show();
//
	//	//Gdx.gl.glClearColor(1, 0, 0, 1);
	//	//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	//	//batch.begin();
	//	//batch.draw(img, 0, 0);
	//	//batch.end();
	//}
	//
	//@Override
	//public void dispose () {
	//	//batch.dispose();
	//	//img.dispose();
	//}

	//public void setScreen(GameScreen screen) {
	//	this.game = screen;
	//}
}
