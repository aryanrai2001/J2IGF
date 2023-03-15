package com.j2igf.framework.core;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;
import javax.swing.JFrame;

public class Engine implements Runnable
{
	private final Stack<Context> contexts;
	private final Window window;
	private final Thread thread;
	private final boolean unlockFrameRate;
	private final int desiredFPS;
	private boolean running;

	public Engine(Window window)
	{
		this.contexts = new Stack<>();
		this.window = window;
		this.thread = new Thread(this);
		this.running = false;
		unlockFrameRate = false;
		desiredFPS = 60;
		window.getJFrame().setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		window.getJFrame().addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				stop(false);
			}
		});
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
	}

	public void run()
	{
		int fps = 0;
		int ups = 0;
		long lastTime = System.nanoTime();
		long timer1 = System.currentTimeMillis(), timer2;
		double timeSlice = 1000000000.0 / desiredFPS;
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
				contexts.peek().update();
				ups++;
				timeAccumulated -= timeSlice;
				shouldRender = true;
			}
			if (shouldRender)
			{
				contexts.peek().render();
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

			window.updateFrame();

			timer2 = System.currentTimeMillis();
			if (timer2 - timer1 > 1000)
			{
				timer1 = timer2;
				System.out.println(ups + " UPS | " + fps + " FPS");
				fps = 0;
				ups = 0;
			}
		}
		window.getJFrame().dispose();
		System.exit(0);
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
