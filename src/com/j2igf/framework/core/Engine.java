package com.j2igf.framework.core;

import com.j2igf.framework.graphics.bitmap.FontAtlas;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;
import javax.swing.JFrame;

public class Engine implements Runnable
{
	private final Stack<Context> contexts;
	private final Thread thread;
	private final int targetUPS;
	private boolean running;

	private Engine(int targetUPS)
	{
		this.contexts = new Stack<>();
		this.thread = new Thread(this);
		this.targetUPS = targetUPS;
		this.running = false;
	}

	public static void create(int desiredUPS)
	{
		J2IGF.engine = new Engine(desiredUPS);
		J2IGF.window.getJFrame().setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		J2IGF.window.getJFrame().addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				if (!J2IGF.engine.running)
				{
					J2IGF.window.getJFrame().dispose();
					System.exit(0);
				}
				J2IGF.engine.stop(false);
			}
		});
	}

	public void update()
	{
		if (!contexts.isEmpty())
			contexts.peek().update();
	}

	public void render()
	{
		if (!contexts.isEmpty())
			contexts.peek().render();
	}

	public void addContext(Context context)
	{
		context.init();
		contexts.push(context);
	}

	public void removeCurrentContext()
	{
		if (contexts.isEmpty())
			return;
		contexts.pop().dispose();
	}

	public void start()
	{
		if (running)
			return;
		running = true;
		thread.start();
		try
		{
			thread.join();
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
		J2IGF.window.dispose();
		System.exit(0);
	}

	public void run()
	{
		int ups = 0, fps = 0;
		int updates = 0, frames = 0;
		long lastTime = System.nanoTime();
		double timeSlice = 1000000000.0 / targetUPS;
		double timeAccumulated = 0;
		double timer = 0;

		while (running)
		{
			long currentTime = System.nanoTime();
			long frameTime = currentTime - lastTime;
			lastTime = currentTime;
			timeAccumulated += frameTime;
			while (timeAccumulated >= timeSlice)
			{
				J2IGF.update(J2IGF.time.getTimeScale() * timeSlice / 1000000000.0);
				updates++;
				timeAccumulated -= timeSlice;
			}
			J2IGF.render(J2IGF.time.getTimeScale() * frameTime / 1000000000.0);
			frames++;

			if (J2IGF.isDebugMode())
			{
				if (timer > 1)
				{
					ups = updates;
					fps = frames;
					timer = updates = frames = 0;
				}
				timer += J2IGF.time.getDeltaTime();

				FontAtlas originalFont = J2IGF.renderer.getFont();
				J2IGF.renderer.setFont(FontAtlas.defaultFont);
				J2IGF.renderer.fillRect(0, 0, 125, 12, 0xff000000);
				J2IGF.renderer.drawText(0, 0, 0xffffff, ups + " UPS | " + fps + " FPS");
				J2IGF.renderer.setFont(originalFont);
			}

			J2IGF.window.updateFrame();
		}
	}

	public void stop(boolean disposeContexts)
	{
		if (!running)
			return;
		running = false;
		if (disposeContexts)
		{
			while (!contexts.isEmpty())
				removeCurrentContext();
		}
	}
}
