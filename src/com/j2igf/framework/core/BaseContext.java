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

package com.j2igf.framework.core;

import com.j2igf.framework.event.Debug;
import com.j2igf.framework.event.KeyCode;
import com.j2igf.framework.graphics.auxiliary.FontAtlas;
import com.j2igf.framework.graphics.visual.Sprite;

/**
 * This is the default context that is used when no other context is available.
 * It displays a message on the screen and exits the game when the ESCAPE key is pressed.
 *
 * @author Aryan Rai
 */
public final class BaseContext extends Context {
    /**
     * This is the timer that is used to keep track of time to update the debug info every second.
     */
    private float timer;

    /**
     * This is the constructor for the BaseContext class.
     * It takes in an Engine object and passes it to the constructor of the base class Context.
     *
     * @param engine This is an instance of the Engine class.
     *               It can not be null.
     * @see Engine
     */
    public BaseContext(Engine engine) {
        super(engine);
    }

    /**
     * This method overrides the init method of the base class Context.
     *
     * @see Context#init()
     */
    @Override
    public void init() {
        int background = 0xFF1E272C;
        int foreground = 0xFFff5370;

        Sprite label = new FontAtlas("Calibri", window.getHeight() / 10, true)
                            .textToSprite("No Context Available!", foreground);
        int labelX = (window.getWidth() - label.getWidth()) / 2;
        int labelY = (window.getHeight() - label.getHeight()) / 2;

        Sprite subLabel = new FontAtlas("Calibri", window.getHeight() / 25, true)
                            .textToSprite("Press ESCAPE to exit.", foreground);
        int subLabelX = (window.getWidth() - subLabel.getWidth()) / 2;
        int subLabelY = (window.getHeight() + label.getHeight()) / 2;

        renderer.clear(background);
        renderer.enableAlphaBlending();
        label.render(renderer, labelX, labelY);
        subLabel.render(renderer, subLabelX, subLabelY);
        renderer.disableAlphaBlending();
    }

    /**
     * This method overrides the update method of the base class Context.
     *
     * @see Context#update()
     */
    @Override
    public void update() {
        if (timer >= 1) {
            Debug.enableDebugMode();
            Debug.renderMessage("Fixed Frames Per Second: " + engine.getFfps() +
                                "\nFrames Per Second: " + engine.getFps());
            timer = 0;
            Debug.disableDebugMode();
        }
    }

    /**
     * This method overrides the fixedUpdate method of the base class Context.
     *
     * @see Context#fixedUpdate()
     */
    @Override
    public void fixedUpdate() {
        if (input.isKeyDown(KeyCode.ESCAPE))
            engine.stop();
        timer += time.getDeltaTime();
    }
}