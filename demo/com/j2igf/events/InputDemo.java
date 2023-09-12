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

package com.j2igf.events;

import com.j2igf.core.Context;
import com.j2igf.core.Engine;
import com.j2igf.core.Window;
import com.j2igf.event.KeyCode;

public final class InputDemo extends Context {

    /*
     * I will use these variables for this simple demo that demonstrates the input system.
     * Here, I have made a HUD that shows the input states of the mouse and keyboard.
     */
    private int mouseX, mouseY, lineOffset;
    private boolean up, down, left, right;
    private boolean mouseLeft, mouseRight;

    public InputDemo(Engine engine) {
        super(engine);
        lineOffset = 0;
        mouseX = mouseY = 0;
        up = down = left = right = false;
        mouseLeft = mouseRight = false;
    }

    public static void main(String[] args) {
        Engine engine = new Engine(new Window("Input Demo", 940, 720, 1), 60);
        engine.addContext(InputDemo.class);
        engine.start();
    }

    @Override
    public void fixedUpdate() {
        /*
         * Here, I am setting all the variables using the Input class object.
         * I am using the isKey() method to check if the key is being pressed.
         * This method also has a isKeyDown() variant that checks if the key just pressed.
         * And a isKeyUp() variant that checks if the key just released.
         */
        up = input.isKey(KeyCode.UP);
        down = input.isKey(KeyCode.DOWN);
        left = input.isKey(KeyCode.LEFT);
        right = input.isKey(KeyCode.RIGHT);

        /*
         * I am doing the same for the mouse buttons by calling the isButton() method.
         * This method also has a isButtonDown() variant that checks if the button just pressed.
         * And a isButtonUp() variant that checks if the button just released.
         */
        mouseLeft = input.isButton(1);
        mouseRight = input.isButton(3);

        /*
         * I am getting the mouse position using the getMouseX() and getMouseY() methods.
         * These return the mouse position in pixel coordinates relative to the window.
         */
        mouseX = input.getMouseX();
        mouseY = input.getMouseY();

        /*
         * I am getting the mouse scroll using the getScroll() method.
         * This returns -1 if the mouse wheel is scrolled up, and 1 if the mouse wheel is scrolled down.
         * Otherwise, if mouse wheel is not scrolled, it returns 0.
         */
        lineOffset = (lineOffset + input.getScroll() * 5) % 20;
    }

    @Override
    public void update() {
        renderer.clear(0xff263238);

        /*
         * Here, I am just rendering a HUD that represents the Arrow keys.
         * The fillRect() will only be called if the boolean is set to true.
         */
        renderer.drawRect(200, 100, 50, 50, 5, 0xff96ee00);
        if (up) renderer.fillRect(210, 110, 30, 30, 0xff96ee00);
        renderer.drawRect(140, 160, 50, 50, 5, 0xff96ee00);
        if (left) renderer.fillRect(150, 170, 30, 30, 0xff96ee00);
        renderer.drawRect(200, 160, 50, 50, 5, 0xff96ee00);
        if (down) renderer.fillRect(210, 170, 30, 30, 0xff96ee00);
        renderer.drawRect(260, 160, 50, 50, 5, 0xff96ee00);
        if (right) renderer.fillRect(270, 170, 30, 30, 0xff96ee00);
        renderer.drawText(185, 220, 0xff96ee00, "Arrow Keys");

        /*
         * Here,I am doing the same for left and right mouse buttons.
         * The fillRect() will only be called if the boolean is set to true.
         */
        renderer.fillRect(175, 460, 100, 100, 0xff96ee00);
        renderer.drawRect(175, 400, 50, 50, 5, 0xff96ee00);
        if (mouseLeft) renderer.fillRect(185, 410, 30, 30, 0xff96ee00);
        renderer.drawRect(225, 400, 50, 50, 5, 0xff96ee00);
        if (mouseRight) renderer.fillRect(235, 410, 30, 30, 0xff96ee00);
        renderer.drawText(175, 570, 0xff96ee00, "Mouse Buttons");

        /*
         * Here, I am rendering a Page like graphics that you can scroll using mouse wheel.
         * This is being controlled by the lineOffset variable.
         */
        renderer.drawRect(400, 100, 400, 460, 5, 0xff96ee00);
        for (int y = 120 + lineOffset; y < 550 + lineOffset; y += 20)
            renderer.drawLine(400, y, 800, y, 0xff96ee00);
        renderer.drawText(560, 570, 0xff96ee00, "Scroll Wheel");

        /*
         * Here, I am rendering a small square at the mouse position.
         * This is being controlled by the mouseX and mouseY variables.
         */
        renderer.fillRect(mouseX - 10, mouseY - 10, 20, 20, 0xff96ee00);
        renderer.drawText(mouseX - 50, mouseY + 20, 0xff96ee00, "Mouse Position");
    }
}
