package com.j2igf.framework.graphics.image;
public class TileSet extends Bitmap
{
	private final int numTilesX;
	private final int numTilesY;
	private final int tileWidth;
	private final int tileHeight;

	public TileSet(Bitmap src, int numTilesX, int numTilesY)
	{
		this.width = src.width;
		this.height = src.height;
		this.numTilesX = numTilesX;
		this.numTilesY = numTilesY;
		if (width % numTilesX != 0 || height % numTilesY != 0)
		{
			System.err.println("Bitmap dimensions are incompatible with TileSet.");
			System.exit(-1);
		}
		this.originX = src.originX;
		this.originY = src.originY;
		this.pixels = src.pixels;
		this.tileWidth = width / numTilesX;
		this.tileHeight = height / numTilesY;
	}

	public Sprite getTile(int index)
	{
		if (index >= numTilesX * numTilesY)
			return null;

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
			for (int j = 0; j < tileWidth; j++)
			{
				tile.pixels[j + i * tileWidth] = pixels[(xPos + j) + (yPos + i) * width];
			}
		}
		return tile;
	}
}
