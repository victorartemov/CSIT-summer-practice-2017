package com.csiit.suumerpractic.lukoicat.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;


import java.util.Map;


/**
 * Created by Juli on 04.07.2017.
 */

public class World extends Stage {

    public static float CAMERA_WIDTH = 8f;
    public static float CAMERA_HEIGHT = 5f;
   // Array<Zoombie> zoombie = new Array<Zoombie>();

    //Array<Brick> bricks = new Array<Brick>();
    public float ppuX;
    public float ppuY;
    Actor selectedActor = null;
    Map<String, TextureRegion> textureRegions;

    public World(int x, int y, boolean b, SpriteBatch spriteBatch, Map<String, TextureRegion> textureRegions) {
        super.getViewport().update(x, y, b);
        this.textureRegions = textureRegions;
        ppuX = getWidth() / CAMERA_WIDTH;
        ppuY = getHeight() / CAMERA_HEIGHT;
        addActor(new Player(new Vector2(4, 2), this));
        addActor(new Player(new Vector2(4, 4), this));
        addActor(new FirstZoombie(this,new Vector2(6,3)));

       // generateZoombieOnPosition(1,1);

        //addActor(new Brick(new Vector2(0,1), this));
        //addActor(new Brick(new Vector2(5,6), this));
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
            else ((FirstZoombie) actor).update(delta);

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
        if (actor != null)
            //запоминаем
            selectedActor = actor;
        return actor;
    }

    //создание зомби
 /*   public void generateZoombieOnPosition(int count, int type) {
        int x = 0, y = 0;

        for (int i = 0; i < count; ++i) {

            x = 1;
            y = 4;

            switch (type) {
                case 1:
                    zoombie.add(new FirstZoombie(this, new Vector2(x, y)));
                    break;
            }
        }
    }
*/
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
