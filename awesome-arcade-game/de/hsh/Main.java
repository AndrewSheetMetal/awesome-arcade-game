package de.hsh;


import java.awt.Dimension;

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
		setMinimumSize(new Dimension(100,100));
		menuScreen = new MenuScreen();
		gameScreen = new GameScreen(null);
		setScreen(gameScreen);
		gameScreen.setFocusable(true);
		gameScreen.requestFocus();
		//setScreen(menuScreen);
	}
	
	public void setScreen(JPanel pScreen){		

		getContentPane().add(pScreen);
	}
}
