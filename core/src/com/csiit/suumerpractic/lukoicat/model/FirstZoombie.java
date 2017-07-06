package com.csiit.suumerpractic.lukoicat.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

/**
 * Created by Рената on 06.07.2017.
 */
public class FirstZoombie extends Actor  {

    public static  float SIZE = 0.9f;
    public static final float SPEED = 2.5f;
    private int counter = 0;

    public Vector2 getVelocity() {
        return velocity;
    }

    public enum Direction {
        LEFT, RIGHT, UP, DOWN, NONE
    }
    public Direction direction;


    protected Rectangle bounds = new Rectangle();
    public static enum State {NONE, DYING, DEAD }
    protected State state;
    Vector2 	acceleration = new Vector2();

    protected  String name = "NpcBase" ;
    public static final float height = 1.0f;
    public static final float width = 0.4f;
    World world;

    Vector2 position = new Vector2();

    //для выч. движения
    Vector2 velocity = new Vector2();

    @Override
    public void draw(Batch batch, float parentAlfa) {

        batch.draw(world.textureRegions.get("player"), getX(), getY(), getWidth(), getHeight());
        batch.setColor(1, 1, 1, 1);
    }


    public FirstZoombie(World world, Vector2 vector2) {

        this.position = vector2;
        this.bounds.width = SIZE;
        this.bounds.height = SIZE;
        state = State.NONE;
        direction = Direction.NONE;
        this.world = world;
        getVelocity().y = SPEED;
        state = State.NONE;

        this.world = world;
        this.position = position;

        setHeight(height * world.ppuY);
        setWidth(width * world.ppuX);
        setX(position.x * world.ppuX);
        setY(position.y * world.ppuY);

        setFirstDirection();
    }
    public void update(float delta) {
        changeDirection(delta);
        position.add(velocity.scl(delta));
        setX(position.x * world.ppuX);
        setY(position.y * world.ppuY);

    }

    public Vector2 getPosition() {
        return position;
    }

    protected void resetVelocity(){
        getVelocity().x =0;
        getVelocity().y =0;
    }


    public boolean checkX(float offset){
        return getPosition().x - (int)getPosition().x < offset;
    }

    public boolean checkY(float offset){
        return getPosition().y - (int)getPosition().y < offset;
    }

    public void goToPlayer(float delta, float speed){

        int bX = Math.round(world.getSelectedActor().getPosition().x);
        int bY = Math.round(world.getSelectedActor().getPosition().y);
        int X = Math.round(getPosition().x);
        int Y = Math.round(getPosition().y);

        float offset = delta*speed*2;

        if(bX == X){
            if(checkX(offset)){
                if(bY>Y) {
                    direction = Direction.UP;
                    getVelocity().y = speed;
                }
                else{
                    direction = Direction.DOWN;
                    getVelocity().y = -speed;
                }
            }
            else{
                if(getPosition().x - X>0){

                    direction = Direction.LEFT;
                    getVelocity().x = -speed;

                }
                if(getPosition().x - X<0){
                    direction = Direction.RIGHT;
                    getVelocity().x = speed;
                }
            }

        }

        if(bY == Y ){
            if(checkY(offset)){
                if(bX>X) {
                    direction = Direction.RIGHT;
                    getVelocity().x = speed;
                }
                else{
                    direction = Direction.LEFT;
                    getVelocity().x = -speed;
                }
            }
            else{
                if(getPosition().y - Y>0 ){

                    direction = Direction.DOWN;
                    getVelocity().y = -speed;

                }
                if(getPosition().y - Y<0){
                    direction = Direction.UP;
                    getVelocity().y = speed;
                }
            }



        }
    }


    protected boolean seePlayer(){
        boolean see = false;
        int bX = Math.round(world.getSelectedActor().getPosition().x);
        int bY = Math.round(world.getSelectedActor().getPosition().y);

        int X = Math.round(getPosition().x);
        int Y = Math.round(getPosition().y);

        if(bX == X || bY == Y){
            see = true;

        }

        return see;
    }

    Random generator = new Random();

    private void setFirstDirection(){
        direction = Direction.UP;
        getVelocity().y = SPEED;
    }


    public void newDirection(boolean checkCur) {

        System.out.println("Зашел new direction");
        if(!checkCur)
            counter = 0;

        int newDir =1+ generator.nextInt(4);
        resetVelocity();
        Direction oldDir = direction;
        if(newDir == 1 &&(direction != Direction.UP || !checkCur)){
            getVelocity().y =SPEED;

            direction = Direction.UP;
        }


        if(oldDir == direction )
            if(newDir == 2 && (direction != Direction.DOWN || !checkCur)){
                getVelocity().y =-SPEED;

                direction = Direction.DOWN;
            }

        if(oldDir == direction)
            if(newDir == 3 && (direction != Direction.RIGHT || !checkCur)){

                getVelocity().x =SPEED;
                direction = Direction.RIGHT;
            }



        if(oldDir == direction)
            if(newDir == 4 && (direction != Direction.LEFT || !checkCur)) {
                getVelocity().x = -SPEED;
                direction = Direction.LEFT;
            }

    }

    public  void changeDirection(float delta){

        if( counter>50)
        {
            newDirection(false);
        }
        else counter++;

    }



}