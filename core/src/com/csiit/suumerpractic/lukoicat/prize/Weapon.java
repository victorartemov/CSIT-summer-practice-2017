package com.csiit.suumerpractic.lukoicat.prize;

//import suvitruf.classic.bomberman.view.WorldRenderer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.csiit.suumerpractic.lukoicat.model.World;
import com.csiit.suumerpractic.lukoicat.model.constant.Constant;

public class Weapon extends Actor implements Constant {


    private static float SIZE = 1f;


    private Rectangle bounds = new Rectangle();
    private Weapon weaponeType;
    private World world;
    private String name;
    private int damage;
    private Vector2 velocity;
    private State state;
    private Vector2 position;

    private final float height = 0.1f;
    private final float width = 0.2f;

    public Weapon(World world, Vector2 position, Weapon weaponeType, String name, int damage) {
        this.weaponeType = weaponeType;
        this.name = name;
        this.world = world;
        this.position = position;
        this.bounds.width = SIZE;
        this.bounds.height = SIZE;
        this.damage = damage;
        this.state = State.NONE;
        this.velocity = new Vector2();

        setHeight(height * world.ppuY);
        setWidth(width * world.ppuX);
        setX(position.x * world.ppuX);
        setY(position.y * world.ppuY);
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void draw(Batch batch, float parentAlfa) {
        batch.draw(world.textureRegions.get(name), getX(), getY(), getWidth(), getHeight());
        batch.setColor(1, 1, 1, 1);
    }

    public void update(float delta) {
        if (state == State.TAKEN) {
            setWidth(0);
            setHeight(0);
        }

    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }


    public Weapon getWeaponeType() {
        return weaponeType;
    }
}