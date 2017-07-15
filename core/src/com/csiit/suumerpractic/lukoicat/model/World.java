package com.csiit.suumerpractic.lukoicat.model;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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


/**
 * Created by Juli on 04.07.2017.
 */

public class World extends Stage implements Constant {

    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;

    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    Viewport viewport;

    public static float CAMERA_WIDTH = 8f;
    public static float CAMERA_HEIGHT = 5f;
    public OrthographicCamera cam;
    ShapeRenderer shapeRenderer;
    TiledMap tiledMap;

    MyGame game;

    public float ppuX;
    public float ppuY;
    public Actor selectedActor = null;

    public Map<String, TextureRegion> textureRegions;

    public Player getPlayer() {
        return player;
    }


    private Player player;
    private int zombieCount = 2;

    public void setMap(){
        tiledMap = game.getAssetManager().get("maps/map_lykoi_1.1.tmx");
        orthogonalTiledMapRenderer = new
            OrthogonalTiledMapRenderer(tiledMap, 1f);
        orthogonalTiledMapRenderer.setView(cam);
        orthogonalTiledMapRenderer.setMap(tiledMap);
    }
    public World(int x, int y, boolean b, SpriteBatch spriteBatch, Map<String, TextureRegion> textureRegions, MyGame game) {
        this.cam = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        this.cam.position.set(WORLD_WIDTH/6, WORLD_WIDTH/6, 0);
        this.cam.update();

        this.game = game;
        viewport =  new FitViewport(WORLD_WIDTH/4, WORLD_HEIGHT/4, cam);
        viewport.apply(true);
        shapeRenderer = new ShapeRenderer();
        setMap();

        super.getViewport().update(x, y, b);
        this.textureRegions = textureRegions;

        ppuX = getWidth() / CAMERA_WIDTH;
        ppuY = getHeight() / CAMERA_HEIGHT;
        player = new Player(new Vector2(3, 2), this);
        selectedActor = player;

        addActor(player);

        generateZombie(zombieCount, ZombieType.NORMAL);
        generateWeapon(Weapon.GUN);
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


    public void update(float delta) {
        orthogonalTiledMapRenderer.getBatch().setProjectionMatrix(cam.combined);
        if(getPlayer().getX() + orthogonalTiledMapRenderer.getViewBounds().getX() >= 485 ) {
            if (getPlayer().getDirection().get(Direction.RIGHT))
            {
                cam.position.x += getPlayer().SPEED/2;
            } else if (getPlayer().getDirection().get(Direction.LEFT)) {
                cam.position.x -= getPlayer().SPEED;
            }
        }
        System.out.println("Coord map: " + orthogonalTiledMapRenderer.getViewBounds().getX() +"," + orthogonalTiledMapRenderer.getViewBounds().getY());
        System.out.println("Coord player: (" + getPlayer().getX() + "," + getPlayer().getY() + ")");
        //System.out.println("Coord Origin player: (" + getPlayer().getOriginX(). + "," + getPlayer().getOriginY() + ")");


        orthogonalTiledMapRenderer.setView(cam);
        orthogonalTiledMapRenderer.render();
        cam.update();
        drawDebug();

        for (Actor actor : this.getActors()) {
            if (actor instanceof Player)
                ((Player) actor).update(delta);
            else if (actor instanceof Zombie) {
                ((Zombie) actor).update(delta);
            } else
                ((com.csiit.suumerpractic.lukoicat.prize.Weapon) actor).update(delta);
        }
    }

    public void setPP(float x, float y) {
        ppuX = x;
        ppuY = y;
    }

    //двигаем выбранного игрока
    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

        moveSelected(x, y);
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


    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        resetSelected();
        return true;
    }

   //создание зомби
    private void generateZombie(int count, ZombieType zombieType) {
        for (int i = 0; i < count; i++) {
            addActor(zombieType.choseZombie(this, CAMERA_WIDTH, CAMERA_HEIGHT));
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
            ((Player) selectedActor).ChangeNavigation(x, this.getHeight() - y);
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

}

