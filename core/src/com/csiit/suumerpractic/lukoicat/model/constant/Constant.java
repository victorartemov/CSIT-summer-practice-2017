package com.csiit.suumerpractic.lukoicat.model.constant;

import com.badlogic.gdx.math.Vector2;
import com.csiit.suumerpractic.lukoicat.model.World;
import com.csiit.suumerpractic.lukoicat.model.zombie.Zombie;

import java.util.Random;

/**
 * Created by Рената on 06.07.2017.
 */

// мб создать один большой enum?
public interface Constant {
    enum Direction {
        LEFT, RIGHT, UP, DOWN, NONE
    }

    enum State {
        NONE, WALKING, DEAD
    }

    enum Weapone {
        NONE, STONE, GUN;
        private int damage;

        public int damage() {
            switch (this) {
                case GUN:
                    damage = 30;
                    break;
                case NONE:
                    damage = 15;
                    break;
                case STONE:
                    damage = 20;
                    break;
            }
            return damage;
        }
    }

    enum ZombieType {
        NORMAL, BOSS;

        public Zombie choseZombie(World world, float width, float hight) {
            Zombie zombie = null;
            Vector2 vector2 = new Vector2(new Random((int)width).nextFloat(), new Random((int)hight).nextFloat());
            switch (this) {
                case NORMAL:
                    zombie = new Zombie(world, vector2, 0.9f, 2, 2, 50, "monster");
                    break;

            }
            return zombie;
        }
    }
}
