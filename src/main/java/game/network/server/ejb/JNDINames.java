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

package game.network.server.ejb;

/**
 * This interface holds static variables with the JNDI
 * names of the EJBs and the database.
 */
public interface JNDINames {

    String DB_NAME =
            "java:comp/env/jdbc/gameDB";

    String PLAYER_BEAN =
            "java:comp/env/ejb/Player";

    String ONLINE_PLAYER_BEAN =
            "java:comp/env/ejb/OnlinePlayer";

    String HIGH_SCORES_BEAN =
            "java:comp/env/ejb/HighScores";

    String SEQUENCE_FACTORY_BEAN =
            "java:comp/env/ejb/SequenceFactory";

}
