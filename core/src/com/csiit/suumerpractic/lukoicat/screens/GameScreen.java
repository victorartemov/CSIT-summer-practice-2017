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
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csiit.suumerpractic.lukoicat.MyGame;
import com.csiit.suumerpractic.lukoicat.model.player.Player;
import com.csiit.suumerpractic.lukoicat.model.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Juli on 04.07.2017.
 */
public class GameScreen implements Screen {

    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TiledMap tiledMap;
    final MyGame game;

    public OrthographicCamera cam;
    public World world;
    private SpriteBatch spriteBatch;
    Texture textureMen;
    Texture textureMonster;
    Texture textureGun;
    Viewport viewport;

    public Map<String, TextureRegion> textureRegions = new HashMap<String, TextureRegion>();

    public int width;
    public int height;
    private ShapeRenderer shapeRenderer;

    public GameScreen(final MyGame game) {
        this.game = game;
    }

    @Override
    public void show() {

        this.cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        this.cam.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);
        this.cam.update();

        viewport =  new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, cam);
        viewport.apply(true);
        shapeRenderer = new ShapeRenderer();
        tiledMap = game.getAssetManager().get("map_lykoi.tmx");

        spriteBatch = new SpriteBatch();
        loadTextures();

        world = new World(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, spriteBatch, textureRegions);
        Gdx.input.setInputProcessor(world);
        orthogonalTiledMapRenderer = new
                OrthogonalTiledMapRenderer(tiledMap, spriteBatch);
        orthogonalTiledMapRenderer.setView(cam);
        orthogonalTiledMapRenderer.setView(cam);

    }

    private void draw() {
       spriteBatch.setProjectionMatrix(cam.projection);
       spriteBatch.setTransformMatrix(cam.view);
       orthogonalTiledMapRenderer.render();
    }
    private void loadTextures() {
       // mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1);

        textureMen = new Texture(Gdx.files.internal("men.png"));
        TextureRegion tmp[][] = TextureRegion.split(textureMen, textureMen.getWidth(), textureMen.getHeight());
        textureRegions.put("player", tmp[0][0]);

        textureMonster = new Texture(Gdx.files.internal("monster.png"));
        TextureRegion textureMonst[][] = TextureRegion.split(textureMonster, textureMonster.getWidth(), textureMonster.getHeight());
        textureRegions.put("monster", textureMonst[0][0]);

        textureGun = new Texture(Gdx.files.internal("gun.png"));
        TextureRegion textureGu[][] = TextureRegion.split(textureGun, textureGun.getWidth(), textureGun.getHeight());
        textureRegions.put("gun", textureGu[0][0]);

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

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.DARK_GRAY.r, Color.DARK_GRAY.g,
                Color.DARK_GRAY.b, Color.DARK_GRAY.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(cam.projection);
        shapeRenderer.setTransformMatrix(cam.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.end();
    }
    @Override
    public void render(float delta) {

       update(delta);
       clearScreen();
       draw();
       drawDebug();

       world.update(delta);
       world.draw();
       game.batch.begin();
       game.font.draw(game.batch, "Count life: " + ((Player)world.getActors().get(0)).getCountLife(), 50, 50);
       game.batch.end();
    }

    private void update(float delta) {
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