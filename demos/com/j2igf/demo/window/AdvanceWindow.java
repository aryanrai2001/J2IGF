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

package com.j2igf.demo.window;

import com.j2igf.framework.core.Window;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AdvanceWindow {

    /*
     * In this demo I have made a loop to update the window.
     * I will use this variable to manually terminate the loop.
     */
    private static boolean running = true;

    /*
     * This is a private constructor that prevents object creation from outside.
     */
    private AdvanceWindow() {
    }

    public static void main(String[] args) {
        Window window = new Window("Advance Window", 940, 720, 4);

        /*
         * The width and height of the window in pixels are adjusted based on the pixel scale.
         * You can get the actual width and height of the window by calling getWidth() and getHeight() respectively.
         */
        int width = window.getWidth();
        int height = window.getHeight();

        /*
         * Similarly, you can get the pixel scale by calling getPixelScale().
         * And the title by calling getTitle().
         * For this demo I won't be using them.
         * Instead, I will just retrieve the framebuffer and manipulate it.
         * You can get the frame buffer by calling getFrameBuffer().
         */
        int[] pixels = window.getFrameBuffer();

        /*
         * Here, I am setting a custom close operation for the window.
         * This will set running to false when the window is closed
         * You can set a custom close operation by calling setCustomCloseOperation().
         */
        window.setCustomCloseOperation(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                running = false;
            }
        });

        /*
         * Now you are free to manipulate the framebuffer as you wish.
         * However, the changes will be visible only when the framebuffer is updated.
         * For this demo I will be creating an animated pattern on the framebuffer.
         */
        float offsetVal = 0;
        while (running) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int index = x + y * width;
                    if (x < width / 2) {
                        if (y < height / 2)
                            pixels[index] = (x ^ y);
                        else
                            pixels[index] = (x ^ y) << 8;
                    } else {
                        if (y < height / 2)
                            pixels[index] = (x ^ y) << 16;
                        else {
                            int color = (x ^ y);
                            pixels[index] = color << (int) (offsetVal) % 24;
                        }
                    }
                }
            }
            offsetVal = offsetVal + 0.01f;

            /*
             * The window needs to constantly be updated in order to render properly.
             * Normally, the Engine takes care of this for us. But since I am not using the Engine,
             * I am updating the window manually. You will learn to use the Engine in later demos.
             * You can update the frame buffer by calling updateFrame().
             */
            window.updateFrame();
        }

        /*
         * The Engine also takes care of disposing the window for us.
         * But here I need to call the dispose() method manually.
         * This will dispose the window and free up resources to prevent memory leaks.
         */
        window.dispose();
    }
}
