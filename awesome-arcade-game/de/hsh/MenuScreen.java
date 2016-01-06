package de.hsh;

import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.hsh.Objects.CircleButton;

public class MenuScreen extends Screen {
	private static final long serialVersionUID = 1L;
	private Image img;

	public MenuScreen(final Main main) {

		// ImageIcon image = new ImageIcon("/image/ohrlaub.jpg");
		// JLabel bglabel = new JLabel();
		// bglabel.setLayout(null);
		// bglabel.setIcon(image);
		// bglabel.setVisible(true);
		// this.add(bglabel);
		// this.setVisible(true);
		// JFrame menu= new JFrame();
		// menu.getContentPane().add(this);

		this.img = new ImageIcon("image/ohrlaub.jpg").getImage();
		//Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
		Dimension size = new Dimension(Main.SIZE, Main.SIZE);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
		
		CircleButton bsp = new CircleButton("Start", 50, 50, 100,100, Color.RED, Color.WHITE);
		add(bsp);
		JButton newGameBtn = new JButton("Neues Spiel");
		JButton highscoreBtn = new JButton("Highscore");
		JButton creditsBtn = new JButton("Credits");
		JButton hilfeBtn = new JButton("Hilfe/Steuerung");
		JButton endeBtn = new JButton("Beenden");
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createRigidArea(new Dimension(0, 200)));
		
		
		add(newGameBtn);
		newGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		add(Box.createRigidArea(new Dimension(0, 50)));
		add(Box.createHorizontalGlue());
		
		add(highscoreBtn);
		highscoreBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(Box.createRigidArea(new Dimension(0, 50)));
		
		add(creditsBtn);
		creditsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		add(Box.createRigidArea(new Dimension(0, 50)));
		add(Box.createHorizontalGlue());
		
		add(hilfeBtn);
		hilfeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		add(Box.createRigidArea(new Dimension(0, 50)));
		add(Box.createHorizontalGlue());
		
		add(endeBtn);
		endeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		newGameBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				//SVEN: GameScreen mit Level 1 initialisieren
				GameScreen gameScreen = new GameScreen(main.createBattlefields(1), 1, main);
				main.setScreen(gameScreen);
				setVisible(false);
				gameScreen.setFocusable(true);
				gameScreen.requestFocus();

			}
		});
		
		highscoreBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				HighscoreScreen highscoreScreen = new HighscoreScreen(main);
			
				main.setScreen(highscoreScreen);
				setVisible(false);
				highscoreScreen.setFocusable(true);
				highscoreScreen.requestFocus();
				
			}
			
		});
		
		creditsBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CreditsScreen creditsScreen = new CreditsScreen(main);
				
				main.setScreen(creditsScreen);
				setVisible(false);
				creditsScreen.setFocusable(true);
				creditsScreen.requestFocus();
				
			}
			
		});
		
		hilfeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0){
				HelpControlScreen hcScreen = new HelpControlScreen(main);
				
				main.setScreen(hcScreen);
				setVisible(false);
				hcScreen.setFocusable(true);
				hcScreen.requestFocus();
				
			}
			
		});
		
		endeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				beendenDialog();
			}
		});
		
	}

	public void beendenDialog() {
		Object[] options = {"Ohhhh, jaa",
                "Nein, noch nicht"};
		int n = JOptionPane.showOptionDialog(null,
		    "Möchten Sie das Spiel wirklich beenden?",
		    "Beenden?",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    options,
		    options[1]);
		
		System.out.println(n);
		if(n == 0) {
			System.exit(0);
		}
	}
	
	

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}

}
