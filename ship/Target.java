package game.ship;

public interface Target {

    public boolean isCollision(int x0, int y0, int x1, int y1);
    
    public void hit(long damage);
    
}
