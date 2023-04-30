package com.j2igf.framework.core;

import com.j2igf.framework.event.Input;
import com.j2igf.framework.graphics.Renderer;
import com.j2igf.framework.graphics.auxiliary.FontAtlas;
import com.j2igf.framework.graphics.visual.Sprite;

public class BaseContext extends Context {
    private Sprite label, subLabel;
    private int labelX, labelY, subLabelX, subLabelY;

    public BaseContext(Window window, Input input, Renderer renderer, Engine engine) {
        super(window, input, renderer, engine);
    }

    @Override
    public void init() {
        label = new FontAtlas("Times New Roman", window.getHeight() / 10, true).textToSprite("No Context Available!", 0xFFFF0000);
        labelX = (window.getWidth() - label.getWidth()) / 2;
        labelY = (window.getHeight() - label.getHeight()) / 2;

        subLabel = new FontAtlas("Times New Roman", window.getHeight() / 25, true).textToSprite("Press ESCAPE to exit.", 0xFFaa0000);
        subLabelX = (window.getWidth() - subLabel.getWidth()) / 2;
        subLabelY = (window.getHeight() + label.getHeight()) / 2;
    }

    @Override
    public void update() {
        if (input.isKeyDown(Input.ESCAPE))
            engine.stop();
    }

    @Override
    public void render() {
        renderer.clear(0);
        renderer.enableAlphaBlending();
        label.render(renderer, labelX, labelY);
        subLabel.render(renderer, subLabelX, subLabelY);
        engine.showDebugInfo("UPS: " + engine.getUps() + "\nFPS: " + engine.getFps());
        renderer.disableAlphaBlending();
    }
}