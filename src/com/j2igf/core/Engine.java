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

package com.j2igf.core;

import com.j2igf.event.Debug;
import com.j2igf.event.Input;
import com.j2igf.event.Time;
import com.j2igf.graphics.Renderer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the main class of the framework.
 * It is responsible for managing the main loop and the application contexts.
 * It also provides access to the window, renderer, input and time instances.
 *
 * @author Aryan Rai
 */
public final class Engine {

    /**
     * This is the bit position for Stop interrupt.
     */
    private static final byte INTERRUPT_STOP = 0;

    /**
     * This is the bit position for Context Switch interrupt.
     */
    private static final byte INTERRUPT_CONTEXT_SWITCH = 1;

    /**
     * This array stores the context classes.
     */
    private final List<Class<? extends Context>> contexts;

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
     * This is the thread that runs the main loop.
     */
    private final Thread thread;

    /**
     * This is a constant integer that stores the target updates per second.
     */
    private final int targetFixedFPS;

    /**
     * This the handle to the active context.
     */
    private Context currentContext;

    /**
     * This is a boolean that stores the running state of the application.
     */
    private boolean running;

    /**
     * This is a long that stores the interrupts that will be handled at the end of evey loop integration.
     * Depending on which bit is set, the corresponding interrupt will be handled.
     */
    private long currentInterrupts;

    /**
     * This is an integer that stores the index of the next context.
     */
    private int nextContextIndex;

    /**
     * This is an integer that stores the current frames per second.
     */
    private float fps;

    /**
     * This is an integer that stores the current fixed frames per second.
     */
    private float ffps;

    /**
     * This is the constructor of the Engine class.
     *
     * @param window         This is an object of the Window class.
     *                       It can not be null.
     * @param targetFixedFPS It sets the target frames per second.
     *                       It must be between 15 and 120 inclusive.
     */
    public Engine(Window window, int targetFixedFPS) {
        if (window == null) {
            Debug.logError(getClass().getSimpleName() + " -> Window instance can not be null!");
            System.exit(-1);
        } else if (targetFixedFPS < 5 || targetFixedFPS > 120) {
            Debug.logError(getClass().getSimpleName() + " -> Target fixed FPS must be between 5 and 120 inclusive!");
            System.exit(-1);
        }
        this.currentInterrupts = 0;
        this.window = window;
        this.renderer = new Renderer(window);
        this.input = new Input(window);
        this.time = new Time();
        this.thread = new Thread(new MainLoop());
        this.targetFixedFPS = targetFixedFPS;
        this.running = false;
        this.currentContext = null;
        this.contexts = new ArrayList<>();
        this.contexts.add(BaseContext.class);
        CloseOperation closeOperation = new CloseOperation();
        window.setCustomCloseOperation(closeOperation);
        Debug.initRaster(renderer);
        Debug.logInfo("Engine initialized!");
    }

    /**
     * This method is used to handle the interrupts.
     */
    private void handleInterrupt() {
        if ((currentInterrupts & (1L << INTERRUPT_STOP)) != 0) {
            if (!running) {
                window.dispose();
                Debug.logInfo("Application closed!");
                System.exit(0);
            }
            running = false;
        }
        if ((currentInterrupts & (1L << INTERRUPT_CONTEXT_SWITCH)) != 0) {
            currentContext = createContext(contexts.get(nextContextIndex));
        }
        currentInterrupts = 0;
    }

