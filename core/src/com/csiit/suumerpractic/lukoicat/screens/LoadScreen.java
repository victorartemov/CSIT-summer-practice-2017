package com.csiit.suumerpractic.lukoicat.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csiit.suumerpractic.lukoicat.MyGame;
import com.csiit.suumerpractic.lukoicat.model.World;


/**
 * Created by Juli on 11.07.2017.
 */
public class LoadScreen implements Screen {
    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;
    private static final float PROGRESS_BAR_WIDTH = 100;
    private static final float PROGRESS_BAR_HEIGHT = 25;

    final MyGame game;

    public OrthographicCamera cam;
    Viewport viewport;

    public int width;
    public int height;
    float progress = 0;
    private ShapeRenderer shapeRenderer;


    public LoadScreen(final MyGame game) {
        this.game = game;
    }
    @Override
    public void show() {
        cam = new OrthographicCamera();
        cam.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);
        cam.update();
        viewport = new FitViewport(WORLD_WIDTH / 2, WORLD_HEIGHT/2, cam);
        shapeRenderer = new ShapeRenderer();

        game.getAssetManager().load("maps/map_lykoi_1.1.tmx", TiledMap.class);
    }

    private void draw() {
        shapeRenderer.setProjectionMatrix(cam.projection);
        shapeRenderer.setTransformMatrix(cam.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(
                (WORLD_WIDTH - PROGRESS_BAR_WIDTH) / 2, WORLD_HEIGHT / 2 -
                        PROGRESS_BAR_HEIGHT / 2,
                progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
        shapeRenderer.end();
    }
    @Override
    public void render(float delta) {
        update();
        clearScreen();
        draw();
    }

    private void clearScreen(){
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g,
                Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
    private void update() {
        if (game.getAssetManager().update()) {
           game.setScreen(new GameScreen(game));
        } else {
            progress = game.getAssetManager().getProgress();

        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
