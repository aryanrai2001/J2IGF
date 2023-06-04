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

package com.j2igf.framework.graphics.visual;

import com.j2igf.framework.event.Debug;
import com.j2igf.framework.graphics.Renderer;

public class Animator {
    private final Animation[] animations;
    private int currentAnimation;

    public Animator(int states) {
        if (states <= 1) {
            Debug.logError(getClass().getName() + " -> Illegal argument for Animator constructor!");
            System.exit(-1);
        }
        animations = new Animation[states];
        currentAnimation = 0;
    }

    public void update(float deltaTime) {
        animations[currentAnimation].update(deltaTime);
    }

    public void render(Renderer renderer, int x, int y) {
        animations[currentAnimation].render(renderer, x, y);
    }

    public void reset() {
        animations[currentAnimation].reset();
    }

    public void changeState(int index, boolean reset) {
        if (currentAnimation == index)
            return;
        if (index >= animations.length || index < 0) {
            Debug.logError(getClass().getName() + " -> Illegal arguments for Animation.changeState() method!");
            System.exit(-1);
        }
        currentAnimation = index;
        if (reset)
            reset();
    }

    public void setState(int index, Animation animation) {
        if (index >= animations.length || index < 0) {
            Debug.logError(getClass().getName() + " -> Illegal arguments for Animation.setState() method!");
            System.exit(-1);
        }
        animations[index] = animation;
    }
}
