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

package com.j2igf.graphics.visual;

import com.j2igf.event.Debug;
import com.j2igf.graphics.Renderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * This class is responsible for handling sprites.
 *
 * @author Aryan Rai
 */
public class Sprite {
    /**
     * The pixels of the sprite.
     */
    protected int[] pixels;

    /**
     * The width of the sprite.
     */
    protected int width;

    /**
     * The height of the sprite.
     */
    protected int height;

    /**
     * The starting x coordinate of the sprite when using transformed rendering.
     */
    protected int transformedStartX;

    /**
     * The starting y coordinate of the sprite when using transformed rendering.
     */
    protected int transformedStartY;

    /**
     * The ending x coordinate of the sprite when using transformed rendering.
     */
    protected int transformedEndX;

    /**
     * The ending y coordinate of the sprite when using transformed rendering.
     */
    protected int transformedEndY;

    /**
     * The x coordinate of the origin of the sprite.
     */
    protected float originX;

    /**
     * The y coordinate of the origin of the sprite.
     */
    protected float originY;

    /**
     * The x scale of the sprite.
     */
    protected float scaleX;

    /**
     * The y scale of the sprite.
     */
    protected float scaleY;

    /**
     * The cosine of the rotation of the sprite.
     */
    protected float cos;

    /**
     * The sine of the rotation of the sprite.
     */
    protected float sin;

    /**
     * This is the default constructor of the Sprite class.
     */
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

    /**
     * This is the copy constructor of the Sprite class.
     *
     * @param sprite The sprite to copy.
     */
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

    /**
     * This is a parameterized constructor of the Sprite class.
     *
     * @param width  The width of the sprite.
     * @param height The height of the sprite.
     */
    public Sprite(int width, int height) {
        this();
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
    }

    /**
     * This is a parameterized constructor of the Sprite class.
     *
     * @param path The path of an image file to load as a Sprite.
     */
    public Sprite(String path) {
        this();

        if (path == null) {
            Debug.logError(getClass().getName() + " -> Invalid Path!");
            System.exit(-1);
        }

        BufferedImage image = null;
        String source = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(path)));
            source = "resource";
        } catch (IOException e1) {
            Debug.log(getClass().getName() + " -> Could not load image from resource: " + path);
            try {
                image = ImageIO.read(new File(path));
                source = "file";
            } catch (IOException e2) {
                Debug.log(getClass().getName() + " -> Could not load image from file: " + path);
            }
        }

        if (image == null) {
            Debug.logError(getClass().getName() + " -> Failed loading image!");
            System.exit(-1);
        } else {
            Debug.log(getClass().getName() + " -> Loaded image from " + source + ": " + path);
        }

        this.width = image.getWidth();
        this.height = image.getHeight();
        this.pixels = image.getRGB(0, 0, width, height, null, 0, width);
        image.flush();
    }

    /**
     * This method renders the sprite to the screen.
     *
     * @param renderer The renderer to use.
     * @param x        The x coordinate to render the sprite at.
     * @param y        The y coordinate to render the sprite at.
     */
    public void render(Renderer renderer, int x, int y) {
        if (renderer == null) {
            Debug.logError(getClass().getName() + " -> Renderer instance can not be null!");
            System.exit(-1);
        }
        x -= (int) (originX * width);
        y -= (int) (originY * height);
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

    /**
     * This method renders the sprite to the screen with a specific rotation and scale.
     *
     * @param renderer The renderer to use.
     * @param x        The x coordinate to render the sprite at.
     * @param y        The y coordinate to render the sprite at.
     */
    public void renderTransformed(Renderer renderer, int x, int y) {
        if (renderer == null) {
            Debug.logError(getClass().getName() + " -> Renderer instance can not be null!");
            System.exit(-1);
        }
        for (int currY = transformedStartY; currY < transformedEndY; currY++) {
            for (int currX = transformedStartX; currX < transformedEndX; currX++) {
                int xVal = (int) (((currX * cos - currY * sin) / scaleX) + width * originX);
                int yVal = (int) (((currX * sin + currY * cos) / scaleY) + height * originY);
                renderer.setPixel(x + currX, y + currY, getPixel(xVal, yVal));
            }
        }
    }

    /**
     * This method applies the current transformation to be reflected in transformed rendering.
     * Call this method after changing the transform values like scale, rotation, and origin.
     */
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

    /**
     * This method applies the current transformation to a copy of the sprite and returns it.
     *
     * @return The transformed sprite.
     */
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

    /**
     * This method saves the Sprite to a file.
     *
     * @param path The path to save the Sprite to.
     * @param name The name of the saved file.
     */
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

    /**
     * This method sets a pixel on the sprite to the specified color.
     *
     * @param x     The x coordinate of the pixel.
     * @param y     The y coordinate of the pixel.
     * @param color The color to set the pixel to.
     */
    public void setPixel(int x, int y, int color) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return;
        pixels[x + y * width] = color;
    }

    /**
     * This method gets the color of a pixel on the sprite.
     *
     * @param x The x coordinate of the pixel.
     * @param y The y coordinate of the pixel.
     * @return The color of the pixel.
     */
    public int getPixel(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return 0;
        return pixels[x + y * width];
    }

    /**
     * This method gets the pixels of the sprite as an int[].
     *
     * @return The pixel array of the sprite.
     */
    public int[] getPixels() {
        return pixels;
    }

    /**
     * This method gets the width of the sprite.
     *
     * @return The width of the sprite.
     */
    public int getWidth() {
        return width;
    }

    /**
     * This method gets the height of the sprite.
     *
     * @return The height of the sprite.
     */
    public int getHeight() {
        return height;
    }

    /**
     * This method sets the origin offsets of the sprite.
     *
     * @param oX The normalized x offset of the origin.
     * @param oY The normalized y offset of the origin.
     */
    public void setOrigin(float oX, float oY) {
        this.originX = oX;
        this.originY = oY;
    }

    /**
     * This method sets the scale of the sprite for the transformed rendering.
     *
     * @param scaleX The x scale of the sprite.
     * @param scaleY The y scale of the sprite.
     */
    public void setScale(float scaleX, float scaleY) {
        this.scaleX = Math.abs(scaleX);
        this.scaleY = Math.abs(scaleY);
    }

    /**
     * This method sets the rotation of the sprite for the transformed rendering.
     *
     * @param angleInRadians The angle of the sprite in radians.
     */
    public void setAngleInRadians(float angleInRadians) {
        angleInRadians = (float) Math.atan2(Math.sin(angleInRadians), Math.cos(angleInRadians));
        cos = (float) Math.cos(angleInRadians);
        sin = (float) Math.sin(angleInRadians);
    }

    /**
     * This method sets the rotation of the sprite for the transformed rendering.
     *
     * @param angleInDegrees The angle of the sprite in degrees.
     */
    public void setAngleInDegrees(float angleInDegrees) {
        setAngleInRadians((float) Math.toRadians(angleInDegrees));
    }
}
