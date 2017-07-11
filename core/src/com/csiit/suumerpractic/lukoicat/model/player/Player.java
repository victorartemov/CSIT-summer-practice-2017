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
import com.csiit.suumerpractic.lukoicat.prize.Weapon;

import java.util.HashMap;
import java.util.Map;

public class Player extends Actor implements Constant {

    AnimatorMen animatorMen;

    public static final float SPEED = 1f;
    public static final float height = 1.0f;
    public static final float width = 0.4f;

    private int countLife;
    private Weapon weapon;
    public boolean isCat = false;


    World world;

    Vector2 position = new Vector2();

    //для выч. движения
    Vector2 velocity = new Vector2();


    State state = State.NONE;

    //Мап для направлений
    static Map<Direction, Boolean> direction = new HashMap<Direction, Boolean>();

    static {
        direction.put(Direction.LEFT, false);
        direction.put(Direction.RIGHT, false);
        direction.put(Direction.UP, false);
        direction.put(Direction.DOWN, false);
    }

    float mouseX = -1, mouseY = -1;
    private boolean findGun = false;

    public Player(Vector2 position, World world) {

        this.animatorMen = new AnimatorMen();
        animatorMen.create();
        animatorMen.setWorld(world);
        animatorMen.setSize(getWidth(), getHeight());
        this.countLife = 10;
        this.weapon = Weapon.NONE;
        this.world = world;
        this.position = position;
        setHeight(height * world.ppuY);
        setWidth(width * world.ppuX);
        setX(position.x * world.ppuX);
        setY(position.y * world.ppuY);
        addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

        });
    }

    public void killZombie(Zombie zombie) {
        zombie.makeDamage();
    }

    public int getCountLife() {
        return countLife;
    }


    public void setCountLife(int countLife) {
        this.countLife = countLife;

    }
    //Получить урон

    public void toDamage() {

        countLife--;
        if (countLife == 0) {
            state = State.DEAD;
        }
    }

    public void update(float delta) {
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
            setX(position.x * world.ppuX);
            setY(position.y * world.ppuY);

            if (mouseX != -1 && mouseY != -1 && (mouseY > getY() || mouseY < getY() || mouseX < getX() || mouseX > getX()))
                ChangeNavigation(mouseX, mouseY);
        }
    }


    @Override
    public void draw(Batch batch, float parentAlfa) {
        if (this.equals(world.selectedActor)) {
            batch.setColor(0.9f, 0.9f, 0.9f, 0.9f);
        }
        animatorMen.setPositionMen(getX(), getY());
        if (direction.get(Direction.LEFT)){
            animatorMen.walkLeft(batch);
        }
        else
            if(direction.get(Direction.RIGHT)){
                animatorMen.walkRight(batch);
            }
            else if(direction.get(Direction.DOWN)){
                animatorMen.walkDown(batch);
            }
            else if(direction.get(Direction.UP)){
                animatorMen.walkUp(batch);
            }
            else
                animatorMen.stay(batch);
    }

 /*   //Процедура проверки. Если точка в прямоугольнике актёра, возвращаем актёра.
    public Actor hit(float x, float y, boolean touchable) {

        return x > 0 && x < getWidth() && y > 0 && y < getHeight() ? this : null;
    }
*/

    public void ChangeNavigation(float x, float y) {
        mouseX = x;
        mouseY = y;

        resetWay();

        if (y > getY()) {
            upPressed();
        }
        if (y < getY()) {
            downPressed();
        }

        if (x < getX()) {
            leftPressed();
        }
        if (x > (getPosition().x + width) * world.ppuX)
            rightPressed();
        processInput();

        if( !findGun){
            findGun();

        }
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

    private void findGun() {

        float width = this.getWidth();
        float hight = this.getHeight();

        float weaponWidth = world.getWeapone().getWidth();
        float weaponeHight = world.getWeapone().getHeight();

        float x = this.getPosition().x;
        float y = this.getPosition().y;

        float xW = world.getWeapone().getPosition().x;
        float yW = world.getWeapone().getPosition().y;

        if ((x + width) >= xW && x <= xW )
            if ((y + hight) >= yW && y <= yW) {
                this.weapon = world.getWeapone().getWeaponeType();
                world.getWeapone().setState(State.TAKEN);
                findGun=true;

                animatorMen.giveGun();
                animatorMen.create();
                animatorMen.setPositionMen(getX(), getY());
            }

        if ((x + width) <= (xW + weaponWidth) && x >= (xW + weaponWidth))
            if ((y + hight) >= (yW + weaponeHight) && y <= (yW + weaponeHight)) {
                this.weapon = world.getWeapone().getWeaponeType();
                world.getWeapone().setState(State.TAKEN);
                findGun=true;
            }

    }

    public void setState(State state) {
        this.state = state;

    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void leftPressed() {
        direction.get(direction.put(Direction.LEFT, true));
    }

    public void rightPressed() {
        direction.get(direction.put(Direction.RIGHT, true));
    }

    public void upPressed() {
        direction.get(direction.put(Direction.UP, true));
    }

    public void downPressed() {
        direction.get(direction.put(Direction.DOWN, true));
    }

    public void leftReleased() {
        direction.get(direction.put(Direction.LEFT, false));
    }

    public void rightReleased() {
        direction.get(direction.put(Direction.RIGHT, false));
    }

    public void upReleased() {
        direction.get(direction.put(Direction.UP, false));
    }

    public void downReleased() {
        direction.get(direction.put(Direction.DOWN, false));
    }

    public State getState() {
        return state;
    }


}
