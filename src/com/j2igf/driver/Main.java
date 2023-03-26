package com.j2igf.driver;

import com.j2igf.framework.core.J2IGF;

public class Main
{
	public static void main(String[] args)
	{
		J2IGF.Data data = new J2IGF.Data();
		data.width = 1000;
		data.height = 1000;
		data.title = "MyIGF";
		data.debugMode = true;
		J2IGF.initialize(data);
		J2IGF.engine.addContext(new TestContext());
		J2IGF.engine.start();
	}
}
