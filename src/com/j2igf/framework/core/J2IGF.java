package com.j2igf.framework.core;

import com.j2igf.framework.graphics.Renderer;

public final class J2IGF
{
	public static Window window;
	public static Engine engine;
	public static Renderer renderer;
	private static int width, height, renderScale;

	public static void createWindow(int width, int height, int renderScale, String title)
	{
		J2IGF.width = width;
		J2IGF.height = height;
		J2IGF.renderScale = renderScale;
		Window.create(width, height, title);
	}

	public static void createEngine(int desiredUPS, boolean unlockFrameRate, boolean debugMode)
	{
		Engine.create(desiredUPS, unlockFrameRate, debugMode);
	}

	public static void createRenderer()
	{
		Renderer.create();
	}

	public static int getWidth()
	{
		return width;
	}

	public static int getHeight()
	{
		return height;
	}

	public static int getRenderScale()
	{
		return renderScale;
	}
}
