package com.j2igf.framework.core;

import com.j2igf.framework.event.Input;
import com.j2igf.framework.graphics.Renderer;

public abstract class Context
{
	public abstract void init();

	public abstract void update(Input input);

	public abstract void render(Renderer renderer);
}
