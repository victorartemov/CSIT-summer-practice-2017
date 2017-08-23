package com.csiit.suumerpractic.lukoicat.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csiit.suumerpractic.lukoicat.MyGame;
import com.csiit.suumerpractic.lukoicat.model.constant.Constant;
import com.csiit.suumerpractic.lukoicat.model.player.Player;
import com.csiit.suumerpractic.lukoicat.model.zombie.Zombie;
import com.csiit.suumerpractic.lukoicat.prize.Health;

import java.util.Map;

public class World extends Stage implements Constant {

    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;

    private static final float GAME_SCREEN_WIDTH = 1280;
    private static final float GAME_SCREEN_HEIGHT = 720;

    //private static final float WIDTH = 608;
    //private static final float HEIGHT = 1080;

    private float gamePpuX, gamePpuY;

    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    Viewport viewport;

    public static float CAMERA_WIDTH = 8f;
    public static float CAMERA_HEIGHT = 5f;
    public OrthographicCamera cam;
    ShapeRenderer shapeRenderer;
    TiledMap tiledMap;

    MyGame game;

    public float ppuX; //pixels per unit
    public float ppuY;
    public Actor selectedActor = null;

    public Map<String, TextureRegion> textureRegions;

    public Player getPlayer() {
        return player;
    }

    private Player player;
    private int zombieCount = 50;

    public void setMap() {
        tiledMap = game.getAssetManager().get("maps/map_lykoi_1.3.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f);
        orthogonalTiledMapRenderer.setView(cam);
        orthogonalTiledMapRenderer.setMap(tiledMap);
    }


    public World(int x, int y, boolean b, SpriteBatch spriteBatch, Map<String, TextureRegion> textureRegions, MyGame game) {
        this.cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        this.cam.position.set(WORLD_WIDTH / 6, WORLD_WIDTH / 6, 0);
        this.cam.update();

        this.game = game;
        viewport = new FitViewport(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, cam);
        viewport.apply(true);
        setViewport(viewport);
        shapeRenderer = new ShapeRenderer();
        setMap();

        gamePpuX = GAME_SCREEN_WIDTH / cam.viewportWidth; //для персонажа
        gamePpuY = GAME_SCREEN_HEIGHT / cam.viewportHeight; //для персонажа

        super.getViewport().update(x, y, b);
        this.textureRegions = textureRegions;

        ppuX = getWidth() / CAMERA_WIDTH; //пока что используется в зомби, не удалять
        ppuY = getHeight() / CAMERA_HEIGHT; //пока что используется в зомби, не удалять

        player = new Player(new Vector2(860, 1390), this);
       //player = new Player(new Vector2(800,400), this);

        selectedActor = player;

        addActor(player);

        generateZombie(zombieCount, ZombieType.NORMAL);
        generateWeapon(Weapon.GUN);
        checkCollision();
    }


    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(cam.projection);
        shapeRenderer.setTransformMatrix(cam.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.end();
    }

    public Player getSelectedActor() {
        if (selectedActor instanceof Player)
            return (Player) selectedActor;
        return null;
    }

    public void checkCollision() {
        MapObjects collisions = orthogonalTiledMapRenderer.getMap().getLayers().get(1).getObjects();
        float posX = player.getX();
        float posY = player.getY(); //getHeight()*this.getGamePpuY() -

        // System.out.print(orthogonalTiledMapRenderer.getMap().getLayers().get(0).getName());
        boolean collisDetect = false;
        for (int i = 0; i < collisions.getCount(); i++) {
            MapObject object = collisions.get(i);
            if (posX > object.getProperties().get("x", Float.class) && (posX < (object.getProperties().get("x", Float.class) + object.getProperties().get("width", Float.class))) &&
                    posY > object.getProperties().get("y", Float.class) &&
                    (posY < (object.getProperties().get("y", Float.class) + object.getProperties().get("height", Float.class)))) {

                System.out.println("Collision detected " + object.getName());
                player.stop();
                collisDetect = true;
            }
            else if(collisDetect == false){
                player.setCollisDetect(-1);
            }
        }
    }
    public void update(float delta) {
        //ppuRefrash();
        checkCollision();
        cam.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
        cam.update();
        orthogonalTiledMapRenderer.getBatch().setProjectionMatrix(cam.combined);
        orthogonalTiledMapRenderer.setView(cam);
        orthogonalTiledMapRenderer.render();
        drawDebug();

        for (Actor actor : this.getActors()) {
            if (actor instanceof Player)
                ((Player) actor).act(delta);
            else if (actor instanceof Zombie) {
                ((Zombie) actor).update(delta);
            } else
                ((com.csiit.suumerpractic.lukoicat.prize.Weapon) actor).update(delta);
        }
    }

    //двигаем выбранного игрока
    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

        // moveSelected(x/gamePpuX+cam.position.x, 0);
        moveSelected(x + (gamePpuX * cam.position.x)-cam.viewportWidth, (this.getHeight() - y) / gamePpuY + cam.position.y);
       // System.out.println((selectedActor.getX()));
      //  System.out.println(x - cam.viewportWidth + 2 * cam.position.x);
        // System.out.println(cam.position.x);
        // System.out.println((this.getHeight()-y)/gamePpuY+cam.position.y);
        // System.out.println(selectedActor.getX() + " - " + x + " - " + cam.position.x + " - "  + cam.position.x));
        for (Actor actor : getActors()) {
            if (actor instanceof Zombie) {
                if (((Zombie) actor).canKill(x, this.getHeight() - y)) {
                    player.killZombie((Zombie) actor);
                    System.out.println("makeDamage");
                }
            }
        }
        return true;
    }

