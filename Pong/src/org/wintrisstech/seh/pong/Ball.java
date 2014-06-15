package org.wintrisstech.seh.pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.Timer;

public class Ball implements ActionListener {
	private final int radius = 30;
	private int boundsWidth, boundsHeight;
	private int xPos, yPos;
	private int xSpeed = 10, ySpeed = 10;
	private Paddle leftPaddle, rightPaddle;
	public static final String HIT_LEFT_WALL_PROPERTY = "Hits left wall";
	public static final String HIT_RIGHT_WALL_PROPERTY = "Hits right wall";
	private PropertyChangeSupport propertyChangeSupport;
	private boolean hitLeftWall = false;
	private boolean hitRightWall = false;

	public Ball(int width, int height, Timer ticker, Paddle leftPaddle,
			Paddle rightPaddle) {
		this.boundsWidth = width;
		this.boundsHeight = height;
		xPos = boundsWidth / 2;
		yPos = boundsHeight / 2;
		this.leftPaddle = leftPaddle;
		this.rightPaddle = rightPaddle;
		propertyChangeSupport = new PropertyChangeSupport(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		xPos += xSpeed;
		yPos += ySpeed;
		if (xPos < radius && !hitLeftWall) {
			hitLeftWall = true;
			propertyChangeSupport.firePropertyChange(HIT_LEFT_WALL_PROPERTY,
					false, true);
		} else if (xPos > boundsWidth - radius && !hitRightWall) {
			hitRightWall = true;
			propertyChangeSupport.firePropertyChange(HIT_RIGHT_WALL_PROPERTY,
					false, true);
		}
		if (yPos < radius) {
			ySpeed = Math.abs(ySpeed);
		} else if (yPos > boundsHeight - radius) {
			ySpeed = -Math.abs(ySpeed);
		}
		if (collide(leftPaddle) && !hitLeftWall) {
			xSpeed = Math.abs(xSpeed);
		} else if (collide(rightPaddle) && !hitRightWall) {
			xSpeed = -Math.abs(xSpeed);
		}
	}

	private boolean collide(Paddle paddle) {
		Rectangle2D paddleShape = paddle.getShape();
		Shape ballShape = new Ellipse2D.Double(xPos - radius, yPos - radius,
				2 * radius, 2 * radius);
		return ballShape.intersects(paddleShape);
	}

	public void paintSelf(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillOval(xPos - radius, yPos - radius, 2 * radius, 2 * radius);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void reset() {
		xPos = boundsWidth / 2;
		yPos = boundsHeight / 2;
		xSpeed = -xSpeed;
		hitLeftWall = false;
		hitRightWall = false;
	}
}
