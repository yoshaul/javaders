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


import java.awt.*;

/**
 * A <code>Sprite</code> class is the base class for all the movable and
 * renderable game objects in the game (ships, bullets etc.).
 * It contains implementation for general methods needed by all sprites
 * and implements the <code>Renderable</code> and <code>Movable</code>
 * interfaces.
 * This basic sprite uses <code>Image</code> as its rendered object.
 */
public abstract class Sprite implements Movable, Renderable {

    protected float x;        // Vertical position in pixels
    protected float y;        // Horizontal position in pixels
    protected float dx;        // Vertical velocity (pixels/sec)
    protected float dy;        // Horizontal velocity (pixels/sec)

    protected boolean active = true;

    private Image spriteImage;

    /**
     * @param x     Vertical location of the ship (from left)
     * @param y     Horizontal location of the ship (from top)
     * @param dx    Max vertical velocity (pixels/sec)
     * @param dy    Max horizontal velocity (pixels/sec)
     * @param image Sprite's image
     */
    public Sprite(float x, float y, float dx, float dy, Image image) {
        this(x, y, dx, dy);
        this.spriteImage = image;
    }

    /**
     * @param x  Vertical location of the ship (from left)
     * @param y  Horizontal location of the ship (from top)
     * @param dx Max vertical velocity (pixels/sec)
     * @param dy Max horizontal velocity (pixels/sec)
     */
    public Sprite(float x, float y, float dx, float dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Updates the sprites position according to the elapsed time.
     *
     * @param elapsedTime Time elapsed since last update in milliseconds
     */
    @Override
    public void updatePosition(long elapsedTime) {

        x += dx * elapsedTime;
        y += dy * elapsedTime;

    }

    /**
     * Render the image in the current position.
     */
    @Override
    public void render(Graphics g) {

        g.drawImage(spriteImage, Math.round(x),
                Math.round(y), null);

    }

    /**
     * @return Returns the dx.
     */
    public float getDx() {
        return dx;
    }

    /**
     * @param dx The dx to set.
     */
    public void setDx(float dx) {
        this.dx = dx;
    }

    /**
     * @return Returns the dy.
     */
    public float getDy() {
        return dy;
    }

    /**
     * @param dy The dy to set.
     */
    public void setDy(float dy) {
        this.dy = dy;
    }

    /**
     * @return Returns the x.
     */
    public float getX() {
        return x;
    }

    /**
     * @param x The x to set.
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return Returns the y.
     */
    public float getY() {
        return y;
    }

    /**
     * @param y The y to set.
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * @return Returns the sprite width
     */
    public int getWidth() {
        return spriteImage.getWidth(null);
    }

    /**
     * @return Returns the sprite height
     */
    public int getHeight() {
        return spriteImage.getHeight(null);
    }

    public float getCenterX() {
        return x + (float) (getWidth() / 2);
    }

    public float getCenterY() {
        return y + (float) (getHeight() / 2);
    }

    /**
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return True if the sprite is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @return Returns the spriteImage.
     */
    public Image getSpriteImage() {
        return spriteImage;
    }

    /**
     * @param spriteImage The spriteImage to set.
     */
    public void setSpriteImage(Image spriteImage) {
        this.spriteImage = spriteImage;
    }
}
