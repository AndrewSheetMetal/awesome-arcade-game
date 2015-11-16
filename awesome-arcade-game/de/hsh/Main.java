package de.hsh;


import java.awt.Dimension;
import java.awt.Point;
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
		setMinimumSize(new Dimension(500,400));
		menuScreen = new MenuScreen(this);
		//gameScreen = new GameScreen(createBattlefields());
		//setScreen(gameScreen);
		//gameScreen.setFocusable(true);
		//gameScreen.requestFocus();
		setScreen(menuScreen);
		menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}
	
	public void setScreen(JPanel pScreen){		

		getContentPane().add(pScreen);
	}
	
	
}
