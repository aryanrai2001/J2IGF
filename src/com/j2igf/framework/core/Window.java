package com.j2igf.framework.core;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Window
{
	private JFrame frame;
	private Canvas canvas;
	private BufferedImage image;
	private BufferStrategy strategy;
	private Graphics graphics;

	public Window(int width, int height, String title)
	{
		Dimension size = new Dimension(width, height);
		frame = new JFrame(title);
		canvas = new Canvas();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		canvas.setMinimumSize(size);
		canvas.setPreferredSize(size);
		canvas.setMaximumSize(size);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

	public void update()
	{
		graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
		strategy.show();
	}
}
