package com.csiit.suumerpractic.lukoicat.model.player;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.csiit.suumerpractic.lukoicat.animation.AnimatorMen;
import com.csiit.suumerpractic.lukoicat.model.World;
import com.csiit.suumerpractic.lukoicat.model.constant.Constant;
import com.csiit.suumerpractic.lukoicat.model.zombie.Zombie;
import sun.nio.cs.ext.MacHebrew;

import java.util.HashMap;
import java.util.Map;

public class Player extends Actor implements Constant {

    private AnimatorMen animatorMen;

    private static final float SPEED;

    private int countLife;
    private boolean isCat;
    private World world;
    private Vector2 position;

    //для выч. движения
    private Vector2 velocity;
    private State state;
    private int collisDetect;
    private Weapon weapon;

    //Мап для направлений
    private static Map<Direction, Boolean> direction;

    private float mouseX;
    private float mouseY;
    private boolean findGun;
    private boolean flag;
    private float xW, yW, x, y;

    static {
        SPEED = 15f;
        direction = new HashMap<Direction, Boolean>();
        direction.put(Direction.LEFT, false);
        direction.put(Direction.RIGHT, false);
        direction.put(Direction.UP, false);
        direction.put(Direction.DOWN, false);
    }


    public Player(Vector2 position, World world) {
        this.animatorMen = new AnimatorMen();
        this.animatorMen.create();
        this.animatorMen.setWorld(world);
        this.animatorMen.setSize(getWidth(), getHeight());
        this.weapon = Weapon.NONE;
        this.world = world;
        this.position = position;
        this.velocity = new Vector2();

        this.countLife = 10;
        this.isCat = false;
        this.state = State.NONE;
        this.mouseX = -1;
        this.mouseY = -1;
        this.findGun = false;
        this.flag = true;

        this.setX(position.x / world.getGamePpuX());
        this.setY(position.y / world.getGamePpuX());
    }


    public void killZombie(Zombie zombie) {
        zombie.makeDamage();
    }

    public void toDamage() {
        countLife--;
        if (countLife <= 0) {
            state = State.DEAD;
        }
    }

    @Override
    public void act(float delta) {
        if (state != State.DEAD) {
            if (direction.get(Direction.LEFT)) {
                if (Math.abs(mouseX - getX()) < SPEED && Math.abs(mouseY - getY()) < SPEED) {
                    mouseY = -1;
                    mouseX = -1;
                    return;
                }
            } else {
                if (Math.abs(mouseX - getX()) <= getWidth() && Math.abs(mouseY - getY()) < SPEED) {
                    mouseY = -1;
                    mouseX = -1;
                    return;
                }
            }
            position.add(velocity.scl(delta));
            setX(position.x / world.getGamePpuX());
            setY(position.y / world.getGamePpuY());

            if (mouseX != -1 && mouseY != -1 &&
                    (mouseY > getY() || mouseY < getY() || mouseX - getX() > 1 || mouseX < getX() * world.getGamePpuX())) {
                changeNavigation(mouseX, mouseY);

            }
          /*  if(mouseX -getX()> width * world.getGamePpuX()){
                changeNavigation(mouseX, mouseY);
            }*/
        } else {
            setWidth(0);
        }
        //говнокод, не смотреть( но иначе никак)хах
        if (flag) {
            xW = world.getWeapone().getX();
            yW = world.getWeapone().getY();
            flag = false;
        }
    }


    @Override
    public void draw(Batch batch, float parentAlfa) {

        animatorMen.setPositionMen(getX(), getY());

        // animatorMen.create();
        if (direction.get(Direction.LEFT)) {
            animatorMen.walkLeft(batch);
        } else if (direction.get(Direction.RIGHT)) {
            animatorMen.walkRight(batch);
        } else if (direction.get(Direction.DOWN)) {
            animatorMen.walkDown(batch);
        } else if (direction.get(Direction.UP)) {
            animatorMen.walkUp(batch);
        } else
            animatorMen.stay(batch);
    }

    public void changeNavigation(float x, float y) {
        mouseX = x;
        mouseY = y;

        resetWay();

        if (y > getY()) {
            upPressed();
        } else {
            downPressed();
        }
        if (x < getX() * world.getGamePpuX()) {
            leftPressed();
        } else {
            rightPressed();
        }

        if (!findGun) {
            findGun();
        }
        processInput();
    }

    public void resetWay() {
        rightReleased();
        leftReleased();
        downReleased();
        upReleased();

        getVelocity().x = 0;
        getVelocity().y = 0;
    }

    private void processInput() {
        if (collisDetect == -1) {
            if (direction.get(Direction.LEFT))
                getVelocity().x = -Player.SPEED;

            if (direction.get(Direction.RIGHT))
                getVelocity().x = Player.SPEED;

            if (direction.get(Direction.UP))
                getVelocity().y = Player.SPEED;

            if (direction.get(Direction.DOWN))
                getVelocity().y = -Player.SPEED;

            if ((direction.get(Direction.LEFT) && direction.get(Direction.RIGHT)) ||
                    (!direction.get(Direction.LEFT) && (!direction.get(Direction.RIGHT))))
                getVelocity().x = 0;

            if ((direction.get(Direction.UP) && direction.get(Direction.DOWN)) ||
                    (!direction.get(Direction.UP) && (!direction.get(Direction.DOWN))))
                getVelocity().y = 0;
        }
    }

    //все тормозит из-а присваивания переменных, никак иначе сделать не получается

    private void findGun() {

        x = this.getX();
        y = this.getY();

        if (!(xW > x + 30 || xW + 16 < x || yW + 20 < y || yW > y + 50)) {
            findGun = true;
        }

        if (findGun) {
            this.weapon = world.getWeapone().getWeaponeType();
            world.getWeapone().setState(State.TAKEN);
            animatorMen.giveGun();
            animatorMen.create();
            animatorMen.setPositionMen(getX(), getY());
        }

    }

    public boolean isFindGun(){
        return findGun;
    }

    private void leftPressed() {
        direction.get(direction.put(Direction.LEFT, true));
    }

    private void rightPressed() {
        direction.get(direction.put(Direction.RIGHT, true));
    }

    private void upPressed() {
        direction.get(direction.put(Direction.UP, true));
    }

    private void downPressed() {
        direction.get(direction.put(Direction.DOWN, true));
    }

    private void leftReleased() {
        direction.get(direction.put(Direction.LEFT, false));
    }

    private void rightReleased() {
        direction.get(direction.put(Direction.RIGHT, false));
    }

    private void upReleased() {
        direction.get(direction.put(Direction.UP, false));
    }

    private void downReleased() {
        direction.get(direction.put(Direction.DOWN, false));
    }

    public void stop() {
        collisDetect = -1;
        //-1 - нет коллизий
        //0 - встретил коллизию, когда шел влево
        //1 - ...вправо
        //2 - ...вверх
        //3 - ...вниз

        if (direction.get(Direction.LEFT)) {
            collisDetect = 0;
        }
        if (direction.get(Direction.RIGHT)) {
            collisDetect = 1;
        }
        if (direction.get(Direction.UP)) {
            collisDetect = 2;
        }
        if (direction.get(Direction.DOWN)) {
            collisDetect = 3;
        }
        resetWay();
    }

    public void reincarnate() {
        if (!isCat) {
            isCat = true;
        } else {
            isCat = false;
        }
        animatorMen.reincarnate();
        animatorMen.create();
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public int getCountLife() {
        return countLife;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setCollisDetect(int collisDetect) {
        this.collisDetect = collisDetect;
    }
}
