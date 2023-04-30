package com.j2igf.driver;

import com.j2igf.framework.core.Engine;
import com.j2igf.framework.core.Window;
import com.j2igf.framework.event.Input;
import com.j2igf.framework.graphics.Renderer;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        Window window = new Window("Test", 1280, 720, 1);
        Input input = new Input(window);
        Renderer renderer = new Renderer(window);
        Engine engine = new Engine(window, input, renderer, 60);
        engine.start();
    }
}
