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
import com.j2igf.framework.graphics.Renderer;
import com.j2igf.framework.graphics.auxiliary.FontAtlas;
import com.j2igf.framework.graphics.auxiliary.SpriteSheet;
import com.j2igf.framework.graphics.visual.Animation;
import com.j2igf.framework.graphics.visual.Sprite;

public class AnimationDemo extends Context {
    private SpriteSheet spriteSheet;
    private Animation animation;

    public AnimationDemo(Engine engine) {
        super(engine);
    }

    public static void main(String[] args) {
        Engine engine = new Engine(new Window("Animation Demo", 800, 600, 1), 60);
        engine.addContext(AnimationDemo.class);
        engine.start();
    }

    @Override
    public void init() {
        renderer.enableAlphaBlending();

        /*
         * For animation, you need a SpriteSheet that you either load from file or create yourself.
         * Here, I am rendering 13 frames on a Sprite to create our SpriteSheet.
         * These frames contain the text "TYPING EFFECT" with each frame having one more letter than the previous one.
         */
        Sprite sprite = new Sprite(13 * 200, 40);
        Renderer spriteRenderer = new Renderer(sprite);
        spriteRenderer.setFont(new FontAtlas("Comic Sans MS", 24, true));
        String text = "TYPING EFFECT";
        spriteRenderer.enableAlphaBlending();
        for (int i = 0; i < 13; i++) {
            spriteRenderer.drawText((i * 200) + 10, 10, 0xffffffff, text.substring(0, i + 1));
        }

        /*
         * I must create a SpriteSheet from the Sprite I just created, if I want to use it for Animation.
         */
        spriteSheet = new SpriteSheet(sprite, 13, 1);

        /*
         * The Animation class constructor takes the following parameters :-
         * 1. The SpriteSheet to extract animation frames
         * 2. The Time object for animation to get the delta time
         * 3. A boolean to tell whether the sprite sheet should be scanned vertically for frames
         * 4. The starting frame index
         * 5. The number of frames in the animation
         * 6. The speed of the animation in frames per second
         */
        animation = new Animation(spriteSheet, time, false, 0, 13, 10);
    }

    @Override
    public void update() {
        renderer.clear(0xff263238);

        /*
         * Here I am rendering the individual frames of the SpriteSheet for comparison.
         */
        for (int i = 0; i < 13; i++)
            spriteSheet.getSprite(i, false).render(renderer, 150, 50 + (i * 40));

        /*
         * Rendering the animation is as simple as rendering a Sprite.
         * But it won't update automatically.
         */
        animation.render(renderer, 550, 275);
    }

    @Override
    public void fixedUpdate() {
        /*
         * You must update the animation every frame to make it work.
         * It can be done in either update() or fixedUpdate().
         */
        animation.update();
    }
}
