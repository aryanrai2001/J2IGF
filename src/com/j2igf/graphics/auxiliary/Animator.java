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

package com.j2igf.graphics.auxiliary;

import com.j2igf.event.Debug;
import com.j2igf.graphics.Renderer;
import com.j2igf.graphics.visual.Animation;

/**
 * This class is responsible for handling animation states.
 *
 * @author Aryan Rai
 */
public class Animator {
    /**
     * The animations of the animator.
     */
    private final Animation[] animations;

    /**
     * The current animation index.
     */
    private int currentAnimation;

    /**
     * This is the constructor of the Animator class.
     *
     * @param states The number of states of the animator.
     */
    public Animator(int states) {
        if (states <= 1) {
            Debug.logError(getClass().getName() + " -> Illegal argument for Animator constructor!");
            System.exit(-1);
        }
        animations = new Animation[states];
        currentAnimation = 0;
    }

    /**
     * This method is responsible for updating the animation.
     */
    public void update() {
        animations[currentAnimation].update();
    }

    /**
     * This method is responsible for rendering the animation.
     *
     * @param renderer The renderer to use.
     * @param x        The x coordinate to render the animation at.
     * @param y        The y coordinate to render the animation at.
     */
    public void render(Renderer renderer, int x, int y) {
        animations[currentAnimation].render(renderer, x, y);
    }

    /**
     * This method is responsible for resetting the animation.
     */
    public void reset() {
        animations[currentAnimation].reset();
    }

    /**
     * This method is responsible for changing the animation state.
     *
     * @param index The index of the animation state to change to.
     * @param reset Whether to reset the animation after changing the state.
     */
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

    /**
     * This method is responsible for setting or changing the animation state.
     *
     * @param index     The index of the animation state to set or change.
     * @param animation The animation to set or change the state to.
     */
    public void setState(int index, Animation animation) {
        if (index >= animations.length || index < 0) {
            Debug.logError(getClass().getName() + " -> Illegal arguments for Animation.setState() method!");
            System.exit(-1);
        }
        animations[index] = animation;
    }
}
