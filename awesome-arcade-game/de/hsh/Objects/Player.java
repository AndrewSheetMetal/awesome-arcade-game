package de.hsh.Objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

public class Player extends Movable {
	private int livePoints;
	private Color color;
	
	@Override
	public void setDirection(Point2D direction) {
		this.direction = direction;
		
		//System.out.println("Direction "+direction);
		
		// TODO Auto-generated method stub
		
	}
	
	public Player() {
		setDirection(new Point2D.Double(0,0));
		setPosition(new Point2D.Double(50,50));
		setColor(Color.RED);
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		//RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		//rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		
		//g2d.setRenderingHint(null, rh);
		
		Dimension size = getSize();
		double w = size.getWidth();
		double h = size.getHeight();
		
		Ellipse2D circle2D = new Ellipse2D.Double();
		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(getColor());
		
		Point2D pos = this.getPosition();
		
		circle2D.setFrame(pos.getX(), pos.getY(), w, h);
		
		//pos.x += 1;
		//pos.y += 1;
		//pos.x = pos.x % 300;
		//pos.y = pos.y % 300;
		//System.out.println("Position: "+pos);
		
		this.setPosition(pos);
		
		g2d.draw(circle2D);
		

	}
	
	public Dimension getSize() {
		Dimension toReturn = new Dimension();
		toReturn.setSize(50, 50);
		return toReturn;
	}
}
