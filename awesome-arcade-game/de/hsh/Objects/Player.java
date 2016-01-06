package de.hsh.Objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.*;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import de.hsh.GameScreen;

public class Player extends Movable {
	
	private int lifePoints;
	private Color color;
	private BufferedImage img;
	
	// ALEX
	private int mSpeed;
		
	public Player() {
		setColor(Color.RED);
		lifePoints = 3;
		try {
			img=ImageIO.read(new File("image/stand.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void draw(Graphics g) {
		
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
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
		g2d.setColor(getColor());
		g2d.fill(circle2D);
		//g.drawImage(img, (int)pos.getX()+img.getWidth()/2,(int)pos.getY()+img.getHeight()/2, null);

	}

	@Override
	public Point getCenter() {
		return new Point((int)getPosition().getX()+getSize().width/2,(int)getPosition().getY()+getSize().height/2);
	}
	
	@Override
	public Dimension getSize() {
		Dimension toReturn = new Dimension();
		toReturn.setSize(50, 50);
		return toReturn;
	}
	
	public Rectangle2D getBounds() {
		return new Rectangle2D.Double(getPosition().getX(), getPosition().getY(), getSize().getWidth(), getSize().getHeight());
	}
		
	public void setSpeed(int pSpeed)
	{
		mSpeed = pSpeed;
	}
	
	public int getSpeed()
	{
		return mSpeed;
	}

	public void setLifePoints(int life){
		lifePoints = life;
	}
	public int getLifePoints(){
		return lifePoints;
	}

	public void updatePosition(double speed, float pDeltaTime) {
		Point2D pos = getPosition();
		//Point2D direction = getDirection();
		pos.setLocation(pos.getX() + speed*(getDirection().getX()*pDeltaTime), pos.getY() + speed*(getDirection().getY()*pDeltaTime));
		setPosition(pos);
	}
}
