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

package com.j2igf.driver;

import com.j2igf.framework.core.Engine;
import com.j2igf.framework.core.Window;
import com.j2igf.framework.event.Input;
import com.j2igf.framework.event.Time;
import com.j2igf.framework.graphics.Renderer;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        Window window = new Window("Test", 1280, 720, 1);
        Renderer renderer = new Renderer(window);
        Input input = new Input(window);
        Time time = new Time();
        Engine engine = new Engine(window, renderer, input, time, 60);
        engine.start();
    }
}
