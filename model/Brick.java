package com.csiit.suumerpractic.lukoicat.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Juli on 04.07.2017.
 */
public class Brick{
    static final float SIZE = 0.5f;
    Vector2 position = new Vector2();
    Rectangle bounds = new Rectangle();

    public Brick(Vector2 position) {
        this.position = position;
        this.bounds.width = SIZE;
        this.bounds.height = SIZE;
    }

    public Rectangle getBounds() {
        return bounds;
    }

  // @Override
  // public void draw(Batch batch, float parentAlfa) {
  //     if (this.equals(world.selectedActor)) {
  //         batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
  //     }
  //     batch.draw(world.textureRegions.get("brick1"), getX(), getY(),getWidth(), getHeight());
  //     batch.setColor(1, 1, 1, 1);
  // }

    public Vector2 getPosition() {
        return position;
    }
}
