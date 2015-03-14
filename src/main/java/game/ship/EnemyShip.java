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

import game.ship.bonus.Bonus;
import game.ship.bonus.PowerUp;
import game.ship.bonus.WeaponUpgrade;
import game.ship.weapon.Bullet;
import game.ship.weapon.WeaponFactory;
import game.sound.SoundFactory;
import game.util.ResourceManager;

import java.awt.*;

/**
 * The <code>EnemyShip</code> class extends the abstract Ship class
 * and defines common behaviours for enemy ships.
 */
public class EnemyShip extends Ship {

    private final float WEAPON_BONUS_PROBABILITY = 0.07f;
    private final float POWERUP_BONUS_PROBABILITY = 0.8f;

    private final long maxDecisionTime = 10000;

    private long timeInAIState;
    private AIState aiState;    // AI state of the ship

    private long initArmor;    // The armor this ship was contructed with

    /**
     * Construct a new enemy ship.
     *
     * @see ShipProperties
     * @see Ship#Ship(int, int, float, float, ShipProperties)
     */
    public EnemyShip(int objectId, int shipType,
                     float x, float y, ShipProperties prop) {

        super(objectId, shipType, x, y, prop.maxDX, prop.maxDY,
                prop.image, WeaponFactory.getWeapon(
                        prop.weaponType, prop.weaponLevel,
                        prop.weaponDirection),
                prop.armor, prop.damage,
                prop.hitScoreValue, prop.destroyScoreValue);

        this.initArmor = armor;
        aiState = AIState.AI_STATE_NORMAL;
        timeInAIState = maxDecisionTime;    // force state change

    }

    /**
     * Construct a new enemy ship from a model.
     *
     * @param model ShipModel object
     * @see ShipModel
     * @see Ship#Ship(ShipModel)
     */
    public EnemyShip(ShipModel model) {
        this(model.objectId, model.shipType, model.x, model.y,
                ShipProperties.getShipProperties(model.shipType));
    }

    /**
     * Overrides the render method. Call the super method and
     * add power left of the ship in percentage.
     *
     * @see Ship#render(Graphics)
     */
    @Override
    public void render(Graphics g) {
        super.render(g);

        if (isActive()) {
            // Draw the armor left to this ship in percents

            float armorLeft = (float) this.armor / this.initArmor;
            int armorPrecent = (int) (armorLeft * 100);

            g.setFont(ResourceManager.getFont(Font.BOLD, 10));

            // The color becomes more reddish when the ship looses armor
            g.setColor(new Color(1 - armorLeft, armorLeft, 0.0f));


            g.drawString(armorPrecent + "%",
                    Math.round(this.getX()),
                    Math.round(this.getY()) + 10);
        }
    }

    /**
     * Override the update method. Call the super method and adds
     * some random activities (shooting, changing direction, etc.).
     *
     * @see Ship#update(long)
     */
    @Override
    public void update(long elapsedTime) {

        super.update(elapsedTime);

        timeInAIState += elapsedTime;

        boolean changeState = false;
        if (shipContainer.isController()) {

            long randomTime = (long) (maxDecisionTime * Math.random());

            changeState = timeInAIState >= randomTime ||
                    timeInAIState >= maxDecisionTime;

            // Only the controller machine generate random events
            if (changeState) {
                aiState = aiState.getNextAIState();
                timeInAIState = 0;
                aiState.update(this);
            }
        }

        if (shipContainer.isNetworkGame() && shipContainer.isController()
                && (changeState || timeSinceLastPacket > 2500 * Math.random())) {
            // Send state update
            createPacket(shipContainer.getNetworkManager());
        }

    }

    /**
     * Hit this ship with the bullet
     */
    @Override
    public void hit(Bullet bullet) {
        if (isNormal()) {
            SoundFactory.playSound("hit1.wav");
            super.hit(bullet);

            // Add the score to the hitting player
            if (bullet.getOwner() instanceof PlayerShip) {
                long damage = bullet.getDamage();
                long actualDamage = Math.min(armor, damage);
                long score = (armor <= 0) ? destroyScoreValue :
                        damageScoreValue * actualDamage;
                PlayerShip ship = (PlayerShip) bullet.getOwner();
                ship.addScore(score);
            }
        }
    }

    /**
     * Hit the ship with a bonus. Enemy ships don't consume bonuses.
     */
    @Override
    public void hit(Bonus bonus) {
        // Enemy ships don't consume bonuses
    }

    /**
     * Returns the enemy ship state.
     */
    @Override
    public ShipState getShipState() {
        return new ShipState(x, y, dx, dy, armor, state);
    }

    /**
     * Override the destroy methos. Call the super method and randomly
     * drop a bonus.
     *
     * @see Ship#destroy()
     */
    @Override
    public void destroy() {
        super.destroy();

        if (shipContainer.isController()) {
            // Only the controller generates random events
            Bonus bonus = null;
            double random = Math.random();
            if (random < WEAPON_BONUS_PROBABILITY) {
                bonus = new WeaponUpgrade(getCenterX(),
                        getCenterY(), WeaponFactory.getRandomWeaponType());

            } else if (random < POWERUP_BONUS_PROBABILITY) {
                int powerBonus = Math.round(initArmor * 0.05f);
                bonus = new PowerUp(getCenterX(), getCenterY(), powerBonus);
            }

            if (bonus != null) {
                // Add the bonus and send to the network player
                // if in network game
                getShipContainer().addBonus(bonus);
                if (getShipContainer().isNetworkGame()) {
                    bonus.createPacket(getShipContainer().getNetworkManager());
                }
            }
        }

        // If it's a network game send the ship state to 
        // make sure it is destroyed in the other player's world
        if (getShipContainer().isNetworkGame()) {
            createPacket(getShipContainer().getNetworkManager());
        }
    }

}
