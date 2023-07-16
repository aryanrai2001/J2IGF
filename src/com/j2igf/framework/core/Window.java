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

package com.j2igf.framework.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * This class is used to create a window and render to it.
 *
 * @author Aryan Rai
 */
public final class Window {
    /**
     * This is the JFrame object.
     */
    private final JFrame frame;
    /**
     * This is the Canvas object.
     */
    private final Canvas canvas;
    /**
     * This is the BufferedImage object.
     */
    private final BufferedImage image;
    /**
     * This is the BufferStrategy object.
     */
    private final BufferStrategy strategy;
    /**
     * This is the Graphics object.
     */
    private final Graphics graphics;
    /**
     * This is the String that stores the window Title.
     */
    private final String title;
    /**
     * This is the width of the window.
     */
    private final int width;
    /**
     * This is the height of the window.
     */
    private final int height;
    /**
     * This is the scale each pixel of the window.
     */
    private final int pixelScale;

    /**
     * This is the constructor of the Window class.
     * It creates a window with the specified title, width, height and pixelScale.
     *
     * @param title The title of the window.
     *              If null, the title will be set to "Untitled".
     * @param width The width of the window.
     *              If less than or equal to 0, the window will be fullscreen.
     *              If greater than the maximum width of the screen,
     *              the width will be set to the maximum width of the screen.
     * @param height The height of the window.
     *               If less than or equal to 0, the window will be fullscreen.
     *               If greater than the maximum height of the screen,
     *               the height will be set to the maximum height of the screen.
     * @param pixelScale The scale of each pixel of the window.
     *                   Example: If pixelScale is 2, each pixel will be 2x2 pixels.
     *                   If less than or equal to 0, the pixelScale will be set to 1.
     */
    public Window(String title, int width, int height, int pixelScale) {
        this.title = title == null ? "Untitled" : title;
        frame = new JFrame(title);
        canvas = new Canvas();

        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle rect;
        int screenWidth, screenHeight;

        boolean fullscreen = width <= 0 || height <= 0;
        if (fullscreen) {
            rect = graphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
            screenWidth = rect.width;
            screenHeight = rect.height;
        } else {
            rect = graphicsEnvironment.getMaximumWindowBounds();
            canvas.setSize(rect.width, rect.height);
            frame.add(canvas);
            frame.pack();
            screenWidth = Math.min(width, rect.width);
            screenHeight = Math.min(height, rect.height - frame.getInsets().top);
        }

        this.width = (screenWidth / pixelScale);
        this.height = (screenHeight / pixelScale);

        if (pixelScale <= 0) pixelScale = 1;
        this.pixelScale = pixelScale;

        image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);

        Dimension size = new Dimension(screenWidth, screenHeight);
        canvas.setMinimumSize(size);
        canvas.setPreferredSize(size);
        canvas.setMaximumSize(size);
        canvas.setSize(size);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);

        if (fullscreen) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
        } else {
            frame.pack();
            frame.setResizable(false);
            frame.setLocation((rect.width - frame.getWidth()) / 2, Math.max(0, (rect.height - frame.getHeight()) / 2));
        }

        frame.setVisible(true);
        canvas.setFocusable(true);
        canvas.requestFocusInWindow();
        canvas.createBufferStrategy(1);
        strategy = canvas.getBufferStrategy();
        graphics = strategy.getDrawGraphics();
    }

    /**
     * This method disposes the window.
     */
    public void dispose() {
        graphics.dispose();
        strategy.dispose();
        frame.dispose();
    }

    /**
     * This method draws the updated frame to the window.
     */
    public void updateFrame() {
        graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        strategy.show();
    }

    /**
     * This method sets the custom close operation of the window.
     *
     * @param closeOperation The custom close operation.
     */
    public void setCustomCloseOperation(WindowAdapter closeOperation) {
        assert closeOperation != null;
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.addWindowListener(closeOperation);
    }

    /**
     * This method gets the frame buffer of the window.
     *
     * @return The frame buffer of the window as an int[] that can be modified.
     */
    public int[] getFrameBuffer() {
        return ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    /**
     * This method gets the JFrame object.
     *
     * @return The JFrame object.
     */
    public JFrame getJFrame() {
        return frame;
    }

    /**
     * This method gets the Canvas object.
     *
     * @return The Canvas object.
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * This method gets the title of the window.
     *
     * @return The title as String.
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method gets the width of the window.
     *
     * @return The width of the window.
     */
    public int getWidth() {
        return width;
    }

    /**
     * This method gets the height of the window.
     *
     * @return The height of the window.
     */
    public int getHeight() {
        return height;
    }

    /**
     * This method gets the scale of each pixel of the window.
     *
     * @return The pixel scale of the window.
     */
    public int getPixelScale() {
        return pixelScale;
    }
}
