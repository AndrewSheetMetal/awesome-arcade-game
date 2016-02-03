package de.hsh;

import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
		
		
	
		this.img = new ImageIcon("image/balls_nigga.jpg").getImage();
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	
		
		//Vertikale BOX b1 fuer die Buttons, in der Mitte angelegt
		
		//Box b1= Box.createVerticalBox();
		
		Dimension size = new Dimension(Main.SIZE, Main.SIZE);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		
		//Runde Buttons initialisieren
		
		CircleButton newGameBtn = new CircleButton("Start",(getWidth()/2)+40 ,50,80,80,Color.BLUE, Color.BLACK);
		CircleButton highscoreBtn = new CircleButton("Highscore",(getWidth()/2)+40,50,80,80,Color.BLUE, Color.BLACK);
		CircleButton creditsBtn = new CircleButton("Credits", (getWidth()/2)+40,50,80,80,Color.BLUE, Color.BLACK);
		CircleButton hilfeBtn = new CircleButton("Hilfe/Steuerung",(getWidth()/2)+40,50,80,80,Color.BLUE, Color.BLACK);
		CircleButton endeBtn = new CircleButton("Beenden",(getWidth()/2)+40,50,80,80,Color.BLUE, Color.BLACK);
		

		//Buttons zum Layout hinzufuegen
		
		add(newGameBtn);
		add(highscoreBtn);
		add(creditsBtn);
		add(hilfeBtn);
		add(endeBtn);

		newGameBtn.addMouseListener(new MouseListener() {
		

			@Override
			public void mouseClicked(MouseEvent arg0) {
				//SVEN: GameScreen mit Level 1 initialisieren
				GameScreen gameScreen = new GameScreen(main.createBattlefields(1), 1, main);
				main.setScreen(gameScreen);
				setVisible(false);
				gameScreen.setFocusable(true);
				gameScreen.requestFocus();
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		highscoreBtn.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				HighscoreScreen highscoreScreen = new HighscoreScreen(main);
				
				main.setScreen(highscoreScreen);
				setVisible(false);
				highscoreScreen.setFocusable(true);
				highscoreScreen.requestFocus();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}


		
			
			
		});
		
		hilfeBtn.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				HelpControlScreen  helpScreen= new HelpControlScreen(main);
				main.setScreen(helpScreen);
				setVisible(false);
				helpScreen.setFocusable(true);
				helpScreen.requestFocus();
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
		
		creditsBtn.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				CreditsScreen creditsScreen = new CreditsScreen(main);
				main.setScreen(creditsScreen);
				setVisible(false);
				creditsScreen.setFocusable(true);
				creditsScreen.requestFocus();
				
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
	
			
		});
		
		endeBtn.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				beendenDialog();
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			
			
		});
		
	}

	public void beendenDialog() {
		Object[] options = {"Ohhhh, jaa man!!",
                "Nein, noch nicht"};
		int n = JOptionPane.showOptionDialog(null,
		    "Sicher?",
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
		//Skalierung
		g.translate((getWidth()-700)/2,(getHeight()-700)/2);
	}

}
