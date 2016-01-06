package de.hsh.Objects;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;

public abstract class Actor 
{
	private Point2D mPosition;
	private Color mColor;
	
	public void setPosition(Point2D point2d) 
	{
		this.mPosition = point2d;
	}
	
	public Point2D getPosition() 
	{
		return this.mPosition;
	}

	// ALEX
	public Color getColor() 
	{
		return mColor;
	}
	
	// ALEX
	public void setColor(Color pColor) 
	{
		mColor = pColor;
	}
}
