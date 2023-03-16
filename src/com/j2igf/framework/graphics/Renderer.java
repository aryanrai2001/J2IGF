package com.j2igf.framework.graphics;

import com.j2igf.framework.core.J2IGF;

public class Renderer
{
	private final int[] pixels;
	private final int width;
	private final int height;

	public static void create()
	{
		J2IGF.renderer = new Renderer();
	}

	private Renderer()
	{
		if (J2IGF.window == null)
		{
			System.err.println("A Window must be created before Renderer.");
			System.exit(-1);
		}
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
				pixels[x + y * width] = color;
			}
		}
	}

	public void setPixels(int x, int y, int color)
	{
		if (x < 0 || x >= width || y < 0 || y >= height)
			return;
		pixels[x + y * width] = color;
	}
}
