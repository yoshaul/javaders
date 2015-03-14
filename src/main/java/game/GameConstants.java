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

    public final static long FRAME_SLEEP_TIME = 20;

    public final static String RESOURCES = "resources";

    public final static String IMAGES = "images";

    public final static String SOUNDS = "sounds";

    public final static String IMAGES_DIR = RESOURCES + "/" + IMAGES + "/";

    public final static String SOUNDS_DIR = RESOURCES + "/" + SOUNDS + "/";

    public final static String CONFIG_DIR = "config";

    public final static String DBName = "java:comp/env/jdbc/gameDB";

    public final static int PLAYER1_ID = 1;

    public final static int PLAYER2_ID = 2;

    public final static int ENEMY_MANAGER_ID = 3;

    public final static int PLAYER_MANAGER_ID = 4;

    public final static int FIRST_ENEMY_SHIP_ID = 1001;

    public final static String GAME_FONT = "gameFont.ttf";

    public final static String GAME_CURSOR = "targetCur.gif";

}
