package com.j2igf.framework.event;

import com.j2igf.framework.core.J2IGF;

import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener
{

	private final int NUM_KEYS = 256, NUM_BUTTONS = 5;

	private boolean[] keys;
	private boolean[] lastKeys;
	private boolean[] buttons;
	private boolean[] lastButtons;
	private int mouseX, mouseY, scroll;

	private Input()
	{
		this.mouseX = 0;
		this.mouseY = 0;
		this.reset();
	}

	public static void create()
	{
		J2IGF.input = new Input();
		J2IGF.window.getCanvas().addKeyListener(J2IGF.input);
		J2IGF.window.getCanvas().addMouseListener(J2IGF.input);
		J2IGF.window.getCanvas().addMouseMotionListener(J2IGF.input);
		J2IGF.window.getCanvas().addMouseWheelListener(J2IGF.input);
	}

	public final void reset()
	{
		this.keys = new boolean[NUM_KEYS];
		this.lastKeys = new boolean[NUM_KEYS];
		this.buttons = new boolean[NUM_BUTTONS];
		this.lastButtons = new boolean[NUM_BUTTONS];
	}

	public final void update()
	{
		for (int i = 0; i < NUM_KEYS; i++)
			lastKeys[i] = keys[i];
		for (int i = 0; i < NUM_BUTTONS; i++)
			lastButtons[i] = buttons[i];
		scroll = 0;
	}

	public boolean isKeyDown(int keyCode)
	{
		return !lastKeys[keyCode] && keys[keyCode];
	}

	public boolean isKey(int keyCode)
	{
		return keys[keyCode];
	}

	public boolean isKeyUp(int keyCode)
	{
		return lastKeys[keyCode] && !keys[keyCode];
	}

	public boolean isButtonDown(int button)
	{
		return !lastButtons[button] && buttons[button];
	}

	public boolean isButton(int button)
	{
		return buttons[button];
	}

	public boolean isButtonUp(int button)
	{
		return lastButtons[button] && !buttons[button];
	}

	public int getMouseX()
	{
		return mouseX;
	}

	public int getMouseY()
	{
		return mouseY;
	}

	public int getScroll()
	{
		return scroll;
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void mousePressed(MouseEvent e)
	{
		buttons[e.getButton()] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		buttons[e.getButton()] = false;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		scroll = e.getWheelRotation();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseX = (e.getX() / J2IGF.getRenderScale());
		mouseY = (e.getY() / J2IGF.getRenderScale());
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = (e.getX() / J2IGF.getRenderScale());
		mouseY = (e.getY() / J2IGF.getRenderScale());
	}

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

}
