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

/**
 * This is the main class of the framework.
 * It is responsible for managing the game loop and the game contexts.
 * It also provides access to the window, renderer, input and time instances.
 *
 * @author Aryan Rai
 */
public final class Engine {
    /**
     * This stack is used to manage the game contexts.
     */
    private final Stack<Context> contexts;
    /**
     * This is an object of the Window class.
     */
    private final Window window;
    /**
     * This is an object of the Renderer class.
     */
    private final Renderer renderer;
    /**
     * This is an object of the Input class.
     */
    private final Input input;
    /**
     * This is an object of the Time class.
     */
    private final Time time;
    /**
     * This is the thread that runs the game loop.
     */
    private final Thread thread;
    /**
     * This is a constant integer that stores the target updates per second.
     */
    private final int targetUPS;
    /**
     * This is a boolean that stores the running state of the game.
     */
    private boolean running;
    /**
     * This is an integer that stores the current frames per second.
     */
    private int fps;
    /**
     * This is an integer that stores the current fixed frames per second.
     */
    private int ffps;

    /**
     * This is the constructor of the Engine class.
     *
     * @param window    This is an object of the Window class.
     *                  It can not be null.
     * @param targetUPS It sets the target updates per second.
     *                  It must be greater than 0.
     */
    public Engine(Window window, int targetUPS) {
        if (window == null) {
            Debug.logError(getClass().getName() + " -> Window instance can not be null!");
            System.exit(-1);
        } else if (targetUPS <= 0) {
            Debug.logError(getClass().getName() + " -> Target updates per second must be greater than 0!");
            System.exit(-1);
        }
        this.contexts = new Stack<>();
        this.window = window;
        this.renderer = new Renderer(window);
        this.input = new Input(window);
        this.time = new Time();
        this.thread = new Thread(new GameLoop());
        this.targetUPS = targetUPS;
        this.running = false;
        Debug.setRenderer(renderer);
        CloseOperation closeOperation = new CloseOperation();
        window.setCustomCloseOperation(closeOperation);
        addContext(BaseContext.class);
    }

    /**
     * This method is used to add a new context to the stack.
     *
     * @param <T>          This is the type of the context to be added.
     * @param contextClass This is the class of the context to be added.
     *                     It must extend the Context class.
     */
    public <T extends Context> void addContext(Class<T> contextClass) {
        T context = null;
        try {
            Constructor<T> contextConstructor = contextClass.getConstructor(Engine.class);
            context = contextConstructor.newInstance(this);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            Debug.logError(getClass().getName() + " -> Could not instantiate a valid Context of type " + contextClass.getName() + "!");
            System.exit(-1);
        }
        context.init();
        contexts.push(context);
    }

    /**
     * This method is used to remove the current context from the stack.
     */
    public void removeCurrentContext() {
        if (contexts.peek() instanceof BaseContext)
            return;
        contexts.pop();
    }

    /**
     * This method is used to start the game loop.
     */
    public void start() {
        if (running)
            return;
        running = true;
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Debug.logError(getClass().getName() + " -> Starting Thread failed!", e);
        }
        window.dispose();
        System.exit(0);
    }

    /**
     * This method is used to stop the game loop.
     */
    public void stop() {
        if (!running) {
            window.dispose();
            System.exit(0);
        }
        running = false;
    }

    /**
     * This method is used to get the Window instance.
     *
     * @return Instance of the Window class.
     * @see Window
     */
    public Window getWindow() {
        return window;
    }

    /**
     * This method is used to get the Renderer instance.
     *
     * @return Instance of the Renderer class.
     * @see Renderer
     */
    public Renderer getRenderer() {
        return renderer;
    }

    /**
     * This method is used to get the Input instance.
     *
     * @return Instance of the Input class.
     * @see Input
     */
    public Input getInput() {
        return input;
    }

    /**
     * This method is used to get the Time instance.
     *
     * @return Instance of the Time class.
     * @see Time
     */
    public Time getTime() {
        return time;
    }

    /**
     * This method is used to get the current frames per second.
     *
     * @return Current frames per second.
     */
    public int getFps() {
        return fps;
    }

    /**
     * This method is used to get the current fixed frames per second.
     *
     * @return Current fixed frames per second.
     */
    public int getFfps() {
        return ffps;
    }

    /**
     * This is an inner class that implements the Runnable interface.
     * It is used to run the game loop.
     *
     * @author Aryan Rai
     * @see Runnable
     */
    private final class GameLoop implements Runnable {
        /**
         * This is the default constructor of the GameLoop class.
         */
        private GameLoop() {
        }

        /**
         * This is the run method of the GameLoop class.
         * It contains the logic of the game loop.
         */
        @Override
        public void run() {
            long lastTime = System.nanoTime();
            float nanosecondsInOneSecond = 1000000000.0f;
            float timeSlice = nanosecondsInOneSecond / targetUPS;
            float timeAccumulated = 0;
            float timer = 0;
            int fixedUpdates = 0;
            int updates = 0;
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

    /**
     * This is an inner class that extends the WindowAdapter class.
     * It specifies the action to be performed when the window is closed.
     *
     * @author Aryan Rai
     * @see WindowAdapter
     */
    private final class CloseOperation extends WindowAdapter {
        /**
         * This is the default constructor of the CloseOperation class.
         */
        private CloseOperation() {
        }

        /**
         * This is the windowClosing method of the CloseOperation class.
         * It is called when the window is closed.
         *
         * @param e WindowEvent object.
         */
        @Override
        public void windowClosing(WindowEvent e) {
            stop();
        }
    }
}
