package com.j2igf.framework.core;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class Engine implements Runnable
{
	public static Engine instance;
	private final Thread thread;
	private final Window window;
	private final boolean unlockFrameRate;
	private final int desiredFPS;
	private boolean running;

	public Engine(Window window)
	{
		if (instance != null)
		{
			System.err.println("Multiple instances of Engine can not exist!");
			System.exit(-1);
		}
		else
		{
			instance = this;
		}
		this.thread = new Thread(this);
		this.window = window;
		this.running = false;
		unlockFrameRate = false;
		desiredFPS = 60;
	}

	public void setFrameCloseOperation(JFrame frame)
	{
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				stop();
				frame.dispose();
				System.exit(0);
			}
		});
	}

	public void start()
	{
		if (running)
			return;
		window.disposeFromEngine();
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

		// Init();

		while (running)
		{
			long currentTime = System.nanoTime();
			long deltaTime = currentTime - lastTime;
			lastTime = currentTime;
			timeAccumulated += deltaTime;
			shouldRender = unlockFrameRate;
			while (timeAccumulated >= timeSlice)
			{
				// Update()
				ups++;
				timeAccumulated -= timeSlice;
				shouldRender = true;
			}
			if (shouldRender)
			{
				// Render()
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
	}

	public void stop()
	{
		if (!running)
			return;
		running = false;
		System.out.println("Exiting!");
	}
}
