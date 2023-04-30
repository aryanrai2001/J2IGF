package com.j2igf.framework.core;

import com.j2igf.framework.event.Input;
import com.j2igf.framework.graphics.Renderer;

public abstract class Context {
    protected final Window window;
    protected final Input input;
    protected final Engine engine;
    protected final Renderer renderer;

    public Context(Window window, Input input, Renderer renderer, Engine engine) {
        this.window = window;
        this.engine = engine;
        this.renderer = renderer;
        this.input = input;
    }

    public abstract void init();

    public abstract void update();

    public abstract void render();
}
