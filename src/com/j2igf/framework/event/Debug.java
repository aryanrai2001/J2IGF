/*
 * Copyright (c) 2023, Aryan Rai
 *
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package com.j2igf.framework.event;

import com.j2igf.framework.graphics.Renderer;
import com.j2igf.framework.graphics.auxiliary.FontAtlas;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * This class provides a simple logging system for the framework.
 *
 * @author Aryan Rai
 */
public final class Debug {
    /**
     * This is the logger used by the framework.
     */
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /**
     * These are some variables used by the class to determine the state of the logging system.
     */
    private static boolean initialized, enabled;
    /**
     * This is the renderer used by the Debug class to render debug info.
     */
    private static Renderer renderer;

    static {
        init();
    }

    /**
     * This is the constructor for the Debug class.
     */
    private Debug() {
    }

    /**
     * This function initializes the logging system.
     */
    public static void init() {
        if (initialized)
            return;

        LogManager.getLogManager().reset();
        LOGGER.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();
        LogFormatter formatter = new LogFormatter();
        formatter.setColor(LogFormatter.COLOR.WHITE);
        handler.setFormatter(formatter);
        LOGGER.addHandler(handler);
        LOGGER.setLevel(Level.ALL);

        initialized = true;
        enabled = true;
    }

    /**
     * This function sets the color of the logging system.
     *
     * @param color The color to set the logging system to.
     */
    private static void setColor(LogFormatter.COLOR color) {
        if (!enabled)
            return;
        ConsoleHandler handler = (ConsoleHandler) LOGGER.getHandlers()[0];
        LogFormatter formatter = (LogFormatter) handler.getFormatter();
        formatter.setColor(color);
        handler.setFormatter(formatter);
    }

    /**
     * This function sets the renderer for debug info.
     *
     * @param renderer The renderer using which the debug info will be rendered.
     */
    public static void setRenderer(Renderer renderer) {
        assert (renderer != null);
        Debug.renderer = renderer;
    }

    /**
     * This function enables the logging system.
     */
    public static void enableDebugMode() {
        enabled = true;
    }

    /**
     * This function disables the logging system.
     */
    public static void disableDebugMode() {
        enabled = false;
    }

    /**
     * This function logs a message to the console.
     *
     * @param msg The message to be logged.
     */
    public static void log(String msg) {
        if (!enabled)
            return;
        setColor(LogFormatter.COLOR.WHITE);
        LOGGER.log(Level.INFO, msg);
    }

    /**
     * This function logs a message to the console and throws an exception.
     *
     * @param msg    The message to be logged.
     * @param thrown The exception to be thrown.
     */
    public static void log(String msg, Throwable thrown) {
        if (!enabled)
            return;
        setColor(LogFormatter.COLOR.WHITE);
        LOGGER.log(Level.INFO, msg, thrown);
    }

    /**
     * This function logs a warning to the console.
     *
     * @param msg The warning to be logged.
     */
    public static void logWarning(String msg) {
        if (!enabled)
            return;
        setColor(LogFormatter.COLOR.YELLOW);
        LOGGER.log(Level.WARNING, msg);
    }

    /**
     * This function logs a warning to the console and throws an exception.
     *
     * @param msg    The warning to be logged.
     * @param thrown The exception to be thrown.
     */
    public static void logWarning(String msg, Throwable thrown) {
        if (!enabled)
            return;
        setColor(LogFormatter.COLOR.YELLOW);
        LOGGER.log(Level.WARNING, msg, thrown);
    }

    /**
     * This function logs an error to the console.
     *
     * @param msg The error to be logged.
     */
    public static void logError(String msg) {
        if (!enabled)
            return;
        setColor(LogFormatter.COLOR.RED);
        LOGGER.log(Level.SEVERE, msg);
    }

    /**
     * This function logs an error to the console and throws an exception.
     *
     * @param msg    The error to be logged.
     * @param thrown The exception to be thrown.
     */
    public static void logError(String msg, Throwable thrown) {
        if (!enabled)
            return;
        setColor(LogFormatter.COLOR.RED);
        LOGGER.log(Level.SEVERE, msg, thrown);
    }

