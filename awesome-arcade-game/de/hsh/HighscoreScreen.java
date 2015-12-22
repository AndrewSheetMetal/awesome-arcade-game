package de.hsh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class HighscoreScreen extends Screen {
	private Main main;
	
	
	public HighscoreScreen(Main main) {
		this.main = main;
		
		//this.addKeyListener(new KeyListener )
		
		this.addKeyListener(new KeyListener (){
			@Override
			public void keyPressed(KeyEvent arg0) {
				System.out.println("Return to Main menü");
				returnToMainMenu();
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
			@Override
			public void keyTyped(KeyEvent arg0) {

			}
		});
	}
	
	private void returnToMainMenu() {
		MenuScreen menuScreen = new MenuScreen(main);
		main.getContentPane().removeAll();
		
		main.setScreen(menuScreen);
		
		menuScreen.setVisible(true);
		menuScreen.setFocusable(true);
		menuScreen.requestFocus();
		
		menuScreen.updateUI();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//g.clearRect(0, 0, 700, 500);
		
		g.translate((getWidth()-700)/2, (getHeight()-700)/2);
		
		Graphics2D g2d = (Graphics2D)g;
		
		Color c = new Color(0.7f,0.3f,0.3f,0.5f);
		g2d.setColor(c);
		g2d.fillRect(0, 0, 700, 700);
		
		
		g2d.setColor(Color.RED);
		
		//Font newFont = currentFont.deriveFont(currentFont.getSize() * 1.4F);
		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 2.4F));
		
		
		drawCenteredString("Highscores: ",350,100,g2d);
		
		//Etwas kleinere Schriftgröße für die Highscorewerte
		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 0.7F));
		
		
		Random rand = new Random();
		
		long start = Math.abs(rand.nextInt());
		//Generiere 10 zufällige Einträge
		//TODO Highscores laden
		//TODO Am besten mit Stringformat und rechtsbündig, damit man die Scores besser vergleichen kann
		/*for(int i = 1; i<=10; i++) {
			g2d.drawString(i+". "+rand.nextLong(), 200, 150+i*50);
		}*/
		
		//Erstmal rückwärts
		for(int i = 10; i>=0; i--) {
			start += Math.abs(rand.nextInt());
			g2d.drawString(i+". "+start, 270, 150+i*40);
		}
		
		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 1.15F));
		
		drawCenteredString("Press any key to exit", 350, 650, g2d);
		
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
