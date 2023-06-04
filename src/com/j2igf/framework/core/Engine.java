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

import com.j2igf.framework.event.Debug;
import com.j2igf.framework.event.Input;
import com.j2igf.framework.event.Time;
import com.j2igf.framework.graphics.Renderer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

public final class Engine {
    private final Stack<Context> contexts;
    private final Window window;
    private final Renderer renderer;
    private final Input input;
    private final Time time;
    private final Thread thread;
    private final int targetUPS;
    private boolean running;
    private short fps;
    private short ffps;

    public Engine(Window window, Renderer renderer, Input input, Time time, int targetUPS) {
        Debug.init();
        if (window == null) {
            Debug.logError(getClass().getName() + " -> Window instance can not be null!");
            System.exit(-1);
        } else if (renderer == null) {
            Debug.logError(getClass().getName() + " -> Renderer instance can not be null!");
            System.exit(-1);
        } else if (input == null) {
            Debug.logError(getClass().getName() + " -> Input instance can not be null!");
            System.exit(-1);
        } else if (time == null) {
            Debug.logError(getClass().getName() + " -> Time instance can not be null!");
            System.exit(-1);
        }
        this.contexts = new Stack<>();
        this.window = window;
        this.input = input;
        this.time = time;
        this.renderer = renderer;
        this.thread = new Thread(new GameLoop());
        this.targetUPS = targetUPS;
        this.running = false;
        CloseOperation closeOperation = new CloseOperation();
        window.setCustomCloseOperation(closeOperation);
        addContext(BaseContext.class);
    }

    public <T extends Context> void addContext(Class<T> contextClass) {
        T context = null;
        try {
            Constructor<T> contextConstructor = contextClass.getConstructor(Engine.class);
            context = contextConstructor.newInstance(this);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            Debug.logError(getClass().getName() + " -> Could not instantiate a valid Context of type " + contextClass.getName() + "!");
            System.exit(-1);
        }
        context.init();
        contexts.push(context);
    }

    public void removeCurrentContext() {
        if (contexts.peek() instanceof BaseContext)
            return;
        contexts.pop();
    }

    public void start() {
        if (running)
            return;
        running = true;
        Debug.disableDebugMode();
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Debug.logError(getClass().getName() + " -> Starting Thread failed!", e);
        }
        window.dispose();
        System.exit(0);
    }

    public void stop() {
        if (!running) {
            window.dispose();
            System.exit(0);
        }
        running = false;
    }

    public Window getWindow() {
        return window;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public Input getInput() {
        return input;
    }

    public Time getTime() {
        return time;
    }

    public short getFps() {
        return fps;
    }

    public short getFfps() {
        return ffps;
    }

    private class GameLoop implements Runnable {
        @Override
        public void run() {
            long lastTime = System.nanoTime();
            float nanosecondsInOneSecond = 1000000000.0f;
            float timeSlice = nanosecondsInOneSecond / targetUPS;
            float timeAccumulated = 0;
            float timer = 0;
            short fixedUpdates = 0;
            short updates = 0;
            while (running) {
                long currentTime = System.nanoTime();
                long frameTime = currentTime - lastTime;
                lastTime = currentTime;
                timeAccumulated += frameTime;
                while (timeAccumulated >= timeSlice) {
                    {
                        time.setDeltaTime((time.getTimeScale() * timeSlice) / nanosecondsInOneSecond);
                        time.update();

                        if (!contexts.isEmpty())
                            contexts.peek().fixedUpdate();

                        if (input != null)
                            input.update();
                    }
                    fixedUpdates++;
                    timeAccumulated -= timeSlice;
                }
                {
                    time.setDeltaTime((time.getTimeScale() * frameTime) / nanosecondsInOneSecond);

                    if (!contexts.isEmpty())
                        contexts.peek().update();
                }
                updates++;

                if (timer > 1) {
                    ffps = fixedUpdates;
                    fps = updates;
                    timer = 0;
                    fixedUpdates = 0;
                    updates = 0;
                }
                timer += time.getDeltaTime();
                window.updateFrame();
            }
        }
    }

    private class CloseOperation extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            stop();
        }
    }
}
