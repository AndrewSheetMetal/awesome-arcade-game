package de.hsh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class HelpControlScreen3 extends BasicScreen {
	private static final long serialVersionUID = 1L;

	public HelpControlScreen3(Main main) {
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

		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 2.4F));

		drawCenteredString("Hilfe / Steuerung", 350, 100, g2d);
		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 0.6F));
		drawCenteredString(
				"image/stand.png",
				"Dieses Power-Up sorgt dafür das der Spieler sich für kurze Zeit langsamer bewegt",
				350, 200, g2d);
		drawCenteredString(
				"image/stand.png",
				"Dieses Symbol sorgt dafür das der Spieler ein zufällig gewähltes Power-Up bekommt",
				350, 300, g2d);
		drawCenteredString("image/stand.png",
				"Dieses Power-Up sogt dafür das der Spieler sich für kurze Zeit schneller bewegt",
				350, 400, g2d);
		g2d.setFont(g2d.getFont().deriveFont(g2d.getFont().getSize() * 1.15F));
		drawCenteredString("Seite 3 von 3", 350, 650, g2d);

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

