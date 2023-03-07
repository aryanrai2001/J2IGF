package com.j2igf.framework.core;

import javax.swing.JFrame;
import java.awt.Dimension;

public class Window extends JFrame
{
	public Window(int width, int height, int scale, String title)
	{
		super(title);
		Dimension frameSize = new Dimension(width * scale, height * scale);
		this.setResizable(false);
		this.setMinimumSize(frameSize);
		this.setPreferredSize(frameSize);
		this.setMinimumSize(frameSize);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}
