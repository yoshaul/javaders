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

package game.network.client;

/**
 * This interface hold the JNDI names of the ejbs
 * for accessing from standalone client.
 */
public interface ClientJNDINames {

    public final static String SIGN_IN_BEAN = "SignInBean";

    public final static String PLAYER_BEAN = "PlayerBean";

    public final static String ONLINE_PLAYER_BEAN = "OnlinePlayerBean";

    public final static String HIGH_SCORES_BEAN = "HighScoresBean";

    public final static String TOPIC_CONNECTION_FACTORY =
            "jms/TopicConnectionFactory";

    public final static String INVITATION_TOPIC =
            "jms/invitationTopic";

}
