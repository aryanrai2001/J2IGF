package com.j2igf.framework.graphics.image;

public abstract class Bitmap
{
	protected int width, height;
	protected int[] pixels;

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

	public int getPixel(int x, int y)
	{
		if (x < 0 || x >= width || y < 0 || y >= height)
			return 0xffff00ff;
		return pixels[x + y * width];
	}
}
