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
import com.j2igf.graphics.visual.Sprite;

/**
 * A class that represents a Sprite sheet.
 *
 * @author Aryan Rai
 */
public class SpriteSheet {
    /**
     * The sprites in the sprite sheet.
     */
    private final Sprite[][] sprites;

    /**
     * The number of sprites horizontally.
     */
    private final int numberOfSpritesHorizontally;

    /**
     * The number of sprites vertically.
     */
    private final int numberOfSpritesVertically;

    /**
     * The width of a sprite.
     */
    private final int spriteWidth;

    /**
     * The height of a sprite.
     */
    private final int spriteHeight;

    /**
     * This is the constructor for the SpriteSheet class.
     *
     * @param src                         The source sprite.
     * @param numberOfSpritesHorizontally The number of sprites horizontally.
     * @param numberOfSpritesVertically   The number of sprites vertically.
     */
    public SpriteSheet(Sprite src, int numberOfSpritesHorizontally, int numberOfSpritesVertically) {
        super();
        if (src == null) {
            Debug.logError(getClass().getName() + " -> Sprite instance can not be null!");
            System.exit(-1);
        } else if (numberOfSpritesHorizontally * numberOfSpritesVertically <= 0
                || src.getWidth() % numberOfSpritesHorizontally != 0
                || src.getHeight() % numberOfSpritesVertically != 0) {
            Debug.logError(getClass().getName() + " -> Illegal arguments for SpriteSheet constructor!");
            System.exit(-1);
        }
        this.numberOfSpritesHorizontally = numberOfSpritesHorizontally;
        this.numberOfSpritesVertically = numberOfSpritesVertically;
        this.spriteWidth = src.getWidth() / numberOfSpritesHorizontally;
        this.spriteHeight = src.getHeight() / numberOfSpritesVertically;
        this.sprites = new Sprite[numberOfSpritesVertically][numberOfSpritesHorizontally];
        for (int y = 0; y < numberOfSpritesVertically; y++) {
            for (int x = 0; x < numberOfSpritesHorizontally; x++) {
                sprites[y][x] = new Sprite(spriteWidth, spriteHeight);
                sprites[y][x].setOrigin(0.5f, 0.5f);
                int xPos = x * spriteWidth;
                int yPos = y * spriteHeight;
                for (int i = 0; i < spriteHeight; i++) {
                    System.arraycopy(src.getPixels(), xPos + (yPos + i) * src.getWidth(),
                            sprites[y][x].getPixels(), i * spriteWidth, spriteWidth);
                }
            }
        }
    }

    /**
     * This method returns a sprite from the sprite sheet.
     *
     * @param index        The index of the sprite.
     * @param verticalScan If true, the sprite will be returned by scanning vertically.
     * @return The sprite at the specified index in the sprite sheet.
     */
    public Sprite getSprite(int index, boolean verticalScan) {
        if (index >= numberOfSpritesHorizontally * numberOfSpritesVertically)
            return null;
        if (verticalScan)
            return getSprite(index / numberOfSpritesHorizontally, index % numberOfSpritesHorizontally);
        else
            return getSprite(index % numberOfSpritesHorizontally, index / numberOfSpritesHorizontally);
    }

    /**
     * This method returns a sprite from the sprite sheet.
     *
     * @param x The x coordinate of the sprite.
     * @param y The y coordinate of the sprite.
     * @return The sprite at the specified coordinates in the sprite sheet.
     */
    public Sprite getSprite(int x, int y) {
        if (x < 0 || x > numberOfSpritesHorizontally || y < 0 || y > numberOfSpritesVertically)
            return null;
        return sprites[y][x];
    }

    /**
     * This method returns the number of sprites horizontally.
     *
     * @return The number of sprites horizontally.
     */
    public int getNumberOfSpritesHorizontally() {
        return numberOfSpritesHorizontally;
    }

    /**
     * This method returns the number of sprites vertically.
     *
     * @return The number of sprites vertically.
     */
    public int getNumberOfSpritesVertically() {
        return numberOfSpritesVertically;
    }

    /**
     * This method returns the width of a sprite.
     *
     * @return The width of a sprite.
     */
    public int getSpriteWidth() {
        return spriteWidth;
    }

    /**
     * This method returns the height of a sprite.
     *
     * @return The height of a sprite.
     */
    public int getSpriteHeight() {
        return spriteHeight;
    }
}
