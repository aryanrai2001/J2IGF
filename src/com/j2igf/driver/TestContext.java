package com.j2igf.driver;

import com.j2igf.framework.core.*;
import com.j2igf.framework.event.Input;
import com.j2igf.framework.graphics.dynamicGFX.Animation;
import com.j2igf.framework.graphics.dynamicGFX.Animator;
import com.j2igf.framework.graphics.Renderer;
import com.j2igf.framework.graphics.staticGFX.Image;
import com.j2igf.framework.graphics.staticGFX.TileSet;

public class TestContext extends Context
{
	TileSet playerSprites = new TileSet(new Image("res/images/player.png"), 4, 4);
	Animation down = new Animation(playerSprites, true, 0, 4, 8);
	Animation left = new Animation(playerSprites, true, 4, 4, 8);
	Animation up = new Animation(playerSprites, true, 8, 4, 8);
	Animation right = new Animation(playerSprites, true, 12, 4, 8);
	Animator player = new Animator(4);
	int x;
	int y;
	boolean update;

	@Override
	public void init()
	{
		update = false;
		x = J2IGF.getWidth() / 2;
		y = J2IGF.getHeight() / 2;
		player.setAnimation(0, down);
		player.setAnimation(1, left);
		player.setAnimation(2, up);
		player.setAnimation(3, right);
	}

	@Override
	public void update(Input input)
	{
		if (input.isKeyDown(Input.KeyCode.ESCAPE))
			J2IGF.exit();

		if (input.isKey(Input.KeyCode.S))
		{
			player.setState(0, true);
			update = true;
			y++;
		}
		else if (input.isKey(Input.KeyCode.A))
		{
			player.setState(1, true);
			update = true;
			x--;
		}
		else if (input.isKey(Input.KeyCode.W))
		{
			player.setState(2, true);
			update = true;
			y--;
		}
		else if (input.isKey(Input.KeyCode.D))
		{
			player.setState(3, true);
			update = true;
			x++;
		}
		else
			update = false;

		if (update)
		{
			player.update();
		}
	}

	@Override
	public void render(Renderer renderer)
	{
		renderer.clear(0);
		player.render(renderer, x, y);
	}
}
