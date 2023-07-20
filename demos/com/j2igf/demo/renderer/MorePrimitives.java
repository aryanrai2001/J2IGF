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

package com.j2igf.demo.renderer;

import com.j2igf.framework.core.Window;
import com.j2igf.framework.graphics.Renderer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MorePrimitives {
    private static boolean running = true;

    public static void main(String[] args) {
        Window window = new Window("More Primitives", 800, 600, 2);
        window.setCustomCloseOperation(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                running = false;
            }
        });
        Renderer renderer = new Renderer(window);

        int width = window.getWidth();
        int height = window.getHeight();

        while (running) {
            renderer.clear(0xff263238);

            /*
             * By default, the renderer will not draw transparent pixels.
             * You will have to enable alpha blending to draw transparent pixels.
             * You can do that by calling enableAlphaBlending().
             */
            renderer.enableAlphaBlending();

            /*
             * There are more primitives to use.
             * You can even set individual pixels.
             * This demo will show the flexibility of the renderer.
             */
            renderer.drawTriangle(10, 10, width - 10, height - 10, 10, height - 10, 0xffc3e88d);
            renderer.drawLine(width / 2 - 70, height / 2 + 70, width / 2 + 70, height / 2 - 70, 0xfff07178);
            renderer.drawCircle(width / 2, height / 2, 100, 0xffc3e88d);

            /*
             * Here, I have passed the color value of 0x7f4d7aff.
             * Notice it starts with 0x7f, which means the alpha value is not 1 or 0.
             * It's somewhere in between, and so this circle will be transparent.
             */
            renderer.fillCircle(width / 2, height / 2, 80, 0x7f4d7aff);

            for (int i = 0; i < 500; i++) {
                int x = (int) (Math.random() * width);
                int y = (int) (Math.random() * height);
                renderer.setPixel(x, y, 0xffeeffff);
            }

            window.updateFrame();
        }
        window.dispose();
    }
}
