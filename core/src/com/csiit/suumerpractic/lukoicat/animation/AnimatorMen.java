package com.csiit.suumerpractic.lukoicat.animation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csiit.suumerpractic.lukoicat.model.World;

public class AnimatorMen implements ApplicationListener{

    private static final int FRAME_ROWS = 12;

    private float x;
    private float y;

    private float width;
    private float height;


    World world;
    Animation walkDownAnimation;
    Animation walkUpAnimation;
    Animation walkLeftAnimation;
    Animation walkRightAnimation;

    Texture walkSheet;
    TextureRegion[] walkDownFrames;
    TextureRegion[] walkUpFrames;
    TextureRegion[] walkLeftFrames;
    TextureRegion[] walkRightFrames;

    SpriteBatch spriteBatch;
    TextureRegion currentFrame;
    TextureRegion textureStayMen;
    boolean haveGun = false;

    float stateTime;

    public void setWorld(World world) {
        this.world = world;
    }

    public void giveGun(){
        haveGun = true;
    }


    @Override
    public void create() {
        if(haveGun){
            walkSheet = new Texture(Gdx.files.internal("men_with_gun.gif"));
        }
        else {
            walkSheet = new Texture(Gdx.files.internal("men_all.gif"));
        }
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth(), walkSheet.getHeight()/FRAME_ROWS);
        if(haveGun)
            textureStayMen = tmp[1][0];
        else
            textureStayMen = tmp[0][0];

        walkDownFrames = new TextureRegion[2];
        walkUpFrames = new TextureRegion[2];
        walkLeftFrames = new TextureRegion[2];
        walkRightFrames = new TextureRegion[2];

       int indexD = 0;
       int indexU = 0;
       int indexR = 0;
       int indexL = 0;
       for (int i = 1; i < 3; i++) {
           walkDownFrames[indexD++] = tmp[i][0];
           walkUpFrames[indexU++] = tmp[i+3][0];
           walkRightFrames[indexR++] = tmp[i+6][0];
           walkLeftFrames[indexL++] = tmp[i+9][0];
       }

        walkDownAnimation = new Animation(0.25f, walkDownFrames);
        walkUpAnimation = new Animation(0.25f, walkUpFrames);
        walkLeftAnimation = new Animation(0.25f, walkLeftFrames);
        walkRightAnimation = new Animation(0.25f, walkRightFrames);

        spriteBatch = new SpriteBatch();
        stateTime = 0f;
    }

    public void stay(Batch batch){
        batch.draw(textureStayMen,x,y);
    }
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
    }

    public void walkDown(Batch batch){
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = (TextureRegion) walkDownAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x, y);
    }
    public void walkUp(Batch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = (TextureRegion) walkUpAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x, y);
    }

    public void walkLeft(Batch batch){
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = (TextureRegion) walkLeftAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x, y);
    }
    public void walkRight(Batch batch){
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = (TextureRegion) walkRightAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x, y);
    }


   @Override
   public void pause() {

   }

   @Override
   public void resume() {

   }

   @Override
   public void dispose() {

   }


    public void setPositionMen(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }
}