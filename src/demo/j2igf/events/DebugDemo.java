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

    /*
     * There are four log methods in the Debug class that can be used to log messages to the console.
     * Depending on the type of log, the message will be displayed appropriately.
     * The different types of log methods are discussed below.
     */
    public DebugDemo(Engine engine) {
        super(engine);
        renderer.clear(0xff263238);

        /*
         * The log() method simply prints the message to the console in white text.
         * This is intended to be used by the client that's using the framework for purposes like debugging, etc.
         */
        Debug.log("This is a debug log.");

        /*
         * The logInfo() method prints the message to the console in green text.
         * This is intended to be used by the framework to print information about the framework.
         */
        Debug.logInfo("This is an info log.");

        /*
         * The logWarning() method prints the message to the console in yellow text.
         * This is intended to be used by the framework to print warnings about the framework.
         */
        Debug.logWarning("This is a warning log.");

        /*
         * The logError() method prints the message to the console in red text.
         * This is intended to be used both by the framework and the client to print errors.
         * The errors will show-up in red.
         */
        Debug.logError("This is an error log.");

        /*
         * The logError() method has a variation that also prints the stack-trace information if provided.
         */
        Debug.logError("This is an error log with trace.", new Exception("Example!"));
        Debug.logError("This is an error log with null trace.", null);

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
