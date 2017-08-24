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

import java.util.HashMap;
import java.util.Map;

public class Player extends Actor implements Constant {

    private AnimatorMen animatorMen;

    public static final float SPEED = 15f;
    //не удалять, это влияет на зомби
    private static final float height = 1.0f;
    private static final float width = 0.4f;

    private static final Vector2 MOUSE_ZERO = new Vector2(645, 0); //координата начала карты
    private Vector2 mouseClick;

    private int countLife;
    private boolean stop = false; //если врезался в препятствие
    private Weapon weapon;
    public boolean isCat = false;
    private World world;
    private Vector2 position = new Vector2();

    //для выч. движения
    private Vector2 velocity = new Vector2();
    private State state = State.NONE;
    private int collisDetect;
    private Vector2 globalPos;

    //Мап для направлений
    private static Map<Direction, Boolean> direction;

    static {
        direction = new HashMap<Direction, Boolean>();
        direction.put(Direction.LEFT, false);
        direction.put(Direction.RIGHT, false);
        direction.put(Direction.UP, false);
        direction.put(Direction.DOWN, false);
    }



    public static Map<Direction, Boolean> getDirection() {
        return direction;
    }

    private float mouseX = -1;
    private float mouseY = -1;
    private boolean findGun = false;
    private boolean findHealth = false;

    private boolean flag;

    public Player(Vector2 position, World world) {
        this.animatorMen = new AnimatorMen();
        animatorMen.create();
        animatorMen.setWorld(world);
        animatorMen.setSize(getWidth(), getHeight());
        this.countLife = 10;
        this.weapon = Weapon.NONE;
        this.world = world;
        this.position = position;

        this.flag = true;
        this.globalPos = new Vector2(0, 0);
        this.mouseClick = new Vector2(0, 0);

        setX(position.x / world.getGamePpuX());
        setY(position.y / world.getGamePpuX());


        addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
    }

    private void setGlobalPos() {
        this.globalPos.x = getX();
        this.globalPos.y = getY();
    }


    public Vector2 getGlobalPos() {
        return globalPos;
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

    public void toDamage() {
        countLife--;
        if (countLife <= 0) {
            state = State.DEAD;
        }
    }

    @Override
    public void act(float delta) {
        //Мышь считывает нажатие на экране (а не на глобальной карте),
        //надо сделать так, чтобы как-то запоминалось также значение клика именно на карте,
        // и перс шел к нему, а не к координатам экрана
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
            // System.out.println("mouse = " + mouseX + " " + mouseY);

            setX(position.x / world.getGamePpuX());
            setY(position.y / world.getGamePpuY());
            //  System.out.println(getY());

            if (mouseX != -1 && mouseY != -1 && (mouseY > getY() || mouseY < getY() || mouseX - getX() > width * world.getGamePpuX() || mouseX < getX() * world.getGamePpuX())) {
                changeNavigation(mouseX, mouseY);

            }
          /*  if(mouseX -getX()> width * world.getGamePpuX()){
                changeNavigation(mouseX, mouseY);
            }*/
        } else {
            setWidth(0);
        }
    }


    @Override
    public void draw(Batch batch, float parentAlfa) {
        if (this.equals(world.selectedActor)) {
            batch.setColor(1f, 1f, 1f, 1f);
        }
        //this.animatorMen = new AnimatorMen();

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
        }
        if (y < getY()) {
            downPressed();
        }
        if (x < getX() * world.getGamePpuX()) {
            leftPressed();
        }
        if (x > (getX() + width) * world.getGamePpuX()) {
            rightPressed();
        }
        processInput();
        if (!findGun) {
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
        if(collisDetect == -1) {
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

    private void findGun() {

        float xW = world.getWeapone().getX();
        float yW = world.getWeapone().getY();

        float xWeaponWidth = world.getWeapone().getWidth() + xW;
        float yWeaponHeight = world.getWeapone().getHeight() + yW;

        float x = this.getX();
        float y = this.getY();

        float xWidth = this.getWidth() + x;
        float yHeight = this.getHeight() + y;

        if (xW >= x && xW <= xWidth && xWeaponWidth >= x && xWeaponWidth <= xWidth) {
            if (yW >= y && yW <= yHeight && yWeaponHeight >= y && yWeaponHeight <= yHeight) {
                findGun = true;
            }
        }

        if (findGun) {
            this.weapon = world.getWeapone().getWeaponeType();
            world.getWeapone().setState(State.TAKEN);
            animatorMen.giveGun();
            animatorMen.create();
            animatorMen.setPositionMen(getX(), getY());
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

    public State getState() {
        return state;
    }

    public void stop() {
        collisDetect = -1;
        //-1 - нет коллизий
        //0 - встретил коллизию, когда шел влево
        //1 - ...вправо
        //2 - ...вверх
        //3 - ...вниз

       if(direction.get(Direction.LEFT))
       {
           collisDetect = 0;
       }
       if(direction.get(Direction.RIGHT))
       {
           collisDetect = 1;
       }
       if(direction.get(Direction.UP))
       {
           collisDetect = 2;
       }
       if(direction.get(Direction.DOWN))
       {
           collisDetect = 3;
       }
        resetWay();
    }

    public void setCollisDetect(int collisDetect){
        this.collisDetect = collisDetect;
    }

    public void reincarnate() {
        if(isCat == false){
            isCat = true;
        }
        else {
            isCat = false;
        }
        animatorMen.reincarnate();
        animatorMen.create();
    }
}
