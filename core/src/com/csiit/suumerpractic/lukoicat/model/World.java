package com.csiit.suumerpractic.lukoicat.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csiit.suumerpractic.lukoicat.model.constant.Constant;
import com.csiit.suumerpractic.lukoicat.model.player.Player;
import com.csiit.suumerpractic.lukoicat.model.zombie.Zombie;
import com.csiit.suumerpractic.lukoicat.prize.Weapon;

import java.util.Map;


/**
 * Created by Juli on 04.07.2017.
 */

public class World extends Stage implements Constant {


    public static float CAMERA_WIDTH = 8f;
    public static float CAMERA_HEIGHT = 5f;

    public float ppuX;
    public float ppuY;
    public Actor selectedActor = null;
    public Map<String, TextureRegion> textureRegions;
    Player player;

    public World(int x, int y, boolean b, SpriteBatch spriteBatch, Map<String, TextureRegion> textureRegions) {
        super.getViewport().update(x, y, b);
        this.textureRegions = textureRegions;
        ppuX = getWidth() / CAMERA_WIDTH;
        ppuY = getHeight() / CAMERA_HEIGHT;
        player=new Player(new Vector2(4, 2), this);
        selectedActor=player;
        addActor(player);

        generateZombie(1, ZombieType.NORMAL);
         generateWeapon(Weapon.GUN);

    }

    public Player getSelectedActor() {
        if (selectedActor instanceof Player)
            return (Player) selectedActor;
        return null;
    }

    public void update(float delta) {
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
        return true;
    }


    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        resetSelected();
        return true;
    }

    public Actor hit(float x, float y, boolean touchable) {
        Actor actor = super.hit(x, y, touchable);
        //если выбрали актера
        if (actor instanceof Zombie)
            player.killZombie((Zombie)actor);
        return actor;
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

