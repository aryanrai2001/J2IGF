package com.j2igf.driver;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.Input;
import com.j2igf.framework.core.J2IGF;
import com.j2igf.framework.core.Viewport;
import com.j2igf.framework.graphics.bitmap.FontAtlas;

public class TestContext extends Context
{
	int x = -500;
	int y = -500;
	Viewport myView1 = new Viewport(J2IGF.getWidth() - 200, J2IGF.getHeight() - 200);
	Viewport myView2 = new Viewport(J2IGF.getWidth(), J2IGF.getHeight());
	FontAtlas myFont = new FontAtlas("Times New Roman", 72);

	@Override
	public void init()
	{
		J2IGF.renderer.setFont(myFont);
		J2IGF.renderer.setViewport(myView2);
	}

	@Override
	public void update()
	{
		if (J2IGF.input.isKey(Input.KeyCode.W))
			y--;
		if (J2IGF.input.isKey(Input.KeyCode.A))
			x--;
		if (J2IGF.input.isKey(Input.KeyCode.S))
			y++;
		if (J2IGF.input.isKey(Input.KeyCode.D))
			x++;
		myView2.setPos(x, y);
	}

	@Override
	public void render()
	{
		J2IGF.renderer.clear(0xff);
		J2IGF.renderer.fillRect(0, 0, 580, 90, 0xffffffff);
		J2IGF.renderer.drawRect(0, 0, 580, 90, 10, 0xff00ff00);
		J2IGF.renderer.drawText(15, 9, 0xffff0000, "Hello, World! - " + ((int) J2IGF.time.getTimeStamp()) % 100);
	}

	@Override
	public void dispose()
	{

	}
}
