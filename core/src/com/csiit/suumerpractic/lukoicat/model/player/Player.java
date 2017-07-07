package com.csiit.suumerpractic.lukoicat.model.player;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.csiit.suumerpractic.lukoicat.model.World;
import com.csiit.suumerpractic.lukoicat.model.constant.Constant;

import java.util.HashMap;
import java.util.Map;

public class Player extends Actor implements Constant {

    public static final float SPEED = 1f;
    public static final float height = 1.0f;
    public static final float width = 0.4f;

    private int countLife;
    private Weapone weapone;
    public boolean isCat = false;


    World world;

    Vector2 position = new Vector2();

    //для выч. движения
    Vector2 velocity = new Vector2();

    State state = State.NONE;
    // boolean facingLeft = true;

    //Мап для направлений
    static Map<Direction, Boolean> direction = new HashMap<Direction, Boolean>();

    static {
        direction.put(Direction.LEFT, false);
        direction.put(Direction.RIGHT, false);
        direction.put(Direction.UP, false);
        direction.put(Direction.DOWN, false);
    }

    float mouseX = -1, mouseY = -1;//коряво, но я переделаю, как придумаю др. способ, ибо все остальные 100 у меня не получились, тут свои трудности

    public Player(Vector2 position, World world) {

        this.countLife = 10;
        this.weapone = Weapone.NONE;
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

    public void killZoombie() {

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
            batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        }
        batch.draw(world.textureRegions.get("player"), getX(), getY(), getWidth(), getHeight());
        batch.setColor(1, 1, 1, 1);
    }

    //Процедура проверки. Если точка в прямоугольнике актёра, возвращаем актёра.
    public Actor hit(float x, float y, boolean touchable) {

        return x > 0 && x < getWidth() && y > 0 && y < getHeight() ? this : null;
    }


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
