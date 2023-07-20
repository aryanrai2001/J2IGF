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

package com.j2igf.framework.graphics;

import com.j2igf.framework.core.Window;
import com.j2igf.framework.event.Debug;
import com.j2igf.framework.graphics.auxiliary.FontAtlas;
import com.j2igf.framework.graphics.visual.Sprite;

/**
 * This class is used to render graphics on the target buffer.
 *
 * @author Aryan Rai
 */
public final class Renderer {

    /**
     * The target buffer to render on.
     */
    private final int[] pixels;

    /**
     * The width of the target buffer.
     */
    private final int width;

    /**
     * The height of the target buffer.
     */
    private final int height;

    /**
     * The flag to check if the target buffer is a Sprite.
     */
    private final boolean isSprite;

    /**
     * The font atlas used to render text.
     */
    private FontAtlas fontAtlas;

    /**
     * The global alpha value.
     */
    private float globalAlpha;

    /**
     * The flag to check if alpha is enabled.
     */
    private boolean isAlphaEnabled;

    /**
     * This is a constructor for the Renderer class.
     * It sets the target buffer to the frame buffer of the window.
     *
     * @param window instance of the Window for the frame buffer and dimensions
     */
    public Renderer(Window window) {
        if (window == null) {
            Debug.logError(getClass().getName() + " -> Window instance can not be null!");
            System.exit(-1);
        }
        this.fontAtlas = FontAtlas.DEFAULT_FONT;
        this.globalAlpha = -1;
        this.isAlphaEnabled = false;
        this.pixels = window.getFrameBuffer();
        this.width = window.getWidth();
        this.height = window.getHeight();
        this.isSprite = false;
    }

    /**
     * This is a constructor for the Renderer class.
     * It sets the target buffer to the pixels of the Sprite.
     *
     * @param target instance of the Sprite which this renderer will target.
     */
    public Renderer(Sprite target) {
        if (target == null) {
            Debug.logError(getClass().getName() + " -> Sprite instance can not be null!");
            System.exit(-1);
        }
        this.fontAtlas = FontAtlas.DEFAULT_FONT;
        this.globalAlpha = -1;
        this.isAlphaEnabled = false;
        this.pixels = target.getPixels();
        this.width = target.getWidth();
        this.height = target.getHeight();
        this.isSprite = true;
    }

    /**
     * This method is used to set the font atlas.
     *
     * @param fontAtlas instance of the FontAtlas
     */
    public void setFont(FontAtlas fontAtlas) {
        if (fontAtlas == null) {
            Debug.logError(getClass().getName() + " -> FontAtlas instance can not be null!");
            System.exit(-1);
        }
        this.fontAtlas = fontAtlas;
    }

    /**
     * This method is used to enable alpha blending.
     */
    public void enableAlphaBlending() {
        isAlphaEnabled = true;
    }

    /**
     * This method is used to disable alpha blending.
     */
    public void disableAlphaBlending() {
        isAlphaEnabled = false;
    }

    /**
     * This method is used to set the global alpha value.
     * And use global alpha instead of per-pixel alpha.
     *
     * @param alpha the global alpha value
     */
    public void useGlobalAlpha(float alpha) {
        if (alpha < 0 || alpha > 1) {
            Debug.logError(getClass().getName() + " -> Illegal arguments for Renderer.useGlobalAlpha() method!");
            System.exit(-1);
        }
        globalAlpha = alpha;
    }

    /**
     * This method is used to use per-pixel alpha instead of global alpha.
     */
    public void useLocalAlpha() {
        globalAlpha = -1;
    }

