package com.j2igf.framework.event;

import com.j2igf.framework.core.Window;

import java.awt.event.*;

public final class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
    private static final int NUM_KEYS = 0xFF00;
    private static final int NUM_BUTTONS = 5;
    private final Window window;
    private boolean[] keys;
    private boolean[] lastKeys;
    private boolean[] buttons;
    private boolean[] lastButtons;
    private int mouseX, mouseY, scroll;

    public Input(Window window) {
        if (window == null) {
            Debug.logError(getClass().getName() + " -> Window instance can not be null!");
            System.exit(-1);
        }
        this.window = window;
        this.mouseX = window.getWidth() / 2;
        this.mouseY = window.getHeight() / 2;
        window.getCanvas().addKeyListener(this);
        window.getCanvas().addMouseListener(this);
        window.getCanvas().addMouseMotionListener(this);
        window.getCanvas().addMouseWheelListener(this);
        this.reset();
    }

    public void reset() {
        this.keys = new boolean[NUM_KEYS];
        this.lastKeys = new boolean[NUM_KEYS];
        this.buttons = new boolean[NUM_BUTTONS];
        this.lastButtons = new boolean[NUM_BUTTONS];
    }

    public void update() {
        System.arraycopy(keys, 0, lastKeys, 0, NUM_KEYS);
        System.arraycopy(buttons, 0, lastButtons, 0, NUM_BUTTONS);
        scroll = 0;
    }

    public boolean isKeyDown(int keyCode) {
        return !lastKeys[keyCode] && keys[keyCode];
    }

    public boolean isKey(int keyCode) {
        return keys[keyCode];
    }

    public boolean isKeyUp(int keyCode) {
        return lastKeys[keyCode] && !keys[keyCode];
    }

    public boolean isButtonDown(int button) {
        return !lastButtons[button] && buttons[button];
    }

    public boolean isButton(int button) {
        return buttons[button];
    }

    public boolean isButtonUp(int button) {
        return lastButtons[button] && !buttons[button];
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getScroll() {
        return scroll;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttons[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttons[e.getButton()] = false;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scroll = e.getWheelRotation();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX() / window.getPixelScale();
        mouseY = e.getY() / window.getPixelScale();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX() / window.getPixelScale();
        mouseY = e.getY() / window.getPixelScale();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
