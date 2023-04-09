package com.j2igf.driver;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.J2IGF;
import com.j2igf.framework.event.Input;
import com.j2igf.framework.graphics.Renderer;
import com.j2igf.framework.graphics.visual.Sprite;

public class TestContext extends Context
{
	Sprite spr = new Sprite("res/images/banner.png");
	float angle;
	float x, y;
	float xx, yy;

	@Override
	public void init()
	{
		angle = 0;
		x = y = 1;
		xx = yy = 0.5f;
	}

	@Override
	public void update(Input input)
	{
		if (input.isKeyDown(Input.ESCAPE))
			J2IGF.removeCurrentContext();

		if (input.isKey(Input.UP))
			angle += 0.5f;
		if (input.isKey(Input.DOWN))
			angle -= 0.5f;

		if (input.isKey(Input.SHIFT))
		{
			if (input.isKey(Input.D))
				x += 0.005;
			if (input.isKey(Input.A))
				x -= 0.005;
			if (input.isKey(Input.S))
				y += 0.005;
			if (input.isKey(Input.W))
				y -= 0.005;
		}
		else
		{
			if (input.isKey(Input.D))
				xx += 0.005;
			if (input.isKey(Input.A))
				xx -= 0.005;
			if (input.isKey(Input.S))
				yy += 0.005;
			if (input.isKey(Input.W))
				yy -= 0.005;
		}

		spr.setAngleInDegrees(angle);
		spr.setScale(x, y);
		spr.setOrigin(xx, yy);
		spr.applyTransform();
	}

	@Override
	public void render(Renderer renderer)
	{
		renderer.clear(0);
		spr.renderTransformed(renderer, J2IGF.getInput().getMouseX(), J2IGF.getInput().getMouseY());
	}
}
