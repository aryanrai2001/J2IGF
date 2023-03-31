package com.j2igf.framework.graphics.dynamicGFX;

import com.j2igf.framework.graphics.Renderer;

public class Animator
{
	private final Animation[] animations;
	private int currentAnimation;

	public Animator(int states)
	{
		if (states <= 1)
		{
			System.err.println("There must be at least two animations.");
			System.exit(-1);
		}
		animations = new Animation[states];
		currentAnimation = 0;
	}

	public void update()
	{
		animations[currentAnimation].update();
	}

	public void render(Renderer renderer, int x, int y)
	{
		animations[currentAnimation].render(renderer, x, y);
	}

	public void reset()
	{
		animations[currentAnimation].reset();
	}

	public void setState(int index, boolean reset)
	{
		if (currentAnimation == index)
			return;
		if (index >= animations.length || index < 0)
		{
			System.err.println("Animations index out of bounds!");
			System.exit(-1);
		}
		currentAnimation = index;
		if (reset)
			reset();
	}

	public void setAnimation(int index, Animation animation)
	{
		animations[index] = animation;
	}

	public Animation getAnimation(int index)
	{
		return animations[index];
	}
}
