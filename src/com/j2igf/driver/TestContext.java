package com.j2igf.driver;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.J2IGF;

public class TestContext extends Context
{
	int x, y;
	@Override
	public void init()
	{
		x = y = 0;
		J2IGF.createRenderer();
	}

	@Override
	public void update()
	{
		x++;
		y++;
	}

	@Override
	public void render()
	{
		J2IGF.renderer.setPixels(x, y, 0xffffff);
	}

	@Override
	public void dispose()
	{

	}
}
