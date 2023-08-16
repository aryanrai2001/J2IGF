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

package com.j2igf.framework.event;

import com.j2igf.framework.core.Window;

import java.awt.*;
import java.awt.event.*;

/**
 * This class is used to handle all the input events.
 * It implements the following interfaces:
 * <ul>
 *     <li>{@link KeyListener}</li>
 *     <li>{@link MouseListener}</li>
 *     <li>{@link MouseMotionListener}</li>
 *     <li>{@link MouseWheelListener}</li>
 * </ul>
 *
 * @author Aryan Rai
 */
public final class Input implements KeyListener, MouseListener, MouseWheelListener {
    /**
     * The number of keys supported by the framework.
     */
    private static final int NUM_KEYS = 0xFF00;

    /**
     * The number of mouse buttons supported by the framework.
     */
    private static final int NUM_BUTTONS = 5;

    /**
     * Instance of the Window class.
     */
    private final Window window;

    /**
     * Array of booleans to store the state of the keys.
     */
    private boolean[] keys;

    /**
     * Arrays of booleans to store the state of the keys in the previous update and fixed update frames.
     */
    private boolean[] lastUpdateKeys, lastFixedKeys;

    /**
     * Array of booleans to store the state of the mouse buttons.
     */
    private boolean[] buttons;

    /**
     * Arrays of booleans to store the state of the mouse buttons in the previous update and fixed update frames.
     */
    private boolean[] lastUpdateButtons, lastFixedButtons;

    /**
     * Array variables that are dynamically assigned to the last update and fixed update arrays.
     */
    private boolean[] lastKeys, lastButtons;

    /**
     * Variables to store the coordinates and scroll state of the mouse.
     */
    private int mouseX, mouseY, scroll, scrollUpdate, scrollFixed;

    /**
     * This is the constructor of the Input class.
     *
     * @param window Instance of the Window class.
     */
    public Input(Window window) {
        if (window == null) {
            Debug.logError(getClass().getName() + " -> Window instance can not be null!");
            System.exit(-1);
        }
        this.window = window;
        window.getCanvas().addKeyListener(this);
        window.getCanvas().addMouseListener(this);
        window.getCanvas().addMouseWheelListener(this);
        this.reset();
    }

    /**
     * This method is used to reset the states of input.
     */
    public void reset() {
        this.keys = new boolean[NUM_KEYS];
        this.lastUpdateKeys = new boolean[NUM_KEYS];
        this.lastFixedKeys = new boolean[NUM_KEYS];
        this.lastKeys = null;
        this.buttons = new boolean[NUM_BUTTONS];
        this.lastUpdateButtons = new boolean[NUM_BUTTONS];
        this.lastFixedButtons = new boolean[NUM_BUTTONS];
        this.lastButtons = null;
    }

    /**
     * This method is used to assign the last frame arrays.
     *
     * @param fixed Boolean that is true if the last frame arrays are to be assigned to the fixed update arrays.
     */
    public void assignLastFrame(boolean fixed) {
        if (fixed) {
            lastKeys = lastFixedKeys;
            lastButtons = lastFixedButtons;
            scroll = scrollFixed;
        } else {
            lastKeys = lastUpdateKeys;
            lastButtons = lastUpdateButtons;
            scroll = scrollUpdate;
        }
    }

    /**
     * This method is used to set the last frame arrays for the update method.
     * It also updates the mouse position.
     */
    public void update() {
        Point windowLoc = window.getCanvas().getLocationOnScreen();
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        mouseX = (mousePos.x - windowLoc.x) / window.getPixelScale();
        mouseY = (mousePos.y - windowLoc.y) / window.getPixelScale();

        System.arraycopy(keys, 0, lastUpdateKeys, 0, NUM_KEYS);
        System.arraycopy(buttons, 0, lastUpdateButtons, 0, NUM_BUTTONS);
        scrollUpdate = 0;
    }

    /**
     * This method is used to set the last frame arrays for the fixed update method.
     * It also updates the mouse position.
     */
    public void fixedUpdate() {
        Point windowLoc = window.getCanvas().getLocationOnScreen();
        Point mousePos = MouseInfo.getPointerInfo().getLocation();
        mouseX = (mousePos.x - windowLoc.x) / window.getPixelScale();
        mouseY = (mousePos.y - windowLoc.y) / window.getPixelScale();

        System.arraycopy(keys, 0, lastFixedKeys, 0, NUM_KEYS);
        System.arraycopy(buttons, 0, lastFixedButtons, 0, NUM_BUTTONS);
        scrollFixed = 0;
    }

    /**
     * This method is used to query if a key is just pressed.
     *
     * @param keyCode The key code of the key.
     * @return Boolean that is true if the key is just pressed.
     */
    public boolean isKeyDown(int keyCode) {
        return !lastKeys[keyCode] && keys[keyCode];
    }

    /**
     * This method is used to query if a key is currently pressed.
     *
     * @param keyCode The key code of the key.
     * @return Boolean that is true if the key is currently pressed.
     */
    public boolean isKey(int keyCode) {
        return keys[keyCode];
    }

    /**
     * This method is used to query if a key is just released.
     *
     * @param keyCode The key code of the key.
     * @return Boolean that is true if the key is just released.
     */
    public boolean isKeyUp(int keyCode) {
        return lastKeys[keyCode] && !keys[keyCode];
    }

    /**
     * This method is used to query if a mouse button is just pressed.
     *
     * @param button The button code of the mouse button.
     * @return Boolean that is true if the mouse button is just pressed.
     */
    public boolean isButtonDown(int button) {
        return !lastButtons[button] && buttons[button];
    }

    /**
     * This method is used to query if a mouse button is currently pressed.
     *
     * @param button The button code of the mouse button.
     * @return Boolean that is true if the mouse button is currently pressed.
     */
    public boolean isButton(int button) {
        return buttons[button];
    }

    /**
     * This method is used to query if a mouse button is just released.
     *
     * @param button The button code of the mouse button.
     * @return Boolean that is true if the mouse button is just released.
     */
    public boolean isButtonUp(int button) {
        return lastButtons[button] && !buttons[button];
    }

    /**
     * This method is used to get the x coordinate of the mouse.
     *
     * @return The x coordinate of the mouse.
     */
    public int getMouseX() {
        return mouseX;
    }

    /**
     * This method is used to get the y coordinate of the mouse.
     *
     * @return The y coordinate of the mouse.
     */
    public int getMouseY() {
        return mouseY;
    }

    /**
     * This method is used to get the scroll state of the mouse.
     *
     * @return The scroll state of the mouse.
     */
    public int getScroll() {
        return scroll;
    }

    /**
     * This method is gets called when a key is pressed.
     * It has been overridden to set the key's state to true.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    /**
     * This method is gets called when a key is released.
     * It has been overridden to set the key's state to false.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    /**
     * This method is gets called when a key is typed.
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * This method is gets called when a mouse button is pressed.
     * It has been overridden to set the mouse button's state to true.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        buttons[e.getButton()] = true;
    }

    /**
     * This method is gets called when a mouse button is released.
     * It has been overridden to set the mouse button's state to false.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        buttons[e.getButton()] = false;
    }

    /**
     * This method is gets called when the mouse wheel is moved.
     * It has been overridden to set the scroll state of the mouse.
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        scrollUpdate = scrollFixed = e.getWheelRotation();
    }

    /**
     * This method is gets called when the mouse is clicked.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * This method is gets called when the mouse enters the window.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * This method is gets called when the mouse exits the window.
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }
}
