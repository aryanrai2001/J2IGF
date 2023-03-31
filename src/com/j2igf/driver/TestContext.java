package com.j2igf.driver;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.J2IGF;
import com.j2igf.framework.event.Input;
import com.j2igf.framework.graphics.Renderer;
import com.j2igf.framework.graphics.staticGFX.Image;

public class TestContext extends Context
{
	Image poster = new Image("res/images/poster.png");

	@Override
	public void init()
	{
	}

	@Override
	public void update(Input input)
	{
		if (input.isKeyDown(Input.ESCAPE))
			J2IGF.exit();
	}

	@Override
	public void render(Renderer renderer)
	{
		renderer.clear(0);
		renderer.drawBitmap(J2IGF.getInput().getMouseX(), J2IGF.getInput().getMouseY(), poster);
	}
}
