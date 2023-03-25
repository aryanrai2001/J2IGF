package com.j2igf.framework.graphics;

import com.j2igf.framework.core.J2IGF;
import com.j2igf.framework.graphics.bitmap.Sprite;
import com.j2igf.framework.graphics.bitmap.TileSet;

public class Animation
{
	private final Sprite[] frames;
	private final double fps;
	private float frameIndex;

	public Animation(TileSet tileSet, double fps)
	{
		this.frameIndex = 0;
		this.fps = fps;
		this.frames = new Sprite[tileSet.getNumTilesX() * tileSet.getNumTilesY()];
		for (int i = 0; i < frames.length; i++)
			frames[i] = tileSet.getTile(i);
	}

	public void update()
	{
		frameIndex += fps * J2IGF.time.getDeltaTime();
		if (frameIndex >= frames.length)
			frameIndex = 0;
	}

	public void render(int x, int y)
	{
		J2IGF.renderer.renderBitmap(x, y, frames[(int) frameIndex]);
	}
}
