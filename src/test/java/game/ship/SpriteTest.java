package game.ship;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

/**
 * Tests the Sprite class
 *
 * @author Yossi Shaul
 */
@Test
public class SpriteTest {
    private Sprite sprite;

    @BeforeTest
    public void setUp() {
        sprite = new Sprite(1.0f, 2.0f, 1.5f, 1.5f) {
        };
    }

    @Test
    public void create() {
        assertEquals(1.0f, sprite.getX());
        assertEquals(2.0f, sprite.getY());
        assertEquals(1.5f, sprite.getDx());
        assertEquals(1.5f, sprite.getDy());
    }


}
