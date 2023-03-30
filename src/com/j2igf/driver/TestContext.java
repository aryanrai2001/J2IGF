package com.j2igf.driver;

import com.j2igf.framework.core.Context;
import com.j2igf.framework.core.Input;
import com.j2igf.framework.core.J2IGF;
import com.j2igf.framework.graphics.Animation;
import com.j2igf.framework.graphics.Animator;
import com.j2igf.framework.graphics.bitmap.Image;
import com.j2igf.framework.graphics.bitmap.TileSet;

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
		x = 320;
		y = 180;
		player.setAnimation(0, down);
		player.setAnimation(1, left);
		player.setAnimation(2, up);
		player.setAnimation(3, right);
	}

	@Override
	public void update()
	{
		if (J2IGF.input.isKeyDown(Input.KeyCode.ESCAPE))
			J2IGF.engine.stop(false);

		if (J2IGF.input.isKey(Input.KeyCode.S))
		{
			player.setState(0, true);
			update = true;
			y++;
		}
		else if (J2IGF.input.isKey(Input.KeyCode.A))
		{
			player.setState(1, true);
			update = true;
			x--;
		}
		else if (J2IGF.input.isKey(Input.KeyCode.W))
		{
			player.setState(2, true);
			update = true;
			y--;
		}
		else if (J2IGF.input.isKey(Input.KeyCode.D))
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
	public void render()
	{
		J2IGF.renderer.clear(0);
		player.render(x, y);
	}

	@Override
	public void dispose()
	{

	}
}
