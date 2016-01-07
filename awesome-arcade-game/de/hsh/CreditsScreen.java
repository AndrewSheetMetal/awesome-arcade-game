package de.hsh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

public class CreditsScreen extends BasicScreen implements Runnable {
	private int offsetY = 0;
	
	
	public CreditsScreen(Main main) {
		super(main);
		
		new Thread(this).start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//g.clearRect(0, 0, 700, 500);
		
		g.translate((getWidth()-700)/2, (getHeight()-700)/2);
		
		Graphics2D g2d = (Graphics2D)g;
		
		Color c = new Color(0.3f,0.3f,0.6f,0.5f);
		g2d.setColor(c);
		g2d.fillRect(0, 0, 700, 700);
		
		
		g2d.setColor(Color.RED);
		
		//Font newFont = currentFont.deriveFont(currentFont.getSize() * 1.4F);
		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 2.4F));
		
		
		drawCenteredString("Credits: ",350,moveup(150),g2d);
		
		//Etwas kleinere Schriftgroessee fuer die Namen
		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 0.7F));
		
		int left = 250;
		int top = 200;
		int distance = 50;
		
		g2d.drawString("- Sven Döring", left, moveup(top));
		g2d.drawString("- Alexander Gauggel", left, moveup(top+1*distance));
		g2d.drawString("- Andreas Blech", left, moveup(top+2*distance));
		g2d.drawString("- Alexander Grunewald", left, moveup(top+3*distance));
		g2d.drawString("- Bastian Goldbeck(Cocken)", left, moveup(top+4*distance));
		g2d.drawString("- Philipp Gottschalk", left, moveup(top+5*distance));
		
		
		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 1.15F));
		
		drawCenteredString("Press any key to exit", 350, moveup(600), g2d);
		
	}
	
	private int moveup(int y) {
		return 700-(((700-y)+offsetY)%700);
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D/60D;
		float delta = 0;
		
		
		while(true){
			long now = System.nanoTime();
			delta +=  (now-lastTime)/nsPerTick;
			lastTime = now;			
			
			if(delta >= 1) {
				offsetY++;

				delta = 0;
				updateUI();
			}
		}
		
	}
	
}
