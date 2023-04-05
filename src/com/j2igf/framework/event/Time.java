package com.j2igf.framework.event;

public class Time
{
	private static float timeScale = 1;
	private static float deltaTime = 0;
	private static float timeStamp = 0;

	private Time()
	{
	}

	public static void update()
	{
		timeStamp += deltaTime;
	}

	public static float getTimeScale()
	{
		return timeScale;
	}

	public static void setTimeScale(float timeScale)
	{
		Time.timeScale = timeScale;
	}

	public static float getDeltaTime()
	{
		return deltaTime;
	}

	public static void setDeltaTime(float deltaTime)
	{
		Time.deltaTime = deltaTime;
	}

	public static float getTimeStamp()
	{
		return timeStamp;
	}
}
