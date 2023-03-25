package com.j2igf.driver;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.J2IGF;
import com.j2igf.framework.core.Input;
import com.j2igf.framework.graphics.Animation;
import com.j2igf.framework.graphics.bitmap.Image;
import com.j2igf.framework.graphics.bitmap.TileSet;

public class TestContext extends Context
{
	Image img = new Image("res/images/coin.png");
	TileSet tileSet = new TileSet(img, 8, 1);
	Animation anim = new Animation(tileSet, 15);
	int x, y;

	@Override
	public void init()
	{
		J2IGF.initInput();
		J2IGF.initRenderer();
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
		anim.update();
	}

	@Override
	public void render()
	{
		J2IGF.renderer.clear(0);
		anim.render(J2IGF.input.getMouseX(), J2IGF.input.getMouseY());
	}

	@Override
	public void dispose()
	{

	}
}
