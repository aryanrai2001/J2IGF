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
import com.j2igf.framework.graphics.Renderer;
import com.j2igf.framework.graphics.auxiliary.FontAtlas;
import com.j2igf.framework.graphics.visual.Sprite;

public class TargetSprite extends Context {

    /*
     * You can also use a renderer to target sprites.
     * This is useful when what you render is static, so you can avoid rendering every frame.
     * It is also helpful when you want to use sprite transformations on primitives.
     */
    private Sprite targetSprite;

    public TargetSprite(Engine engine) {
        super(engine);
    }

    public static void main(String[] args) {
        Engine engine = new Engine(new Window("Target Sprite", 800, 600, 1), 60);
        engine.addContext(TargetSprite.class);
        engine.start();
    }

    @Override
    public void init() {
        /*
         * First you need an empty sprite to render on.
         * Here, I create a 200x200 sprite.
         */
        targetSprite = new Sprite(200, 200);

        /*
         * Then you need a renderer to target the sprite.
         * You can instantiate a renderer, similar to how I did in BasicRenderer Demo.
         * But instead of passing the window to the constructor, you pass the target sprite.
         */
        Renderer spriteRenderer = new Renderer(targetSprite);
        spriteRenderer.enableAlphaBlending();

        /*
         * Now you can use the renderer to draw on the sprite.
         * The renderer works exactly the same as it does with the window.
         */
        for (int y = 0; y < 200; y++)
            for (int x = 0; x < 200; x++)
                targetSprite.setPixel(x, y, (~x ^ y) << 8);
        spriteRenderer.setFont(new FontAtlas("Calibri", 200, true));
        spriteRenderer.drawText(0, 0, 0xff0096ee, "42");

        /*
         * Once you have drawn the desired graphics on the sprite.
         * You can also transform it as you please.
         * Here, I have scaled it, and rotated it.
         */
        targetSprite.setScale(0.75f, 1f);
        targetSprite.setAngleInRadians((float) (3.14159265359));
        targetSprite.applyTransform();
        targetSprite = targetSprite.getTransformed();
    }

    @Override
    public void update() {
        renderer.clear(0xff263238);

        /*
         * Lastly, you can simply render the sprite as you would with any other sprite.
         * Or you can save the drawn sprite to a file using the saveToFile() method.
         * Example - targetSprite.saveToFile("[Path to directory]", "[File name with extension]");
         * But for this demo I will avoid any file I/O.
         */
        targetSprite.render(renderer, input.getMouseX(), input.getMouseY());
    }

    @Override
    public void fixedUpdate() {
    }
}