    /**
     * This method is used to clear the target buffer to a specific color.
     *
     * @param color the color to which the target buffer will be cleared
     */
    public void clear(int color) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[x + y * width] = color | (isSprite ? 0 : 0xff000000);
            }
        }
    }

    /**
     * This method is used to draw a pixel on the target buffer.
     *
     * @param x     the x coordinate of the pixel
     * @param y     the y coordinate of the pixel
     * @param color the color of the pixel
     */
    public void setPixel(int x, int y, int color) {
        int alpha = (color >>> 24);
        if (alpha == 0 || x < 0 || x >= width || y < 0 || y >= height)
            return;

        if (isAlphaEnabled && alpha < 0xff) {
            int previousColor = pixels[x + y * width];
            int red = (previousColor >> 16) & 0xff;
            int green = (previousColor >> 8) & 0xff;
            int blue = (previousColor) & 0xff;
            if (globalAlpha == -1) {
                float alphaF = (float) alpha / 0xff;
                red = (int) (((1 - alphaF) * red + alphaF * ((color >> 16) & 0xff)));
                green = (int) ((1 - alphaF) * green + alphaF * ((color >> 8) & 0xff));
                blue = (int) ((1 - alphaF) * blue + alphaF * (color & 0xff));
            } else {
                red = (int) (((1 - globalAlpha) * red + globalAlpha * ((color >> 16) & 0xff)));
                green = (int) ((1 - globalAlpha) * green + globalAlpha * ((color >> 8) & 0xff));
                blue = (int) ((1 - globalAlpha) * blue + globalAlpha * (color & 0xff));
            }
            pixels[x + y * width] = 0xff000000 | red << 16 | green << 8 | blue;
        } else
            pixels[x + y * width] = color | 0xff000000;
    }

    /**
     * This method is used to draw a line on the target buffer.
     *
     * @param x0    the x coordinate of the first point
     * @param y0    the y coordinate of the first point
     * @param x1    the x coordinate of the second point
     * @param y1    the y coordinate of the second point
     * @param color the color of the line
     */
    public void drawLine(int x0, int y0, int x1, int y1, int color) {
        int dx = Math.abs(x1 - x0);
        int sx = x0 < x1 ? 1 : -1;
        int dy = -Math.abs(y1 - y0);
        int sy = y0 < y1 ? 1 : -1;
        int error = dx + dy;

        while (true) {
            setPixel(x0, y0, color);
            if (x0 == x1 && y0 == y1)
                break;
            int e2 = 2 * error;
            if (e2 >= dy) {
                if (x0 == x1)
                    break;
                error = error + dy;
                x0 = x0 + sx;
            }
            if (e2 <= dx) {
                if (y0 == y1)
                    break;
                error = error + dx;
                y0 = y0 + sy;
            }
        }
    }

    /**
     * This method is used to draw a rectangle on the target buffer.
     *
     * @param x           the x coordinate of the rectangle
     * @param y           the y coordinate of the rectangle
     * @param width       the width of the rectangle
     * @param height      the height of the rectangle
     * @param strokeWidth the stroke width of the rectangle
     * @param color       the color of the rectangle
     */
    public void drawRect(int x, int y, int width, int height, int strokeWidth, int color) {
        if (width <= 0 || height <= 0)
            return;
        int x0 = x;
        int y0 = y;
        int x1 = x + width;
        int y1 = y + height;
        strokeWidth = Math.min(strokeWidth, Math.min(width, height) + 1);
        while (strokeWidth > 0) {
            drawLine(x0, y0, x1, y0, color);
            drawLine(x0, y1, x1, y1, color);
            drawLine(x0, y0, x0, y1, color);
            drawLine(x1, y0, x1, y1, color);
            x0++;
            y0++;
            x1--;
            y1--;
            strokeWidth--;
        }
    }

    /**
     * This method is used to fill a rectangle on the target buffer.
     *
     * @param x      the x coordinate of the rectangle
     * @param y      the y coordinate of the rectangle
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     * @param color  the color of the rectangle
     */
    public void fillRect(int x, int y, int width, int height, int color) {
        if (width <= 0 || height <= 0)
            return;
        for (int yy = y; yy <= y + height; yy++) {
            for (int xx = x; xx <= x + width; xx++) {
                setPixel(xx, yy, color);
            }
        }
    }

    /**
     * This method is used to draw a circle on the target buffer.
     *
     * @param x      the x coordinate of the circle
     * @param y      the y coordinate of the circle
     * @param radius the radius of the circle
     * @param color  the color of the circle
     */
    public void drawCircle(int x, int y, int radius, int color) {
        if (radius <= 0)
            return;
        int currX = 0, currY = radius;
        int decisionParameter = 3 - 2 * radius;
        setPixel(x + currX, y + currY, color);
        setPixel(x - currX, y + currY, color);
        setPixel(x + currX, y - currY, color);
        setPixel(x - currX, y - currY, color);
        setPixel(x + currY, y + currX, color);
        setPixel(x - currY, y + currX, color);
        setPixel(x + currY, y - currX, color);
        setPixel(x - currY, y - currX, color);
        while (currY >= currX) {
            currX++;
            if (decisionParameter > 0) {
                currY--;
                decisionParameter = decisionParameter + 4 * (currX - currY) + 10;
            } else
                decisionParameter = decisionParameter + 4 * currX + 6;
            setPixel(x + currX, y + currY, color);
            setPixel(x - currX, y + currY, color);
            setPixel(x + currX, y - currY, color);
            setPixel(x - currX, y - currY, color);
            setPixel(x + currY, y + currX, color);
            setPixel(x - currY, y + currX, color);
            setPixel(x + currY, y - currX, color);
            setPixel(x - currY, y - currX, color);
        }
    }

    /**
     * This method is used to draw a circle on the target buffer.
     *
     * @param x      the x coordinate of the circle
     * @param y      the y coordinate of the circle
     * @param radius the radius of the circle
     * @param color  the color of the circle
     */
    public void fillCircle(int x, int y, int radius, int color) {
        if (radius <= 0)
            return;
        int currX = 0, currY = radius;
        int decisionParameter = 3 - 2 * radius;
        drawLine(x + currY, y + currX, x - currY, y + currX, color);
        while (currY > currX) {
            currX++;
            if (currX == currY)
                break;
            if (decisionParameter > 0) {
                currY--;
                drawLine(x + currX, y + currY, x - currX, y + currY, color);
                drawLine(x + currX, y - currY, x - currX, y - currY, color);
                decisionParameter = decisionParameter + 4 * (currX - currY) + 10;
            } else
                decisionParameter = decisionParameter + 4 * currX + 6;

            if (currY > currX) {
                drawLine(x + currY, y + currX, x - currY, y + currX, color);
                drawLine(x + currY, y - currX, x - currY, y - currX, color);
            }
        }
    }

    /**
     * This method is used to draw a triangle on the target buffer.
     *
     * @param x0    the x coordinate of the first point of the triangle
     * @param y0    the y coordinate of the first point of the triangle
     * @param x1    the x coordinate of the second point of the triangle
     * @param y1    the y coordinate of the second point of the triangle
     * @param x2    the x coordinate of the third point of the triangle
     * @param y2    the y coordinate of the third point of the triangle
     * @param color the color of the triangle
     */
    public void drawTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int color) {
        drawLine(x0, y0, x1, y1, color);
        drawLine(x1, y1, x2, y2, color);
        drawLine(x0, y0, x2, y2, color);
    }

    /**
     * This method is used to fill the bottom part of a triangle on the target buffer.
     *
     * @param x0    the x coordinate of the first point of the triangle
     * @param y0    the y coordinate of the first point of the triangle
     * @param x1    the x coordinate of the second point of the triangle
     * @param y1    the y coordinate of the second point of the triangle
     * @param x2    the x coordinate of the third point of the triangle
     * @param y2    the y coordinate of the third point of the triangle
     * @param color the color of the triangle
     */
    private void fillBottomFlatTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int color) {
        float invslope1 = (float) (x1 - x0) / (float) (y1 - y0);
        float invslope2 = (float) (x2 - x0) / (float) (y2 - y0);

        float curx1 = x0;
        float curx2 = x0;

        for (int scanlineY = y0; scanlineY <= y1; scanlineY++) {
            drawLine((int) curx1, scanlineY, (int) curx2, scanlineY, color);
            curx1 += invslope1;
            curx2 += invslope2;
        }
    }

    /**
     * This method is used to fill the top part of a triangle on the target buffer.
     *
     * @param x0    the x coordinate of the first point of the triangle
     * @param y0    the y coordinate of the first point of the triangle
     * @param x1    the x coordinate of the second point of the triangle
     * @param y1    the y coordinate of the second point of the triangle
     * @param x2    the x coordinate of the third point of the triangle
     * @param y2    the y coordinate of the third point of the triangle
     * @param color the color of the triangle
     */
    private void fillTopFlatTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int color) {
        float invslope1 = (float) (x2 - x0) / (float) (y2 - y0);
        float invslope2 = (float) (x2 - x1) / (float) (y2 - y1);

        float curx1 = x2;
        float curx2 = x2;

        for (int scanlineY = y2; scanlineY > y0; scanlineY--) {
            drawLine((int) curx1, scanlineY, (int) curx2, scanlineY, color);
            curx1 -= invslope1;
            curx2 -= invslope2;
        }
    }

    /**
     * This method is used to fill a triangle on the target buffer.
     *
     * @param x0    the x coordinate of the first point of the triangle
     * @param y0    the y coordinate of the first point of the triangle
     * @param x1    the x coordinate of the second point of the triangle
     * @param y1    the y coordinate of the second point of the triangle
     * @param x2    the x coordinate of the third point of the triangle
     * @param y2    the y coordinate of the third point of the triangle
     * @param color the color of the triangle
     */
    public void fillTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int color) {
        if (y0 > y1) {
            y0 = y0 ^ y1 ^ (y1 = y0);
            x0 = x0 ^ x1 ^ (x1 = x0);
        }
        if (y1 > y2) {
            y1 = y1 ^ y2 ^ (y2 = y1);
            x1 = x1 ^ x2 ^ (x2 = x1);
        }
        if (y0 > y1) {
            y0 = y0 ^ y1 ^ (y1 = y0);
            x0 = x0 ^ x1 ^ (x1 = x0);
        }

        if (y1 == y2) {
            fillBottomFlatTriangle(x0, y0, x1, y1, x2, y2, color);
        } else if (y0 == y1) {
            fillTopFlatTriangle(x0, y0, x1, y1, x2, y2, color);
        } else {
            int x3 = (int) (x0 + ((float) (y1 - y0) / (float) (y2 - y0)) * (x2 - x0));
            fillBottomFlatTriangle(x0, y0, x1, y1, x3, y1, color);
            fillTopFlatTriangle(x1, y1, x3, y1, x2, y2, color);
        }
    }

    /**
     * This method is used to draw text on the target buffer.
     *
     * @param x     the x coordinate of the text
     * @param y     the y coordinate of the text
     * @param color the color of the text
     * @param text  the text to draw
     */
    public void drawText(int x, int y, int color, String text) {
        text = text == null ? "" : text;
        int xOffset = 0;
        int yOffset = 0;
        float alpha = (float) (color >>> 24) / 0xff;
        for (int i = 0; i < text.length(); i++) {
            int ch = text.charAt(i);
            if (ch == '\n') {
                xOffset = 0;
                yOffset += fontAtlas.getHeight() + fontAtlas.getLineSpacing();
            }
            int offset = fontAtlas.getOffset(ch);
            int glyphWidth = fontAtlas.getGlyphWidth(ch);
            for (int yy = 0; yy < fontAtlas.getHeight(); yy++) {
                for (int xx = 0; xx < glyphWidth; xx++) {
                    int fontAlpha = (int) ((fontAtlas.getPixel(xx + offset, yy) >>> 24) * alpha) << 24;
                    setPixel(x + xx + xOffset, y + yy + yOffset, fontAlpha | (color & 0xffffff));
                }
            }
            xOffset += glyphWidth;
        }
    }

    /**
     * This method is used to get the width of the target buffer.
     *
     * @return the width of the target buffer
     */
    public int getWidth() {
        return width;
    }

    /**
     * This method is used to get the height of the target buffer.
     *
     * @return the height of the target buffer
     */
    public int getHeight() {
        return height;
    }
}
