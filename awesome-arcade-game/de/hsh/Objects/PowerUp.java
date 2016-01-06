package de.hsh.Objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Random;

//ALEX: Klasse gefüllt
public class PowerUp extends Actor 
{		
	private PuType mType;
	private static int sPowerUpCounter = 6;
	
	// Konstruktor.
	public PowerUp(int pX, int pY)
	{
		setPosition(new Point2D.Double(pX, pY));
		setColor(Color.GREEN);
		setType(getRandomType(false));
	}
	
	// Liefert den Mittelpunkt.
	public Point getCenter() 
	{
		return new Point((int)getPosition().getX()+getSize().width/2,
				(int)getPosition().getY()+getSize().height/2);
	}
	
	// Liefert die Größe.
	public Dimension getSize() 
	{
		Dimension toReturn = new Dimension();
		toReturn.setSize(30, 30);
		return toReturn;
	}	
		
	// Malt das Power-Up.
	public void draw(Graphics g) 
	{
		Graphics2D g2d = (Graphics2D) g;
				
		Dimension size = getSize();
		double w = size.getWidth();
		double h = size.getHeight();
		
		Ellipse2D circle2D = new Ellipse2D.Double();
		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(getColor());
		
		Point2D pos = this.getPosition();
		
		circle2D.setFrame(pos.getX(), pos.getY(), w, h);
		
		this.setPosition(pos);
		
		g2d.fill(circle2D);	

	}
	
	// Liefert einen zufälligen Power-Up-Typ.
	private PuType getRandomType(boolean pReal)
	{
		Random lRandom = new Random();
		int lType;
		if(pReal)
		{
			lType = lRandom.nextInt(sPowerUpCounter - 1);
		}
		else
		{
			lType = lRandom.nextInt(sPowerUpCounter);
		}
		switch(lType)
		{
			case 0:
				return PuType.Life;
			case 1:
				return PuType.Time;
			case 2:
				return PuType.Speed;
			case 3:
				return PuType.Slow;
			case 4:
				return PuType.Shield;
			case 5:
				return PuType.Random;
			default:
				return PuType.Life;
		}
	}
	
	// Setzt den Power-Up-Typ.
	public void setType(PuType pType)
	{
		mType = pType;
	}
	
	// Definiert den Typen des Power-Ups
	public enum PuType
	{
		Life, Time, Speed, Slow, Shield, Random
	}	
}
