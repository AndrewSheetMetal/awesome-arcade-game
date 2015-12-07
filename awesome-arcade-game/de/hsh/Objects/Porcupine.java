package de.hsh.Objects;

import java.awt.Color;
import java.awt.geom.Point2D;

public class Porcupine extends Enemy 
{
	// ALEX
	public Porcupine() 
	{		
		// Muss am Anfang 0 sein. Wird in GameScreen automatisch geï¿½ndert.
		setDirection(new Point2D.Double(0, 0));		
		setColor(Color.BLUE);
	}
	
	// Lässt das Stachelschwein ggf. vom Spielfeldrand abprallen.
	public void handleIntersectionWithBorder(int pWidth, int pHeight)
	{
		// Gegen den linken oder rechten Rand gestoßen?
		if(hitsBorder(this.getPosition().getX(), this.getSize().getWidth(), pWidth))
		{
			this.changeDirectionFromWall(1, this.getSpeed());
		}
		// Gegen den oberen oder unteren Rand gestoßen?
		else if(hitsBorder(this.getPosition().getY(), this.getSize().getHeight(), pHeight))
		{
			this.changeDirectionFromWall(0, this.getSpeed());
		}
	}
	
	// Prüft, ob das Stachelschwein waagerecht oder senkrecht gegen den Spielfeldrandstößt.
	private boolean hitsBorder(double pPosition, double pSize, int pAreaSize)
	{
		if((pPosition < 1) || ((pPosition + pSize) >= pAreaSize))
		{
			return true;
		}
		return false;
	}
}
