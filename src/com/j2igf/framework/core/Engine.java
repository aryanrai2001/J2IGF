package com.j2igf.framework.core;

import com.j2igf.framework.event.Input;
import com.j2igf.framework.event.Time;
import com.j2igf.framework.graphics.Renderer;
import com.j2igf.framework.graphics.auxiliary.FontAtlas;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Stack;

public final class Engine {
    private final Stack<Context> contexts;
    private final Context baseContext;
    private final Window window;
    private final Input input;
    private final Renderer renderer;
    private final Thread thread;
    private final int targetUPS;
    private boolean running;
    private int fps;
    private int ups;

    public Engine(Window window, Input input, Renderer renderer, int targetUPS) {
        this.contexts = new Stack<>();
        this.baseContext = new BaseContext(window, input, renderer, this);
        addContext(baseContext);
        CloseOperation closeOperation = new CloseOperation();
        this.window = window;
        this.input = input;
        this.renderer = renderer;
        window.setCustomCloseOperation(closeOperation);
        GameLoop loop = new GameLoop();
        this.thread = new Thread(loop);
        this.targetUPS = targetUPS;
        this.running = false;
    }

    public void addContext(Context context) {
        assert context != null;
        context.init();
        contexts.push(context);
    }

    public void removeCurrentContext() {
        if (contexts.peek().equals(baseContext))
            return;
        contexts.pop();
    }

    public void update(float deltaTime) {
        Time.setDeltaTime(deltaTime);
        Time.update();

        if (!contexts.isEmpty())
            contexts.peek().update();

        if (input != null)
            input.update();
    }

    public void render(float deltaTime) {
        Time.setDeltaTime(deltaTime);

        if (!contexts.isEmpty())
            contexts.peek().render();
    }

    public void start() {
        if (running)
            return;
        running = true;
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    public void showDebugInfo(String info) {
        int xOffset = 0;
        int yOffset = 0;
        for (int i = 0; i < info.length(); i++) {
            int ch = info.charAt(i);
            if (ch == '\n') {
                xOffset = 0;
                yOffset += FontAtlas.DEFAULT_FONT.getHeight() + FontAtlas.DEFAULT_FONT.getLineSpacing();
            }
            int offset = FontAtlas.DEFAULT_FONT.getOffset(ch);
            int glyphWidth = FontAtlas.DEFAULT_FONT.getGlyphWidth(ch);
            for (int y = 0; y < FontAtlas.DEFAULT_FONT.getHeight(); y++) {
                for (int x = 0; x < glyphWidth; x++) {
                    int fontAlpha = (FontAtlas.DEFAULT_FONT.getPixel(x + offset, y) >>> 24);
                    if (fontAlpha == 0) {
                        renderer.setPixel(x + xOffset, y + yOffset, 0xff000000);
                    } else {
                        float alphaF = (float) fontAlpha / 0xff;
                        renderer.setPixel(x + xOffset, y + yOffset, 0xff000000 | (int) (alphaF * 0xff) << 16 | (int) (alphaF * 0xff) << 8 | (int) (alphaF * 0xff));
                    }
                }
            }
            xOffset += glyphWidth;
        }
    }

    public int getFps() {
        return fps;
    }

    public int getUps() {
        return ups;
    }

    private class GameLoop implements Runnable {
        @Override
        public void run() {
            int updates = 0, frames = 0;
            long lastTime = System.nanoTime();
            float nanosecondsInOneSecond = 1000000000.0f;
            float timeSlice = nanosecondsInOneSecond / targetUPS;
            float timeAccumulated = 0;
            float timer = 0;

            while (running) {
                long currentTime = System.nanoTime();
                long frameTime = currentTime - lastTime;
                lastTime = currentTime;
                timeAccumulated += frameTime;
                while (timeAccumulated >= timeSlice) {
                    update(Time.getTimeScale() * timeSlice / nanosecondsInOneSecond);
                    updates++;
                    timeAccumulated -= timeSlice;
                }
                render(Time.getTimeScale() * frameTime / nanosecondsInOneSecond);
                frames++;

                if (timer > 1) {
                    ups = updates;
                    fps = frames;
                    timer = 0;
                    updates = 0;
                    frames = 0;
                }
                timer += Time.getDeltaTime();
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
