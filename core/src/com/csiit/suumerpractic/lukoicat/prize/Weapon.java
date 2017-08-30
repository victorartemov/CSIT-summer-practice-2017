package com.csiit.suumerpractic.lukoicat.prize;

//import suvitruf.classic.bomberman.view.WorldRenderer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.csiit.suumerpractic.lukoicat.model.World;
import com.csiit.suumerpractic.lukoicat.model.constant.Constant;

public class Weapon extends Actor implements Constant {

    private Weapon weaponeType;
    private World world;
    private String name;
    private int damage;
    private Vector2 velocity;
    private State state;
    private Vector2 position;

    public Weapon(World world, Vector2 position, Weapon weaponeType, String name) {
        this.weaponeType = weaponeType;
        this.name = name;
        this.world = world;
        this.position = position;


        this.state = State.NONE;
        this.velocity = new Vector2();

        setHeight(16);
        setWidth(16);

        setX(500);
        setY(700);
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void draw(Batch batch, float parentAlfa) {
        batch.draw(world.textureRegions.get(name), getX(), getY());
        batch.setColor(1, 1, 1, 1);
    }

    public void update(float delta) {
        if (state == State.TAKEN) {
            setWidth(0);
            setHeight(0);
            System.out.println(getWidth() + "-wW " + getHeight() + " -wH");
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public Weapon getWeaponeType() {
        return weaponeType;
    }
}