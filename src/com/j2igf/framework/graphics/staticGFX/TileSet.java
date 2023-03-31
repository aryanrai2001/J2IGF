package com.j2igf.framework.graphics.staticGFX;

import com.j2igf.framework.graphics.Bitmap;

public class TileSet extends Bitmap
{
	private final int numTilesX;
	private final int numTilesY;
	private final int tileWidth;
	private final int tileHeight;

	public TileSet(Bitmap src, int numTilesX, int numTilesY)
	{
		this.width = src.getWidth();
		this.height = src.getHeight();
		this.numTilesX = numTilesX;
		this.numTilesY = numTilesY;
		if (width % numTilesX != 0 || height % numTilesY != 0)
		{
			System.err.println("Bitmap dimensions are incompatible with TileSet.");
			System.exit(-1);
		}
		this.originX = src.getOriginX();
		this.originY = src.getOriginY();
		this.pixels = src.getPixels();
		this.tileWidth = width / numTilesX;
		this.tileHeight = height / numTilesY;
	}

	public Sprite getTile(int index, boolean verticalScan)
	{
		if (index >= numTilesX * numTilesY)
			return null;
		if (verticalScan)
			return getTile(index / numTilesX, index % numTilesX);
		else
			return getTile(index % numTilesX, index / numTilesX);
	}

	public Sprite getTile(int x, int y)
	{
		if (x < 0 || x > numTilesX || y < 0 || y > numTilesY)
			return null;

		int xPos = x * tileWidth;
		int yPos = y * tileHeight;
		Sprite tile = new Sprite(tileWidth, tileHeight);
		for (int i = 0; i < tileHeight; i++)
		{
			if (tileWidth >= 0)
				System.arraycopy(pixels, xPos + (yPos + i) * width, tile.getPixels(), i * tileWidth, tileWidth);
		}
		return tile;
	}

	public int getNumTilesX()
	{
		return numTilesX;
	}

	public int getNumTilesY()
	{
		return numTilesY;
	}

	public int getTileWidth()
	{
		return tileWidth;
	}

	public int getTileHeight()
	{
		return tileHeight;
	}
}
