package com.j2igf.framework.core;

public final class J2IGF
{
	public static Window window;
	public static Engine engine;
	public static Window createWindow(int width, int height, String title)
	{
		window = new Window(width, height, title);
		return window;
	}

	public static Engine createEngine(int desiredUPS, boolean unlockFrameRate, boolean debugMode)
	{
		if (window == null)
		{
			System.err.println("A Window must be created before Engine.");
			System.exit(-1);
		}
		engine = new Engine(desiredUPS, unlockFrameRate, debugMode);
		return engine;
	}
}
