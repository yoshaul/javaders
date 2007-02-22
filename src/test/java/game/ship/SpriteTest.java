package game.ship;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 * Tests the Sprite class
 *
 * @author Yossi Shaul
 */
@Test
public class SpriteTest {

    @Test
    public void create() {
        Sprite sprite = new Sprite(1.0f, 2.0f, 1.5f, 1.5f) {
        };
        assertEquals(1.0f, sprite.getX());
        assertEquals(2.0f, sprite.getY());
        assertEquals(1.5f, sprite.getDx());
        assertEquals(1.5f, sprite.getDy());
    }
}
