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

package com.j2igf.graphics.auxiliary;

import com.j2igf.event.Debug;
import com.j2igf.graphics.visual.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * This class is used to create a font atlas for a given font.
 *
 * @author Aryan Rai
 */
public class FontAtlas extends Sprite {

    /**
     * The default font atlas.
     */
    public static final FontAtlas DEFAULT_FONT = new FontAtlas("Arial", 16, true);

    /**
     * The constant that specifies the number of characters that the font atlas will contain.
     */
    private static final int ATLAS_LENGTH = 256;

    /**
     * The array that stores the x-offsets of the characters in the font atlas.
     */
    private final int[] xOffsets;

    /**
     * The array that stores the widths of the characters in the font atlas.
     */
    private final int[] glyphWidths;

    /**
     * The line spacing of the font atlas.
     */
    private int lineSpacing;

    /**
     * This is the constructor of the FontAtlas class.
     * It constructs a font atlas for the given font.
     *
     * @param fontName    the name of the font
     * @param fontSize    the size of the font
     * @param antiAliased whether the font should be anti-aliased or not
     */
    public FontAtlas(String fontName, int fontSize, boolean antiAliased) {
        super();
        if (fontName == null || fontSize < 5) {
            Debug.logError(getClass().getName() + " -> Illegal arguments for FontAtlas constructor!");
            System.exit(-1);
        }

        Font font = new Font(fontName, Font.PLAIN, fontSize);

        this.xOffsets = new int[ATLAS_LENGTH];
        this.glyphWidths = new int[ATLAS_LENGTH];
        this.lineSpacing = 0;

        StringBuilder buffer = new StringBuilder(ATLAS_LENGTH);
        for (int c = 0; c < ATLAS_LENGTH; c++)
            buffer.append((char) c);
        String charMap = buffer.toString();

        int width;
        FontMetrics metrics = new JPanel().getFontMetrics(font);
        width = metrics.stringWidth(charMap) + ATLAS_LENGTH * 2;

        int height;
        FontRenderContext fontRenderContext = new FontRenderContext(new AffineTransform(), true, true);
        GlyphVector mapGlyphVector = font.createGlyphVector(fontRenderContext, charMap);
        height = mapGlyphVector.getPixelBounds(null, 0, 0).height;

        int total = 0;
        int[] sizes = new int[ATLAS_LENGTH];
        for (int c = 0; c < ATLAS_LENGTH; c++) {
            GlyphVector charGlyphVector = font.createGlyphVector(fontRenderContext, String.valueOf((char) c));
            sizes[c] = charGlyphVector.getPixelBounds(null, 0, 0).height;
        }
        for (int size : sizes)
            total += size;
        fontSize = (font.getSize() + (total / sizes.length)) / 2;

        BufferedImage sprite = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = sprite.createGraphics();
        if (antiAliased)
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        graphics.setFont(font);

        graphics.setColor(Color.WHITE);
        for (int c = 0, x = 0; c < ATLAS_LENGTH; c++, x++) {
            this.glyphWidths[c] = metrics.charWidth((char) c);
            this.xOffsets[c] = x;
            graphics.drawString(String.valueOf((char) c), x, fontSize);
            x += this.glyphWidths[c] + (fontSize >> 2);
        }
        this.width = width;
        this.height = height;
        this.pixels = sprite.getRGB(0, 0, width, height, null, 0, width);
    }

    /**
     * This method uses the current font atlas to create a sprite of any string in any color.
     *
     * @param text  the string to be converted to a sprite
     *              (the string must only contain characters that are in the font atlas)
     * @param color the color of the string
     * @return an object of the Sprite class that represents the sprite of the given string in the given color
     */
    public Sprite textToSprite(String text, int color) {
        int textWidth = 0;
        for (int i = 0; i < text.length(); i++)
            textWidth += getGlyphWidth(text.charAt(i));
        Sprite textSprite = new Sprite(textWidth, getHeight());

        int xOffset = 0;
        for (int i = 0; i < text.length(); i++) {
            int ch = text.charAt(i);
            int offset = getOffset(ch);
            int glyphWidth = getGlyphWidth(ch);
            for (int y = 0; y < getHeight(); y++) {
                for (int x = 0; x < glyphWidth; x++) {
                    int textAlpha = (int) ((getPixel(x + offset, y) >>> 24) * ((float) (color >>> 24) / 0xff)) << 24;
                    textSprite.setPixel(x + xOffset, y, textAlpha | (color & 0xffffff));
                }
            }
            xOffset += glyphWidth;
        }

        return textSprite;
    }

    /**
     * This method return the x-offset of the given character in the font atlas.
     *
     * @param ch the character whose x-offset is to be returned
     *           (the character must be in the font atlas)
     * @return the x-offset of the given character in the font atlas
     */
    public int getOffset(int ch) {
        return xOffsets[ch];
    }

    /**
     * This method returns the width of the given character in the font atlas.
     *
     * @param ch the character whose width is to be returned
     *           (the character must be in the font atlas)
     * @return the width of the given character in the font atlas
     */
    public int getGlyphWidth(int ch) {
        return glyphWidths[ch];
    }

    /**
     * This method returns the line spacing of the font atlas.
     *
     * @return the line spacing of the font atlas
     */
    public int getLineSpacing() {
        return lineSpacing;
    }

    /**
     * This method sets the line spacing of the font atlas.
     *
     * @param lineSpacing the new line spacing of the font atlas
     */
    public void setLineSpacing(int lineSpacing) {
        if (lineSpacing < 0)
            return;
        this.lineSpacing = lineSpacing;
    }
}
