package de.hsh;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ScoreScreen  extends Screen implements Runnable  {
	private int level;
	private Main main;
	private double prozentGefuellt;
	private double score;
	
	private int oldHighscore = 10000;
	private int aktuelleWartezeit = -1; //Die aktuelle Wartezeit bis zum nächsten Level, wird runtergezählt
	
	private final int WARTEZEIT = 5; //Die gesamt Wartezeit bis zum nächsten Level, nachdem der Score kalkuliert wurde
	
	public ScoreScreen(Main main, int level, double prozentGefuellt) {
		this.main = main;
		this.level = level;
		this.prozentGefuellt = prozentGefuellt;
		
		score = 5+level; //Der Score wird im Thread berechnet // = calculateScore();
		
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D/60D;
		float delta = 0;
		
		
		/*
		 * Zähle Score und die Prozentanzeige hoch, bis die Prozentanzeige den tatsächlichen Wert erreicht
		 * */
		
		double original = prozentGefuellt; //prozentGefuellt wird in der drawMethode ausgegeben, original ist nur lokal sichtbar
		prozentGefuellt = 0; 
		while(prozentGefuellt < original){
			long now = System.nanoTime();
			delta +=  (now-lastTime)/nsPerTick;
			lastTime = now;			
			
			if(delta >= 0.3) {
				prozentGefuellt += 0.1f;
				
				score = Math.pow(score, 1.0015+level*0.00002);
				
				updateUI();
				delta = 0;

			}
		}
		
		aktuelleWartezeit = WARTEZEIT;
		
		//Gesamtscore
		main.score += (int)score;
		
		while(aktuelleWartezeit > 0) {
			long now = System.nanoTime();
			delta +=  (now-lastTime)/nsPerTick;
			lastTime = now;	
			
			if(delta > 60) {
				aktuelleWartezeit -= 1;
				
				System.out.println("Wartezeit: "+aktuelleWartezeit);
				
				delta = 0;
				
				updateUI();
			}
		}
		
		/*
		 * Nach einer kurzen Wartezeit wird nun das nächste Level gestartet
		 * */
		
		//main.remove(this);
		main.getContentPane().removeAll();
		
		
		GameScreen newLevel = new GameScreen(main.createBattlefields(level+1), level+1, main);
		main.setScreen(newLevel);
		
		newLevel.setFocusable(true);
		newLevel.requestFocus();
		
		//main.remove(this);
		
	}
	
	public void paintComponent(Graphics g) {
		//super.paintComponent(g);
		g.clearRect(0, 0, 700, 500);
		
		g.translate((getWidth()-700)/2, (getHeight()-700)/2);
		
		Graphics2D g2d = (Graphics2D)g;
		
		//Zentrieren
		
		//g2d.translate((getWidth()-700)/2, (getHeight()-700)/2);
		
		
		
		Color c = new Color(0.3f,0.5f,0.3f,0.5f);
		//Color c = Color.decode("0xFF00FF55");
		
		g2d.setColor(c);
		
		
		//g2d.setColor(Color.BLUE);
		
		
		g2d.fillRect(0, 0, 700, 700);
		
		
		g2d.setColor(Color.RED);
		
		//Font newFont = currentFont.deriveFont(currentFont.getSize() * 1.4F);
		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 2.4F));
		
		//drawCenteredString("Level completed: ", w, h, g2d)
		
		//g2d.drawString("Level completed: ",350,200);
		drawCenteredString("Level "+level+" completed: "+((int)(10*prozentGefuellt))/10.f+"%", 350, 150, g2d);
		
		drawCenteredString("Score: "+(int)score+" XP", 350, 220, g2d);
		
		if(oldHighscore < score) {
			drawCenteredString("New Highscore!!! ", 350, 280, g2d);
			
		}
		
		drawCenteredString("Total Score: "+main.score, 350, 370, g2d);
		
		if(aktuelleWartezeit > 0) {
			drawCenteredString("Noch "+(int)aktuelleWartezeit+" Sekunden bis zum nächsten Level", 350, 480, g2d);
		}
		
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
