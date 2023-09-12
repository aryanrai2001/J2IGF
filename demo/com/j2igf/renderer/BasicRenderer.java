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

package com.j2igf.renderer;

import com.j2igf.core.Window;
import com.j2igf.graphics.Renderer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class BasicRenderer {
    private static boolean running = true;

    /*
     * This is a private constructor that prevents object creation from outside.
     */
    private BasicRenderer() {
    }

    public static void main(String[] args) {
        Window window = new Window("Basic Renderer", 800, 600, 2);
        window.setCustomCloseOperation(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                running = false;
            }
        });

        /*
         * A Renderer is a utility class that can be used to draw graphics on a target buffer.
         * It can target either a window or a sprite. Here, I am going to directly render to the window.
         */
        Renderer renderer = new Renderer(window);

        while (running) {

            /*
             * You can clear the screen by calling clear() on renderer.
             * Here, I am passing a color to the Renderer as a 32-bit integer.
             * The color value is stored in the ARGB format (Alpha, Red, Green, Blue).
             * The alpha component determines the transparency of the color.
             * If alpha blending is enabled, it will work as expected. But if it is disabled, it will discard
             * the pixel if the alpha value is 0 and render it if the alpha value is anything above 0.
             *
             * Note - For the clear function, the alpha component is dependent on the target.
             *        If the target is a window, the alpha component of the resulting pixel is always 255.
             *        And if the target is a sprite, the alpha component of the resulting pixel is the same as input.
             *
             * Don't worry if you don't understand this. You will learn more about this later.
             */
            renderer.clear(0xff2B2D30);

            /*
             * You can use the renderer to draw primitives.
             * But it's possible to create complex graphics by combining primitives.
             * Here I am creating a simple battery graphic.
             */
            renderer.fillRect(105, 95, 200, 100, 0xff000000);
            renderer.fillRect(100, 90, 200, 100, 0xff1E1F22);
            renderer.fillRect(280, 130, 15, 20, 0xffffffff);
            renderer.drawRect(110, 100, 170, 80, 4, 0xffffffff);
            renderer.fillTriangle(120, 110, 120, 170, 180, 170, 0xffffffff);

            /*
             * You can draw text by calling drawText() on renderer.
             * By default, the font style and size are fixed.
             * But you will learn to change that later.
             */
            renderer.drawText(161, 211, 0xff000000, "Battery Status");
            renderer.drawText(160, 210, 0xffffffff, "Battery Status");

            window.updateFrame();
        }
        window.dispose();
    }
}
