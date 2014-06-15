package org.wintrisstech.seh.pong;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;

public class Paddle implements ActionListener, KeyListener {
	private final int width = 30, height = 150, move = 10;
	private int xPos, yPos;
	private int boundsHeight;
	private int side;
	public static final int LEFTSIDE = 0, RIGHTSIDE = 1;
	private boolean upPressed = false;
	private boolean downPressed = false;
	private final int upCode, downCode;

	public Paddle(int xPos, int yPos, int height, int side) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.boundsHeight = height;
		this.side = side;
		if (this.side == Paddle.LEFTSIDE) {
			upCode = KeyEvent.VK_W;
			downCode = KeyEvent.VK_S;
		} else {
			upCode = KeyEvent.VK_UP;
			downCode = KeyEvent.VK_DOWN;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == upCode) {
			upPressed = true;
		}
		if (keyCode == downCode) {
			downPressed = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == upCode) {
			upPressed = false;
		}
		if (keyCode == downCode) {
			downPressed = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// this is unused
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (upPressed && !downPressed) {
			yPos -= move;
			if (yPos < height / 2) {
				yPos = height / 2;
			}
		} else if (!upPressed && downPressed) {
			yPos += move;
			if (yPos > boundsHeight - height / 2) {
				yPos = boundsHeight - height / 2;
			}
		}
	}

	public void paintSelf(Graphics g) {
		g.fillRect(xPos - width / 2, yPos - height / 2, width, height);
	}

	public Rectangle2D getShape() {
		return new Rectangle2D.Double(xPos - width / 2, yPos - this.height / 2,
				width, this.height);
	}

}
