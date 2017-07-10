package com.csiit.suumerpractic.lukoicat.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csiit.suumerpractic.lukoicat.model.constant.Constant;
import com.csiit.suumerpractic.lukoicat.model.player.Player;
import com.csiit.suumerpractic.lukoicat.model.zoombie.Zoombie;


import java.util.Collection;
import java.util.Map;


/**
 * Created by Juli on 04.07.2017.
 */

public class World extends Stage implements Constant {


    public static final float CAMERA_WIDTH = 8f;
    public static final float CAMERA_HEIGHT = 5f;
    public static float ppuX;
    public static float ppuY;
    public Actor selectedActor = null;
    public Map<String, TextureRegion> textureRegions;

    public World(int x, int y, boolean b, SpriteBatch spriteBatch, Map<String, TextureRegion> textureRegions) {
        super.getViewport().update(x, y, b);
        this.textureRegions = textureRegions;
        ppuX = getWidth() / CAMERA_WIDTH;
        ppuY = getHeight() / CAMERA_HEIGHT;

        addActor(new Player(new Vector2(4, 2), this));
        addActor(new Player(new Vector2(4, 4), this));

        generateZoombieOnPosition(1, ZoombieType.NORMAL);

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
            else ((Zoombie) actor).update(delta);

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
        if (actor != null) { //запоминаем
            selectedActor = actor;
        }

        return actor;
    }

    //создание зомби
    public void generateZoombieOnPosition(int count, ZoombieType zoombieType ) {
        for(int i=0;i<count;i++){
            addActor(zoombieType.choseZoombie(this, CAMERA_WIDTH,CAMERA_HEIGHT));
        }

    }

    /**
     * Передвижение выбранного актера по параметрам
     *
     * @param x
     * @param y
     */
    private void moveSelected(float x, float y) {
        if (selectedActor != null && selectedActor instanceof com.csiit.suumerpractic.lukoicat.model.player.Player) {
            ((com.csiit.suumerpractic.lukoicat.model.player.Player) selectedActor).ChangeNavigation(x, this.getHeight() - y);
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


    public float getPpuX() {
        return ppuX;
    }

    public float getPpuY() {
        return ppuY;
    }

    public Map<String, TextureRegion> getTextureRegions() {
        return textureRegions;
    }
}
