package com.j2igf.framework.core;

public class Time
{
	private double timeScale;
	private double deltaTime;
	private double timeStamp;

	private Time()
	{
		timeScale = 1;
		deltaTime = 0;
		timeStamp = 0;
	}

	public static void create()
	{
		J2IGF.time = new Time();
	}

	public void update()
	{
		timeStamp += deltaTime;
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

	public double getTimeStamp()
	{
		return timeStamp;
	}
}
