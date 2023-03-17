package com.j2igf.driver;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.J2IGF;
import com.j2igf.framework.event.Input;

public class TestContext extends Context
{

	@Override
	public void init()
	{
		J2IGF.initInput();
	}

	@Override
	public void update()
	{
		if (J2IGF.input.isKeyDown(Input.KeyCode.ESCAPE))
		{
			System.out.println("Escape Pressed Down!");
			J2IGF.engine.stop(true);
		}
	}

	@Override
	public void render()
	{

	}

	@Override
	public void dispose()
	{

	}
}
