package de.hsh;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuScreen extends Screen{
	private static final long serialVersionUID = 1L;
	
	public MenuScreen(){
		
		
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JButton newGameBtn = new JButton("Neues Spiel");
		JButton higscoreBtn = new JButton("Highscore");
		JButton creditsBtn = new JButton("Credits");
		add(newGameBtn);
		newGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(higscoreBtn);
		higscoreBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(creditsBtn);
		creditsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		newGameBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GameScreen tmp =new GameScreen(null);
				setVisible(true);
				setVisible(false);
				
			}
		});
		}
	}
