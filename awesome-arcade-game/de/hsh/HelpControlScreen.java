package de.hsh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class HelpControlScreen extends Screen  {
	//private static final long serialVersionUID = 1L;
	private Main main;
	public HelpControlScreen(Main main) {
		this.main = main;
		
		//Falls irgend eine Taste gedrückt wird, so wird zum MainMenü zurückgekehrt.
		this.addKeyListener(new KeyListener (){
			@Override
			public void keyPressed(KeyEvent arg0) {
				System.out.println("gott next page");
				gotoNextPage();
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
			}
			@Override
			public void keyTyped(KeyEvent arg0) {

			}
		});

	}
	
	private void gotoNextPage() {
		HelpControlScreen2 sc2 = new HelpControlScreen2(main);
		main.getContentPane().removeAll();
		
		main.setScreen(sc2);
		
		sc2.setVisible(true);
		sc2.setFocusable(true);
		sc2.requestFocus();
		
		sc2.updateUI();
		
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

		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 2.4F));

		drawCenteredString("Hilfe / Steuerung", 350, 100, g2d);
		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 0.6F));
		drawCenteredString(
				"image/Ball.png",
				"Die Spielfigur muss das Spielfeld mittels gezogener Mauern auf einen",
				350, 200, g2d);
		drawCenteredString(
				"Prozentsatz reduzieren um ein Level erfolgreich abzuschlie�en",
				350, 220, g2d);
		drawCenteredString(
				"image/Gegner.png",
				" Diese Gegner bewegen sich innerhalb des Spielfeldes und ziehen dem",
				350, 300, g2d);
		drawCenteredString(
				" Spieler bei Ber�hrung ein Leben ab. Au�erdem zerst�ren sie die gezogenen",
				350, 320, g2d);
		drawCenteredString(
				"Mauern solange diese noch nicht komplett errichtet wurden ",
				350, 340, g2d);
		drawCenteredString("image/Stachelschwein.png",
				"Stachelschweine bewegen sich nur auf der Fl�che au�erhalb ",
				350, 400, g2d);
		drawCenteredString("des Spielfeldes und ziehen dem Spieler ebenfalls",
				350, 420, g2d);
		drawCenteredString("bei Ber�hrung ein Leben ab", 350, 440, g2d);
		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 1.15F));
		drawCenteredString("Seite 1 von 3", 350, 650, g2d);

	}

	protected void drawCenteredString(String imgName, String s, int w, int h,
			Graphics2D g2d) {
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
		g2d.drawImage(img, x - imgW, (int) (y - g2d.getFontMetrics()
				.getStringBounds(s, g2d).getHeight()), null);
	}

}
