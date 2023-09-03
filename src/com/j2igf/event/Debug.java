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

package com.j2igf.event;

import com.j2igf.graphics.Renderer;
import com.j2igf.graphics.auxiliary.FontAtlas;

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
    private static final Logger LOGGER;

    /**
     * This is the formatter used by the logger.
     */
    private static final LogFormatter LOG_FORMATTER;

    /**
     * This is the renderer used to render the debugRaster.
     */
    private static Renderer renderer;

    /**
     * This is the raster used to render the debug information.
     */
    private static int[] debugRaster;

    /**
     * This is the width of the debugRaster.
     */
    private static int rasterWidth;

    /**
     * This is the height of the debugRaster.
     */
    private static int rasterHeight;

    static {
        LogManager.getLogManager().reset();
        LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        LOG_FORMATTER = new LogFormatter();
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(LOG_FORMATTER);

        LOGGER.setUseParentHandlers(false);
        LOGGER.addHandler(consoleHandler);
        LOGGER.setLevel(Level.ALL);
    }

    /**
     * Private constructor for the Debug class to prevent instantiation.
     */
    private Debug() {
    }

    /**
     * This function initializes the debugRaster.
     *
     * @param renderer The Renderer to be used to render the debugRaster.
     */
    public static void initRaster(Renderer renderer) {
        if (renderer == null || debugRaster != null)
            return;
        Debug.renderer = renderer;
        Debug.debugRaster = new int[renderer.getWidth() * renderer.getHeight()];
    }

    /**
     * This function logs a message to the console.
     *
     * @param msg The message to be logged.
     */
    public static void log(String msg) {
        LOG_FORMATTER.setColor(LogFormatter.ANSI_WHITE);
        LOGGER.log(Level.INFO, msg);
    }

    /**
     * This function logs information to the console.
     *
     * @param msg The info to be logged.
     */
    public static void logInfo(String msg) {
        LOG_FORMATTER.setColor(LogFormatter.ANSI_GREEN);
        LOGGER.log(Level.INFO, msg);
    }

    /**
     * This function logs a warning to the console.
     *
     * @param msg The warning to be logged.
     */
    public static void logWarning(String msg) {
        LOG_FORMATTER.setColor(LogFormatter.ANSI_YELLOW);
        LOGGER.log(Level.WARNING, msg);
    }

    /**
     * This function logs an error to the console.
     *
     * @param msg The error to be logged.
     */
    public static void logError(String msg) {
        LOG_FORMATTER.setColor(LogFormatter.ANSI_RED);
        LOGGER.log(Level.SEVERE, msg);
    }

    /**
     * This function logs an error to the console along with stack-trace.
     *
     * @param msg    The error to be logged.
     * @param thrown The exception to print stack-trace from.
     */
    public static void logError(String msg, Throwable thrown) {
        LOG_FORMATTER.setColor(LogFormatter.ANSI_RED);
        LOGGER.log(Level.SEVERE, msg, thrown);
    }

    /**
     * This function renders the debugRaster using the assigned Renderer.
     */
    public static void updateFrame() {
        if (debugRaster == null)
            return;
        for (int y = 0; y < rasterHeight; y++) {
            for (int x = 0; x < rasterWidth; x++) {
                renderer.setPixel(x, y, debugRaster[x + y * renderer.getWidth()]);
            }
        }
    }

    /**
     * This function renders debug information to the debugRaster.
     *
     * @param message The message to be rendered.
     *                If this parameter is null, nothing will be rendered.
     */
    public static void renderMessage(String message) {
        if (debugRaster == null || message == null)
            return;
        for (int y = 0; y < rasterHeight; y++)
            for (int x = 0; x < rasterWidth; x++)
                debugRaster[x + y * renderer.getWidth()] = 0;
        int xOffset = 0, yOffset = 0;
        rasterWidth = rasterHeight = 0;
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
                        debugRaster[(x + xOffset) + (y + yOffset) * renderer.getWidth()] = 0xff000000;
                    } else {
                        float alphaF = (float) fontAlpha / 0xff;
                        debugRaster[(x + xOffset) + (y + yOffset) * renderer.getWidth()] = 0xff000000 |
                                (int) (alphaF * 0xff) << 16 |
                                (int) (alphaF * 0xff) << 8 |
                                (int) (alphaF * 0xff);
                    }
                    rasterWidth = Math.max(rasterWidth, x + xOffset);
                    rasterHeight = Math.max(rasterHeight, y + yOffset);
                }
            }
            xOffset += glyphWidth;
        }
    }

    /**
     * This is the inner class that defined the log formatter.
     *
     * @author Aryan Rai
     * @see Formatter
     */
    public static class LogFormatter extends Formatter {
        /**
         * This constant defines the ANSI color code for red.
         */
        public static final String ANSI_RED = "\u001B[91m";
        /**
         * This constant defines the ANSI color code for green.
         */
        public static final String ANSI_GREEN = "\u001B[92m";
        /**
         * This constant defines the ANSI color code for yellow.
         */
        public static final String ANSI_YELLOW = "\u001B[93m";
        /**
         * This constant defines the ANSI color code for white.
         */
        public static final String ANSI_WHITE = "\u001B[97m";
        /**
         * This is the date format used by the formatter.
         */
        private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
        /**
         * This variable stores the color code of the log.
         */
        private String color;

        /**
         * This is the default constructor for the LogFormatter class.
         */
        public LogFormatter() {
            super();
            color = "\u001B[0m";
        }

        /**
         * This method sets the color of the log.
         *
         * @param color The color of to be set.
         */
        public void setColor(String color) {
            assert (
                    color.equals(ANSI_RED) ||
                            color.equals(ANSI_GREEN) ||
                            color.equals(ANSI_YELLOW) ||
                            color.equals(ANSI_WHITE)
            );
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
            builder.append(color);
            builder.append('[');
            builder.append(DATE_FORMAT.format(new Date(record.getMillis())));
            builder.append(']');
            builder.append(' ');
            builder.append(record.getLevel().getName());
            builder.append(':');
            builder.append(' ');
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

            Throwable thrown = record.getThrown();
            if (thrown != null) {
                StringBuilder stackTraceBuilder = new StringBuilder();
                int stackTraceWidth = thrown.toString().length();

                stackTraceBuilder.append('\n');
                stackTraceBuilder.append(thrown);
                stackTraceBuilder.append('\n');
                StackTraceElement[] stackTrace = thrown.getStackTrace();
                for (StackTraceElement element : stackTrace) {
                    stackTraceBuilder.append('\t');
                    String elementString = element.toString();
                    stackTraceWidth = Math.max(stackTraceWidth, elementString.length() + 4);
                    stackTraceBuilder.append(elementString);
                    stackTraceBuilder.append('\n');
                }

                builder.append('\n');
                builder.append("-".repeat(stackTraceWidth));
                builder.append(stackTraceBuilder);
                builder.append("-".repeat(stackTraceWidth));
            }
            builder.append('\n');
            return builder.toString();
        }
    }
}
