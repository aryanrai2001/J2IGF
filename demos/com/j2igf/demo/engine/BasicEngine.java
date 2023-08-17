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

import com.j2igf.framework.core.Engine;
import com.j2igf.framework.core.Window;

public class BasicEngine {

    /*
     * This is a private constructor that prevents object creation from outside.
     */
    private BasicEngine() {
    }

    public static void main(String[] args) {
        Window window = new Window("Basic Engine", 800, 600, 1);

        /*
         * The Engine manages the core loop of the program, and it manages a fixed update rate.
         * You have to specify the target updates per second for the fixed update rate of the engine.
         */
        int targetFixedFPS = 60;

        /*
         * The Engine needs a Window instance to be hooked to.
         * It automatically initializes a renderer and other components that you might need.
         * I am also providing a target fixed FPS for the engine to run at.
         */
        Engine engine = new Engine(window, targetFixedFPS);

        /*
         * You need to start the engine to enter the main loop.
         * Note - The Engine works on a state based architecture, so in order to run anything on this engine,
         *        you need to provide a state to the engine. In J2IGF, these states are referred to as "Contexts".
         *        You can add these contexts to the engine at any time, and it will keep stacking. Only the topmost
         *        context will be active. You can also remove the active context from the engine at any time.
         *        For this demo, I won't be adding any context to the engine, so the engine will be initialized
         *        with a base context.
         */
        engine.start();
    }
}
