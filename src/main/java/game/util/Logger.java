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

package game.util;

import javax.swing.*;
import java.awt.*;

/**
 * The <code>Logger</code> class is a simple logging helper.
 * Most of the game errors and exceptions are sent to the Logger.
 * It also handles the command line parameters.
 */
public class Logger {

    private static boolean debug;    // debug mode flag
    private static boolean invulnerable; // player ships vulnerability

    /**
     * Initializes the Logger, check if in debug mode.
     * This method should be called once when the game starts.
     * (can also be used to redirect out streams)
     *
     * @param args Arguments from the command line
     */
    public static void init(String[] args) {
        for (String param : args) {
            if (param.equals("debug")) {
                debug = true;
            } else if (param.equals("invulnerable")) {
                invulnerable = true;
            }
        }
    }


    /**
     * Print the message to the screen.
     *
     * @param message Message to display.
     */
    public static void screen(String message) {
        System.out.println(message);
    }

    /**
     * Print the error to the screen.
     *
     * @param error Error message to display.
     */
    public static void error(String error) {
        System.err.println(error);
    }

    /**
     * Prints the stack trace of the exception to the screen.
     *
     * @param e Exception to print.
     */
    public static void exception(Exception e) {
        e.printStackTrace();
    }

    /**
     * Displays a <code>JOptionPane</code> error dialog.
     *
     * @param parent  Parent dialog window.
     * @param message Message to display.
     */
    public static void showErrorDialog(Component parent, String message) {

        JOptionPane.showMessageDialog(parent, message,
                "Error", JOptionPane.ERROR_MESSAGE);

    }

    /**
     * Prints to the screen the prefix and the total used memory if in
     * debug mode.
     *
     * @param prefix Prefix message
     */
    public static void printMemoryUsage(String prefix) {
        if (isDebug()) {
            Runtime runtime = Runtime.getRuntime();
            long usedMemory = runtime.totalMemory() - runtime.freeMemory();
            screen(prefix + "\t" + usedMemory);
        }
    }

    /**
     * Returns true if the debug flasg is on. The debug flag is
     * taket from the system property.
     *
     * @return True if in debug mode
     */
    public static boolean isDebug() {
        return debug;
    }

    /**
     * Returns true if the application started with the invulnerable
     * flag on.
     */
    public static boolean isInvulnerable() {
        return invulnerable;
    }

}
