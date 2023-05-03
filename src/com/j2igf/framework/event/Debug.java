package com.j2igf.framework.event;

import com.j2igf.framework.graphics.Renderer;
import com.j2igf.framework.graphics.auxiliary.FontAtlas;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public final class Debug {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static boolean enabled = false, initialized = false;

    static {
        init();
    }

    private Debug() {
    }

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
        enableDebugMode();

        initialized = true;
    }

    private static void setColor(LogFormatter.COLOR color) {
        if (!enabled)
            return;
        ConsoleHandler handler = (ConsoleHandler) LOGGER.getHandlers()[0];
        LogFormatter formatter = (LogFormatter) handler.getFormatter();
        formatter.setColor(color);
        handler.setFormatter(formatter);
    }

    public static void enableDebugMode() {
        enabled = true;
        LOGGER.setLevel(Level.ALL);
    }

    public static void disableDebugMode() {
        enabled = false;
        LOGGER.setLevel(Level.OFF);
    }

    public static void log(String msg) {
        setColor(LogFormatter.COLOR.WHITE);
        LOGGER.log(Level.INFO, msg);
    }

    public static void log(String msg, Throwable thrown) {
        setColor(LogFormatter.COLOR.WHITE);
        LOGGER.log(Level.INFO, msg, thrown);
    }

    public static void logWarning(String msg) {
        setColor(LogFormatter.COLOR.YELLOW);
        LOGGER.log(Level.WARNING, msg);
    }

    public static void logWarning(String msg, Throwable thrown) {
        setColor(LogFormatter.COLOR.YELLOW);
        LOGGER.log(Level.WARNING, msg, thrown);
    }

    public static void logError(String msg) {
        setColor(LogFormatter.COLOR.RED);
        LOGGER.log(Level.SEVERE, msg);
    }

    public static void logError(String msg, Throwable thrown) {
        setColor(LogFormatter.COLOR.RED);
        LOGGER.log(Level.SEVERE, msg, thrown);
    }

    public static void renderMessage(Renderer renderer, String message) {
        if (message == null || renderer == null)
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

    public static class LogFormatter extends Formatter {
        public enum COLOR {
            BLACK,
            RED,
            GREEN,
            YELLOW,
            BLUE,
            PURPLE,
            CYAN,
            WHITE
        }

        private COLOR color = COLOR.WHITE;
        private final String[] ansiColorCodes = {
                "\u001B[30m", "\u001B[31m", "\u001B[32m", "\u001B[33m",
                "\u001B[34m", "\u001B[35m", "\u001B[36m", "\u001B[37m"
        };

        public void setColor(COLOR color) {
            this.color = color;
        }

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

        private String calcDate(long milliseconds) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date resultdate = new Date(milliseconds);
            return dateFormat.format(resultdate);
        }
    }
}
