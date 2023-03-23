package com.j2igf.driver;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.J2IGF;
import com.j2igf.framework.event.Input;
import com.j2igf.framework.graphics.image.Bitmap;

public class TestContext extends Context
{

	Bitmap img = new Bitmap(1000, 1000);
	int x, y;
	@Override
	public void init()
	{
		// Drawing to bitmap using renderer
		J2IGF.initInput();
		J2IGF.initRenderer();
		J2IGF.renderer.setTarget(img);
		J2IGF.renderer.fillTriangle(100, 150, 250, 450, 600, 350, 0xffff0000);
		J2IGF.renderer.fillCircle(500, 500, 150, 0xff00ff00);
		J2IGF.renderer.fillRect( 500, 600, 800, 900, 0xff0000ff);
		J2IGF.renderer.resetTarget();

		//Drawing to bitmap manually using setPixel() method
		for(int i = 0; i < img.getWidth(); i++)
		{
			img.setPixel(i, i, 0xffffff00);
		}
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
		J2IGF.renderer.renderBitmap(x, y, img);
	}

	@Override
	public void dispose()
	{

	}
}
