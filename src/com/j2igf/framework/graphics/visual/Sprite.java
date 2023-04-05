package com.j2igf.framework.graphics.visual;

import com.j2igf.framework.core.J2IGF;
import com.j2igf.framework.graphics.Renderer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class Sprite
{
	protected int originX;
	protected int originY;
	protected int width;
	protected int height;
	protected int[] pixels;

	public Sprite(Sprite bitmap)
	{
		this.originX = bitmap.getOriginX();
		this.originY = bitmap.getOriginY();
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
		this.pixels = Arrays.copyOf(bitmap.getPixels(), bitmap.getPixels().length);
	}

	public Sprite(int width, int height)
	{
		this.originX = 0;
		this.originY = 0;
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
	}

	public Sprite(String path)
	{
		if (path == null)
			return;

		BufferedImage image = null;
		try
		{
			image = ImageIO.read(new File(path));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		assert image != null;

		this.originX = 0;
		this.originY = 0;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.pixels = image.getRGB(0, 0, width, height, null, 0, width);
		image.flush();
	}

	protected Sprite()
	{
		this.originX = 0;
		this.originY = 0;
		this.width = 0;
		this.height = 0;
		this.pixels = null;
	}

	public void render(Renderer renderer, int x, int y)
	{
		x -= originX;
		y -= originY;

		int startX = 0, startY = 0;
		int endX = width, endY = height;

		if (x < 0)
			startX -= x;
		if (y < 0)
			startY -= y;
		if (x + endX > J2IGF.getWidth())
			endX = J2IGF.getWidth() - x;
		if (y + endY > J2IGF.getHeight())
			endY = J2IGF.getHeight() - y;

		for (int currY = startY; currY < endY; currY++)
		{
			for (int currX = startX; currX < endX; currX++)
			{
				renderer.setPixel(x + currX, y + currY, getPixel(currX, currY));
			}
		}
	}

	public void saveToFile(String path, String name)
	{
		File outputFile = new File(path + "/" + name + ".png");
		try
		{
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			image.setRGB(0, 0, width, height, pixels, 0, width);
			ImageIO.write(image, "png", outputFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void setPixel(int x, int y, int color)
	{
		if (x < 0 || x >= width || y < 0 || y >= height || (color & 0xffffff) == 0xff00ff)
			return;
		pixels[x + y * width] = color;
	}

	public int getPixel(int x, int y)
	{
		if (x < 0 || x >= width || y < 0 || y >= height)
			return 0x00000000;
		return pixels[x + y * width];
	}

	public void setOrigin(int originX, int originY)
	{
		this.originX = originX;
		this.originY = originY;
	}

	public int getOriginX()
	{
		return originX;
	}

	public int getOriginY()
	{
		return originY;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public int[] getPixels()
	{
		return pixels;
	}
}
