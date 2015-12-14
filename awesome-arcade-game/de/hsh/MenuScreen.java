package de.hsh;

import java.applet.AudioClip;
import java.awt.BorderLayout;
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
import javax.swing.JPanel;

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

		JButton newGameBtn = new JButton("Neues Spiel");
		JButton highscoreBtn = new JButton("Highscore");
		JButton creditsBtn = new JButton("Credits");
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
		newGameBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				//Beim Buttonklick sound abspielen
				
				/*try {
			         URL url = this.getClass().getClassLoader().getResource("sound/doot.wav");
			         AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			         Clip clip = AudioSystem.getClip();
			         clip.open(audioIn);
			         clip.start();
			      } catch (UnsupportedAudioFileException e) {
			         e.printStackTrace();
			      } catch (IOException e) {
			         e.printStackTrace();
			      } catch (LineUnavailableException e) {
			         e.printStackTrace();
			      }
				*/
				
				
				
				//SVEN: GameScreen mit Level 1 inintialisieren
				GameScreen gameScreen = new GameScreen(createBattlefields(), 1);
				main.setScreen(gameScreen);
				setVisible(false);
				gameScreen.setFocusable(true);
				gameScreen.requestFocus();

			}
		});
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

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}

}
