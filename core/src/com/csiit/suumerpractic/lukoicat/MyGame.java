package com.csiit.suumerpractic.lukoicat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.csiit.suumerpractic.lukoicat.screens.GameScreen;
import com.csiit.suumerpractic.lukoicat.screens.LoadScreen;

public class MyGame extends Game {

    private final AssetManager assetManager = new AssetManager();
    public SpriteBatch batch;
    public BitmapFont font;

    final String FONT_CHARS = "абвгдежзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
    public GameScreen game;

    @Override
    public void create () {
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new
                InternalFileHandleResolver()));
        batch = new SpriteBatch();
        final String FONT_PATH = "fonts/8bit16.ttf";
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_PATH));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = FONT_CHARS;
        parameter.size = 15;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();

        setScreen(new LoadScreen(this));
       // setScreen(new FirstScreen(this));
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
