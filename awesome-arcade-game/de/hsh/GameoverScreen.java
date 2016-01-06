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
		
		/*
		 * Zahlen und Buchstaben gehören zum Namen
		 * Backspace führt dazu, dass das letzt Zeichen vom Spielernamen gelöscht wird
		 * Enter setzt den TotalScore zurück und zeigt den HighscoreScreen an.
		 * */
		this.addKeyListener(new KeyListener (){
			@Override
			public void keyPressed(KeyEvent arg0) {
				if((arg0.getKeyChar() >= 'A' && arg0.getKeyChar() <= 'Z') || (arg0.getKeyChar() >= 'a' && arg0.getKeyChar() <= 'z') || (arg0.getKeyChar() >= '0' && arg0.getKeyChar() <= '9'))
				{
					name += arg0.getKeyChar();	
				}
				else if(arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					if(name.length() > 0) {
						name = name.substring(0, name.length()-1);
					}
				}
				else if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
					//TODO Speichern falls unter den Top Ten
					//Beim Betätigen von Enter wird der HighscoreScreen angezeigt 
					resetTotalScore();
					showHighscoreScreen();
				}
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
	
	private void resetTotalScore() {
		main.score = 0;
	}
	
	private void showHighscoreScreen() {
		System.out.println("Show HighscoreScreen");
		HighscoreScreen highscoreScreen = new HighscoreScreen(main);

		main.setScreen(highscoreScreen);

		highscoreScreen.setVisible(true);
		highscoreScreen.setFocusable(true);
		highscoreScreen.requestFocus();
		highscoreScreen.updateUI();
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
				updateUI();
				delta = 0;
			}
		}
		
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
		
		drawCenteredString("Press \"Enter\" zu continue",350,400,g2d);
		
	}
	
	/*
	 * Schreibt horizontalzentrierten Text
	 * */
	

	
}
