package com.j2igf.framework.graphics.auxiliary;

import com.j2igf.framework.event.Debug;
import com.j2igf.framework.graphics.visual.Sprite;

public class TileSet extends Sprite {
    private int numberOfTilesHorizontally;
    private int numberOfTilesVertically;
    private int tileWidth;
    private int tileHeight;

    public TileSet(Sprite src, int numberOfTilesHorizontally, int numberOfTilesVertically) {
        super();
        if (src == null) {
            Debug.logError(getClass().getName() + " -> Sprite instance can not be null!");
            System.exit(-1);
        } else if ((numberOfTilesHorizontally <= 1 && numberOfTilesVertically <= 1)
                || (numberOfTilesHorizontally <= 0)
                || (numberOfTilesVertically <= 0)){
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

    public Sprite getTile(int index, boolean verticalScan) {
        if (index >= numberOfTilesHorizontally * numberOfTilesVertically)
            return null;
        if (verticalScan)
            return getTile(index / numberOfTilesHorizontally, index % numberOfTilesHorizontally);
        else
            return getTile(index % numberOfTilesHorizontally, index / numberOfTilesHorizontally);
    }

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

    public void setNumberOfTilesHorizontally(int numberOfTilesHorizontally) {
        if ((numberOfTilesHorizontally <= 1 && numberOfTilesVertically <= 1)
                || (numberOfTilesHorizontally <= 0)
                || (numberOfTilesVertically <= 0)){
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

    public int getNumberOfTilesHorizontally() {
        return numberOfTilesHorizontally;
    }

    public void setNumberOfTilesVertically(int numberOfTilesVertically) {
        if ((numberOfTilesHorizontally <= 1 && numberOfTilesVertically <= 1)
                || (numberOfTilesHorizontally <= 0)
                || (numberOfTilesVertically <= 0)){
            Debug.logError(getClass().getName() +
                    " -> Illegal arguments for TileSet.setNumberOfTilesVertically() method!");
            System.exit(-1);
        }
        this.numberOfTilesVertically = numberOfTilesVertically;
        this.tileHeight = height / numberOfTilesVertically;
        if (height % numberOfTilesVertically != 0){
            Debug.logError(getClass().getName() +
                    " -> Illegal arguments for TileSet.setNumberOfTilesVertically() method!");
            System.exit(-1);
        }
    }

    public int getNumberOfTilesVertically() {
        return numberOfTilesVertically;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }
}
