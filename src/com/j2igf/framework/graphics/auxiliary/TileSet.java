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

package com.j2igf.framework.graphics.auxiliary;

import com.j2igf.framework.event.Debug;
import com.j2igf.framework.graphics.visual.Sprite;

/**
 * A class that represents a tile set.
 *
 * @author Aryan Rai
 */
public class TileSet extends Sprite {
    /**
     * The number of tiles horizontally.
     */
    private int numberOfTilesHorizontally;

    /**
     * The number of tiles vertically.
     */
    private int numberOfTilesVertically;

    /**
     * The width of a tile.
     */
    private int tileWidth;

    /**
     * The height of a tile.
     */
    private int tileHeight;

    /**
     * This is the constructor for the TileSet class.
     *
     * @param src                       The source sprite.
     * @param numberOfTilesHorizontally The number of tiles horizontally.
     * @param numberOfTilesVertically   The number of tiles vertically.
     */
    public TileSet(Sprite src, int numberOfTilesHorizontally, int numberOfTilesVertically) {
        super();
        if (src == null) {
            Debug.logError(getClass().getName() + " -> Sprite instance can not be null!");
            System.exit(-1);
        } else if ((numberOfTilesHorizontally <= 1 && numberOfTilesVertically <= 1)
                || (numberOfTilesHorizontally <= 0)
                || (numberOfTilesVertically <= 0)) {
            Debug.logError(getClass().getName() + " -> Illegal arguments for TileSet constructor!");
            System.exit(-1);
        }
        this.pixels = src.getPixels();
        this.width = src.getWidth();
        this.height = src.getHeight();
        this.numberOfTilesHorizontally = numberOfTilesHorizontally;
        this.numberOfTilesVertically = numberOfTilesVertically;
        this.tileWidth = width / numberOfTilesHorizontally;
        this.tileHeight = height / numberOfTilesVertically;
        if (width % numberOfTilesHorizontally != 0 || height % numberOfTilesVertically != 0) {
            Debug.logError(getClass().getName() + " -> Illegal arguments for TileSet constructor!");
            System.exit(-1);
        }
    }

    /**
     * This method returns a tile from the tile set.
     *
     * @param index        The index of the tile.
     * @param verticalScan If true, the tile will be returned by scanning vertically.
     * @return The tile at the specified index in the tile set.
     */
    public Sprite getTile(int index, boolean verticalScan) {
        if (index >= numberOfTilesHorizontally * numberOfTilesVertically)
            return null;
        if (verticalScan)
            return getTile(index / numberOfTilesHorizontally, index % numberOfTilesHorizontally);
        else
            return getTile(index % numberOfTilesHorizontally, index / numberOfTilesHorizontally);
    }

    /**
     * This method returns a tile from the tile set.
     *
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return The tile at the specified coordinates in the tile set.
     */
    public Sprite getTile(int x, int y) {
        if (x < 0 || x > numberOfTilesHorizontally || y < 0 || y > numberOfTilesVertically)
            return null;
        int xPos = x * tileWidth;
        int yPos = y * tileHeight;
        Sprite tile = new Sprite(tileWidth, tileHeight);
        for (int i = 0; i < tileHeight; i++) {
            if (tileWidth >= 0)
                System.arraycopy(pixels, xPos + (yPos + i) * width, tile.getPixels(), i * tileWidth, tileWidth);
        }
        return tile;
    }

    /**
     * This method returns the number of tiles horizontally.
     *
     * @return The number of tiles horizontally.
     */
    public int getNumberOfTilesHorizontally() {
        return numberOfTilesHorizontally;
    }

    /**
     * This method sets the number of tiles horizontally.
     *
     * @param numberOfTilesHorizontally The number of tiles horizontally.
     */
    public void setNumberOfTilesHorizontally(int numberOfTilesHorizontally) {
        if ((numberOfTilesHorizontally <= 1 && numberOfTilesVertically <= 1)
                || (numberOfTilesHorizontally <= 0)
                || (numberOfTilesVertically <= 0)) {
            Debug.logError(getClass().getName() +
                    " -> Illegal arguments for TileSet.setNumberOfTilesHorizontally() method!");
            System.exit(-1);
        }
        this.numberOfTilesHorizontally = numberOfTilesHorizontally;
        this.tileWidth = width / numberOfTilesHorizontally;
        if (width % numberOfTilesHorizontally != 0) {
            Debug.logError(getClass().getName() +
                    " -> Illegal arguments for TileSet.setNumberOfTilesHorizontally() method!");
            System.exit(-1);
        }
    }

    /**
     * This method returns the number of tiles vertically.
     *
     * @return The number of tiles vertically.
     */
    public int getNumberOfTilesVertically() {
        return numberOfTilesVertically;
    }

    /**
     * This method sets the number of tiles vertically.
     *
     * @param numberOfTilesVertically The number of tiles vertically.
     */
    public void setNumberOfTilesVertically(int numberOfTilesVertically) {
        if ((numberOfTilesHorizontally <= 1 && numberOfTilesVertically <= 1)
                || (numberOfTilesHorizontally <= 0)
                || (numberOfTilesVertically <= 0)) {
            Debug.logError(getClass().getName() +
                    " -> Illegal arguments for TileSet.setNumberOfTilesVertically() method!");
            System.exit(-1);
        }
        this.numberOfTilesVertically = numberOfTilesVertically;
        this.tileHeight = height / numberOfTilesVertically;
        if (height % numberOfTilesVertically != 0) {
            Debug.logError(getClass().getName() +
                    " -> Illegal arguments for TileSet.setNumberOfTilesVertically() method!");
            System.exit(-1);
        }
    }

    /**
     * This method returns the width of a tile.
     *
     * @return The width of a tile.
     */
    public int getTileWidth() {
        return tileWidth;
    }

    /**
     * This method returns the height of a tile.
     *
     * @return The height of a tile.
     */
    public int getTileHeight() {
        return tileHeight;
    }
}
