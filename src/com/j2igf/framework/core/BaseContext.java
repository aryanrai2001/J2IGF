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

public final class BaseContext extends Context {
    private float timer;

    public BaseContext(Engine engine) {
        super(engine);
    }

    @Override
    public void init() {
        Sprite label = new FontAtlas("Times New Roman", window.getHeight() / 10, true).textToSprite("No Context Available!", 0xFFFF0000);
        int labelX = (window.getWidth() - label.getWidth()) / 2;
        int labelY = (window.getHeight() - label.getHeight()) / 2;

        Sprite subLabel = new FontAtlas("Times New Roman", window.getHeight() / 25, true).textToSprite("Press ESCAPE to exit.", 0xFFaa0000);
        int subLabelX = (window.getWidth() - subLabel.getWidth()) / 2;
        int subLabelY = (window.getHeight() + label.getHeight()) / 2;

        renderer.enableAlphaBlending();
        label.render(renderer, labelX, labelY);
        subLabel.render(renderer, subLabelX, subLabelY);
        renderer.disableAlphaBlending();

        Debug.enableDebugMode();
    }

    @Override
    public void fixedUpdate() {
        if (input.isKeyDown(KeyCode.ESCAPE))
            engine.stop();
        timer += time.getDeltaTime();
    }

    @Override
    public void update() {
        if (timer >= 1) {
            Debug.renderMessage(renderer, "Fixed Frames Per Second: " + engine.getFfps() + "\nFrames Per Second: " + engine.getFps());
            timer = 0;
        }
    }
}