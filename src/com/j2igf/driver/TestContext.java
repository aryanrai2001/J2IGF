package com.j2igf.driver;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.J2IGF;
import com.j2igf.framework.event.Input;
import com.j2igf.framework.graphics.image.Image;
import com.j2igf.framework.graphics.image.Sprite;
import com.j2igf.framework.graphics.image.TileSet;

public class TestContext extends Context
{
	Image img = new Image("[INPUT_PATH]");
	TileSet tileSet = new TileSet(img, 10, 10);
	Sprite tile = tileSet.getTile(5, 9);
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
	}

	@Override
	public void render()
	{
		J2IGF.renderer.clear(0);
		J2IGF.renderer.renderBitmap(J2IGF.input.getMouseX(), J2IGF.input.getMouseY(), tile);
	}

	@Override
	public void dispose()
	{

	}
}
