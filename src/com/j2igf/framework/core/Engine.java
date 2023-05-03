package com.j2igf.framework.core;

import com.j2igf.framework.event.Debug;
import com.j2igf.framework.event.Input;
import com.j2igf.framework.event.Time;
import com.j2igf.framework.graphics.Renderer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;

public final class Engine {
    private final Stack<Context> contexts;
    private final Window window;
    private final Renderer renderer;
    private final Input input;
    private final Time time;
    private final BaseContext baseContext;
    private final Thread thread;
    private final int targetUPS;
    private boolean running;
    private short fps;
    private short ffps;

    public Engine(Window window, Renderer renderer, Input input, Time time, int targetUPS) {
        Debug.init(this);
        if (window == null) {
            Debug.logError(getClass().getName() + " -> Window instance can not be null!");
            System.exit(0);
        } else if (renderer == null) {
            Debug.logError(getClass().getName() + " -> Renderer instance can not be null!");
            System.exit(0);
        } else if (input == null) {
            Debug.logError(getClass().getName() + " -> Input instance can not be null!");
            System.exit(0);
        } else if (time == null) {
            Debug.logError(getClass().getName() + " -> Time instance can not be null!");
            System.exit(0);
        }
        this.contexts = new Stack<>();
        CloseOperation closeOperation = new CloseOperation();
        this.window = window;
        this.input = input;
        this.time = time;
        this.renderer = renderer;
        window.setCustomCloseOperation(closeOperation);
        this.baseContext = new BaseContext(this);
        addContext(baseContext);
        GameLoop loop = new GameLoop();
        this.thread = new Thread(loop);
        this.targetUPS = targetUPS;
        this.running = false;
    }

    public void addContext(Context context) {
        if (context == null) {
            Debug.logError(getClass().getName() + " -> Context instance can not be null!");
            System.exit(0);
        }
        context.init();
        contexts.push(context);
    }

    public void removeCurrentContext() {
        if (contexts.peek().equals(baseContext))
            return;
        contexts.pop();
    }

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
