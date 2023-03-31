package com.j2igf.framework.event;

import com.j2igf.framework.core.J2IGF;
import java.awt.event.*;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener
{
	private final int NUM_KEYS = 0xFF00, NUM_BUTTONS = 5;
	private boolean[] keys;
	private boolean[] lastKeys;
	private boolean[] buttons;
	private boolean[] lastButtons;
	private int mouseX, mouseY, scroll;

	private Input()
	{
		this.mouseX = J2IGF.getWidth() / 2;
		this.mouseY = J2IGF.getHeight() / 2;
		this.reset();
	}

	public static void create()
	{
		J2IGF.setInput(new Input());
		J2IGF.getWindow().getCanvas().addKeyListener(J2IGF.getInput());
		J2IGF.getWindow().getCanvas().addMouseListener(J2IGF.getInput());
		J2IGF.getWindow().getCanvas().addMouseMotionListener(J2IGF.getInput());
		J2IGF.getWindow().getCanvas().addMouseWheelListener(J2IGF.getInput());
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
		System.arraycopy(keys, 0, lastKeys, 0, NUM_KEYS);
		System.arraycopy(buttons, 0, lastButtons, 0, NUM_BUTTONS);
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
	public void keyTyped(KeyEvent e)
	{
	}

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
		mouseX = e.getX() / J2IGF.getPixelScale();
		mouseY = e.getY() / J2IGF.getPixelScale();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX() / J2IGF.getPixelScale();
		mouseY = e.getY() / J2IGF.getPixelScale();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	public static class KeyCode
	{
		public static final int ENTER = '\n';
		public static final int BACK_SPACE = '\b';
		public static final int TAB = '\t';
		public static final int CANCEL = 0x03;
		public static final int CLEAR = 0x0C;
		public static final int SHIFT = 0x10;
		public static final int CONTROL = 0x11;
		public static final int ALT = 0x12;
		public static final int PAUSE = 0x13;
		public static final int CAPS_LOCK = 0x14;
		public static final int ESCAPE = 0x1B;
		public static final int SPACE = 0x20;
		public static final int PAGE_UP = 0x21;
		public static final int PAGE_DOWN = 0x22;
		public static final int END = 0x23;
		public static final int HOME = 0x24;
		public static final int LEFT = 0x25;
		public static final int UP = 0x26;
		public static final int RIGHT = 0x27;
		public static final int DOWN = 0x28;
		public static final int COMMA = 0x2C;
		public static final int MINUS = 0x2D;
		public static final int PERIOD = 0x2E;
		public static final int SLASH = 0x2F;
		public static final int KEY0 = 0x30;
		public static final int KEY1 = 0x31;
		public static final int KEY2 = 0x32;
		public static final int KEY3 = 0x33;
		public static final int KEY4 = 0x34;
		public static final int KEY5 = 0x35;
		public static final int KEY6 = 0x36;
		public static final int KEY7 = 0x37;
		public static final int KEY8 = 0x38;
		public static final int KEY9 = 0x39;
		public static final int SEMICOLON = 0x3B;
		public static final int EQUALS = 0x3D;
		public static final int A = 0x41;
		public static final int B = 0x42;
		public static final int C = 0x43;
		public static final int D = 0x44;
		public static final int E = 0x45;
		public static final int F = 0x46;
		public static final int G = 0x47;
		public static final int H = 0x48;
		public static final int I = 0x49;
		public static final int J = 0x4A;
		public static final int K = 0x4B;
		public static final int L = 0x4C;
		public static final int M = 0x4D;
		public static final int N = 0x4E;
		public static final int O = 0x4F;
		public static final int P = 0x50;
		public static final int Q = 0x51;
		public static final int R = 0x52;
		public static final int S = 0x53;
		public static final int T = 0x54;
		public static final int U = 0x55;
		public static final int V = 0x56;
		public static final int W = 0x57;
		public static final int X = 0x58;
		public static final int Y = 0x59;
		public static final int Z = 0x5A;
		public static final int OPEN_BRACKET = 0x5B;
		public static final int BACK_SLASH = 0x5C;
		public static final int CLOSE_BRACKET = 0x5D;
		public static final int NUMPAD0 = 0x60;
		public static final int NUMPAD1 = 0x61;
		public static final int NUMPAD2 = 0x62;
		public static final int NUMPAD3 = 0x63;
		public static final int NUMPAD4 = 0x64;
		public static final int NUMPAD5 = 0x65;
		public static final int NUMPAD6 = 0x66;
		public static final int NUMPAD7 = 0x67;
		public static final int NUMPAD8 = 0x68;
		public static final int NUMPAD9 = 0x69;
		public static final int MULTIPLY = 0x6A;
		public static final int ADD = 0x6B;
		public static final int SEPARATOR = 0x6C;
		public static final int SUBTRACT = 0x6D;
		public static final int DECIMAL = 0x6E;
		public static final int DIVIDE = 0x6F;
		public static final int DELETE = 0x7F;
		public static final int NUM_LOCK = 0x90;
		public static final int SCROLL_LOCK = 0x91;
		public static final int F1 = 0x70;
		public static final int F2 = 0x71;
		public static final int F3 = 0x72;
		public static final int F4 = 0x73;
		public static final int F5 = 0x74;
		public static final int F6 = 0x75;
		public static final int F7 = 0x76;
		public static final int F8 = 0x77;
		public static final int F9 = 0x78;
		public static final int F10 = 0x79;
		public static final int F11 = 0x7A;
		public static final int F12 = 0x7B;
		public static final int F13 = 0xF000;
		public static final int F14 = 0xF001;
		public static final int F15 = 0xF002;
		public static final int F16 = 0xF003;
		public static final int F17 = 0xF004;
		public static final int F18 = 0xF005;
		public static final int F19 = 0xF006;
		public static final int F20 = 0xF007;
		public static final int F21 = 0xF008;
		public static final int F22 = 0xF009;
		public static final int F23 = 0xF00A;
		public static final int F24 = 0xF00B;
		public static final int PRINTSCREEN = 0x9A;
		public static final int INSERT = 0x9B;
		public static final int HELP = 0x9C;
		public static final int META = 0x9D;
		public static final int BACK_QUOTE = 0xC0;
		public static final int QUOTE = 0xDE;
		public static final int KP_UP = 0xE0;
		public static final int KP_DOWN = 0xE1;
		public static final int KP_LEFT = 0xE2;
		public static final int KP_RIGHT = 0xE3;
		public static final int DEAD_GRAVE = 0x80;
		public static final int DEAD_ACUTE = 0x81;
		public static final int DEAD_CIRCUMFLEX = 0x82;
		public static final int DEAD_TILDE = 0x83;
		public static final int DEAD_MACRON = 0x84;
		public static final int DEAD_BREVE = 0x85;
		public static final int DEAD_ABOVEDOT = 0x86;
		public static final int DEAD_DIAERESIS = 0x87;
		public static final int DEAD_ABOVERING = 0x88;
		public static final int DEAD_DOUBLEACUTE = 0x89;
		public static final int DEAD_CARON = 0x8a;
		public static final int DEAD_CEDILLA = 0x8b;
		public static final int DEAD_OGONEK = 0x8c;
		public static final int DEAD_IOTA = 0x8d;
		public static final int DEAD_VOICED_SOUND = 0x8e;
		public static final int DEAD_SEMIVOICED_SOUND = 0x8f;
		public static final int AMPERSAND = 0x96;
		public static final int ASTERISK = 0x97;
		public static final int QUOTEDBL = 0x98;
		public static final int LESS = 0x99;
		public static final int GREATER = 0xa0;
		public static final int BRACELEFT = 0xa1;
		public static final int BRACERIGHT = 0xa2;
		public static final int AT = 0x0200;
		public static final int COLON = 0x0201;
		public static final int CIRCUMFLEX = 0x0202;
		public static final int DOLLAR = 0x0203;
		public static final int EURO_SIGN = 0x0204;
		public static final int EXCLAMATION_MARK = 0x0205;
		public static final int INVERTED_EXCLAMATION_MARK = 0x0206;
		public static final int LEFT_PARENTHESIS = 0x0207;
		public static final int NUMBER_SIGN = 0x0208;
		public static final int PLUS = 0x0209;
		public static final int RIGHT_PARENTHESIS = 0x020A;
		public static final int UNDERSCORE = 0x020B;
		public static final int WINDOWS = 0x020C;
		public static final int CONTEXT_MENU = 0x020D;

		private KeyCode()
		{
		}
	}
}
