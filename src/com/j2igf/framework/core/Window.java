package com.j2igf.framework.core;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
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

	public Window(int width, int height, String title)
	{
		Dimension size = new Dimension(width, height);
		frame = new JFrame(title);
		canvas = new Canvas();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		canvas.setMinimumSize(size);
		canvas.setPreferredSize(size);
		canvas.setMaximumSize(size);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setFocusable(true);
		frame.requestFocusInWindow();

		canvas.createBufferStrategy(2);
		strategy = canvas.getBufferStrategy();
		graphics = strategy.getDrawGraphics();
	}

	public void updateFrame()
	{
		graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
		strategy.show();
	}

	public int[] getFrameBuffer()
	{
		return ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	}

	public JFrame getJFrame()
	{
		return frame;
	}

	public Canvas getCanvas()
	{
		return canvas;
	}
}
