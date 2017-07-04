package com.csiit.suumerpractic.lukoicat.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Juli on 04.07.2017.
 */
public class Brick {
    static final float SIZE = 1f;
    Vector2 position = new Vector2();
    Rectangle bounds = new Rectangle();

    public Brick(Vector2 position) {
        this.position = position;
        this.bounds.width = SIZE;
        this.bounds.height = SIZE;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Vector2 getPosition() {
        return position;
    }
}
