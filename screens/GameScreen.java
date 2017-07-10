package com.csiit.suumerpractic.lukoicat.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csiit.suumerpractic.lukoicat.MyGame;
import com.csiit.suumerpractic.lukoicat.model.player.Player;
import com.csiit.suumerpractic.lukoicat.model.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Juli on 04.07.2017.
 */
public class GameScreen implements Screen {

    final MyGame game;

    public OrthographicCamera cam;
    public World world;
    private SpriteBatch spriteBatch;
    Texture textureMen;
    Texture textureMonster;
    public Map<String, TextureRegion> textureRegions = new HashMap<String, TextureRegion>();

    public int width;
    public int height;

    public GameScreen(final MyGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        this.cam = new OrthographicCamera(World.CAMERA_WIDTH, World.CAMERA_HEIGHT);
        SetCamera(World.CAMERA_WIDTH / 2f, World.CAMERA_HEIGHT / 2f);
        spriteBatch = new SpriteBatch();
        loadTextures();
        world = new World(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, spriteBatch, textureRegions);
        Gdx.input.setInputProcessor(world);
    }

    private void loadTextures() {
        textureMen = new Texture(Gdx.files.internal("men.png"));
        TextureRegion tmp[][] = TextureRegion.split(textureMen, textureMen.getWidth(), textureMen.getHeight());
        textureRegions.put("player", tmp[0][0]);

        textureMonster = new Texture(Gdx.files.internal("monster.png"));
        TextureRegion textureMonst[][] = TextureRegion.split(textureMonster, textureMonster.getWidth(), textureMonster.getHeight());
        textureRegions.put("monster", textureMonst[0][0]);

        //  //Получение регионов. Атлас состоит из 4 изображений одинакового размера
        //  TextureRegion tmp[][] = TextureRegion.split(texture, texture.getWidth() / 2, texture.getHeight() / 2);
        //  textureRegions.put("player", tmp[0][0]);
        //  textureRegions.put("zoombie", tmp[0][1]);
        //  textureRegions.put("brick2", tmp[1][0]);
        //  textureRegions.put("brick3", tmp[1][1]);
    }


    public void SetCamera(float x, float y) {
        this.cam.position.set(x, y, 0);
        this.cam.update();
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        world.getViewport().update(width, height, true);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        world.update(delta);
        world.draw();

        game.batch.begin();
        game.font.draw(game.batch, "Count life: " + ((Player)world.getActors().get(0)).getCountLife(), 50, 50);
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