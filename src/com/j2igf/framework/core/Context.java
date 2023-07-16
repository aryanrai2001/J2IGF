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

package com.j2igf.framework.core;

import com.j2igf.framework.event.Debug;
import com.j2igf.framework.event.Input;
import com.j2igf.framework.event.Time;
import com.j2igf.framework.graphics.Renderer;

/**
 * The Context class is the base class for all contexts.
 * <p>
 *     A context is a state of the game. For example, the main menu, the game, the pause menu, etc.
 *     Each context has its own init(), update() and fixedUpdate() methods.
 * </p>
 *
 * @author Aryan Rai
 */
public abstract class Context {
    /**
     * This is the object of the Window class.
     *
     * @see Window
     */
    protected final Window window;
    /**
     * This is the object of the Renderer class.
     *
     * @see Renderer
     */
    protected final Renderer renderer;
    /**
     * This is the object of the Input class.
     *
     * @see Input
     */
    protected final Input input;
    /**
     * This is the object of the Time class.
     *
     * @see Time
     */
    protected final Time time;
    /**
     * This is the object of the Engine class.
     *
     * @see Engine
     */
    protected final Engine engine;

    /**
     * This is the constructor for the Context class.
     * It takes in an Engine object and uses it to initialize all the fields of the class.
     *
     * @param engine This is an instance of the Engine class.
     *               It can not be null.
     * @see Engine
     */
    public Context(Engine engine) {
        if (engine == null) {
            Debug.logError(getClass().getName() + " -> Engine instance can not be null!");
            System.exit(-1);
        }
        this.window = engine.getWindow();
        this.renderer = engine.getRenderer();
        this.input = engine.getInput();
        this.time = engine.getTime();
        this.engine = engine;
    }

    /**
     * This method is called when the context is created.
     * It is used to initialize the context.
     * And it is called only once.
     */
    public abstract void init();

    /**
     * This method is called every frame.
     * It is used to update the context.
     */
    public abstract void update();

    /**
     * This method is called every frame at a fixed time step.
     * It is used to update the context at a fixed rate.
     */
    public abstract void fixedUpdate();
}
