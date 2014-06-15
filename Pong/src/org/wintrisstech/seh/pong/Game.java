package org.wintrisstech.seh.pong;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Game extends JPanel implements Runnable, ActionListener,
		PropertyChangeListener, KeyListener {
	private static final int WIDTH = 750, HEIGHT = 500;
	private static final int TARGET_SCORE = 3;
	private static final Font SCORE_FONT = new Font(Font.DIALOG, Font.PLAIN, 72);
	private Timer ticker = new Timer(20, this);
	private Paddle paddleLeft = new Paddle(50, HEIGHT / 2, HEIGHT,
			Paddle.LEFTSIDE);
	private Paddle paddleRight = new Paddle(WIDTH - 50, HEIGHT / 2, HEIGHT,
			Paddle.RIGHTSIDE);
	private Ball ball = new Ball(WIDTH, HEIGHT, ticker, paddleLeft, paddleRight);
	private int leftScore, rightScore;
	private boolean outOfBounds;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}

	@Override
	public void run() {
		JFrame frame = new JFrame("SPong (A Pong Game)");
		frame.add(this);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
		frame.addKeyListener(paddleLeft);
		frame.addKeyListener(paddleRight);
		frame.addKeyListener(this);
		JOptionPane.showMessageDialog(this, "Get Ready", "Ready",
				JOptionPane.INFORMATION_MESSAGE);
		ticker.addActionListener(paddleLeft);
		ticker.addActionListener(paddleRight);
		ticker.addActionListener(ball);
		ticker.start();
		ball.addPropertyChangeListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		ball.paintSelf(g);
		g.setFont(SCORE_FONT);
		g.setColor(Color.BLUE);
		paddleLeft.paintSelf(g);
		g.drawString(String.valueOf(leftScore), WIDTH / 4, 72);
		g.setColor(new Color(150, 0, 255));
		paddleRight.paintSelf(g);
		g.drawString(String.valueOf(rightScore), 3 * WIDTH / 4, 72);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Ball.HIT_LEFT_WALL_PROPERTY)) {
			rightScore++;
			repaint();
			JOptionPane.showMessageDialog(this, "Right Player Score!",
					"Score!", JOptionPane.INFORMATION_MESSAGE);
		} else if (evt.getPropertyName().equals(Ball.HIT_RIGHT_WALL_PROPERTY)) {
			leftScore++;
			repaint();
			JOptionPane.showMessageDialog(this, "Left Player Score!", "Score!",
					JOptionPane.INFORMATION_MESSAGE);
		}
		if (leftScore == TARGET_SCORE || rightScore == TARGET_SCORE) {
			JOptionPane.showMessageDialog(this,
					(leftScore == TARGET_SCORE ? "Left" : "Right")
							+ " Player WON!", "Winner",
					JOptionPane.INFORMATION_MESSAGE);
			int replay = JOptionPane.showConfirmDialog(this,
					"Would you like to play SPong again? ", "Play Again?",
					JOptionPane.YES_NO_OPTION);
			if (replay == JOptionPane.YES_OPTION) {
				restart();
			} else {
				System.exit(0);
			}
		} else {
			outOfBounds = true;
			JOptionPane.showMessageDialog(this,
					"Next ball. Hit Space to Start", "",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void restart() {
		leftScore = 0;
		rightScore = 0;
		ball.reset();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// This is unused
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (outOfBounds && e.getKeyCode() == KeyEvent.VK_SPACE) {
			ball.reset();
			outOfBounds = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// This is unused
	}
}
