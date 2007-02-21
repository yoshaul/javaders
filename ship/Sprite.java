
package game.ship;


import java.awt.Graphics;
import java.awt.Image;


public abstract class Sprite implements Movable, Renderable {

    protected float x;		// Vertical position in pixels
    protected float y;		// Horizontal position in pixels
    protected float dx;		// Vertical velocity
    protected float dy;		// Horizontal velocity
    
    protected boolean active = true;
    
    private Image spriteImage;
    
    
    public Sprite(float x, float y, float dx, float dy, Image image) {
        this(x, y, dx, dy);
        this.spriteImage = image;
    }
    
    public Sprite(float x, float y, float dx, float dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;        
    }
    
    public void updatePosition(long elapsedTime) {
        
        x += dx * elapsedTime;
        y += dy * elapsedTime;
                
    }
    
    public void render(Graphics g) {
        
        g.drawImage(spriteImage, (int)Math.round(x), 
                (int)Math.round(y), null);
        
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
    public int getCenterX() {
        return (int)Math.round(x + getWidth() / 2 );
    }
    public int getCenterY() {
        return (int)Math.round(y + getHeight() /2 );
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
