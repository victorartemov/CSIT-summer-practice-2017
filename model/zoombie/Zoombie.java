package com.csiit.suumerpractic.lukoicat.model.zoombie;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.csiit.suumerpractic.lukoicat.model.World;
import com.csiit.suumerpractic.lukoicat.model.constant.Constant;
import com.csiit.suumerpractic.lukoicat.model.player.Player;

import java.util.Random;

/**
 * Created by Рената on 06.07.2017.
 */
public class Zoombie extends Actor implements Constant {

    private final float SIZE;
    private final float Radius;
    private final float SPEED;
    private final float height = 1.0f;
    private final float width = 0.4f;
    private World world;
    private Vector2 position;
    private Vector2 velocity;
    private Rectangle bounds;
    private int counter = 0;
    private Direction direction;
    private State state;
    private int life;
    private String name;


    public Zoombie(World world, Vector2 vector2, float SIZE, float Radius, float SPEED, int life, String name) {

        this.name = name;
        this.SIZE = SIZE;
        this.Radius = Radius;
        this.SPEED = SPEED;
        this.life = life;

        this.bounds = new Rectangle();
        bounds.width = SIZE;
        bounds.height = SIZE;

        this.position = vector2;
        setHeight(height * world.ppuY);
        setWidth(width * world.ppuX);
        setX(position.x * world.ppuX);
        setY(position.y * world.ppuY);

        this.velocity = new Vector2();
        this.state = State.NONE;
        this.direction = Direction.NONE;
        this.world = world;

        getVelocity().y = SPEED;
        setFirstDirection();
    }

    public void update(float delta) {
        changeDirection(delta);
        position.add(velocity.scl(delta));
        setX(position.x * world.ppuX);
        setY(position.y * world.ppuY);
    }

    public void draw(Batch batch, float parentAlfa) {
        batch.draw(world.textureRegions.get(name), getX(), getY(), getWidth(), getHeight());
        batch.setColor(1, 1, 1, 1);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    private void changeDirection(float delta) {

        if (counter > 50) {
            newDirection(false);
        } else counter++;

        if (world.selectedActor != null && canSeePlayer())
            goToPlayer(delta, SPEED);
    }

    private void goToPlayer(float delta, float speed) {

        int bX = Math.round(world.getSelectedActor().getPosition().x);
        int bY = Math.round(world.getSelectedActor().getPosition().y);
        int X = Math.round(getPosition().x);
        int Y = Math.round(getPosition().y);

        float offset = delta * speed * 2;


        if (Math.abs(bX - X) < getWidth() / 2) {
            if (checkX(offset)) {
                if (bY > Y) {
                    direction = Direction.UP;
                    getVelocity().y = speed;
                } else {
                    direction = Direction.DOWN;
                    getVelocity().y = -speed;
                }
            } else {
                if (getPosition().x - X > 0) {
                    direction = Direction.LEFT;
                    getVelocity().x = -speed;
                }
                if (getPosition().x - X < 0) {
                    direction = Direction.RIGHT;
                    getVelocity().x = speed;
                }
            }

        }
        if (Math.abs(bY - Y) < height / 2) {
            if (checkY(offset)) {
                if (bX > X) {
                    direction = Direction.RIGHT;
                    getVelocity().x = speed;
                } else {
                    direction = Direction.LEFT;
                    getVelocity().x = -speed;
                }
            } else {
                if (getPosition().y - Y > 0) {
                    direction = Direction.DOWN;
                    getVelocity().y = -speed;
                }
                if (getPosition().y - Y < 0) {
                    direction = Direction.UP;
                    getVelocity().y = speed;
                }
            }

        }

        if (canKill()) {
            if (world.selectedActor instanceof Player && counter > 50) {
                ((Player) world.selectedActor).toDamage();
            }

        }
    }
    
    private boolean canKill() {

        int bX = Math.round(world.getSelectedActor().getPosition().x);
        int bY = Math.round(world.getSelectedActor().getPosition().y);
        int X = Math.round(getPosition().x);
        int Y = Math.round(getPosition().y);

        return Math.sqrt(Math.pow(X - bX, 2) + Math.pow(Y - bY, 2)) < SPEED ? true : false;
    }

    private boolean canSeePlayer() {
        boolean see = false;
        if (world.getSelectedActor() instanceof Player && ((Player) world.getSelectedActor()).getState() != State.DEAD) {

            int bX = Math.round(world.getSelectedActor().getPosition().x);
            int bY = Math.round(world.getSelectedActor().getPosition().y);

            int X = Math.round(getPosition().x);
            int Y = Math.round(getPosition().y);

            float b = Math.abs(world.getSelectedActor().getPosition().x - getPosition().x);
            float a = Math.abs(world.getSelectedActor().getPosition().y - getPosition().y);

            if (Math.sqrt((a * a + b * b)) < Radius) {
                see = true;
            }
        }

        return see;
    }


    private void setFirstDirection() {
        direction = Direction.UP;
        getVelocity().y = SPEED;
    }


    private void newDirection(boolean checkCur) {

        if (!checkCur)
            counter = 0;

        int newDir = 1 + new Random().nextInt(4);
        resetVelocity();
        Direction oldDir = direction;
        if (newDir == 1 && (direction != Direction.UP || !checkCur)) {
            getVelocity().y = SPEED;

            direction = Direction.UP;
        }


        if (oldDir == direction)
            if (newDir == 2 && (direction != Direction.DOWN || !checkCur)) {
                getVelocity().y = -SPEED;
                direction = Direction.DOWN;
            }

        if (oldDir == direction)
            if (newDir == 3 && (direction != Direction.RIGHT || !checkCur)) {

                getVelocity().x = SPEED;
                direction = Direction.RIGHT;
            }


        if (oldDir == direction)
            if (newDir == 4 && (direction != Direction.LEFT || !checkCur)) {
                getVelocity().x = -SPEED;
                direction = Direction.LEFT;
            }

    }

    private void resetVelocity() {
        getVelocity().x = 0;
        getVelocity().y = 0;
    }

    private boolean checkX(float offset) {
        return getPosition().x - (int) getPosition().x < offset;
    }

    private boolean checkY(float offset) {
        return getPosition().y - (int) getPosition().y < offset;
    }


}