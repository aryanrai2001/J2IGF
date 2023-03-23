package com.j2igf.framework.graphics.image;

import java.util.Arrays;

public class Sprite extends Bitmap
{
	public Sprite(Bitmap bitmap, boolean copy)
	{
		this.width = bitmap.width;
		this.height = bitmap.height;
		if (copy)
			this.pixels = Arrays.copyOf(bitmap.pixels, bitmap.pixels.length);
		else
			this.pixels = bitmap.pixels;
	}

	public Sprite(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
	}

	public void setPixel(int x, int y, int color)
	{
		if (x < 0 || x >= width || y < 0 || y >= height)
			return;
		pixels[x + y * width] = color;
	}
}
