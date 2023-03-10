package com.j2igf.framework.core;

import javax.swing.JFrame;
import java.awt.Dimension;

public class Window
{
	private JFrame frame;
	public Window(int width, int height, String title)
	{
		Dimension frameSize = new Dimension(width, height);
		frame = new JFrame(title);
		frame.setResizable(false);
		frame.setMinimumSize(frameSize);
		frame.setPreferredSize(frameSize);
		frame.setMaximumSize(frameSize);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setFocusable(true);
		frame.requestFocusInWindow();
	}
}
