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

    Texture walkSheet;
    TextureRegion[] walkFrames;
    SpriteBatch spriteBatch;
    TextureRegion currentFrame;

    float stateTime;

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public void create() {
        walkSheet = new Texture(Gdx.files.internal("men_all.gif"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth(), walkSheet.getHeight()/FRAME_ROWS);
        walkFrames = new TextureRegion[2];

       int index = 0;
       for (int i = 1; i < 3; i++) {
               walkFrames[index++] = tmp[i][0];
           }
        walkDownAnimation = new Animation(0.25f, walkFrames);
        spriteBatch = new SpriteBatch();
        stateTime = 0f;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
    }


    public void render(Batch batch) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = (TextureRegion) walkDownAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x, y, 42,100);
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

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;

    }
}