    public float getCameraHeight() {
        return cam.viewportHeight;
    }

    public float getCamPositionY() {
        return cam.position.y;
    }


    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        resetSelected();
        return true;

    }

    //создание зомби
    private void generateZombie(int count, ZombieType zombieType) {
        for (int i = 0; i < count; i++) {
            addActor(zombieType.choseZombie(this, WORLD_WIDTH, WORLD_HEIGHT));
        }
    }

    private void generateWeapon(Weapon weapon) {
        addActor(weapon.makeWeapon(this, CAMERA_WIDTH, CAMERA_HEIGHT));
    }

    public com.csiit.suumerpractic.lukoicat.prize.Weapon getWeapone() {
        com.csiit.suumerpractic.lukoicat.prize.Weapon weapon = null;
        for (Actor actor : this.getActors()) {
            if (actor instanceof com.csiit.suumerpractic.lukoicat.prize.Weapon)
                weapon = (com.csiit.suumerpractic.lukoicat.prize.Weapon) actor;
        }
        return weapon;
    }

    public Health getHealth() {
        Health health = null;
        for (Actor actor : this.getActors()) {
            if (actor instanceof Health)
                health = (Health) actor;
        }
        return health;
    }


    /**
     * Передвижение выбранного актера по параметрам
     *
     * @param x
     * @param y
     */
    private void moveSelected(float x, float y) {
        if (selectedActor != null && selectedActor instanceof Player) {
            ((Player) selectedActor).changeNavigation(x, y);
        }
    }

    /**
     * Сбрасываем текущий вектор и направление движения
     */
    private void resetSelected() {
        if (selectedActor != null && selectedActor instanceof Player) {
            ((Player) selectedActor).resetWay();
        }
    }

    //Обновление ppu, пока не используется, но может понадобиться
    public void ppuRefrash() {
        float newWidth = Gdx.graphics.getWidth();
        float newHeight = Gdx.graphics.getHeight();

        float gameScale = newHeight / newWidth > getGameScreenHeight() / getGameScreenWidth()
                ? newWidth / getGameScreenWidth() : newHeight / getGameScreenHeight();


        cam.viewportWidth = (int) (getGameScreenWidth() + (newWidth - getGameScreenWidth() * gameScale) / gameScale);
        cam.viewportHeight = (int) (getGameScreenHeight()
                + (newHeight - getGameScreenHeight() * gameScale) / gameScale);
        gamePpuX = newWidth / cam.viewportWidth;
        gamePpuY = newHeight / cam.viewportHeight;
    }

    public float getGameScreenWidth() {
        return GAME_SCREEN_WIDTH;
    }

    public float getGameScreenHeight() {
        return GAME_SCREEN_HEIGHT;
    }

    public float getGamePpuX() {
        return gamePpuX;
    }

    public float getGamePpuY() {
        return gamePpuY;
    }

}

