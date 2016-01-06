package de.hsh;


import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Screen extends JPanel {

	private static final long serialVersionUID = 1L;

	private Main main;
	protected void drawCenteredString(String s, int w, int h, Graphics2D g2d) {
	    //FontMetrics fm = g.getFontMetrics();

int x = (int)(w - g2d.getFontMetrics().getStringBounds(s, g2d).getWidth()/2);

//int x = w;//(w - fm.stringWidth(s)) / 2;
int y = h;//(fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
    g2d.drawString(s, x, y);
}
	
}
