package com.j2igf.framework.event;

public class Time
{
	private static double timeScale = 1;
	private static double deltaTime = 0;
	private static double timeStamp = 0;

	private Time() { }

	public static void update()
	{
		timeStamp += deltaTime;
	}

	public static double getTimeScale()
	{
		return timeScale;
	}

	public static void setTimeScale(double timeScale)
	{
		Time.timeScale = timeScale;
	}

	public static double getDeltaTime()
	{
		return deltaTime;
	}

	public static void setDeltaTime(double deltaTime)
	{
		Time.deltaTime = deltaTime;
	}

	public static double getTimeStamp()
	{
		return timeStamp;
	}
}
