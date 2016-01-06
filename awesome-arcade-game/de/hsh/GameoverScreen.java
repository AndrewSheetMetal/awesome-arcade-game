package de.hsh;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameoverScreen  extends Screen implements Runnable  {
	private int level;
	private Main main;
	//private double score;
	
	private String name;

	public GameoverScreen(Main main, int level) {
		this.main = main;
		this.level = level;
		name = "";
		

		this.addKeyListener(new KeyListener (){
			@Override
			public void keyPressed(KeyEvent arg0) {
				name += arg0.getKeyChar();
				System.out.println("Return to Main menü");
				//returnToMainMenu();
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
			@Override
			public void keyTyped(KeyEvent arg0) {

			}
		});
		
		new Thread(this).start();
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
				//prozentGefuellt += 0.1f;
				
				//score = Math.pow(score, 1.0015+level*0.00002);
				
				updateUI();
				delta = 0;

			}
		}
		
		
		/*
		 * Nach einer kurzen Wartezeit wird nun das nächste Level gestartet
		 * */
		
		//main.remove(this);
/*		main.getContentPane().removeAll();
		
		
		GameScreen newLevel = new GameScreen(main.createBattlefields(level+1), level+1, main);
		main.setScreen(newLevel);
		
		newLevel.setFocusable(true);
		newLevel.requestFocus();
	*/	
		//main.remove(this);
		
	}
	
	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		g.clearRect(0, 0, 700, 500);
		g.translate((getWidth()-700)/2, (getHeight()-700)/2);
		Graphics2D g2d = (Graphics2D)g;
		
		Color c = new Color(0.3f,0.5f,0.3f,0.5f);
		g2d.setColor(c);
		g2d.fillRect(0, 0, 700, 700);
		g2d.setColor(Color.RED);
		
		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 2.4F));
		
		drawCenteredString("Game Over :(", 350, 150, g2d);
		
		
		drawCenteredString("Total Score: "+main.score, 350, 200, g2d);
		
		drawCenteredString("Enter Name: "+name, 350, 250, g2d);
		
		//if(aktuelleWartezeit > 0) {
			//drawCenteredString("Noch "+(int)aktuelleWartezeit+" Sekunden bis zum nächsten Level", 350, 480, g2d);
		//}
		
	}
	
	/*
	 * Schreibt horizontalzentrierten Text
	 * */
	private void drawCenteredString(String s, int w, int h, Graphics2D g2d) {
			    //FontMetrics fm = g.getFontMetrics();
		
		int x = (int)(w - g2d.getFontMetrics().getStringBounds(s, g2d).getWidth()/2);
		
		//int x = w;//(w - fm.stringWidth(s)) / 2;
		int y = h;//(fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
		    g2d.drawString(s, x, y);
	}

	
}
