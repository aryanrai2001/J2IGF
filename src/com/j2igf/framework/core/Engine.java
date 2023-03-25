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
		int fps = 0;
		int ups = 0;
		long lastTime = System.nanoTime();
		long timer1 = System.currentTimeMillis(), timer2;
		double timeSlice = 1000000000.0 / targetUPS;
		double timeAccumulated = 0;

		while (running)
		{
			long currentTime = System.nanoTime();
			long frameTime = currentTime - lastTime;
			double deltaTime = J2IGF.time.getTimeScale() * frameTime / 1000000000.0;
			lastTime = currentTime;
			timeAccumulated += frameTime;
			while (timeAccumulated >= timeSlice)
			{
				J2IGF.update(J2IGF.time.getTimeScale() * timeSlice / 1000000000.0);
				ups++;
				timeAccumulated -= timeSlice;
			}
			J2IGF.render(deltaTime);
			fps++;

			timer2 = System.currentTimeMillis();
			if (timer2 - timer1 > 1000)
			{
				timer1 = timer2;
				if (J2IGF.isDebugMode())
					J2IGF.window.getJFrame().setTitle(ups + " UPS | " + fps + " FPS");
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
