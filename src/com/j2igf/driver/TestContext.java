package com.j2igf.driver;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.J2IGF;
import com.j2igf.framework.event.Input;
import com.j2igf.framework.graphics.Renderer;
import com.j2igf.framework.graphics.visual.Sprite;

public class TestContext extends Context
{
	Sprite poster = new Sprite("res/images/poster.png");

	@Override
	public void init()
	{
	}

	@Override
	public void update(Input input)
	{
		if (input.isKeyDown(Input.ESCAPE))
			J2IGF.removeCurrentContext();
	}

	@Override
	public void render(Renderer renderer)
	{
		renderer.clear(0);
		poster.render(renderer, J2IGF.getInput().getMouseX(), J2IGF.getInput().getMouseY());
	}
}
