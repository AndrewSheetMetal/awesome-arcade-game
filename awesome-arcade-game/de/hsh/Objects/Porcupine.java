package de.hsh.Objects;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.List;

import de.hsh.Battlefield;

public class Porcupine extends Enemy 
{
	// ALEX
	public Porcupine() 
	{		
		super();
		setColor(Color.BLUE);
		// TODO: Unschön?
		this.mType = this.getClass();
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
	
	@Override
	public void spawn(List<Battlefield> pBattlefields) 
	{
		this.createSpawnPosition(pBattlefields, 1);
		this.setRandomDirection();
	}
}
