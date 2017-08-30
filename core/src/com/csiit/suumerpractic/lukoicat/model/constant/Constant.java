package com.csiit.suumerpractic.lukoicat.model.constant;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.csiit.suumerpractic.lukoicat.model.World;
import com.csiit.suumerpractic.lukoicat.model.zombie.Zombie;
import com.csiit.suumerpractic.lukoicat.prize.Weapon;

import java.util.Random;

/**
 * Created by Рената on 06.07.2017.
 */

public interface Constant {
    enum Direction {
        LEFT, RIGHT, UP, DOWN, NONE
    }

    enum State {
        NONE, WALKING, DEAD, TAKEN
    }

    enum Weapon {
        NONE, GUN;

        public com.csiit.suumerpractic.lukoicat.prize.Weapon makeWeapon(World world, float width, float height) {
            com.csiit.suumerpractic.lukoicat.prize.Weapon weapon = null;
            Vector2 vector2 = new Vector2(MathUtils.random(width), MathUtils.random(height));
            switch (this) {
                case GUN:
                    weapon = new com.csiit.suumerpractic.lukoicat.prize.Weapon(world, vector2, Weapon.GUN, "gun");
                    break;
            }
            return weapon;
        }
    }

    enum ZombieType {
        NORMAL, BOSS;

        public Zombie choseZombie(World world, float width, float height) {
            Zombie zombie = null;
            Vector2 vector2 = new Vector2(MathUtils.random(400, 1000), MathUtils.random(335, 709));
            switch (this) {
                case NORMAL:
                    zombie = new Zombie(world, vector2, 200, 2, 3);
                    break;
            }
            return zombie;
        }
    }
}

