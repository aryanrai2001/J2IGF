package com.j2igf.framework.core;

import com.j2igf.framework.graphics.bitmap.Bitmap;
import com.j2igf.framework.graphics.bitmap.FontAtlas;

public class Renderer
{
	private int[] pixels;
	private int width;
	private int height;
	private float globalAlpha;
	private boolean alphaEnabled;

	private boolean isScreen;

	private Viewport viewport;
	private FontAtlas fontAtlas;

	private Renderer(boolean alphaEnabled)
	{
		this.globalAlpha = -1;
		this.alphaEnabled = alphaEnabled;
		this.viewport = Viewport.defaultViewport;
		this.fontAtlas = FontAtlas.defaultFont;
		resetTarget();
	}

	public static void create(boolean alphaEnabled)
	{
		J2IGF.renderer = new Renderer(alphaEnabled);
	}

	public void setFont(FontAtlas fontAtlas)
	{
		assert fontAtlas != null;
		this.fontAtlas = fontAtlas;
	}

	public FontAtlas getFont()
	{
		return fontAtlas;
	}

	public void setViewport(Viewport viewport)
	{
		assert viewport != null;
		this.viewport = viewport;
	}

	public Viewport getViewport()
	{
		return viewport;
	}

	public void setTarget(Bitmap target)
	{
		if (target == null)
			return;
		this.pixels = target.getPixels();
		this.width = target.getWidth();
		this.height = target.getHeight();
		this.alphaEnabled = false;
		this.isScreen = false;
	}

	public void resetTarget()
	{
		this.pixels = J2IGF.window.getFrameBuffer();
		this.width = J2IGF.getWidth();
		this.height = J2IGF.getHeight();
		this.alphaEnabled = J2IGF.isAlphaEnabled();
		this.isScreen = true;
	}

	public void useGlobalAlpha(float alpha)
	{
		if (alpha < 0 || alpha > 1)
		{
			System.err.println("Alpha value must be between 0 and 1.");
			System.exit(-1);
		}
		globalAlpha = alpha;
	}

	public void useLocalAlpha()
	{
		globalAlpha = -1;
	}

	public void clear(int color)
	{
		for (int y = viewport.getYRealPos(); y < viewport.getYRealPos() + viewport.getHeight(); y++)
		{
			for (int x = viewport.getXRealPos(); x < viewport.getXRealPos() + viewport.getWidth(); x++)
			{
				pixels[x + y * width] = color | 0xff000000;
			}
		}
	}

	public void showDebugInfo(String info)
	{
		int xOffset = 0;
		for (int i = 0; i < info.length(); i++)
		{
			int ch = info.charAt(i);
			int offset = FontAtlas.defaultFont.getOffset(ch);
			int glyphWidth = FontAtlas.defaultFont.getGlyphWidth(ch);
			for (int y = 0; y < FontAtlas.defaultFont.getHeight(); y++)
			{
				for (int x = 0; x < glyphWidth; x++)
				{
					int fontAlpha = (FontAtlas.defaultFont.getPixel(x + offset, y) & 0xff000000) >> 24;
					if (fontAlpha == 0)
					{
						pixels[x + xOffset + y * width] = 0xff000000;
					}
					else
					{
						float alphaF = (float) fontAlpha / 0xff;
						pixels[x + xOffset + y * width] = 0xff000000 | (int) (alphaF * 0xff) << 16 | (int) (alphaF * 0xff) << 8 | (int) (alphaF * 0xff);
					}
				}
			}
			xOffset += glyphWidth;
		}
	}

	public void setPixel(int x, int y, int color)
	{
		if (isScreen)
		{
			x = viewport.transformX(x);
			y = viewport.transformY(y);
		}
		else if (x < 0 || x >= width || y < 0 || y >= height)
			return;

		int alpha = (color >> 24) & 0xff;
		if (x == -1 || y == -1 || alpha == 0)
			return;

		if (alphaEnabled)
		{
			int previousColor = pixels[x + y * width];
			int red = (previousColor >> 16) & 0xff;
			int green = (previousColor >> 8) & 0xff;
			int blue = (previousColor) & 0xff;
			if (globalAlpha == -1)
			{
				float alphaF = (float) alpha / 0xff;
				red = (int) (((1 - alphaF) * red + alphaF * ((color >> 16) & 0xff)));
				green = (int) ((1 - alphaF) * green + alphaF * ((color >> 8) & 0xff));
				blue = (int) ((1 - alphaF) * blue + alphaF * (color & 0xff));
			}
			else
			{
				red = (int) (((1 - globalAlpha) * red + globalAlpha * ((color >> 16) & 0xff)));
				green = (int) ((1 - globalAlpha) * green + globalAlpha * ((color >> 8) & 0xff));
				blue = (int) ((1 - globalAlpha) * blue + globalAlpha * (color & 0xff));
			}
			pixels[x + y * width] = 0xff000000 | red << 16 | green << 8 | blue;
		}
		else
			pixels[x + y * width] = color | 0xff000000;
	}

