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

public final class FullScreenWindow {
    private FullScreenWindow() {
    }

    public static void main(String[] args) {
        String title = "Title doesn't matter in Fullscreen";
        int pixelScale = 1;

        /*
         * A Window can be either bordered or fullscreen.
         * If you want to create a fullscreen window, you can set either width or height to 0.
         */
        int width = 0;
        int height = 0;

        /*
         * *** IMPORTANT ***
         * Remember to allow users to exit the program when creating a fullscreen window.
         * Some people might get confused and panic if they can't exit the program through the cross button.
         * Alternatively, you can press Alt + F4 to terminate.
         */
        new Window(title, width, height, pixelScale);
    }
}
