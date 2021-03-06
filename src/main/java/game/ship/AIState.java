/*
 * This file is part of Javaders.
 * Copyright (c) Yossi Shaul
 *
 * Javaders is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Javaders is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Javaders.  If not, see <http://www.gnu.org/licenses/>.
 */

package game.ship;

import java.util.Random;

/**
 * The AI state class performs actions like movement and
 * firing on a ship according to the events chances defined
 * for this state. It also returns the next state when the ship
 * decides it's time to change state.
 */
public class AIState {

    private static final int AI_TYPE_AGGRESIVE = 1;
    private static final int AI_TYPE_NORMAL = 2;
    private static final int AI_TYPE_COWARD = 3;

    // State chances (each state chances should sum to 1.0)
    private static final float AGGRESIVE_ATTACK_CHANCE = 0.6f;
    private static final float AGGRESIVE_NORMAL_CHANCE = 0.3f;
    public static final float AGGRESIVE_FLEE_CHANCE = 0.1f;

    private static final float COWARD_FLEE_CHANCE = 0.55f;
    private static final float COWARD_NORMAL_CHANCE = 0.30f;
    public static final float COWARD_ATTACK_CHANCE = 0.15f;

    private static final AIState AI_STATE_ATTACK =
            new AIState(AI_TYPE_AGGRESIVE, 0.3f, 0.8f);
    public static final AIState AI_STATE_NORMAL =
            new AIState(AI_TYPE_NORMAL, 0.5f, 0.5f);
    private static final AIState AI_STATE_FLEE =
            new AIState(AI_TYPE_COWARD, 0.7f, 0.2f);

    private float moveChance;
    private float fireChance;
    private int aiType;

    private Random rand;

    /**
     * Construct a new AIState.
     *
     * @param moveChance Chance for making a movement.
     * @param fireChance Chance for firing.
     */
    private AIState(int type, float moveChance, float fireChance) {
        this.aiType = type;
        this.moveChance = moveChance;
        this.fireChance = fireChance;

        rand = new Random();
    }

    /**
     * Randomly update the ship according to the events chances.
     *
     * @param ship Enemy ship to update
     */
    public void update(EnemyShip ship) {

        if (rand.nextFloat() < fireChance) {
            ship.shoot();
        }
        if (rand.nextFloat() < moveChance) {
            int direction = (rand.nextFloat() > 0.5) ? -1 : 1;
            ship.setDx(direction * (rand.nextFloat() * ship.getMaxDX()));
        }
        if (rand.nextFloat() < moveChance) {
            int direction = (rand.nextFloat() > 0.5) ? -1 : 1;
            ship.setDy(direction * (rand.nextFloat() * ship.getMaxDX()));
        }
    }

    /**
     * Randomly selects the next AI state. The current state has
     * some affect on the probability of the next state.
     */
    public AIState getNextAIState() {
        AIState nextState = AI_STATE_NORMAL;
        float rand = (float) Math.random();
        switch (aiType) {
            case AI_TYPE_AGGRESIVE:
                if (rand < AGGRESIVE_ATTACK_CHANCE) {
                    nextState = AI_STATE_ATTACK;
                } else if (rand < (AGGRESIVE_ATTACK_CHANCE + AGGRESIVE_NORMAL_CHANCE)) {
                    nextState = AI_STATE_NORMAL;
                } else {
                    nextState = AI_STATE_FLEE;
                }
                break;
            case AI_TYPE_COWARD:
                if (rand < COWARD_FLEE_CHANCE) {
                    nextState = AI_STATE_FLEE;
                } else if (rand < (AGGRESIVE_ATTACK_CHANCE + COWARD_NORMAL_CHANCE)) {
                    nextState = AI_STATE_NORMAL;
                } else {
                    nextState = AI_STATE_ATTACK;
                }
                break;
        }

        return nextState;

    }

}
