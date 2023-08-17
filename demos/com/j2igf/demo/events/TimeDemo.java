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

package com.j2igf.demo.events;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.Engine;
import com.j2igf.framework.core.Window;
import com.j2igf.framework.graphics.auxiliary.FontAtlas;

public class TimeDemo extends Context {

    /*
     * I will use these variables for this simple demo that shows an arrow following a circumference.
     * It is frame-rate independent, and it speeds up and slows down as I change the timescale.
     */
    private final int size, radius;
    private final float speed;
    private int seconds;
    private float angle, timeDilation;

    public TimeDemo(Engine engine) {
        super(engine);
        size = 20;
        radius = 250;
        seconds = 0;
        angle = 0;
        speed = 1;
        timeDilation = 0.01f;
        renderer.setFont(new FontAtlas("Impact", 32, true));
        renderer.enableAlphaBlending();
    }

    public static void main(String[] args) {
        Engine engine = new Engine(new Window("Time Demo", 940, 720, 1), 60);
        engine.addContext(TimeDemo.class);
        engine.start();
    }

    @Override
    public void fixedUpdate() {
        /*
         * Notice I am using the time object to get the delta time, which is the time between frames.
         * You use the delta time to make the updates per frame into updates per unit time.
         * This way, the speed of the arrow will be frame rate independent.
         * The delta time is in seconds.
         *
         * Note - You can use the delta time in both the update and fixedUpdate methods.
         *        It will accurately represent the time between frames in both cases.
         */
        angle += speed * time.getDeltaTime();

        /*
         * You can also use the time object to get and set the timescale.
         * Changing the timescale essentially changes the speed of all the time dependent entities.
         * Here, I am simply using the getTimeScale() method to get the timescale.
         * Then I check if it is greater than 1.5 or less than 0.5, and change the timeDilation accordingly.
         * I add the timeDilation to the timeScale, and then set the new timeScale using the setTimeScale() method.
         */
        float timeScale = time.getTimeScale();
        if (timeScale >= 1.5f)
            timeDilation = -0.01f;
        else if (timeScale <= 0.5f)
            timeDilation = 0.01f;
        timeScale += timeDilation;
        time.setTimeScale(timeScale);

        /*
         * The getTimeStamp() method returns the time since the start of the program in seconds.
         */
        seconds = (int) time.getTimeStamp();
    }

    @Override
    public void update() {
        renderer.clear(0xff263238);
        renderer.drawCircle(window.getWidth() / 2, window.getHeight() / 2, radius, 0xff96ee00);

        int x0 = (int) (Math.cos(angle) * (radius - size)) + window.getWidth() / 2;
        int y0 = (int) (Math.sin(angle) * (radius - size)) + window.getHeight() / 2;
        int x1 = (int) (Math.cos(angle + (size / 1000f)) * (radius + size)) + window.getWidth() / 2;
        int y1 = (int) (Math.sin(angle + (size / 1000f)) * (radius + size)) + window.getHeight() / 2;
        int x2 = (int) (Math.cos(angle + (size / 100f)) * (radius)) + window.getWidth() / 2;
        int y2 = (int) (Math.sin(angle + (size / 100f)) * (radius)) + window.getHeight() / 2;

        renderer.fillTriangle(x0, y0, x1, y1, x2, y2, 0xff962121);
        renderer.drawText(window.getWidth() / 2 - 50, window.getHeight() / 2 - 20, 0xff2155b6, "Time: ");
        renderer.drawText(window.getWidth() / 2 + 30, window.getHeight() / 2 - 20, 0xff2155b6, Integer.toString(seconds));
    }
}
