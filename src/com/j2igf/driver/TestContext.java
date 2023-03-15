package com.j2igf.driver;

import com.j2igf.framework.core.Context;

public class TestContext extends Context
{
	@Override
	public void init()
	{
		System.out.println("Test Context Initialized");
	}

	@Override
	public void update()
	{
		System.out.println("Test Context Updating");
	}

	@Override
	public void render()
	{
		System.out.println("Test Context Rendering");
	}

	@Override
	public void dispose()
	{
		System.out.println("Test Context Disposed");
	}
}