    /**
     * This method is used to start the main loop.
     */
    public void start() {
        if (running)
            return;
        Debug.logInfo("Starting Engine...");
        currentContext = createContext(contexts.size() == 1 ? contexts.get(0) : contexts.get(1));
        running = true;
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Debug.logError(getClass().getSimpleName() + " -> Starting Thread failed!");
        }
        window.dispose();
        Debug.logInfo("Application closed!");
        System.exit(0);
    }

    /**
     * This method is used to stop the main loop.
     */
    public void stop() {
        currentInterrupts |= (1L << INTERRUPT_STOP);
        Debug.logInfo("Stopping Engine...");
    }

    /**
     * This method is used to set the context class at the specified index.
     * It shall only be called before starting the engine.
     *
     * @param contextClass Class of the context.
     *                     It can not be null.
     */
    public void addContext(Class<? extends Context> contextClass) {
        if (running) {
            Debug.logError(getClass().getSimpleName() + " -> Can not set context while engine is running!");
            System.exit(-1);
        }
        if (contextClass == null) {
            Debug.logError(getClass().getSimpleName() + " -> Invalid Context class!");
            System.exit(-1);
        }
        contexts.add(contextClass);
        Debug.logInfo(contextClass.getSimpleName() + " -> Context added!");
    }

    /**
     * This method is used to switch the context.
     * It shall only be called from within a context.
     *
     * @param index Index of the context.
     *              It must be greater than or equal to 0 and less than the total number of contexts.
     */
    public void switchContext(int index) {
        index++;
        if (index < 1 || index >= contexts.size()) {
            Debug.logError(getClass().getSimpleName() + " -> Context index out of bounds!");
            System.exit(-1);
        }
        nextContextIndex = index;
        currentInterrupts |= (1L << INTERRUPT_CONTEXT_SWITCH);
        Debug.logInfo("Context switched to -> " + contexts.get(index).getSimpleName() + "!");
    }

    /**
     * This method is used to create a context of the specified class.
     *
     * @param contextClass Class of the context.
     *                     It can not be null.
     * @return Instance of the context.
     */
    private Context createContext(Class<? extends Context> contextClass) {
        Context context = null;
        try {
            Constructor<? extends Context> contextConstructor = contextClass.getDeclaredConstructor(Engine.class);
            contextConstructor.setAccessible(true);
            context = contextConstructor.newInstance(this);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            Debug.logError(getClass().getSimpleName() +
                    " -> Could not instantiate a valid Context of type " + contextClass.getSimpleName() + "!");
            System.exit(-1);
        }
        Debug.logInfo(contextClass.getSimpleName() + " -> Context initialized!");
        return context;
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
    public float getFps() {
        return fps;
    }

    /**
     * This method is used to get the current fixed frames per second.
     *
     * @return Current fixed frames per second.
     */
    public float getFfps() {
        return ffps;
    }

    /**
     * This is an inner class that implements the Runnable interface.
     * It is used to run the main loop.
     *
     * @author Aryan Rai
     * @see Runnable
     */
    private final class MainLoop implements Runnable {
        /**
         * This is the default constructor of the MainLoop class.
         */
        private MainLoop() {
        }

        /**
         * This is the run method of the MainLoop class.
         * It contains the logic of the main loop.
         */
        @Override
        public void run() {
            long lastTime = System.nanoTime(), currentTime;
            float timeAccumulated = 0, adjustedFrameTime;
            float timeSlice = 1.0f / targetFixedFPS;
            float timer = 0;
            int fixedUpdates = 0;
            int updates = 0;

            while (running) {
                currentTime = System.nanoTime();
                adjustedFrameTime = ((currentTime - lastTime) * time.getTimeScale()) / 1000000000.0f;
                lastTime = currentTime;
                timeAccumulated += adjustedFrameTime;

                while (timeAccumulated >= timeSlice) {
                    time.setDeltaTime(timeSlice);
                    time.update();
                    input.assignLastFrame(true);
                    currentContext.fixedUpdate();
                    input.fixedUpdate();
                    fixedUpdates++;
                    timeAccumulated -= timeSlice;
                }

                time.setDeltaTime(adjustedFrameTime);
                input.assignLastFrame(false);
                currentContext.update();
                input.update();
                updates++;

                if (timer > 1) {
                    ffps = fixedUpdates;
                    fps = updates;
                    timer = 0;
                    fixedUpdates = 0;
                    updates = 0;
                }
                timer += time.getDeltaTime();
                Debug.updateFrame();
                window.updateFrame();
                handleInterrupt();
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
