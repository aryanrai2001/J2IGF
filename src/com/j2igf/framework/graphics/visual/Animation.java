package com.j2igf.framework.graphics.visual;

import com.j2igf.framework.event.Debug;
import com.j2igf.framework.graphics.Renderer;
import com.j2igf.framework.graphics.auxiliary.TileSet;

public class Animation {
    private final Sprite[] frames;
    private final float fps;
    private float frameIndex;

    public Animation(TileSet tileSet, boolean verticalScan, int offset, int size, float fps) {
        if (tileSet == null) {
            Debug.logError(getClass().getName() + " -> TileSet instance can not be null!");
            System.exit(0);
        }
        else if (offset + size > tileSet.getNumberOfTilesHorizontally() * tileSet.getNumberOfTilesVertically()) {
            Debug.logError(getClass().getName() + " -> Illegal arguments for Animation constructor!");
            System.exit(0);
        }
        this.frameIndex = 0;
        this.fps = fps;
        this.frames = new Sprite[size];
        for (int i = 0; i < size; i++) {
            frames[i] = tileSet.getTile(offset + i, verticalScan);
        }
    }

    public void update(float deltaTime) {
        frameIndex += fps * deltaTime;
        if (frameIndex >= frames.length)
            frameIndex = 0;
    }

    public void render(Renderer renderer, int x, int y) {
        frames[(int) frameIndex].render(renderer, x, y);
    }

    public void reset() {
        frameIndex = 0;
    }
}
