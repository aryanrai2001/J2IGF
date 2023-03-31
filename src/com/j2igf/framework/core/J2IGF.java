package com.j2igf.framework.core;

import com.j2igf.framework.event.Input;
import com.j2igf.framework.event.Time;
import com.j2igf.framework.graphics.Renderer;
import java.util.Stack;

public final class J2IGF
{
	public static final int FLAG_DEBUG_MODE = 0x1;
	public static final int FLAG_FULL_SCREEN = 0x2;

	private static final Stack<Context> contexts = new Stack<>();
	private static Window window;
	private static Engine engine;
	private static Renderer renderer;
	private static Input input;

	private static String title = "";
	private static int width = 0;
	private static int height = 0;
	private static int pixelScale = 1;
	private static int targetUPS = 60;
	private static int flags = FLAG_FULL_SCREEN;
	
	private static boolean initialized = false;

	private J2IGF()
	{
	}

	public static void initialize()
	{
		Window.create();
		Engine.create();
		Renderer.create();
		Input.create();
		initialized = true;
	}

	public static void addContext(Context context)
	{
		context.init();
		contexts.push(context);
	}

	public static void removeCurrentContext()
	{
		if (contexts.isEmpty())
			return;
		contexts.pop();
	}

	public static void update(double deltaTime)
	{
		Time.setDeltaTime(deltaTime);
		Time.update();

		if (!contexts.isEmpty())
			contexts.peek().update(input);

		if (input != null)
			input.update();
	}

	public static void render(double deltaTime)
	{
		Time.setDeltaTime(deltaTime);

		if (!contexts.isEmpty())
			contexts.peek().render(renderer);
	}

	public static void run()
	{
		engine.start();
	}

	public static void exit()
	{
		engine.stop();
	}

	public static String getTitle()
	{
		return title;
	}

	public static void setTitle(String title)
	{
		if (initialized)
			return;
		J2IGF.title = title;
	}

	public static int getWidth()
	{
		return width;
	}

	public static void setWidth(int width)
	{
		if (initialized)
			return;
		J2IGF.width = width;
		flags &= ~FLAG_FULL_SCREEN;
	}

	public static int getHeight()
	{
		return height;
	}

	public static void setHeight(int height)
	{
		if (initialized)
			return;
		J2IGF.height = height;
		flags &= ~FLAG_FULL_SCREEN;
	}

	public static int getPixelScale()
	{
		return pixelScale;
	}

	public static void setPixelScale(int pixelScale)
	{
		if (initialized)
			return;
		J2IGF.pixelScale = pixelScale;
	}

	public static int getTargetUPS()
	{
		return targetUPS;
	}

	public static void setTargetUPS(int targetUPS)
	{
		if (initialized)
			return;
		J2IGF.targetUPS = targetUPS;
	}

	public static void toggleFlags(int flags)
	{
		if (initialized)
			return;
		J2IGF.flags ^= flags;
	}

	public static Window getWindow()
	{
		return window;
	}

	public static void setWindow(Window window)
	{
		if (initialized)
			return;
		J2IGF.window = window;
	}

	public static Engine getEngine()
	{
		return engine;
	}

	public static void setEngine(Engine engine)
	{
		if (initialized)
			return;
		J2IGF.engine = engine;
	}

	public static Renderer getRenderer()
	{
		return renderer;
	}

	public static void setRenderer(Renderer renderer)
	{
		if (initialized)
			return;
		J2IGF.renderer = renderer;
	}

	public static Input getInput()
	{
		return input;
	}

	public static void setInput(Input input)
	{
		if (initialized)
			return;
		J2IGF.input = input;
	}

	public static boolean isDebugMode()
	{
		return (flags & FLAG_DEBUG_MODE) != 0;
	}

	public static boolean isFullScreen()
	{
		return (flags & FLAG_FULL_SCREEN) != 0;
	}
}
