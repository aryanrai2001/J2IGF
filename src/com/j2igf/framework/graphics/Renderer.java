package com.j2igf.framework.graphics;

import com.j2igf.framework.core.J2IGF;
import com.j2igf.framework.graphics.image.Bitmap;

public class Renderer
{
	private int[] pixels;
	private int width;
	private int height;

	private Renderer()
	{
		resetTarget();
	}

	public static void create()
	{
		if (J2IGF.window == null)
		{
			System.err.println("A Window must be created before Renderer.");
			System.exit(-1);
		}
		J2IGF.renderer = new Renderer();
	}

	public void setTarget(Bitmap target)
	{
		if (target == null)
			return;
		this.pixels = target.getPixels();
		this.width = target.getWidth();
		this.height = target.getHeight();
	}

	public void resetTarget()
	{
		this.pixels = J2IGF.window.getFrameBuffer();
		this.width = J2IGF.getWidth();
		this.height = J2IGF.getHeight();
	}

	public void clear(int color)
	{
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				pixels[x + y * width] = color | 0xff000000;
			}
		}
	}

	public void setPixel(int x, int y, int color)
	{
		if (x < 0 || x >= width || y < 0 || y >= height || (color & 0xffffff) == 0xff00ff)
			return;
		pixels[x + y * width] = color;
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

	public void drawRect(int x0, int y0, int x1, int y1, int strokeWidth, int color)
	{
		if (x0 > x1)
			x0 = x0 ^ x1 ^ (x1 = x0);
		if (y0 > y1)
			y0 = y0 ^ y1 ^ (y1 = y0);
		strokeWidth = Math.min(strokeWidth, Math.min(x1 - x0, y1 - y0) + 1);
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

	public void fillRect(int x0, int y0, int x1, int y1, int color)
	{
		if (x0 > x1)
			x0 = x0 ^ x1 ^ (x1 = x0);
		if (y0 > y1)
			y0 = y0 ^ y1 ^ (y1 = y0);
		for (int y = y0; y <= y1; y++)
		{
			for (int x = x0; x <= x1; x++)
			{
				setPixel(x, y, color);
			}
		}
	}

	public void drawCircle(int x, int y, int radius, int color)
	{
		int currX = 0, currY = Math.abs(radius);
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
		radius = Math.abs(radius);
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
		width = Math.abs(width);
		height = Math.abs(height);
		if (width < 1)
		{
			drawLine(x, y - height, x, y + height, color);
			return;
		}
		if (height < 1)
		{
			drawLine(x - width, y, x + width, y, color);
			return;
		}
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
		width = Math.abs(width);
		height = Math.abs(height);
		if (width < 1)
		{
			drawLine(x, y - height, x, y + height, color);
			return;
		}
		if (height < 1)
		{
			drawLine(x - width, y, x + width, y, color);
			return;
		}
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

	public void renderBitmap(int x, int y, Bitmap bitmap)
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
}
