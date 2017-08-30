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
    private int counter;
    private Direction direction;
    private State state;
    private int life;


    public Zombie(World world, Vector2 vector2, float Radius, float SPEED, int life) {

        this.animatorZombie = new AnimatorZombie();
        animatorZombie.create();
        animatorZombie.setWorld(world);
        animatorZombie.setSize(getWidth(), getHeight());

        this.Radius = Radius;
        this.SPEED = SPEED;
        this.life = life;

        this.position = vector2;
        this.setX(position.x);
        this.setY(position.y);

        this.velocity = new Vector2();
        this.getVelocity().y = SPEED;

        this.counter = 0;
        this.state = State.NONE;
        this.direction = Direction.NONE;
        this.world = world;

        setFirstDirection();
    }

    public Actor hit(float x, float y, boolean touchable) {
        return x > 0 && x < getWidth() && y > 0 && y < getHeight() ? this : null;
    }

    public void update(float delta) {
        if (state == State.DEAD) {
            setWidth(0);
        } else {
            changeDirection();
            position.add(velocity.scl(delta));
            setX(position.x + (world.getWidth() / world.getPpuX()));
            setY(position.y);
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

    private void changeDirection() {

        if (counter > 50) {
            newDirection();
        } else counter++;

        if (canSeePlayer() && !canKill()) {
            goToPlayer();
        }
        if (canKill()) {
            if (counter > 50) {
                world.getPlayer().toDamage();
            }
        }
    }

    private void goToPlayer() {

        if (getY() < world.getPlayer().getY()) {
            up();
        } else {
            down();
        }
        if (getX() >= world.getPlayer().getX()) {
            left();
        } else {
            right();
        }
    }

    private boolean canKill() {
        //80 и 30 размеры персонажа
        return Math.abs(world.getPlayer().getX() - getX()) <= 30 / 2 && Math.abs(world.getPlayer().getY() - getY()) <= 80 / 2;

    }

    private boolean canSeePlayer() {

        if (State.DEAD != world.getPlayer().getState()) {

            float b = Math.abs(world.getPlayer().getPosition().x - getPosition().x * 2);
            float a = Math.abs(world.getPlayer().getPosition().y - getPosition().y * 2);
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
        float xZ = getX() * 2;
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