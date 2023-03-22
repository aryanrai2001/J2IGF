package com.j2igf.driver;

import com.j2igf.framework.core.J2IGF;

public class Main
{
	public static void main(String[] args)
	{
		J2IGF.initWindow(1000, 1000, 1, "Test");
		J2IGF.initEngine(60, false, false);
		J2IGF.engine.addContext(new TestContext());
		J2IGF.engine.start();
	}
}
