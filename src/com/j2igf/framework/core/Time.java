package com.j2igf.framework.core;

public class Time
{
	private double timeScale;
	private double deltaTime;

	private Time()
	{
		timeScale = 1;
		deltaTime = 0;
	}

	public static void create()
	{
		J2IGF.time = new Time();
	}

	public double getTimeScale()
	{
		return timeScale;
	}

	public void setTimeScale(double timeScale)
	{
		this.timeScale = timeScale;
	}

	public double getDeltaTime()
	{
		return deltaTime;
	}

	public void setDeltaTime(double deltaTime)
	{
		this.deltaTime = deltaTime;
	}
}
