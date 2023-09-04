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

package demo.j2igf.animation;

import com.j2igf.core.Context;
import com.j2igf.core.Engine;
import com.j2igf.core.Window;
import com.j2igf.event.KeyCode;
import com.j2igf.graphics.Renderer;
import com.j2igf.graphics.auxiliary.Animator;
import com.j2igf.graphics.auxiliary.SpriteSheet;
import com.j2igf.graphics.visual.Animation;
import com.j2igf.graphics.visual.Sprite;

public final class AnimatorDemo extends Context {
    private final Sprite frames;
    private final Animator animator;

    public AnimatorDemo(Engine engine) {
        super(engine);
        renderer.enableAlphaBlending();
        frames = new Sprite(400, 400);
        Renderer frameRenderer = new Renderer(frames);

        /*
         * Each draw call here specifies the leg positions for each frame of animation.
         * I am creating a 4 frame waling animation.
         */
        drawPlayerSprite(frameRenderer, 50, 50, 50);
        drawPlayerSprite(frameRenderer, 35, 60, 150);
        drawPlayerSprite(frameRenderer, 50, 50, 250);
        drawPlayerSprite(frameRenderer, 60, 35, 350);


        SpriteSheet spriteSheet = new SpriteSheet(frames, 4, 4);
        animator = new Animator(4);
        for (int i = 0; i < 4; i++)
            animator.setState(i, new Animation(spriteSheet, time, false, i * 4, 4, 7));
    }

    public static void main(String[] args) {
        Engine engine = new Engine(new Window("Animator Demo", 800, 600, 1), 60);
        engine.addContext(AnimatorDemo.class);
        engine.start();
    }

    /*
     * This is just a helper method that I have made to render a player sprite sheet.
     * It simply renders primitives that look like a top-down player sprite.
     * Then it does the same thing 4 times, each time rotating the sprite by 90 degrees.
     */
    private void drawPlayerSprite(Renderer frameRenderer, int leftLegY, int rightLegY, int sequenceX) {
        Sprite sprite = new Sprite(100, 100);
        Renderer spriteRenderer = new Renderer(sprite);
        spriteRenderer.clear(0);
        spriteRenderer.fillCircle(25, leftLegY, 15, 0xff492b12);
        spriteRenderer.fillCircle(75, rightLegY, 15, 0xff492b12);
        spriteRenderer.fillRect(75, 20, 15, 25, 0xfff9c492);
        spriteRenderer.fillRect(80, 10, 5, 15, 0xff000000);
        spriteRenderer.fillRect(0, 40, 100, 30, 0xfff17a4d);
        spriteRenderer.fillCircle(50, 45, 25, 0xff9c6231);
        for (int i = 0; i < 4; i++) {
            sprite.setAngleInDegrees(i * 90);
            sprite.applyTransform();
            sprite.getTransformed().render(frameRenderer, sequenceX, i * 100 + 50);
        }
    }

    @Override
    public void fixedUpdate() {

        /*
         * Here, I am just checking for the arrow keys and changing the animation state accordingly.
         * Changing the animation state is like switching to a different animation.
         * If no key is pressed, I am resetting the animator which reset the current animation state
         * to the first frame.
         */
        if (input.isKey(KeyCode.UP))
            animator.changeState(0, true);
        else if (input.isKey(KeyCode.LEFT))
            animator.changeState(1, true);
        else if (input.isKey(KeyCode.DOWN))
            animator.changeState(2, true);
        else if (input.isKey(KeyCode.RIGHT))
            animator.changeState(3, true);
        else
            animator.reset();

        /*
         * Updating the animator is similar to updating an animation.
         * It is required to update the animator every frame to make it work.
         */
        animator.update();
    }

    @Override
    public void update() {
        renderer.clear(0xff46a248);

        /*
         * Here, I am just rendering the entire sprite sheet I created with drawPlayerSprite() method.
         * It stores all the animation frames horizontally, and the animation states vertically.
         */
        frames.render(renderer, 300, 100);
        renderer.drawText(335, 530, 0xffffffff, "The sprite sheet we made with drawPlayerSprite()");

        /*
         * Rendering the animator is similar to rendering an animation or a sprite.
         */
        animator.render(renderer, 150, 300);
        renderer.drawText(105, 360, 0xffffffff, "The Animator");
    }
}
