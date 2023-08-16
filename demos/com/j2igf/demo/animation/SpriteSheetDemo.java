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

package com.j2igf.demo.animation;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.Engine;
import com.j2igf.framework.core.Window;
import com.j2igf.framework.graphics.auxiliary.SpriteSheet;
import com.j2igf.framework.graphics.visual.Sprite;

public class SpriteSheetDemo extends Context {
    /*
     * SpriteSheet is a class that allows you to extract individual Sprites from a SpriteSheet.
     * It can also be used to retrieve part of any regular sprite as long as it is of the desired dimensions.
     * You can use this to store related Sprites in a single Sprite and then extract them when needed.
     */
    private final Sprite sprite;
    private final SpriteSheet spriteSheet;

    public SpriteSheetDemo(Engine engine) {
        super(engine);
        renderer.enableAlphaBlending();

        /*
         * Normally, you would load a Sprite from a file, but for this example I will create one.
         * Here, I am rendering 4 different patterns on a single Sprite.
         */
        sprite = new Sprite(400, 100);
        for (int i = 0; i < 400; i += 100) {
            for (int y = 0; y < 100; y++) {
                for (int x = i; x < i + 100; x++) {
                    sprite.setPixel(x, y, (x ^ ~y) << (i / 10));
                }
            }
        }

        /*
         * To create a SpriteSheet you need a Sprite object that stores the sprite sheet,
         * and the number of sprites in that sheet horizontally and vertically.
         */
        spriteSheet = new SpriteSheet(sprite, 4, 1);
    }

    public static void main(String[] args) {
        Engine engine = new Engine(new Window("SpriteSheet Demo", 800, 600, 1), 60);
        engine.addContext(SpriteSheetDemo.class);
        engine.start();
    }

    @Override
    public void update() {
        renderer.clear(0xff263238);
        sprite.render(renderer, 100, 250);
        renderer.drawText(210, 360, 0xffffffff, "The Sprite sheet we made.");

        /*
         * To retrieve a Sprite from the SpriteSheet, you can use the getSprite() method.
         * There are two variants of this method, one that takes an index and one that takes the coordinates.
         * The method that takes the index also takes a boolean to specify whether to scan vertically or not,
         * then it returns the Sprite at that index, starting from the top left.
         */
        spriteSheet.getSprite(0, false).render(renderer, 650, 100);
        spriteSheet.getSprite(1, false).render(renderer, 650, 225);
        spriteSheet.getSprite(2, false).render(renderer, 650, 350);
        spriteSheet.getSprite(3, false).render(renderer, 650, 475);
        renderer.drawText(520, 535, 0xffffffff, "Individual Sprites from the sprite sheet.");
    }

    @Override
    public void fixedUpdate() {
    }
}
