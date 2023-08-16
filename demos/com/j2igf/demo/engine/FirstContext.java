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

package com.j2igf.demo.engine;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.Engine;
import com.j2igf.framework.core.Window;

public class FirstContext extends Context {

    private final int radius;
    /*
     * I will use these variables for this simple demo that shows a circle bouncing around the screen.
     * This is all encapsulated within a context that you will add to the engine.
     */
    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;

    /*
     * You need to provide a constructor that takes in an Engine instance and passes it to the super class.
     * This is because the super class needs to know which engine it is attached to.
     * It also populates the context with handles to framework components that help you interact with the engine.
     * These handles are:
     * 1. engine - The Engine instance that this context is attached to.
     * 2. window - The Window instance that the engine is hooked to.
     * 3. renderer - The Renderer instance that renders to the window.
     * 4. input - The Input instance that listens for input events on the window.
     *            I will discuss Input in detail later.
     * 5. time - The Time instance that manages the timing of the engine.
     *           I will discuss Time in detail later.
     *
     * You can use the constructor to initialize any variables or resources that you might need.
     */
    public FirstContext(Engine engine) {
        super(engine);
        xSpeed = 10;
        ySpeed = 10;
        radius = 100;
        x = (int) (Math.random() * (window.getWidth() - radius * 2)) + radius;
        y = (int) (Math.random() * (window.getHeight() - radius * 2)) + radius;
    }

    public static void main(String[] args) {
        Engine engine = new Engine(new Window("First Context", 940, 720, 1), 60);

        /*
         * This is how you add a context to the engine.
         * The addContext() must only be called before starting the engine.
         * Notice that this class extends Context, which is provided by the framework.
         * The Context class is an abstract class, so you need to implement the abstract methods.
         * These methods are update() and fixedUpdate().
         */
        engine.addContext(FirstContext.class);

        engine.start();
    }

    /*
     * This method is called every frame.
     * You typically use this method to update things in are context that don't need to be updated at a fixed rate.
     * For example, you can use this method to listen for input events, or we can use this for rendering to the window.
     */
    @Override
    public void update() {
        /*
         * Each context needs to clear the frame buffer before rendering.
         * Otherwise, you might see interesting but undesirable artifacts.
         */
        renderer.clear(0xff263238);

        renderer.fillCircle(x, y, radius, 0xff96ee00);
    }

    /*
     * This method is called every frame at a fixed rate.
     * So if the target FPS is set to 60, this method will be called 60 times per second.
     * You typically use this method to update things in our context that need to be updated at a fixed rate.
     * For example, you can use this method to update the physics of our context. Or you can use this method to update
     * the position of entities in our context.
     */
    @Override
    public void fixedUpdate() {
        if (x + xSpeed + radius > window.getWidth() || x + xSpeed - radius < 0) {
            xSpeed *= -1;
        }
        if (y + ySpeed + radius > window.getHeight() || y + ySpeed - radius < 0) {
            ySpeed *= -1;
        }
        x += xSpeed;
        y += ySpeed;
    }
}
