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

package com.j2igf.demo.sprite;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.Engine;
import com.j2igf.framework.core.Window;
import com.j2igf.framework.graphics.auxiliary.FontAtlas;
import com.j2igf.framework.graphics.visual.Sprite;

public class MoreSprite extends Context {
    private Sprite fontSprite, transformedSprite;

    public MoreSprite(Engine engine) {
        super(engine);
    }

    public static void main(String[] args) {
        Engine engine = new Engine(new Window("More Sprite", 800, 600, 1), 60);
        engine.addContext(MoreSprite.class);
        engine.start();
    }

    @Override
    public void init() {
        renderer.enableAlphaBlending();

        /*
         * The FontAtlas class is a method that will allow you to create a Sprite from a given string.
         * This is helpful when you want to render a string that doesn't change.
         * Rendering a Sprite of a string is a lot more efficient than the drawString().
         */
        FontAtlas atlas = new FontAtlas("Algerian", 256, true);
        fontSprite = atlas.textToSprite("42", 0xff962100);

        /*
         * The Sprite class has a method called getTransformed() that returns a new Sprite
         * that is a copy of the original one, but with the transformations applied.
         * This is useful when you don't want to transform a sprite every frame.
         * Rendering a Sprite that is already transformed is a lot more efficient than the renderTransformed().
         */
        fontSprite.setOrigin(0.5f, 0.5f);
        fontSprite.setScale(0.5f, 0.5f);
        fontSprite.setAngleInDegrees(45);
        transformedSprite = fontSprite.getTransformed();
    }

    @Override
    public void update() {
        renderer.clear(0xff263238);
        fontSprite.render(renderer, 250, 300);
        transformedSprite.render(renderer, 550, 300);
    }

    @Override
    public void fixedUpdate() {
    }
}