	public void drawLine(int x0, int y0, int x1, int y1, int color)
	{
		int dx = Math.abs(x1 - x0);
		int sx = x0 < x1 ? 1 : -1;
		int dy = -Math.abs(y1 - y0);
		int sy = y0 < y1 ? 1 : -1;
		int error = dx + dy;

		while (true)
		{
			setPixel(x0, y0, color);
			if (x0 == x1 && y0 == y1)
				break;
			int e2 = 2 * error;
			if (e2 >= dy)
			{
				if (x0 == x1)
					break;
				error = error + dy;
				x0 = x0 + sx;
			}
			if (e2 <= dx)
			{
				if (y0 == y1)
					break;
				error = error + dx;
				y0 = y0 + sy;
			}
		}
	}

	public void drawRect(int x, int y, int width, int height, int strokeWidth, int color)
	{
		if (width <= 0 || height <= 0)
			return;
		int x0 = x;
		int y0 = y;
		int x1 = x + width;
		int y1 = y + height;
		strokeWidth = Math.min(strokeWidth, Math.min(width, height) + 1);
		while (strokeWidth > 0)
		{
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

	public void fillRect(int x, int y, int width, int height, int color)
	{
		if (width <= 0 || height <= 0)
			return;
		for (int yy = y; yy <= y + height; yy++)
		{
			for (int xx = x; xx <= x + width; xx++)
			{
				setPixel(xx, yy, color);
			}
		}
	}

	public void drawCircle(int x, int y, int radius, int color)
	{
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
		while (currY >= currX)
		{
			currX++;
			if (decisionParameter > 0)
			{
				currY--;
				decisionParameter = decisionParameter + 4 * (currX - currY) + 10;
			}
			else
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

	public void fillCircle(int x, int y, int radius, int color)
	{
		if (radius <= 0)
			return;
		int currX = 0, currY = radius;
		int decisionParameter = 3 - 2 * radius;
		drawLine(x + currY, y + currX, x - currY, y + currX, color);
		while (currY >= currX)
		{
			currX++;
			if (decisionParameter > 0)
			{
				currY--;
				decisionParameter = decisionParameter + 4 * (currX - currY) + 10;
			}
			else
				decisionParameter = decisionParameter + 4 * currX + 6;
			drawLine(x + currX, y + currY, x - currX, y + currY, color);
			drawLine(x + currX, y - currY, x - currX, y - currY, color);
			drawLine(x + currY, y + currX, x - currY, y + currX, color);
			drawLine(x + currY, y - currX, x - currY, y - currX, color);
		}
	}

	public void drawEllipse(int x, int y, int width, int height, int color)
	{
		if (width <= 0 || height <= 0)
			return;
		int currX, currY;
		int dx, dy;
		int error;
		int TwoASquare, TwoBSquare;
		int StoppingX, StoppingY;
		TwoASquare = 2 * width * width;
		TwoBSquare = 2 * height * height;
		currX = width;
		currY = 0;
		dx = height * height * (1 - 2 * width);
		dy = width * width;
		error = 0;
		StoppingX = TwoBSquare * width;
		StoppingY = 0;
		while (StoppingX >= StoppingY)
		{
			setPixel(x + currX, y + currY, color);
			setPixel(x - currX, y + currY, color);
			setPixel(x - currX, y - currY, color);
			setPixel(x + currX, y - currY, color);
			currY++;
			StoppingY += TwoASquare;
			error += dy;
			dy += TwoASquare;
			if ((2 * error + dx) > 0)
			{
				currX--;
				StoppingX -= TwoBSquare;
				error += dx;
				dx += TwoBSquare;
			}
		}
		currX = 0;
		currY = height;
		dx = height * height;
		dy = width * width * (1 - 2 * height);
		error = 0;
		StoppingX = 0;
		StoppingY = TwoASquare * height;
		while (StoppingX <= StoppingY)
		{
			setPixel(x + currX, y + currY, color);
			setPixel(x - currX, y + currY, color);
			setPixel(x - currX, y - currY, color);
			setPixel(x + currX, y - currY, color);
			currX++;
			StoppingX += TwoBSquare;
			error += dx;
			dx += TwoBSquare;
			if ((2 * error + dy) > 0)
			{
				currY--;
				StoppingY -= TwoASquare;
				error += dy;
				dy += TwoASquare;
			}
		}
	}

	public void fillEllipse(int x, int y, int width, int height, int color)
	{
		if (width <= 0 || height <= 0)
			return;
		int currX, currY;
		int dx, dy;
		int error;
		int TwoASquare, TwoBSquare;
		int StoppingX, StoppingY;
		TwoASquare = 2 * width * width;
		TwoBSquare = 2 * height * height;
		currX = width;
		currY = 0;
		dx = height * height * (1 - 2 * width);
		dy = width * width;
		error = 0;
		StoppingX = TwoBSquare * width;
		StoppingY = 0;
		while (StoppingX >= StoppingY)
		{
			drawLine(x + currX, y + currY, x - currX, y + currY, color);
			drawLine(x - currX, y - currY, x + currX, y - currY, color);
			currY++;
			StoppingY += TwoASquare;
			error += dy;
			dy += TwoASquare;
			if ((2 * error + dx) > 0)
			{
				currX--;
				StoppingX -= TwoBSquare;
				error += dx;
				dx += TwoBSquare;
			}
		}
		currX = 0;
		currY = height;
		dx = height * height;
		dy = width * width * (1 - 2 * height);
		error = 0;
		StoppingX = 0;
		StoppingY = TwoASquare * height;
		while (StoppingX <= StoppingY)
		{
			drawLine(x + currX, y + currY, x - currX, y + currY, color);
			drawLine(x - currX, y - currY, x + currX, y - currY, color);
			currX++;
			StoppingX += TwoBSquare;
			error += dx;
			dx += TwoBSquare;
			if ((2 * error + dy) > 0)
			{
				currY--;
				StoppingY -= TwoASquare;
				error += dy;
				dy += TwoASquare;
			}
		}
	}

	public void drawTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int color)
	{
		drawLine(x0, y0, x1, y1, color);
		drawLine(x1, y1, x2, y2, color);
		drawLine(x0, y0, x2, y2, color);
	}

	private void fillBottomFlatTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int color)
	{
		float invslope1 = (float) (x1 - x0) / (float) (y1 - y0);
		float invslope2 = (float) (x2 - x0) / (float) (y2 - y0);

		float curx1 = x0;
		float curx2 = x0;

		for (int scanlineY = y0; scanlineY <= y1; scanlineY++)
		{
			drawLine((int) curx1, scanlineY, (int) curx2, scanlineY, color);
			curx1 += invslope1;
			curx2 += invslope2;
		}
	}

	private void fillTopFlatTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int color)
	{
		float invslope1 = (float) (x2 - x0) / (float) (y2 - y0);
		float invslope2 = (float) (x2 - x1) / (float) (y2 - y1);

		float curx1 = x2;
		float curx2 = x2;

		for (int scanlineY = y2; scanlineY > y0; scanlineY--)
		{
			drawLine((int) curx1, scanlineY, (int) curx2, scanlineY, color);
			curx1 -= invslope1;
			curx2 -= invslope2;
		}
	}

	public void fillTriangle(int x0, int y0, int x1, int y1, int x2, int y2, int color)
	{
		if (y0 > y1)
		{
			y0 = y0 ^ y1 ^ (y1 = y0);
			x0 = x0 ^ x1 ^ (x1 = x0);
		}
		if (y1 > y2)
		{
			y1 = y1 ^ y2 ^ (y2 = y1);
			x1 = x1 ^ x2 ^ (x2 = x1);
		}
		if (y0 > y1)
		{
			y0 = y0 ^ y1 ^ (y1 = y0);
			x0 = x0 ^ x1 ^ (x1 = x0);
		}

		if (y1 == y2)
		{
			fillBottomFlatTriangle(x0, y0, x1, y1, x2, y2, color);
		}
		else if (y0 == y1)
		{
			fillTopFlatTriangle(x0, y0, x1, y1, x2, y2, color);
		}
		else
		{
			int x3 = (int) (x0 + ((float) (y1 - y0) / (float) (y2 - y0)) * (x2 - x0));
			fillBottomFlatTriangle(x0, y0, x1, y1, x3, y1, color);
			fillTopFlatTriangle(x1, y1, x3, y1, x2, y2, color);
		}
	}

	public void drawBitmap(int x, int y, Bitmap bitmap)
	{
		x -= bitmap.getOriginX();
		y -= bitmap.getOriginY();

		int startX = 0, startY = 0;
		int endX = bitmap.getWidth(), endY = bitmap.getHeight();

		if (x < 0)
			startX -= x;
		if (y < 0)
			startY -= y;
		if (x + endX > width)
			endX = width - x;
		if (y + endY > height)
			endY = height - y;

		for (int currY = startY; currY < endY; currY++)
		{
			for (int currX = startX; currX < endX; currX++)
			{
				setPixel(x + currX, y + currY, bitmap.getPixel(currX, currY));
			}
		}
	}

	public void drawText(int x, int y, int color, String text)
	{
		int xOffset = 0;
		for (int i = 0; i < text.length(); i++)
		{
			int ch = text.charAt(i);
			int offset = fontAtlas.getOffset(ch);
			int glyphWidth = fontAtlas.getGlyphWidth(ch);
			for (int yy = 0; yy < fontAtlas.getHeight(); yy++)
			{
				for (int xx = 0; xx < glyphWidth; xx++)
				{
					int fontAlpha = (fontAtlas.getPixel(xx + offset, yy) & 0xff000000);
					setPixel(x + xx + xOffset, y + yy, fontAlpha | (color & 0xffffff));
				}
			}
			xOffset += glyphWidth;
		}
	}
}
