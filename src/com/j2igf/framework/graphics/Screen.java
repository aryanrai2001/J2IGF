package com.j2igf.framework.graphics;

import com.j2igf.framework.core.J2IGF;

public class Screen
{
	public static final Screen DEFAULT_SCREEN = new Screen(J2IGF.getWidth(), J2IGF.getHeight());
	private final int width;
	private final int height;
	private final int xRealPos;
	private final int yRealPos;
	private int xPos;
	private int yPos;

	public Screen(int width, int height)
	{
		width = Math.min(width, J2IGF.getWidth());
		height = Math.min(height, J2IGF.getHeight());
		this.width = width;
		this.height = height;
		this.xRealPos = (J2IGF.getWidth() - width) / 2;
		this.yRealPos = (J2IGF.getHeight() - height) / 2;
		this.xPos = xRealPos;
		this.yPos = yRealPos;
	}

	public int transformX(int x)
	{
		x += xPos;
		if (x < xRealPos || x >= xRealPos + width)
			return -1;
		else
			return x;
	}

	public int transformY(int y)
	{
		y += yPos;
		if (y < xRealPos || y >= xRealPos + height)
			return -1;
		else
			return y;
	}

	public void setPos(int x, int y)
	{
		this.xPos = xRealPos - x;
		this.yPos = yRealPos - y;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public int getXRealPos()
	{
		return xRealPos;
	}

	public int getYRealPos()
	{
		return yRealPos;
	}

	public int getXPos()
	{
		return xPos;
	}

	public int getYPos()
	{
		return yPos;
	}
}
