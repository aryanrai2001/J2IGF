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

package demo.j2igf.events;

import com.j2igf.core.Context;
import com.j2igf.core.Engine;
import com.j2igf.core.Window;
import com.j2igf.event.Debug;

public final class DebugDemo extends Context {
    public DebugDemo(Engine engine) {
        super(engine);
        renderer.clear(0xff263238);

        /*
         * You can log messages to the console using the following methods.
         * There are three types of logs: info, warning and error.
         * Depending on the type of log, the message will be displayed in a different color.
         */
        Debug.log("This is a info log.");
        Debug.logWarning("This is a warning log.");
        Debug.logError("This is a error log.");

        /*
         * You can also render debug info to the Window by using the renderMessage() method.
         */
        Debug.renderMessage("Print Debug Info Here.\nIt Supports Multi-line Messages.");

        /*
         * By default, the debug mode is enabled, as you won't be able to see the error logs.
         * You can disable the debug mode by using the Debug.disableDebugMode().
         * You can also enable it back by using the Debug.enableDebugMode().
         * It might not be needed in a single context application, but it is useful in multi-context applications.
         */
        Debug.disableDebugMode();
    }

    public static void main(String[] args) {
        Engine engine = new Engine(new Window("Debug Demo", 256, 128, 1), 60);
        engine.addContext(DebugDemo.class);
        engine.start();
    }

    @Override
    public void fixedUpdate() {
    }

    @Override
    public void update() {
    }
}
