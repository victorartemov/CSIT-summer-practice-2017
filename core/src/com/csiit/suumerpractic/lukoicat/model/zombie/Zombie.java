package com.csiit.suumerpractic.lukoicat.model.zombie;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.csiit.suumerpractic.lukoicat.animation.AnimatorZombie;
import com.csiit.suumerpractic.lukoicat.model.World;
import com.csiit.suumerpractic.lukoicat.model.constant.Constant;
import java.util.Random;

public class Zombie extends Actor implements Constant {

    private AnimatorZombie animatorZombie;

    private final float Radius;
    private final float SPEED;
    private World world;
    private Vector2 position;
    private Vector2 velocity;
    private int counter = 0;
    private Direction direction;
    private State state;
    private int life;
    private String name;


    public Zombie(World world, Vector2 vector2, float Radius, float SPEED, int life, String name) {

        this.animatorZombie = new AnimatorZombie();
        animatorZombie.create();
        animatorZombie.setWorld(world);
        animatorZombie.setSize(getWidth(), getHeight());

        this.name = name;

        this.Radius = Radius;
        this.SPEED = SPEED;
        this.life = life;

        this.position = vector2;
        setX(position.x * world.ppuX);
        setY(position.y * world.ppuY);

        this.velocity = new Vector2();
        this.state = State.NONE;
        this.direction = Direction.NONE;
        this.world = world;

        getVelocity().y = SPEED;
        setFirstDirection();
    }

    public Actor hit(float x, float y, boolean touchable) {
        return x > 0 && x < getWidth() && y > 0 && y < getHeight() ? this : null;
    }

    public void update(float delta) {
        if (state == State.DEAD) {
            setWidth(0);
        } else {
            changeDirection(delta);
            position.add(velocity.scl(delta));
            setX(position.x * world.ppuX);
            setY(position.y * world.ppuY);
        }
    }

    public void draw(Batch batch, float parentAlfa) {
        if (state != State.DEAD) {
            animatorZombie.setPositionMen(getX(), getY());
            if (direction == Direction.LEFT) {
                animatorZombie.walkLeft(batch);
            } else if (direction == Direction.RIGHT) {
                animatorZombie.walkRight(batch);
            } else if (direction == Direction.DOWN) {
                animatorZombie.walkDown(batch);
            } else if (direction == Direction.UP) {
                animatorZombie.walkUp(batch);
            } else
                animatorZombie.stayZombie(batch);
            batch.setColor(1, 1, 1, 1);
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    private void changeDirection(float delta) {

        if (counter > 50) {
            newDirection();
        } else counter++;

        if (canSeePlayer() && !canKill()) {
            goToPlayer(delta, SPEED);
        }
        if (canKill())
            if (counter > 50) {
                world.getPlayer().toDamage();
            }
    }

    private void goToPlayer(float delta, float speed) {

        int pX = Math.round(world.getPlayer().getX());
        int pY = Math.round(world.getPlayer().getY());
        int x = Math.round(getX() + 1);
        int y = Math.round(getY());

        if (y < pY) {
            up();
        } else if (y >= pY) {
            down();
        }
        if (x >= pX) {
            left();
        } else if (x < pX) {
            right();
        }
    }

    private boolean canKill() {
        int pX = Math.round(world.getPlayer().getX());
        int pY = Math.round(world.getPlayer().getY());
        int x = Math.round(getX());
        int y = Math.round(getY());

        return Math.abs(pX - x) <= world.getPlayer().getWidth()/2 && Math.abs(pY - y) <= world.getPlayer().getHeight()/2;

    }

    private boolean canSeePlayer() {

        if (State.DEAD != world.getPlayer().getState()) {

            float b = Math.abs(world.getPlayer().getPosition().x - getPosition().x);
            float a = Math.abs(world.getPlayer().getPosition().y - getPosition().y);
            if (Math.sqrt((a * a + b * b)) < Radius) {
                return true;
            }
        }
        return false;
    }


    private void setFirstDirection() {
        direction = Direction.UP;
        getVelocity().y = SPEED;
    }


    private void newDirection() {

        counter = 0;

        int newDir = 1 + new Random().nextInt(4);
        resetVelocity();
        Direction oldDir = direction;
        if (newDir == 1 && (direction != Direction.UP)) {
            up();
        }

        if (oldDir == direction)
            if (newDir == 2 && (direction != Direction.DOWN)) {
                down();
            }

        if (oldDir == direction)
            if (newDir == 3 && (direction != Direction.RIGHT)) {
                right();
            }

        if (oldDir == direction)
            if (newDir == 4 && (direction != Direction.LEFT)) {
                left();
            }

    }

    private void resetVelocity() {
        getVelocity().x = 0;
        getVelocity().y = 0;
    }

    public void makeDamage() {
        life--;
        if (life == 0) {
            state = State.DEAD;
        }
        System.out.println("life - " + life);
    }

    public boolean canKill(float x, float y) {
        float xZ = getX();
        float yZ = getY();
        return (x >= xZ) && (x <= (xZ + 30)) && (y >= yZ) && (y <= (yZ + 100)) && canSeePlayer();
    }

    private void right() {
        getVelocity().x = SPEED;
        direction = Direction.RIGHT;
    }

    private void down() {
        getVelocity().y = -SPEED;
        direction = Direction.DOWN;
    }

    private void up() {
        getVelocity().y = SPEED;
        direction = Direction.UP;
    }

    private void left() {
        getVelocity().x = -SPEED;
        direction = Direction.LEFT;
    }
}