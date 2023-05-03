package com.j2igf.framework.event;

public final class Time {
    private float timeScale;
    private float deltaTime;
    private float timeStamp;

    public Time(){
        timeScale = 1;
        deltaTime = 0;
        timeStamp = 0;
    }

    public void update() {
        timeStamp += deltaTime;
    }

    public float getTimeScale() {
        return timeScale;
    }

    public void setTimeScale(float timeScale) {
        this.timeScale = timeScale;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(float deltaTime) {
        this.deltaTime = deltaTime;
    }

    public float getTimeStamp() {
        return timeStamp;
    }
}
