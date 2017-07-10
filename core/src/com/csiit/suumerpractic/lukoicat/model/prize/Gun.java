package com.csiit.suumerpractic.lukoicat.model.prize;

import java.util.Timer;
import java.util.TimerTask;

//import suvitruf.classic.bomberman.view.WorldRenderer;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.csiit.suumerpractic.lukoicat.model.constant.Constant;

public class Gun  implements Constant{


    public static  float SIZE = 1f;

    public Vector2 position = new Vector2();
    protected Rectangle bounds = new Rectangle();
    //private  float  curState = 0;
    public int length;


    Timer timer;

    public State state;
    private float  animationState;

    public Gun( Vector2 pos, int length){
        this.length = length;
        state =  State.NONE;

        this.position = pos;
        this.bounds.width = SIZE;
        this.bounds.height = SIZE;
	


        animationState = 0;



    }


    public void setState(State state){
        this.state = state;
    }

    public Vector2 getPosition() {
        return position;
    }


    public Rectangle getBounds() {
        return bounds;
    }


}