package com.csiit.suumerpractic.lukoicat.controller;

import com.csiit.suumerpractic.lukoicat.model.Player;
import com.csiit.suumerpractic.lukoicat.model.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Juli on 04.07.2017.
 */


public class WorldController {
    enum Keys {
        LEFT, RIGHT, UP, DOWN
    }
    public Player player;

    static Map<Keys, Boolean> keys = new HashMap<WorldController.Keys, Boolean>();
    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.UP, false);
        keys.put(Keys.DOWN, false);
    }

    public WorldController(World world) {
        this.player = world.getPlayer();
    }

    public void leftPressed() {
        keys.get(keys.put(Keys.LEFT, true));
    }

    public void rightPressed() {
        keys.get(keys.put(Keys.RIGHT, true));
    }

    public void upPressed() {
        keys.get(keys.put(Keys.UP, true));
    }

    public void downPressed() {
        keys.get(keys.put(Keys.DOWN, true));
    }

    //Отпустили мышь

    public void leftReleased() {
        keys.put(Keys.LEFT, false);
    }

    public void rightReleased() {
        keys.put(Keys.RIGHT, false);
    }

    public void upReleased() {
        keys.put(Keys.UP, false);
    }

    public void downReleased() {
        keys.put(Keys.DOWN, false);
    }

    public void update(float delta) {
        processInput();
        player.update(delta);
    }

    public void resetWay(int x, int y){
        rightReleased();
        leftReleased();
        downReleased();
        upReleased();
    }
    private void processInput() {
        if (keys.get(Keys.LEFT))
            player.getVelocity().x = -Player.SPEED;

        if (keys.get(Keys.RIGHT))
            player.getVelocity().x =Player.SPEED;

        if (keys.get(Keys.UP))
            player.getVelocity().y = Player.SPEED;

        if (keys.get(Keys.DOWN))
            player.getVelocity().y = -Player.SPEED;

        if ((keys.get(Keys.LEFT) && keys.get(Keys.RIGHT)) ||
                (!keys.get(Keys.LEFT) && (!keys.get(Keys.RIGHT))))
            player.getVelocity().x = 0;

        if ((keys.get(Keys.UP) && keys.get(Keys.DOWN)) ||
                (!keys.get(Keys.UP) && (!keys.get(Keys.DOWN))))
            player.getVelocity().y = 0;
    }
}
