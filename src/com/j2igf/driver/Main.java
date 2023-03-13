package com.j2igf.driver;

import com.j2igf.framework.core.Window;

public class Main
{
	public static void main(String[] args)
	{
		Window window = new Window(640, 480, "Test");
		while (true)
		{
			window.update();
		}
	}
}
