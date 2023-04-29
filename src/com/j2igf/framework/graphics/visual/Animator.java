package com.j2igf.framework.graphics.visual;

import com.j2igf.framework.graphics.Renderer;

public class Animator {
    private final Animation[] animations;
    private int currentAnimation;

    public Animator(int states) {
        if (states <= 1) {
            System.err.println("There must be at least two animation states.");
            System.exit(-1);
        }
        animations = new Animation[states];
        currentAnimation = 0;
    }

    public void update() {
        animations[currentAnimation].update();
    }

    public void render(Renderer renderer, int x, int y) {
        animations[currentAnimation].render(renderer, x, y);
    }

    public void reset() {
        animations[currentAnimation].reset();
    }

    public void changeState(int index, boolean reset) {
        if (currentAnimation == index)
            return;
        if (index >= animations.length || index < 0) {
            System.err.println("Animations index out of bounds!");
            System.exit(-1);
        }
        currentAnimation = index;
        if (reset)
            reset();
    }

    public void setState(int index, Animation animation) {
        if (index >= animations.length || index < 0) {
            System.err.println("Animations index out of bounds!");
            System.exit(-1);
        }
        animations[index] = animation;
    }
}
