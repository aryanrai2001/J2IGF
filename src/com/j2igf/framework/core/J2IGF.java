package com.j2igf.framework.core;

import com.j2igf.framework.event.Input;
import com.j2igf.framework.event.Time;
import com.j2igf.framework.graphics.Renderer;
import com.j2igf.framework.graphics.staticGFX.FontAtlas;
import com.j2igf.framework.graphics.staticGFX.Sprite;
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
	private static final Context baseContext = new Context()
	{
		private Sprite label;
		private int x, y;

		@Override
		public void init()
		{
			label = new FontAtlas("Times New Roman", J2IGF.height / 10, true).textToSprite("No Context Available!", 0xFFFF0000);
			x = (J2IGF.width - label.getWidth()) / 2;
			y = (J2IGF.height - label.getHeight()) / 2;
		}

		@Override
		public void update(Input input)
		{
			if (input.isKeyDown(Input.ESCAPE))
				J2IGF.exit();
		}

		@Override
		public void render(Renderer renderer)
		{
			renderer.clear(0);
			renderer.enableAlphaBlending();
			renderer.drawBitmap(x, y, label);
			renderer.disableAlphaBlending();
		}
	};
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
		Input.create();
		renderer = new Renderer(window.getFrameBuffer(), width, height);
		initialized = true;
		addContext(baseContext);
	}

	public static void addContext(Context context)
	{
		assert context != null;
		context.init();
		contexts.push(context);
	}

	public static void removeCurrentContext()
	{
		if (contexts.peek().equals(baseContext))
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

	public static void showDebugInfo(String info)
	{
		int[] pixels = window.getFrameBuffer();
		int xOffset = 0;
		for (int i = 0; i < info.length(); i++)
		{
			int ch = info.charAt(i);
			int offset = FontAtlas.DEFAULT_FONT.getOffset(ch);
			int glyphWidth = FontAtlas.DEFAULT_FONT.getGlyphWidth(ch);
			for (int y = 0; y < FontAtlas.DEFAULT_FONT.getHeight(); y++)
			{
				for (int x = 0; x < glyphWidth; x++)
				{
					int fontAlpha = (FontAtlas.DEFAULT_FONT.getPixel(x + offset, y) >>> 24);
					if (fontAlpha == 0)
					{
						pixels[x + xOffset + y * width] = 0xff000000;
					}
					else
					{
						float alphaF = (float) fontAlpha / 0xff;
						pixels[x + xOffset + y * width] = 0xff000000 | (int) (alphaF * 0xff) << 16 | (int) (alphaF * 0xff) << 8 | (int) (alphaF * 0xff);
					}
				}
			}
			xOffset += glyphWidth;
		}
	}
}
