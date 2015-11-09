package de.hsh;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuScreen extends Screen{
	private static final long serialVersionUID = 1L;
	
	public MenuScreen(final Main main){
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
				
				GameScreen gameScreen = new GameScreen(createBattlefields());
				main.setScreen(gameScreen);
				gameScreen.setFocusable(true);
				gameScreen.requestFocus();
				
			}
		});
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
