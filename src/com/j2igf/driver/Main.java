package com.j2igf.driver;

import com.j2igf.framework.core.J2IGF;

public class Main
{
	public static void main(String[] args)
	{
		J2IGF.initWindow(126, 126, 7, "Test");
		J2IGF.initEngine(60, true, true);
		J2IGF.engine.addContext(new TestContext());
		J2IGF.engine.start();
	}
}
