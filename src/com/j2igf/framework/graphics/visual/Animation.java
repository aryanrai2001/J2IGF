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
import com.j2igf.framework.graphics.auxiliary.TileSet;

/**
 * This class is responsible for handling animations.
 *
 * @author Aryan Rai
 */
public class Animation {

    /**
     * The frames of the animation as Sprite objects.
     */
    private final Sprite[] frames;

    /**
     * The frames per second of the animation.
     */
    private final float fps;

    /**
     * The current frame index.
     */
    private float frameIndex;

    /**
     * This is the constructor of the Animation class.
     *
     * @param tileSet The TileSet object to use.
     * @param verticalScan Whether to scan the TileSet vertically or horizontally.
     * @param offset The offset of the first frame.
     * @param size The size of the animation.
     * @param fps The frames per second of the animation.
     */
    public Animation(TileSet tileSet, boolean verticalScan, int offset, int size, float fps) {
        if (tileSet == null) {
            Debug.logError(getClass().getName() + " -> TileSet instance can not be null!");
            System.exit(-1);
        }
        else if (offset + size > tileSet.getNumberOfTilesHorizontally() * tileSet.getNumberOfTilesVertically()) {
            Debug.logError(getClass().getName() + " -> Illegal arguments for Animation constructor!");
            System.exit(-1);
        }
        this.frameIndex = 0;
        this.fps = fps;
        this.frames = new Sprite[size];
        for (int i = 0; i < size; i++) {
            frames[i] = tileSet.getTile(offset + i, verticalScan);
        }
    }

    /**
     * This method updates the animation.
     *
     * @param deltaTime The time passed since the last frame.
     */
    public void update(float deltaTime) {
        frameIndex += fps * deltaTime;
        if (frameIndex >= frames.length)
            frameIndex = 0;
    }

    /**
     * This method renders the animation.
     *
     * @param renderer The Renderer object to use.
     * @param x The x coordinate of the animation.
     * @param y The y coordinate of the animation.
     */
    public void render(Renderer renderer, int x, int y) {
        frames[(int) frameIndex].render(renderer, x, y);
    }

    /**
     * This method resets the animation.
     */
    public void reset() {
        frameIndex = 0;
    }
}
