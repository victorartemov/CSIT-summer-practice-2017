package com.csiit.suumerpractic.lukoicat.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.csiit.suumerpractic.lukoicat.MyGame;
import com.csiit.suumerpractic.lukoicat.screens.GameScreen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new MyGame(), config);
	}
}
