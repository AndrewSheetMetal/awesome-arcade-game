package de.hsh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class ScoreScreen  extends Screen {
	private int level;
	private Main main;
	private double prozentGefuellt;
	private int score;
	
	
	
	public ScoreScreen(Main main, int level, double prozentGefuellt) {
		this.main = main;
		this.level = level;
		this.prozentGefuellt = prozentGefuellt;
		
		score = calculateScore();
	}
	
	private int calculateScore() {
		int score = 100;
		
		
		return score;
	}

	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		
		g2d.setColor(Color.BLUE);
		
		
		g2d.fillRoundRect(300, 200, 200, 400, 10, 10);
		
		
		//g2d.drawString("Score: "+score, 200, 200);
		//g.drawImage(img, 0, 0, null);
	}
	
	
}
