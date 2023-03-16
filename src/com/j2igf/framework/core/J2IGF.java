package com.j2igf.framework.core;

import com.j2igf.framework.graphics.Renderer;
import com.j2igf.framework.event.Input;

public final class J2IGF
{
	public static Window window;
	public static Engine engine;
	public static Renderer renderer;
	public static Input input;
	private static int width, height, renderScale;
	private static boolean debugMode;

	public static void initWindow(int width, int height, int renderScale, String title)
	{
		J2IGF.width = width;
		J2IGF.height = height;
		J2IGF.renderScale = renderScale;
		Window.create(width, height, title);
	}

	public static void initEngine(int desiredUPS, boolean unlockFrameRate, boolean debugMode)
	{
		J2IGF.debugMode = debugMode;
		Engine.create(desiredUPS, unlockFrameRate, debugMode);
	}

	public static void initRenderer()
	{
		Renderer.create();
	}

	public static void initInput()
	{
		Input.create();
	}

	public static void update()
	{
		if (input != null)
			input.update();
		engine.update();
	}

	public static void render()
	{
		engine.render();
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

	public static boolean isDebugMode()
	{
		return debugMode;
	}
}
