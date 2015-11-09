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
		setMinimumSize(new Dimension(400,400));
		menuScreen = new MenuScreen();
		gameScreen = new GameScreen(createBattlefields());
		setScreen(gameScreen);
		gameScreen.setFocusable(true);
		gameScreen.requestFocus();
		//setScreen(menuScreen);
	}
	
	public void setScreen(JPanel pScreen){		

		getContentPane().add(pScreen);
	}
	
	public List<Battlefield> createBattlefields() {
		List<Battlefield> fields = new ArrayList<Battlefield>();
		Battlefield field = new Battlefield();
		
		field.addPoint(30,30);
		field.addPoint(30,170);
		field.addPoint(220,170);
		field.addPoint(220,320);
		field.addPoint(460,320);
		field.addPoint(460,30);
		
		
		fields.add(field);
		return fields;
	}
}
