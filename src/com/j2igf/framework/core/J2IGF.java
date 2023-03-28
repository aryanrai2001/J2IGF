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

	public static class Data
	{
		public int width;
		public int height;
		public int renderScale;
		public int targetUPS;
		public boolean debugMode;
		public boolean alphaEnabled;
		public boolean fullscreen;
		public String title;

		public Data()
		{
			width = 1280;
			height = 720;
			renderScale = 1;
			targetUPS = 60;
			debugMode = false;
			alphaEnabled = true;
			fullscreen = false;
			title = "Untitled";
		}
	}

	private J2IGF()
	{
	}

	public static void initialize(Data data)
	{
		J2IGF.renderScale = data.renderScale;
		Window.create(data.width, data.height, data.fullscreen, data.title);
		J2IGF.width = window.getImage().getWidth();
		J2IGF.height = window.getImage().getHeight();

		J2IGF.debugMode = data.debugMode;
		Engine.create(data.targetUPS);
		Time.create();

		J2IGF.alphaEnabled = data.alphaEnabled;
		Renderer.create(alphaEnabled);

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
