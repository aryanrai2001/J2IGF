package com.j2igf.framework.graphics.visual;

import com.j2igf.framework.event.Time;
import com.j2igf.framework.graphics.Renderer;
import com.j2igf.framework.graphics.auxiliary.TileSet;

public class Animation
{
	private final Sprite[] frames;
	private final float fps;
	private float frameIndex;

	public Animation(TileSet tileSet, boolean verticalScan, int offset, int size, float fps)
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
		frameIndex += fps * Time.getDeltaTime();
		if (frameIndex >= frames.length)
			frameIndex = 0;
	}

	public void render(Renderer renderer, int x, int y)
	{
		frames[(int) frameIndex].render(renderer, x, y);
	}

	public void reset()
	{
		frameIndex = 0;
	}
}
