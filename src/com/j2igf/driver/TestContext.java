package com.j2igf.driver;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.J2IGF;
import java.awt.event.KeyEvent;

public class TestContext extends Context
{
	int x, y;

	@Override
	public void init()
	{
		x = y = 0;
		J2IGF.initInput();
		J2IGF.initRenderer();
	}

	@Override
	public void update()
	{
		if (J2IGF.input.isKey(KeyEvent.VK_W))
			y--;
		if (J2IGF.input.isKey(KeyEvent.VK_S))
			y++;
		if (J2IGF.input.isKey(KeyEvent.VK_A))
			x--;
		if (J2IGF.input.isKey(KeyEvent.VK_D))
			x++;
	}

	@Override
	public void render()
	{
		J2IGF.renderer.clear(0);
		J2IGF.renderer.setPixels(x, y, 0xffffff);
	}

	@Override
	public void dispose()
	{

	}
}
