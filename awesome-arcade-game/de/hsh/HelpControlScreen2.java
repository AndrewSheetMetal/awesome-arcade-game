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

public class HelpControlScreen2 extends Screen  {
	private static final long serialVersionUID = 1L;
	private Main main;
	public HelpControlScreen2(Main main) {
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
		HelpControlScreen3 sc3 = new HelpControlScreen3(main);
		main.getContentPane().removeAll();
		
		main.setScreen(sc3);
		
		sc3.setVisible(true);
		sc3.setFocusable(true);
		sc3.requestFocus();
		
		sc3.updateUI();
		
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
				"image/stand.png",
				"Dieses Power-Up sorgt dafuer das der Spieler ein zusaetzliches Leben bekommt",
				350, 200, g2d);
		drawCenteredString(
				"image/stand.png",
				"Dieses Power-Up sorgt dafuer das der Spieler Zeit gutgeschrieben bekommt",
				350, 300, g2d);
		drawCenteredString("image/stand.png",
				"Dieses Power-Up sogt dafuer das der Spieler sich f�r kurze Zeit schneller bewegt",
				350, 400, g2d);
		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 1.15F));
		drawCenteredString("Seite 2 von 3", 350, 650, g2d);

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
