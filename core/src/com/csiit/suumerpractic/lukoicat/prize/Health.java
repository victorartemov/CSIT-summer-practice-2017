package com.csiit.suumerpractic.lukoicat.prize;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.csiit.suumerpractic.lukoicat.model.World;
import com.csiit.suumerpractic.lukoicat.model.constant.Constant;

/**
 * Created by Рената on 07.07.2017.
 */
public class Health extends Actor implements Constant {

    private World world;
    private String name;

    private State state;
    private Vector2 position;

    private final float height = 0.2f;
    private final float width = 0.2f;
    private final int speed = 100;
    private float cameraWidht;
    private float cameraHeight;

    public Health(World world, String name, float cameraWidth, float cameraHeight) {

        this.cameraHeight = cameraHeight;
        this.cameraWidht = cameraWidth;
        this.name = name;
        this.world = world;

        this.state = State.NONE;
        this.cameraWidht = cameraWidth;
        this.cameraHeight = cameraHeight;

        nextHealth();
        makeHealth();
    }

    private void makeHealth() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (state != State.TAKEN) {
                    if (state == State.TAKEN) {
                        nextHealth();
                    }
                    try {
                        Thread.sleep(speed * 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void nextHealth() {
        //state = State.NONE;
        setHeight(height * world.ppuY);
        setWidth(width * world.ppuX);
        setX(MathUtils.random(cameraWidht) * world.ppuY);
        setY(MathUtils.random(cameraHeight) * world.ppuY);
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


}
