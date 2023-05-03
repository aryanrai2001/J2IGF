package com.j2igf.framework.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public final class Window {
    private final JFrame frame;
    private final Canvas canvas;
    private final BufferedImage image;
    private final BufferStrategy strategy;
    private final Graphics graphics;
    private final String title;
    private final int width;
    private final int height;
    private final int pixelScale;

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

    public void dispose() {
        graphics.dispose();
        strategy.dispose();
        frame.dispose();
    }

    public void updateFrame() {
        graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        strategy.show();
    }

    public void setCustomCloseOperation(WindowAdapter closeOperation) {
        assert closeOperation != null;
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.addWindowListener(closeOperation);
    }

    public int[] getFrameBuffer() {
        return ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    public JFrame getJFrame() {
        return frame;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPixelScale() {
        return pixelScale;
    }
}
