package com.j2igf.framework.graphics.image;

public abstract class Bitmap
{
	protected int originX, originY;
	protected int width, height;
	protected int[] pixels;

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

	public int getPixel(int x, int y)
	{
		if (x < 0 || x >= width || y < 0 || y >= height)
			return 0xffff00ff;
		return pixels[x + y * width];
	}
}
