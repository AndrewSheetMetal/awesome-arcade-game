package de.hsh;


import javax.swing.JFrame;

public class Main extends JFrame{
	private static final long serialVersionUID = 1L;
	public static Main main;
	public static void main(String[] args)  {
		new Main().start();
	}
	private void start(){
		main = this;
		setTitle("Field$");
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		getContentPane().add(new MenuScreen());
	}
}
