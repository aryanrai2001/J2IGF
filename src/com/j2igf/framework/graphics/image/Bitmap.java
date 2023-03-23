package com.j2igf.framework.graphics.image;

public class Bitmap
{
	private final int width, height;
	private final int[] pixels;

	public Bitmap(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
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

	public int getPixel(int x, int y)
	{
		if (x < 0 || x >= width || y < 0 || y >= height)
			return 0xffff00ff;
		return pixels[x + y * width];
	}

	public void setPixel(int x, int y, int color)
	{
		if (x < 0 || x >= width || y < 0 || y >= height)
			return;
		pixels[x + y * width] = color;
	}
}
