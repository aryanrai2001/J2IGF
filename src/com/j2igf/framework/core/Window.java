package com.j2igf.framework.core;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

public class Window
{
	private final JFrame frame;
	private final Canvas canvas;
	private final BufferedImage image;
	private final BufferStrategy strategy;
	private final Graphics graphics;

	private Window(int width, int height, boolean fullscreen, String title)
	{
		frame = new JFrame(title);
		canvas = new Canvas();

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle rect;
		int screenWidth, screenHeight;

		if (fullscreen)
		{
			rect = ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
			screenWidth = rect.width;
			screenHeight = rect.height;
		}
		else
		{
			rect = ge.getMaximumWindowBounds();
			canvas.setSize(rect.width, rect.height);
			frame.add(canvas);
			frame.pack();
			screenWidth = Math.min(width * J2IGF.getRenderScale(), rect.width);
			screenHeight = Math.min(height * J2IGF.getRenderScale(), rect.height - frame.getInsets().top);
		}

		width = screenWidth / J2IGF.getRenderScale();
		height = screenHeight / J2IGF.getRenderScale();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Dimension size = new Dimension(screenWidth, screenHeight);
		canvas.setMinimumSize(size);
		canvas.setPreferredSize(size);
		canvas.setMaximumSize(size);
		canvas.setSize(size);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER);

		if (fullscreen)
		{
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
		}
		else
		{
			frame.pack();
			frame.setResizable(false);
			frame.setLocation((rect.width - frame.getWidth()) / 2, Math.max(0, (rect.height - frame.getHeight()) / 2));
		}

		frame.setVisible(true);
		canvas.setFocusable(true);
		canvas.requestFocusInWindow();
		canvas.createBufferStrategy(1);
		strategy = canvas.getBufferStrategy();
		graphics = strategy.getDrawGraphics();
	}

	public static void create(int width, int height, boolean fullscreen, String title)
	{
		J2IGF.window = new Window(width, height, fullscreen, title);
	}

	public void updateFrame()
	{
		graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
		strategy.show();
	}

	public int[] getFrameBuffer()
	{
		return ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}

	public JFrame getJFrame()
	{
		return frame;
	}

	public Canvas getCanvas()
	{
		return canvas;
	}

	public BufferedImage getImage()
	{
		return image;
	}

	public void dispose()
	{
		graphics.dispose();
		strategy.dispose();
		frame.dispose();
	}
}
