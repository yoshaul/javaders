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

package game.network;

/**
 * The <code>InvalidLoginException</code> is thrown by the server
 * side if the user tried to login with the wrong user name or password
 * and by the client if the ticket (the session id) received from server
 * was null.
 */
public class InvalidLoginException extends Exception {

    public InvalidLoginException() {
        super("Invalid login information");
    }

    public InvalidLoginException(String message) {
        super(message);
    }

}