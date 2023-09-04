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

package demo.j2igf.window;

import com.j2igf.core.Window;

public final class BasicWindow {

    /*
     * This is a private constructor that prevents object creation from outside.
     */
    private BasicWindow() {
    }

    public static void main(String[] args) {
        /*
         * A Window is the most basic component of the framework.
         * You need a window to render anything on to the screen.
         * Initially you can set the title, width, height, and pixel scale of the window.
         */
        String title = "Basic Window";
        int width = 800;
        int height = 600;
        int pixelScale = 1;

        /*
         * This is how you make a window.
         * You must always dispose a window when you are done with it.
         * But for this simple demo, I will avoid that to keep it simple.
         * In later demos, you will learn more about this.
         */
        new Window(title, width, height, pixelScale);
    }
}
