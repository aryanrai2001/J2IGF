package com.j2igf.framework.core;

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
		if (J2IGF.window == null)
		{
			System.err.println("Engine requires the Window instance to initialize.");
			System.exit(-1);
		}
		J2IGF.debugMode = debugMode;
		Engine.create(desiredUPS, unlockFrameRate, debugMode);
	}

	public static void initRenderer()
	{
		if (J2IGF.window == null)
		{
			System.err.println("A Window must be created before Renderer.");
			System.exit(-1);
		}
		Renderer.create();
	}

	public static void initInput()
	{
		if (J2IGF.window == null)
		{
			System.err.println("Input requires the Engine instance to initialize.");
			System.exit(-1);
		}
		Input.create();
	}

	public static void update()
	{
		engine.update();
		if (input != null)
			input.update();
	}

	public static void render()
	{
		engine.render();
		window.updateFrame();
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
