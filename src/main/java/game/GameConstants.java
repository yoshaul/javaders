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

package game;

/**
 * The <code>GameConstants</code> interface holds some game-wide
 * constants.
 */
public final class GameConstants {

    static final long FRAME_SLEEP_TIME = 20;

    public static final String RESOURCES = "resources";

    private static final String IMAGES = "images";

    private static final String SOUNDS = "sounds";

    public static final String IMAGES_DIR = RESOURCES + "/" + IMAGES + "/";

    public static final String SOUNDS_DIR = RESOURCES + "/" + SOUNDS + "/";

    static final String CONFIG_DIR = "config";

    static final int PLAYER1_ID = 1;

    static final int PLAYER2_ID = 2;

    public static final int ENEMY_MANAGER_ID = 3;

    static final int PLAYER_MANAGER_ID = 4;

    static final int FIRST_ENEMY_SHIP_ID = 1001;

    public static final String GAME_FONT = "gameFont.ttf";

    public static final String GAME_CURSOR = "targetCur.gif";

}
