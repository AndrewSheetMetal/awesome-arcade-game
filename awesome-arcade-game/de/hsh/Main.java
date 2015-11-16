package de.hsh;


import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame{
	private static final long serialVersionUID = 1L;
		
	private GameScreen gameScreen;
	private MenuScreen menuScreen;
	
	public static void main(String[] args)  {
		new Main().start();
	}
	
	private void start(){
		setTitle("Field$");
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(500,500));
		menuScreen = new MenuScreen(this);
		//gameScreen = new GameScreen(createBattlefields());
		//setScreen(gameScreen);
		//gameScreen.setFocusable(true);
		//gameScreen.requestFocus();
		setScreen(menuScreen);
		menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
		//In der Mitte des Spielfelds anzeigen
		setLocationRelativeTo(null);
		
	}
	
	public void setScreen(JPanel pScreen){		

		getContentPane().add(pScreen);
	}
	
	
}
