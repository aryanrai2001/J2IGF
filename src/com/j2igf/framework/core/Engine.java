package com.j2igf.framework.core;

import com.j2igf.framework.event.Time;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class Engine
{
	private final int targetUPS;
	private final Thread thread;
	private boolean running;

	private Engine()
	{
		Runnable loop = new Runnable()
		{
			@Override
			public void run()
			{
				int ups = 0, fps = 0;
				int updates = 0, frames = 0;
				long lastTime = System.nanoTime();
				float timeSlice = 1000000000.0f / targetUPS;
				float timeAccumulated = 0;
				float timer = 0;

				while (running)
				{
					long currentTime = System.nanoTime();
					long frameTime = currentTime - lastTime;
					lastTime = currentTime;
					timeAccumulated += frameTime;
					while (timeAccumulated >= timeSlice)
					{
						J2IGF.update(Time.getTimeScale() * timeSlice / 1000000000.0f);
						updates++;
						timeAccumulated -= timeSlice;
					}
					J2IGF.render(Time.getTimeScale() * frameTime / 1000000000.0f);
					frames++;

					if (J2IGF.isDebugMode())
					{
						if (timer > 1)
						{
							ups = updates;
							fps = frames;
							timer = updates = frames = 0;
						}
						timer += Time.getDeltaTime();
						J2IGF.showDebugInfo(ups + " UPS | " + fps + " FPS");
					}

					J2IGF.getWindow().updateFrame();
				}
			}
		};
		this.thread = new Thread(loop);
		this.targetUPS = J2IGF.getTargetUPS();
		this.running = false;
	}

	public static void create()
	{
		J2IGF.setEngine(new Engine());
		J2IGF.getWindow().getJFrame().setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		J2IGF.getWindow().getJFrame().addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				if (!J2IGF.getEngine().running)
				{
					J2IGF.getWindow().getJFrame().dispose();
					System.exit(0);
				}
				J2IGF.getEngine().stop();
			}
		});
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
		J2IGF.getWindow().dispose();
		System.exit(0);
	}

	public void stop()
	{
		if (!running)
			return;
		running = false;
	}
}
