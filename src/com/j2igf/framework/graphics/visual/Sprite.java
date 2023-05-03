package com.j2igf.framework.graphics.visual;

import com.j2igf.framework.event.Debug;
import com.j2igf.framework.graphics.Renderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Sprite {
    protected int[] pixels;
    protected int width;
    protected int height;
    protected int transformedStartX;
    protected int transformedStartY;
    protected int transformedEndX;
    protected int transformedEndY;
    protected float originX;
    protected float originY;
    protected float scaleX;
    protected float scaleY;
    protected float cos;
    protected float sin;

    protected Sprite() {
        this.pixels = null;
        this.width = 0;
        this.height = 0;
        this.transformedStartX = 0;
        this.transformedStartY = 0;
        this.transformedEndX = 0;
        this.transformedEndY = 0;
        this.originX = 0;
        this.originY = 0;
        this.scaleX = 1;
        this.scaleY = 1;
        this.cos = 1;
        this.sin = 0;
    }

    public Sprite(Sprite sprite) {
        this.pixels = Arrays.copyOf(sprite.getPixels(), sprite.getPixels().length);
        this.width = sprite.width;
        this.height = sprite.height;
        this.transformedStartX = sprite.transformedStartX;
        this.transformedStartY = sprite.transformedStartY;
        this.transformedEndX = sprite.transformedEndX;
        this.transformedEndY = sprite.transformedEndY;
        this.originX = sprite.originX;
        this.originY = sprite.originY;
        this.scaleX = sprite.scaleX;
        this.scaleY = sprite.scaleY;
        this.cos = sprite.cos;
        this.sin = sprite.sin;
    }

    public Sprite(int width, int height) {
        this();
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
    }

    public Sprite(String path) {
        this();

        if (path == null) {
            Debug.logError(getClass().getName() + " -> Invalid Path!");
            System.exit(0);
        }

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            Debug.logError(getClass().getName() + " -> Image loading failed!", e);
        }

        if (image == null) {
            Debug.logError(getClass().getName() + " -> Could not load image file!");
            System.exit(0);
        }

        this.width = image.getWidth();
        this.height = image.getHeight();
        this.pixels = image.getRGB(0, 0, width, height, null, 0, width);
        image.flush();
    }

    public void render(Renderer renderer, int x, int y) {
        if (renderer == null) {
            Debug.logError(getClass().getName() + " -> Renderer instance can not be null!");
            System.exit(0);
        }
        x -= originX * width;
        y -= originY * height;
        int startX = 0, startY = 0;
        int endX = width, endY = height;

        if (x < 0)
            startX -= x;
        if (y < 0)
            startY -= y;
        if (x + endX > renderer.getWidth())
            endX = renderer.getWidth() - x;
        if (y + endY > renderer.getHeight())
            endY = renderer.getHeight() - y;

        for (int currY = startY; currY < endY; currY++) {
            for (int currX = startX; currX < endX; currX++) {
                renderer.setPixel(x + currX, y + currY, getPixel(currX, currY));
            }
        }
    }

    public void renderTransformed(Renderer renderer, int x, int y) {
        if (renderer == null) {
            Debug.logError(getClass().getName() + " -> Renderer instance can not be null!");
            System.exit(0);
        }
        for (int currY = transformedStartY; currY < transformedEndY; currY++) {
            for (int currX = transformedStartX; currX < transformedEndX; currX++) {
                int xVal = (int) (((currX * cos - currY * sin) / scaleX) + width * originX);
                int yVal = (int) (((currX * sin + currY * cos) / scaleY) + height * originY);
                renderer.setPixel(x + currX, y + currY, getPixel(xVal, yVal));
            }
        }
    }

    public Sprite getTransformed() {
        applyTransform();
        Sprite sprite = new Sprite(transformedEndX - transformedStartX, transformedEndY - transformedStartY);
        sprite.originX = 0.5f;
        sprite.originY = 0.5f;
        for (int currY = transformedStartY; currY < transformedEndY; currY++) {
            for (int currX = transformedStartX; currX < transformedEndX; currX++) {
                int xVal = (int) (((currX * cos - currY * sin) / scaleX) + width * originX);
                int yVal = (int) (((currX * sin + currY * cos) / scaleY) + height * originY);
                sprite.setPixel(Math.abs(transformedStartX) + currX, Math.abs(transformedStartY) + currY, getPixel(xVal, yVal));
            }
        }
        return sprite;
    }

    public void saveToFile(String path, String name) {
        File outputFile = new File(path + "/" + name + ".png");
        try {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            image.setRGB(0, 0, width, height, pixels, 0, width);
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            Debug.logError(getClass().getName() + " -> Image saving failed!", e);
        }
    }

    public void setPixel(int x, int y, int color) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return;
        pixels[x + y * width] = color;
    }

    public int getPixel(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return 0;
        return pixels[x + y * width];
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setOrigin(float oX, float oY) {
        this.originX = oX;
        this.originY = oY;
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = Math.abs(scaleX);
        this.scaleY = Math.abs(scaleY);
    }

    public void setAngleInRadians(float angleInRadians) {
        angleInRadians = (float) Math.atan2(Math.sin(angleInRadians), Math.cos(angleInRadians));
        cos = (float) Math.cos(angleInRadians);
        sin = (float) Math.sin(angleInRadians);
    }

    public void setAngleInDegrees(float angleInDegrees) {
        setAngleInRadians((float) Math.toRadians(angleInDegrees));
    }

    public void applyTransform() {
        float width = this.width * scaleX;
        float height = this.height * scaleY;

        if (Math.signum(cos) >= 0 && Math.signum(sin) >= 0) {
            transformedStartX = (int) ((width * -originX) * cos + (height * -originY) * sin);
            transformedEndX = (int) ((width * (1 - originX)) * cos + (height * (1 - originY)) * sin);
            transformedStartY = (int) ((width * (1 - originX)) * -sin + (height * -originY) * cos);
            transformedEndY = (int) ((width * -originX) * -sin + (height * (1 - originY)) * cos);
        } else if (Math.signum(cos) < 0 && Math.signum(sin) >= 0) {
            transformedStartX = (int) ((width * (1 - originX)) * cos + (height * -originY) * sin);
            transformedEndX = (int) ((width * -originX) * cos + (height * (1 - originY)) * sin);
            transformedStartY = (int) ((width * (1 - originX)) * -sin + (height * (1 - originY)) * cos);
            transformedEndY = (int) ((width * -originX) * -sin + (height * -originY) * cos);
        } else if (Math.signum(cos) < 0 && Math.signum(sin) < 0) {
            transformedStartX = (int) ((width * (1 - originX)) * cos + (height * (1 - originY)) * sin);
            transformedEndX = (int) ((width * -originX) * cos + (height * -originY) * sin);
            transformedStartY = (int) ((width * -originX) * -sin + (height * (1 - originY)) * cos);
            transformedEndY = (int) ((width * (1 - originX)) * -sin + (height * -originY) * cos);
        } else {
            transformedStartX = (int) ((width * -originX) * cos + (height * (1 - originY)) * sin);
            transformedEndX = (int) ((width * (1 - originX)) * cos + (height * -originY) * sin);
            transformedStartY = (int) ((width * -originX) * -sin + (height * -originY) * cos);
            transformedEndY = (int) ((width * (1 - originX)) * -sin + (height * (1 - originY)) * cos);
        }
    }
}
