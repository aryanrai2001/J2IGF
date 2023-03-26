package com.j2igf.framework.core;

public final class J2IGF
{
	public static Window window;
	public static Engine engine;
	public static Time time;
	public static Renderer renderer;
	public static Input input;
	private static int width, height, renderScale;
	private static boolean debugMode, alphaEnabled;

	public static void initWindow(int width, int height, int renderScale, String title)
	{
		J2IGF.width = width;
		J2IGF.height = height;
		J2IGF.renderScale = renderScale;
		Window.create(width, height, title);
	}

	public static void initEngine(int desiredUPS, boolean debugMode)
	{
		if (J2IGF.window == null)
		{
			System.err.println("Engine requires the Window instance to initialize.");
			System.exit(-1);
		}
		J2IGF.debugMode = debugMode;
		Engine.create(desiredUPS);
		Time.create();
	}

	public static void initRenderer(boolean alphaEnabled)
	{
		if (J2IGF.window == null)
		{
			System.err.println("A Window must be created before Renderer.");
			System.exit(-1);
		}
		J2IGF.alphaEnabled = alphaEnabled;
		Renderer.create(alphaEnabled);
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

	public static void update(double deltaTime)
	{
		time.setDeltaTime(deltaTime);
		time.update();
		engine.update();
		if (input != null)
			input.update();
	}

	public static void render(double deltaTime)
	{
		time.setDeltaTime(deltaTime);
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

	public static boolean isAlphaEnabled()
	{
		return alphaEnabled;
	}
}
