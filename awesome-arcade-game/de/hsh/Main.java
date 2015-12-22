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

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	public static final int SIZE = 500;
	public static final int MARGIN = 200;
	private MenuScreen menuScreen;

	public static void main(String[] args) {
		new Main().start();
	}

	private void start() {
		setTitle("Field$");
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// setExtendedState(JFrame.MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(SIZE+MARGIN, SIZE+MARGIN));
		menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		

	}

	public void setScreen(JPanel pScreen) {
		//getContentPane().removeAll();
		getContentPane().add(pScreen);
		System.out.println("Screens: "+getContentPane().getComponentCount());
		//getContentPane().getComponentCount()
	}
	public List<Battlefield> createBattlefields() {
		List<Battlefield> fields = new ArrayList<Battlefield>();
		Battlefield field = new Battlefield();

		field.addPoint(0, 0);
		field.addPoint(Main.SIZE, 0);
		field.addPoint(Main.SIZE, Main.SIZE);
		field.addPoint(Main.SIZE / 2, Main.SIZE);
		field.addPoint(Main.SIZE / 2, Main.SIZE / 2);
		field.addPoint(0, Main.SIZE / 2);

		fields.add(field);

		return fields;
	}
}
