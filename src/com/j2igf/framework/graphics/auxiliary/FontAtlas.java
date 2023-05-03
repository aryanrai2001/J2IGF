package com.j2igf.framework.graphics.auxiliary;

import com.j2igf.framework.event.Debug;
import com.j2igf.framework.graphics.visual.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class FontAtlas extends Sprite {
    public static final FontAtlas DEFAULT_FONT = new FontAtlas("Dialog.plain", 12, true);
    private static final int ATLAS_LENGTH = 256;
    private final int[] xOffsets;
    private final int[] glyphWidths;
    private int lineSpacing;

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

    public int getOffset(int ch) {
        return xOffsets[ch];
    }

    public int getGlyphWidth(int ch) {
        return glyphWidths[ch];
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public void setLineSpacing(int lineSpacing) {
        if (lineSpacing < 0)
            return;
        this.lineSpacing = lineSpacing;
    }
}
