package com.j2igf.driver;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.Input;
import com.j2igf.framework.core.J2IGF;
import com.j2igf.framework.graphics.bitmap.FontAtlas;
import com.j2igf.framework.graphics.bitmap.Sprite;

public class TestContext extends Context
{
	FontAtlas myFont = new FontAtlas("Times New Roman", 72, true);
	Sprite label1 = myFont.textToSprite("Hello", 0x55ff0000);
	Sprite label2 = myFont.textToSprite("World", 0x5500ff00);

	@Override
	public void init()
	{
		J2IGF.renderer.setFont(myFont);
	}

	@Override
	public void update()
	{
		if (J2IGF.input.isKeyDown(Input.KeyCode.ESCAPE))
			J2IGF.engine.stop(false);
	}

	@Override
	public void render()
	{
		J2IGF.renderer.clear(0);
		J2IGF.renderer.drawBitmap(50, 50, label1);
		J2IGF.renderer.drawBitmap(100, 75, label2);
	}

	@Override
	public void dispose()
	{

	}
}
