package de.hsh.Objects;

import java.awt.Color;
import java.awt.geom.*;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import de.hsh.Battlefield;
import de.hsh.GameScreen;

public class Ball extends Enemy 
{			
	public Ball() 
	{		
		super();
		setColor(Color.WHITE);
		// TODO: Unsch�n?
		this.mType = this.getClass();
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
		
		g2d.fill(circle2D);	

	}
	
	public Point getCenter() {
		return new Point((int)getPosition().getX()+getSize().width/2,
				(int)getPosition().getY()+getSize().height/2);
	}
	
	public Rectangle2D getBounds() {
		return new Rectangle2D.Double(getPosition().getX(), getPosition().getY(), getSize().width, getSize().height);
	}
		
	
	public Dimension getSize() {
		Dimension toReturn = new Dimension();
		toReturn.setSize(50, 50);
		return toReturn;
	}

	@Override
	public void spawn(List<Battlefield> pBattlefields) 
	{
		this.createSpawnPosition(pBattlefields, 0);
		this.setRandomDirection();
	}
}
