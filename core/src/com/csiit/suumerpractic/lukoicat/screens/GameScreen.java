package com.csiit.suumerpractic.lukoicat.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csiit.suumerpractic.lukoicat.MyGame;
import com.csiit.suumerpractic.lukoicat.model.player.Player;
import com.csiit.suumerpractic.lukoicat.model.World;

import java.util.HashMap;
import java.util.Map;


public class GameScreen implements Screen {

    final MyGame game;
    public World world;
    private SpriteBatch spriteBatch;
    Texture textureGun;
   // Texture textureButton;

    public Map<String, TextureRegion> textureRegions = new HashMap<String, TextureRegion>();

    public int width;
    public int height;

    public GameScreen(final MyGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        loadTextures();

        world = new World(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, spriteBatch, textureRegions, game);

        Gdx.input.setInputProcessor(world);
    }


    private void loadTextures() {
        textureGun = new Texture(Gdx.files.internal("gun.png"));
        TextureRegion textureGu[][] = TextureRegion.split(textureGun, textureGun.getWidth(), textureGun.getHeight());
        textureRegions.put("gun", textureGu[0][0]);

       // textureButton = new Texture(Gdx.files.internal("button_cat.png"));
       // TextureRegion textureB[][] = TextureRegion.split(textureButton, textureButton.getWidth(), textureButton.getHeight());
       // textureRegions.put("button", textureB[0][0]);
    }


    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        world.getViewport().update(width, height, true);
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.DARK_GRAY.r, Color.DARK_GRAY.g,
                Color.DARK_GRAY.b, Color.DARK_GRAY.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        world.update(delta);
        world.act(delta);
        world.getCamera().update();
        world.getBatch().setProjectionMatrix(world.getCamera().combined);
        world.draw();
        game.batch.begin();
        game.font.draw(game.batch, "Count life: " + ((Player)world.getActors().get(0)).getCountLife(), 50, 50);
        game.font.draw(game.batch, "C - reincarnate in lykoi-cat", world.getWidth()+ 320, 50);
        game.batch.end();
    }


    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }


    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

}