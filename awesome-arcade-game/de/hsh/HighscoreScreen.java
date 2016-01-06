package de.hsh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hsh.Objects.HighscoreEntry;

public class HighscoreScreen extends BasicScreen {
	
	
	public HighscoreScreen(Main main) {
		super(main);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//g.clearRect(0, 0, 700, 500);
		
		g.translate((getWidth()-700)/2, (getHeight()-700)/2);
		
		Graphics2D g2d = (Graphics2D)g;
		
		Color c = new Color(0.3f,0.3f,0.6f,0.5f);
		g2d.setColor(c);
		g2d.fillRect(0, 0, 700, 700);
		
		
		g2d.setColor(Color.RED);
		
		//Font newFont = currentFont.deriveFont(currentFont.getSize() * 1.4F);
		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 2.4F));
		
		
		drawCenteredString("Highscores: ",350,100,g2d);
		
		//Etwas kleinere Schriftgröße für die Highscorewerte
		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 0.7F));
		
		
		/*Random rand = new Random();
		
		long start = Math.abs(rand.nextInt());
		//Generiere 10 zufällige Einträge
		//TODO Highscores laden
		//TODO Am besten mit Stringformat und rechtsbündig, damit man die Scores besser vergleichen kann
		for(int i = 1; i<=10; i++) {
			g2d.drawString(i+". "+rand.nextLong(), 200, 150+i*50);
		}
		
		//Erstmal rückwärts
		for(int i = 10; i>=0; i--) {
			start += Math.abs(rand.nextInt());
			g2d.drawString(i+". "+start, 270, 150+i*40);
		}*/
		for(int i=0; i<main.highscore.getList().size();i++){
			g2d.drawString(i+1+" "+main.highscore.getList().get(i).getName()+" "+main.highscore.getList().get(i).getPoints(), 270, 150+i*40);
		}
		
		
		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 1.15F));
		
		drawCenteredString("Press any key to exit", 350, 650, g2d);
		
	}
	
}
