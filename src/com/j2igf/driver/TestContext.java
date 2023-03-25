package com.j2igf.driver;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.J2IGF;
import com.j2igf.framework.core.Input;
import com.j2igf.framework.graphics.bitmap.Image;

public class TestContext extends Context
{
	Image sampleAlpha = new Image("res/images/sampleAlpha.png");
	Image poster = new Image("res/images/poster.png");
	int x, y;

	float alpha = -1;

	@Override
	public void init()
	{
		J2IGF.initInput();
		J2IGF.initRenderer(true);
		sampleAlpha.setOrigin(360, 360);
		poster.setOrigin(360, 360);
	}

	@Override
	public void update()
	{
		if (J2IGF.input.isKeyDown(Input.KeyCode.ESCAPE))
		{
			System.out.println("Escape Pressed Down!");
			J2IGF.engine.stop(true);
		}
		if (J2IGF.input.isKey(Input.KeyCode.W))
			y--;
		if (J2IGF.input.isKey(Input.KeyCode.A))
			x--;
		if (J2IGF.input.isKey(Input.KeyCode.S))
			y++;
		if (J2IGF.input.isKey(Input.KeyCode.D))
			x++;
		alpha += 0.01;
		if (alpha >= 1) alpha = -1;
	}

	@Override
	public void render()
	{
		J2IGF.renderer.clear(0);
		J2IGF.renderer.renderBitmap(500, 500, poster);
		J2IGF.renderer.useGlobalAlpha(Math.abs(alpha));
		J2IGF.renderer.renderBitmap(J2IGF.input.getMouseX(), J2IGF.input.getMouseY(), sampleAlpha);
		J2IGF.renderer.useLocalAlpha();
	}

	@Override
	public void dispose()
	{

	}
}
