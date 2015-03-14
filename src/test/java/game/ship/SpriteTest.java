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

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

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
