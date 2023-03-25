package com.j2igf.framework.graphics.bitmap;

import java.util.Arrays;

public class Sprite extends Bitmap
{
	public Sprite(Bitmap bitmap, boolean copy)
	{
		this.originX = bitmap.originX;
		this.originY = bitmap.originY;
		this.width = bitmap.width;
		this.height = bitmap.height;
		if (copy)
			this.pixels = Arrays.copyOf(bitmap.pixels, bitmap.pixels.length);
		else
			this.pixels = bitmap.pixels;
	}

	public Sprite(int width, int height)
	{
		this.originX = 0;
		this.originY = 0;
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
	}

	public void setPixel(int x, int y, int color)
	{
		if (x < 0 || x >= width || y < 0 || y >= height || (color & 0xffffff) == 0xff00ff)
			return;
		pixels[x + y * width] = color;
	}
}
