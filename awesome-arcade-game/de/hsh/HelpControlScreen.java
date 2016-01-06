package de.hsh;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HelpControlScreen extends BasicScreen {
	private Main main;
	private Image img;
	public HelpControlScreen(Main main) {
		super(main);
		
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.translate((getWidth() - 700) / 2, (getHeight() - 700) / 2);

		Graphics2D g2d = (Graphics2D) g;

		Color c = new Color(0.3f, 0.3f, 0.6f, 0.5f);
		g2d.setColor(c);
		g2d.fillRect(0, 0, 700, 700);

		g2d.setColor(Color.RED);

		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 1.4F));

		drawCenteredString("Hilfe / Steuerung", 350, 100, g2d);
		drawCenteredString("image/ball.png","Die Spielfigur wird mit Hilfe der Pfeiltasten in alle Himmelsrichtungen bewegt",350, 200, g2d);
		drawCenteredString("image/Gegner.png","Gegner der dem Spieler bei berührung ein Leben abzieht", 350, 300, g2d);
		drawCenteredString("image/Stachelschwein.png","Stachelschweine spawnen außerhalb des Spielfeldes", 350, 400, g2d);
		
		drawCenteredString("Press any Key to exit", 350, 650, g2d);
		

	}

	protected void drawCenteredString(String imgName,String s, int w, int h, Graphics2D g2d) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(imgName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int imgW = img.getWidth();

		int x = (int) (w - g2d.getFontMetrics().getStringBounds(s, g2d)
				.getWidth() / 2);

		int y = h;
		g2d.drawString(s, x, y);
		g2d.drawImage(img, x-imgW, (int)(y-g2d.getFontMetrics().getStringBounds(s, g2d).getHeight()), null);
	}

	
	

}
