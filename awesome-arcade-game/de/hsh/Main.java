package de.hsh;


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
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		menuScreen = new MenuScreen();
		setScreen(menuScreen);
	}
	
	public void setScreen(JPanel pScreen){		

		getContentPane().add(pScreen);
	}
}
