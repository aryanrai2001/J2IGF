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

package demo.j2igf.renderer;

import com.j2igf.core.Window;
import com.j2igf.graphics.Renderer;
import com.j2igf.graphics.auxiliary.FontAtlas;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class TextDemo {
    private static boolean running = true;

    /*
     * This is a private constructor that prevents object creation from outside.
     */
    private TextDemo() {
    }

    public static void main(String[] args) {
        Window window = new Window("Text Demo", 800, 600, 1);
        window.setCustomCloseOperation(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                running = false;
            }
        });
        Renderer renderer = new Renderer(window);

        /*
         * You can use any font in this framework by creating a font atlas.
         * A font atlas needs a font style, font size, and  a flag that toggles antialiasing.
         * The font style can be any font that is installed on your system.
         *
         * Note - Antialiasing can only be used if Alpha is enabled.
         */
        FontAtlas fontAtlas1 = new FontAtlas("Calibri", 128, true);
        FontAtlas fontAtlas2 = new FontAtlas("Impact", 72, false);

        /*
         * Notice that without alpha blending, the text will look jagged.
         * So you must enable it when drawing text to allow antialiasing.
         */
        renderer.enableAlphaBlending();

        while (running) {
            renderer.clear(0xff263238);

            /*
             * You can set the font atlas of the renderer by calling setFont().
             * If you don't set the font atlas, the renderer will use the default font atlas.
             * You can access it using FontAtlas.DEFAULT_FONT.
             */
            renderer.setFont(fontAtlas1);
            renderer.drawText(70, 150, 0xff009688, "Hello World!");
            renderer.setFont(fontAtlas2);
            renderer.drawText(220, 300, 0xffff5370, "Bye World ;)");

            window.updateFrame();
        }
        window.dispose();
    }
}
