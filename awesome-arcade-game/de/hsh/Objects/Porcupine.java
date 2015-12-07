package de.hsh.Objects;

import java.awt.Color;
import java.awt.geom.Point2D;

public class Porcupine extends Enemy 
{
	// ALEX
	public Porcupine() 
	{		
		// Muss am Anfang 0 sein. Wird in GameScreen automatisch ge�ndert.
		setDirection(new Point2D.Double(0, 0));		
		setColor(Color.BLUE);
	}
	
	// L�sst das Stachelschwein ggf. vom Spielfeldrand abprallen.
	public void handleIntersectionWithBorder(int pWidth, int pHeight)
	{
		// Gegen den linken oder rechten Rand gesto�en?
		if(hitsBorder(this.getPosition().getX(), this.getSize().getWidth(), pWidth))
		{
			this.changeDirectionFromWall(1, this.getSpeed());
		}
		// Gegen den oberen oder unteren Rand gesto�en?
		else if(hitsBorder(this.getPosition().getY(), this.getSize().getHeight(), pHeight))
		{
			this.changeDirectionFromWall(0, this.getSpeed());
		}
	}
	
	// Pr�ft, ob das Stachelschwein waagerecht oder senkrecht gegen den Spielfeldrandst��t.
	private boolean hitsBorder(double pPosition, double pSize, int pAreaSize)
	{
		if((pPosition < 1) || ((pPosition + pSize) >= pAreaSize))
		{
			return true;
		}
		return false;
	}
}
