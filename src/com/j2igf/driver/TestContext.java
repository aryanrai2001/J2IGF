package com.j2igf.driver;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.J2IGF;
import com.j2igf.framework.graphics.bitmap.FontAtlas;

public class TestContext extends Context
{
	FontAtlas myFont = new FontAtlas("Times New Roman", 72);

	@Override
	public void init()
	{
		J2IGF.initInput();
		J2IGF.initRenderer(true);
		J2IGF.renderer.setFont(myFont);
		FontAtlas.listAllFonts();
	}

	@Override
	public void update()
	{

	}

	@Override
	public void render()
	{
		J2IGF.renderer.clear(0);
		J2IGF.renderer.fillRect(J2IGF.input.getMouseX(), J2IGF.input.getMouseY(), 580, 90, 0xffffffff);
		J2IGF.renderer.drawRect(J2IGF.input.getMouseX(), J2IGF.input.getMouseY(), 580, 90, 10, 0xff00ff00);
		J2IGF.renderer.drawText(J2IGF.input.getMouseX() + 15, J2IGF.input.getMouseY() + 9, 0xffff0000, "Hello, World! - " + ((int) J2IGF.time.getTimeStamp()) % 100);
	}

	@Override
	public void dispose()
	{

	}
}
