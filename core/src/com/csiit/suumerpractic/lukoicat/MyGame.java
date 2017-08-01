package com.csiit.suumerpractic.lukoicat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.csiit.suumerpractic.lukoicat.screens.GameScreen;
import com.csiit.suumerpractic.lukoicat.screens.LoadScreen;

public class MyGame extends Game {

    private final AssetManager assetManager = new AssetManager();
    public SpriteBatch batch;
    public BitmapFont font;

    public GameScreen game;

    @Override
    public void create () {
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new
                InternalFileHandleResolver()));
        batch = new SpriteBatch();
        font = new BitmapFont();
        setScreen(new LoadScreen(this));
    }

    public void render() {
        super.render();
    }
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
