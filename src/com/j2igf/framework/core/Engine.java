package com.j2igf.framework.core;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;
import javax.swing.JFrame;

public class Engine implements Runnable
{
	private final Stack<Context> contexts;
	private final Thread thread;
	private final int targetUPS;
	private final boolean unlockFrameRate, debugMode;
	private boolean running;

	private Engine(int targetUPS, boolean unlockFrameRate, boolean debugMode)
	{
		this.contexts = new Stack<>();
		this.thread = new Thread(this);
		this.targetUPS = targetUPS;
		this.unlockFrameRate = unlockFrameRate;
		this.debugMode = debugMode;
		this.running = false;
	}

	public static void create(int desiredUPS, boolean unlockFrameRate, boolean debugMode)
	{
		if (J2IGF.window == null)
		{
			System.err.println("A Window must be created before Engine.");
			System.exit(-1);
		}
		J2IGF.engine = new Engine(desiredUPS, unlockFrameRate, debugMode);
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
		J2IGF.window.updateFrame();
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
		int fps = 0;
		int ups = 0;
		long lastTime = System.nanoTime();
		long timer1 = System.currentTimeMillis(), timer2;
		double timeSlice = 1000000000.0 / targetUPS;
		double timeAccumulated = 0;
		boolean shouldRender;

		while (running)
		{
			long currentTime = System.nanoTime();
			long deltaTime = currentTime - lastTime;
			lastTime = currentTime;
			timeAccumulated += deltaTime;
			shouldRender = unlockFrameRate;
			while (timeAccumulated >= timeSlice)
			{
				J2IGF.update();
				ups++;
				timeAccumulated -= timeSlice;
				shouldRender = true;
			}
			if (shouldRender)
			{
				J2IGF.render();
				fps++;
			}
			else
			{
				try
				{
					Thread.sleep(1);
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}
			}

			timer2 = System.currentTimeMillis();
			if (timer2 - timer1 > 1000)
			{
				timer1 = timer2;
				if (debugMode)
					System.out.println(ups + " UPS | " + fps + " FPS");
				fps = 0;
				ups = 0;
			}
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
