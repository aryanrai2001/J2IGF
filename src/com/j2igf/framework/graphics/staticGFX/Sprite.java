package com.j2igf.framework.graphics.staticGFX;

import com.j2igf.framework.graphics.Bitmap;
import java.util.Arrays;

public class Sprite extends Bitmap
{
	public Sprite(Bitmap bitmap, boolean copy)
	{
		this.originX = bitmap.getOriginX();
		this.originY = bitmap.getOriginY();
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
		if (copy)
			this.pixels = Arrays.copyOf(bitmap.getPixels(), bitmap.getPixels().length);
		else
			this.pixels = bitmap.getPixels();
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
