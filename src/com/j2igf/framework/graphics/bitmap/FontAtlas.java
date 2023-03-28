package com.j2igf.framework.graphics.bitmap;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class FontAtlas extends Bitmap
{
	public static final FontAtlas defaultFont = new FontAtlas("Dialog.plain", 12, true);
	private final int[] offsets;
	private final int[] glyphWidths;

	public FontAtlas(String fontName, int fontSize, boolean antiAliased)
	{
		if (fontName == null || fontSize < 5)
		{
			System.err.println("Invalid Font parameters!");
			System.exit(-1);
		}

		Font font = new Font(fontName, Font.PLAIN, fontSize);

		this.offsets = new int[256];
		this.glyphWidths = new int[256];

		StringBuilder buffer = new StringBuilder(256);
		for (int c = 0; c < 256; c++)
			buffer.append((char) c);
		String charMap = buffer.toString();

		int width;
		FontMetrics metrics = new JPanel().getFontMetrics(font);
		width = metrics.stringWidth(charMap) + 512;

		int height;
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true);
		GlyphVector vec = font.createGlyphVector(frc, charMap);
		height = vec.getPixelBounds(null, 0, 0).height;

		int total = 0;
		int[] sizes = new int[256];
		for (int c = 0; c < 256; c++)
		{
			GlyphVector gv = font.createGlyphVector(frc, String.valueOf((char) c));
			sizes[c] = gv.getPixelBounds(null, 0, 0).height;
		}
		for (int size : sizes)
			total += size;
		fontSize = (font.getSize() + (total / sizes.length)) / 2;

		BufferedImage sprite = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = sprite.createGraphics();
		if (antiAliased)
			graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		graphics.setFont(font);

		for (int c = 0, x = 0; c < 256; c++, x++)
		{
			this.offsets[c] = x++;
			graphics.setColor(Color.WHITE);
			graphics.drawString(String.valueOf((char) c), x, fontSize);
			x += metrics.charWidth((char) c);
			this.glyphWidths[c] = x - this.offsets[c];
		}
		this.width = width;
		this.height = height;
		this.pixels = sprite.getRGB(0, 0, width, height, null, 0, width);
		this.originX = this.originY = 0;
	}

	public Sprite textToSprite(String text, int color)
	{
		int textWidth = 0;
		for (int i = 0; i < text.length(); i++)
			textWidth += getGlyphWidth(text.charAt(i));
		Sprite textSprite = new Sprite(textWidth, getHeight());

		int xOffset = 0;
		for (int i = 0; i < text.length(); i++)
		{
			int ch = text.charAt(i);
			int offset = getOffset(ch);
			int glyphWidth = getGlyphWidth(ch);
			for (int y = 0; y < getHeight(); y++)
			{
				for (int x = 0; x < glyphWidth; x++)
				{
					int textAlpha = (int) ((getPixel(x + offset, y) >>> 24) * ((float) (color >>> 24) / 0xff)) << 24;
					textSprite.setPixel(x + xOffset, y, textAlpha | (color & 0xffffff));
				}
			}
			xOffset += glyphWidth;
		}

		return textSprite;
	}

	public int getOffset(int ch)
	{
		return offsets[ch];
	}

	public int getGlyphWidth(int ch)
	{
		return glyphWidths[ch];
	}

	public static void listAllFonts()
	{
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		java.awt.Font[] fonts = e.getAllFonts();
		for (java.awt.Font font : fonts)
			System.out.println(font.getName());
	}
}