    /**
     * This function renders debug information to the screen.
     *
     * @param message  The message to be rendered.
     *                 If this parameter is null, nothing will be rendered.
     */
    public static void renderMessage(String message) {
        if (!(enabled && initialized) || message == null || renderer == null)
            return;
        int xOffset = 0;
        int yOffset = 0;
        for (int i = 0; i < message.length(); i++) {
            int ch = message.charAt(i);
            if (ch == '\n') {
                xOffset = 0;
                yOffset += FontAtlas.DEFAULT_FONT.getHeight() + FontAtlas.DEFAULT_FONT.getLineSpacing();
            }
            int offset = FontAtlas.DEFAULT_FONT.getOffset(ch);
            int glyphWidth = FontAtlas.DEFAULT_FONT.getGlyphWidth(ch);
            for (int y = 0; y < FontAtlas.DEFAULT_FONT.getHeight(); y++) {
                for (int x = 0; x < glyphWidth; x++) {
                    int fontAlpha = (FontAtlas.DEFAULT_FONT.getPixel(x + offset, y) >>> 24);
                    if (fontAlpha == 0) {
                        renderer.setPixel(x + xOffset, y + yOffset, 0xff000000);
                    } else {
                        float alphaF = (float) fontAlpha / 0xff;
                        renderer.setPixel(
                                x + xOffset,
                                y + yOffset,
                                0xff000000 |
                                        (int) (alphaF * 0xff) << 16 |
                                        (int) (alphaF * 0xff) << 8 |
                                        (int) (alphaF * 0xff)
                        );
                    }
                }
            }
            xOffset += glyphWidth;
        }
    }

    /**
     * This is the inner class that defined the log formatter.
     *
     * @author Aryan Rai
     */
    public static class LogFormatter extends Formatter {
        /**
         * This array stores the ANSI color codes for the logger.
         */
        private final String[] ansiColorCodes = {
                "\u001B[30m", "\u001B[31m", "\u001B[32m", "\u001B[33m",
                "\u001B[34m", "\u001B[35m", "\u001B[36m", "\u001B[37m"
        };
        /**
         * This variable stores the color of the logger.
         */
        private COLOR color = COLOR.WHITE;

        /**
         * This is the default constructor for the LogFormatter class.
         */
        public LogFormatter() {
            super();
        }

        /**
         * This function sets the color of the logger.
         *
         * @param color The color to be set.
         */
        public void setColor(COLOR color) {
            this.color = color;
        }

        /**
         * This function formats the log record.
         *
         * @param record The log record to be formatted.
         * @return The formatted log record as String.
         */
        @Override
        public String format(LogRecord record) {

            StringBuilder builder = new StringBuilder();
            builder.append(ansiColorCodes[color.ordinal()]);

            builder.append('[');
            builder.append(calcDate(record.getMillis()));
            builder.append(']');
            builder.append(' ');
            builder.append('[');
            builder.append(record.getSourceClassName());
            builder.append(']');
            builder.append(' ');
            builder.append('[');
            builder.append(record.getLevel().getName());
            builder.append(']');

            builder.append(ansiColorCodes[color.ordinal()]);
            builder.append(':');
            builder.append(' ');
            builder.append('\n');
            builder.append(record.getMessage());

            Object[] params = record.getParameters();

            if (params != null) {
                builder.append('\t');
                for (int i = 0; i < params.length; i++) {
                    builder.append(params[i]);
                    if (i < params.length - 1) {
                        builder.append(',');
                        builder.append(' ');
                    }
                }
            }

            builder.append(ansiColorCodes[color.ordinal()]);
            builder.append('\n');
            return builder.toString();
        }

        /**
         * This function calculates the date from the milliseconds.
         *
         * @param milliseconds The milliseconds to be converted.
         * @return The date as String.
         */
        private String calcDate(long milliseconds) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date resultdate = new Date(milliseconds);
            return dateFormat.format(resultdate);
        }

        /**
         * This enum defines the color values supported by the logger.
         */
        public enum COLOR {
            /**
             * This enum value represents the color black.
             */
            BLACK,

            /**
             * This enum value represents the color red.
             */
            RED,

            /**
             * This enum value represents the color green.
             */
            GREEN,

            /**
             * This enum value represents the color yellow.
             */
            YELLOW,

            /**
             * This enum value represents the color blue.
             */
            BLUE,

            /**
             * This enum value represents the color purple.
             */
            PURPLE,

            /**
             * This enum value represents the color cyan.
             */
            CYAN,

            /**
             * This enum value represents the color white.
             */
            WHITE
        }
    }
}
