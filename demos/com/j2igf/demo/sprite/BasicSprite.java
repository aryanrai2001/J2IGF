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
import com.j2igf.framework.graphics.visual.Sprite;

public class BasicSprite extends Context {

    /*
     * Sprites are pre-fabricated visual entities that can simply be renderer to a render target.
     * They are not calculated in real time, rather they are loaded in memory beforehand.
     * Sprites can themselves be composed of other sprites or primitives,
     * as you can set a renderer to target a sprite and render to it.
     */
    private final Sprite sprite;

    /*
     * Sprites can also be rendered in a transformed state, i.e. with a different scale, rotation or origin.
     * I will use this variable to rotate the sprite.
     */
    private float angle;

    public BasicSprite(Engine engine) {
        super(engine);
        /*
         * The Sprite class provides many ways to initialize it into memory,
         * you can access them through its different constructors, those are as follows -
         * Sprite(Sprite) - Creates a copy of the given sprite.
         * Sprite(path) - Loads a sprite from the given image file path.
         * Sprite(width, height) - Creates an empty sprite with the given dimensions.
         *
         * Here, I will be looking at the last one, which creates an empty sprite.
         * You can try the others by yourself.
         */
        sprite = new Sprite(100, 100);

        /*
         * You can set individual pixels of the sprite using the setPixel() method.
         * Here, I have rendered a random pattern on the sprite.
         */
        for (int y = 0; y < 100; y++)
            for (int x = 0; x < 100; x++)
                sprite.setPixel(x, y, (~x ^ y) << 9);


        /*
         * You can also set transform properties on our sprites, such as scale, rotation and origin (pivot).
         * Transform properties are set using the following methods -
         * setOrigin(x, y) - Sets the origin of the sprite to the given coordinates.
         * setScale(x, y) - Sets the scale of the sprite to the given values.
         * setAngleInDegrees(angle) - Sets the rotation angle of the sprite to the given value in degrees.
         * setAngleInRadians(angle) - Sets the rotation angle of the sprite to the given value in radians.
         *
         * The origin of the sprite does not need the applyTransform() method to be reflected,
         * and it works for both render() and renderTransformed() methods.
         *
         * Note - You can set these properties during initialization or every frame, depending on
         *        whether the values are static or dynamic.
         */
        sprite.setOrigin(0.5f, 0.5f);
        sprite.setScale(2, 2);
    }

    public static void main(String[] args) {
        Engine engine = new Engine(new Window("Basic Sprite", 800, 600, 1), 60);
        engine.addContext(BasicSprite.class);
        engine.start();
    }

    @Override
    public void update() {
        renderer.clear(0xff263238);

        /*
         * For visual objects like Sprite, you simply call its render() method.
         * This method takes in a renderer and the x and y coordinates to render the sprite at.
         */
        sprite.render(renderer, 200, 300);

        /*
         * Here, I am setting the rotation angle of the sprite in update() method as it is dynamic.
         * otherwise I could have set it in the constructor and avoided calling applyTransform() every frame.
         */
        sprite.setAngleInDegrees(angle);
        angle = (angle + 0.05f) % 360;

        /*
         * Every time you change any transform property, you must apply the transform.
         * To apply the transform properties to the sprite, you can call the applyTransform() method.
         */
        sprite.applyTransform();

        /*
         * The default render() method will not show the transformed sprite,
         * you must call the renderTransformed() method to render the transformed sprite.
         * I will discuss the reasons for this design choice in a later demo.
         */
        sprite.renderTransformed(renderer, 550, 300);
    }

    @Override
    public void fixedUpdate() {
    }
}
