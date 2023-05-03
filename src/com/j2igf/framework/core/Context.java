package com.j2igf.framework.core;

import com.j2igf.framework.event.Debug;
import com.j2igf.framework.event.Input;
import com.j2igf.framework.event.Time;
import com.j2igf.framework.graphics.Renderer;

public abstract class Context {
    protected final Window window;
    protected final Renderer renderer;
    protected final Input input;
    protected final Time time;
    protected final Engine engine;

    public Context(Engine engine) {
        if (engine == null) {
            Debug.logError(getClass().getName() + " -> Engine instance can not be null!");
            System.exit(-1);
        }
        this.window = engine.getWindow();
        this.renderer = engine.getRenderer();
        this.input = engine.getInput();
        this.time = engine.getTime();
        this.engine = engine;
    }

    public abstract void init();

    public abstract void fixedUpdate();

    public abstract void update();
}
