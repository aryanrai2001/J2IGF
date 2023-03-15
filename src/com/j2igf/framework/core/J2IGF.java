package com.j2igf.framework.core;

import com.j2igf.framework.graphics.Renderer;

public final class J2IGF
{
	public static Window window;
	public static Engine engine;
	public static Renderer renderer;
	public static void createWindow(int width, int height, String title)
	{
		window = new Window(width, height, title);
	}

	public static void createEngine(int desiredUPS, boolean unlockFrameRate, boolean debugMode)
	{
		engine = new Engine(desiredUPS, unlockFrameRate, debugMode);
	}

	public static void createRenderer()
	{
		renderer = new Renderer();
	}
}
