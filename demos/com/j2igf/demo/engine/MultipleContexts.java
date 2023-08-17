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

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.Engine;
import com.j2igf.framework.core.Window;
import com.j2igf.framework.event.KeyCode;
import com.j2igf.framework.graphics.auxiliary.FontAtlas;
import com.j2igf.framework.graphics.visual.Sprite;

public class MultipleContexts {

    public static void main(String[] args) {
        Engine engine = new Engine(new Window("Multiple Context", 940, 720, 1), 60);

        /*
         * You can add as many contexts as you want.
         * But remember the order in which you add the contexts, as it determines the index of each context.
         * The index start from 0 and goes up to the total number of contexts added.
         *
         * For example, here the index of Context1 is 0 and Context2 is 1.
         */
        engine.addContext(Context1.class);
        engine.addContext(Context2.class);

        engine.start();
    }

    /*
     * This is a simple demo that shows how to use multiple contexts.
     * I have created two contexts that are identical except for the background color and the text.
     * You can switch between the two contexts by pressing 1 and 2.
     * Each context is responsible for switching to any other context.
     *
     * Notice, that I have not extended the MultipleContexts class from Context.
     * For this demo I have created the two contexts as inner classes of the MultipleContexts.
     * You can also see that the constructor for both the Context1 and Context2 classes are private.
     * This is to demonstrate that contexts with private constructors will still work.
     */
    private static class Context1 extends Context {
        private Context1(Engine engine) {
            super(engine);

            int background = 0xFFFCF6F5;
            int foreground = 0xFF2BAE66;

            Sprite label = new FontAtlas("Calibri", window.getHeight() / 10, true)
                    .textToSprite("This is Context 1", foreground);
            int labelX = (window.getWidth() - label.getWidth()) / 2;
            int labelY = (window.getHeight() - label.getHeight()) / 2;

            Sprite subLabel = new FontAtlas("Calibri", window.getHeight() / 25, true)
                    .textToSprite("Press 2 to Switch", foreground);
            int subLabelX = (window.getWidth() - subLabel.getWidth()) / 2;
            int subLabelY = (window.getHeight() + label.getHeight()) / 2;

            renderer.clear(background);
            renderer.enableAlphaBlending();
            label.render(renderer, labelX, labelY);
            subLabel.render(renderer, subLabelX, subLabelY);
            renderer.disableAlphaBlending();
        }

        @Override
        public void update() {
            if (input.isKeyDown(KeyCode.KEY2)) {
                /*
                 * This is how you switch to another context.
                 * The switchContext() method takes in the index of the context that you want to switch to.
                 * Remember that each time you switch to any context, a new instance of that context is created.
                 */
                engine.switchContext(1);
            }
        }

        @Override
        public void fixedUpdate() {
        }
    }

    private static class Context2 extends Context {
        private Context2(Engine engine) {
            super(engine);

            int background = 0xFF2BAE66;
            int foreground = 0xFFFCF6F5;

            Sprite label = new FontAtlas("Calibri", window.getHeight() / 10, true)
                    .textToSprite("This is Context 2", foreground);
            int labelX = (window.getWidth() - label.getWidth()) / 2;
            int labelY = (window.getHeight() - label.getHeight()) / 2;

            Sprite subLabel = new FontAtlas("Calibri", window.getHeight() / 25, true)
                    .textToSprite("Press 1 to Switch", foreground);
            int subLabelX = (window.getWidth() - subLabel.getWidth()) / 2;
            int subLabelY = (window.getHeight() + label.getHeight()) / 2;

            renderer.clear(background);
            renderer.enableAlphaBlending();
            label.render(renderer, labelX, labelY);
            subLabel.render(renderer, subLabelX, subLabelY);
            renderer.disableAlphaBlending();
        }

        @Override
        public void fixedUpdate() {
        }

        @Override
        public void update() {
            if (input.isKeyDown(KeyCode.KEY1)) {
                /*
                 * The index must be greater than and equal to 0 and less than the
                 * total number of contexts added before starting the engine.
                 */
                engine.switchContext(0);
            }
        }
    }
}
