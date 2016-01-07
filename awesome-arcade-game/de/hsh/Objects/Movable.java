package de.hsh.Objects;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.*;

// ALEX: Klasse gefüllt
public abstract class Movable extends Actor{
	
	private Point2D mDirection;
	
	public abstract Point getCenter();
	
	public abstract Dimension getSize();
	
	public void setDirection(Point2D pDirection) 
	{
		mDirection = pDirection;	
	}
	
	public Point2D getDirection()
	{
		return mDirection;
	}
}

