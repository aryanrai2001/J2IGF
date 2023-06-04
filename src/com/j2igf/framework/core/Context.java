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

public abstract class Context {
    protected final Window window;
    protected final Renderer renderer;
    protected final Input input;
    protected final Time time;
    protected final Engine engine;

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

    public abstract void init();

    public abstract void fixedUpdate();

    public abstract void update();
}
