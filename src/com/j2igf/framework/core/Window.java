package com.j2igf.framework.core;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public final class Window {
    private final JFrame frame;
    private final Canvas canvas;
    private final BufferedImage image;
    private final BufferStrategy strategy;
    private final Graphics graphics;

    private Window() {
        frame = new JFrame(J2IGF.getTitle());
        canvas = new Canvas();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle rect;
        int screenWidth, screenHeight;

        boolean fullscreen = J2IGF.isFullScreen();
        if (fullscreen) {
            rect = ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
            screenWidth = rect.width;
            screenHeight = rect.height;
        } else {
            if (J2IGF.getWidth() <= 0 || J2IGF.getHeight() <= 0) {
                System.err.println("Width and Height must be a non-zero positive number.");
                System.exit(-1);
            }
            rect = ge.getMaximumWindowBounds();
            canvas.setSize(rect.width, rect.height);
            frame.add(canvas);
            frame.pack();
            screenWidth = Math.min(J2IGF.getWidth(), rect.width);
            screenHeight = Math.min(J2IGF.getHeight(), rect.height - frame.getInsets().top);
        }

        J2IGF.setWidth(screenWidth / J2IGF.getPixelScale());
        J2IGF.setHeight(screenHeight / J2IGF.getPixelScale());

        image = new BufferedImage(J2IGF.getWidth(), J2IGF.getHeight(), BufferedImage.TYPE_INT_RGB);

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

    public static void create() {
        J2IGF.setWindow(new Window());
    }

    public void updateFrame() {
        graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        strategy.show();
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

    public void dispose() {
        graphics.dispose();
        strategy.dispose();
        frame.dispose();
    }
}
