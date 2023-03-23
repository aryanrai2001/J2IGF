package com.j2igf.driver;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.J2IGF;
import com.j2igf.framework.event.Input;
import com.j2igf.framework.graphics.image.Image;

public class TestContext extends Context
{
	Image img = new Image("[INPUT_PATH]");
	int x, y;

	@Override
	public void init()
	{
		// Drawing to Image using renderer
		J2IGF.initInput();
		J2IGF.initRenderer();
		J2IGF.renderer.setTarget(img);
		J2IGF.renderer.fillTriangle(100, 150, 250, 450, 600, 350, 0xffff0000);
		J2IGF.renderer.fillCircle(500, 500, 150, 0xff00ff00);
		J2IGF.renderer.fillRect(500, 600, 800, 900, 0xff0000ff);
		J2IGF.renderer.resetTarget();

		// Saving Image to file
		img.saveToFile("[OUTPUT_PATH]", "[FILENAME]");
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
