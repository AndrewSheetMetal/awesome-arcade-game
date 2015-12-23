package de.hsh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;



/*
 * Diese Klasse fasst einige allgemeine Verhaltensweisen von z.B.
 * dem HighscoreScreen und dem Creditsscreen zusammen.
 * 
 * Es wird ein KeyListener implementiert, sodass man zum MainMenu zurückkehrt, wenn man eine Taste drückt
 * 
 * */
public abstract class BasicScreen extends Screen {
	private Main main;
	
	
	public BasicScreen(Main main) {
		this.main = main;
		
		//Falls irgend eine Taste gedrückt wird, so wird zum MainMenü zurückgekehrt.
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
	}
	
	/*
	 * Schreibt horizontalzentrierten Text
	 * */
	protected void drawCenteredString(String s, int w, int h, Graphics2D g2d) {
			    //FontMetrics fm = g.getFontMetrics();
		
		int x = (int)(w - g2d.getFontMetrics().getStringBounds(s, g2d).getWidth()/2);
		
		//int x = w;//(w - fm.stringWidth(s)) / 2;
		int y = h;//(fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
		    g2d.drawString(s, x, y);
	}
}
