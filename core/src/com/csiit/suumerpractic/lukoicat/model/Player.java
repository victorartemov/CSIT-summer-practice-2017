package com.csiit.suumerpractic.lukoicat.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.HashMap;
import java.util.Map;

public class Player extends Actor {

    public enum State {
        NONE, WALKING, DEAD
    }

    enum Keys {
        LEFT, RIGHT, UP, DOWN
    }

    public static final float SPEED = 1f;
    public static final float height = 1.0f;
    public static final float width = 0.4f;


    World world;

    Vector2 position = new Vector2();

    //для выч. движения
    Vector2 velocity = new Vector2();

    State state = State.NONE;
    // boolean facingLeft = true;

    //Мап для направлений
    static Map<Keys, Boolean> direction = new HashMap<Keys, Boolean>();

    static {
        direction.put(Keys.LEFT, false);
        direction.put(Keys.RIGHT, false);
        direction.put(Keys.UP, false);
        direction.put(Keys.DOWN, false);
    }

    float mouseX = -1, mouseY = -1;//коряво, но я переделаю, как придумаю др. способ, ибо все остальные 100 у меня не получились, тут свои трудности

    public Player(Vector2 position, World world) {

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

    public void update(float delta) {
        position.add(velocity.scl(delta));
        setX(position.x * world.ppuX);
        setY(position.y * world.ppuY);
        if (mouseX == getX() && mouseY == getY()) {
            mouseY = -1;
            mouseX = -1;
        }
        if (mouseX != -1 && mouseY != -1 && (mouseY > getY() || mouseY < getY() || mouseX < getX() || mouseX > getX()))
            ChangeNavigation(mouseX, mouseY);
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
        if (direction.get(Keys.LEFT))
            getVelocity().x = -Player.SPEED;

        if (direction.get(Keys.RIGHT))
            getVelocity().x = Player.SPEED;

        if (direction.get(Keys.UP))
            getVelocity().y = Player.SPEED;

        if (direction.get(Keys.DOWN))
            getVelocity().y = -Player.SPEED;

        if ((direction.get(Keys.LEFT) && direction.get(Keys.RIGHT)) ||
                (!direction.get(Keys.LEFT) && (!direction.get(Keys.RIGHT))))
            getVelocity().x = 0;

        if ((direction.get(Keys.UP) && direction.get(Keys.DOWN)) ||
                (!direction.get(Keys.UP) && (!direction.get(Keys.DOWN))))
            getVelocity().y = 0;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void leftPressed() {
        direction.get(direction.put(Keys.LEFT, true));
    }

    public void rightPressed() {
        direction.get(direction.put(Keys.RIGHT, true));
    }

    public void upPressed() {
        direction.get(direction.put(Keys.UP, true));
    }

    public void downPressed() {
        direction.get(direction.put(Keys.DOWN, true));
    }

    public void leftReleased() {
        direction.get(direction.put(Keys.LEFT, false));
    }

    public void rightReleased() {
        direction.get(direction.put(Keys.RIGHT, false));
    }

    public void upReleased() {
        direction.get(direction.put(Keys.UP, false));
    }

    public void downReleased() {
        direction.get(direction.put(Keys.DOWN, false));
    }

}
