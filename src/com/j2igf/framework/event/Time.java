/*
 * Copyright (c) 2023, Aryan Rai
 *
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package com.j2igf.framework.event;

/**
 * This class is used to handle the Timing of the framework.
 * It is used to get the time stamp and delta time.
 * It is also used to get and set the timescale.
 *
 * @author Aryan Rai
 */
public final class Time {
    /**
     * The timescale of the framework.
     */
    private float timeScale;

    /**
     * The delta time of the framework.
     */
    private float deltaTime;

    /**
     * The time stamp of the framework.
     */
    private float timeStamp;

    /**
     * The constructor for the Time class.
     */
    public Time(){
        timeScale = 1;
        deltaTime = 0;
        timeStamp = 0;
    }

    /**
     * This method is used to update the time stamp.
     */
    public void update() {
        timeStamp += deltaTime;
    }

    /**
     * This method is used to get the timescale.
     *
     * @return The timescale as a float value.
     *         The default value is 1.
     */
    public float getTimeScale() {
        return timeScale;
    }

    /**
     * This method is used to set the timescale.
     *
     * @param timeScale The timescale as a float value.
     *                  The default value is 1.
     */
    public void setTimeScale(float timeScale) {
        this.timeScale = timeScale;
    }

    /**
     * This method is used to get the delta time.
     *
     * @return The delta time as a float value.
     *         The delta time is the time between the last frame and the current frame.
     */
    public float getDeltaTime() {
        return deltaTime;
    }

    /**
     * This method is used to set the delta time.
     * It is supposed to be used by the main loop of the framework.
     * And not by any other class.
     *
     * @param deltaTime The delta time as a float value.
     *                  The delta time is the time between the last frame and the current frame.
     */
    public void setDeltaTime(float deltaTime) {
        this.deltaTime = deltaTime;
    }

    /**
     * This method is used to get the time stamp.
     *
     * @return The time stamp as a float value.
     *         The time stamp is the time since the framework started.
     */
    public float getTimeStamp() {
        return timeStamp;
    }
}
