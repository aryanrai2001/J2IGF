package com.j2igf.framework.graphics;

import com.j2igf.framework.core.J2IGF;
import com.j2igf.framework.graphics.bitmap.Sprite;
import com.j2igf.framework.graphics.bitmap.TileSet;

public class Animation
{
	private final Sprite[] frames;
	private final double fps;
	private float frameIndex;

	public Animation(TileSet tileSet, boolean verticalScan, int offset, int size, double fps)
	{
		if (offset + size > tileSet.getNumTilesX() * tileSet.getNumTilesY())
		{
			System.err.println("Not enough frames in the TileSet!");
			System.exit(-1);
		}
		this.frameIndex = 0;
		this.fps = fps;
		this.frames = new Sprite[size];
		for (int i = 0; i < size; i++)
			frames[i] = tileSet.getTile(offset + i, verticalScan);
	}

	public void update()
	{
		frameIndex += fps * J2IGF.time.getDeltaTime();
		if (frameIndex >= frames.length)
			frameIndex = 0;
	}

	public void render(int x, int y)
	{
		J2IGF.renderer.drawBitmap(x, y, frames[(int) frameIndex]);
	}

	public void reset()
	{
		frameIndex = 0;
	}
}
