package de.hsh.Objects;

import java.awt.Color;
import java.awt.geom.Point2D;

public class Ball extends Enemy 
{			
	public Ball() 
	{		
		// Muss am Anfang 0 sein. Wird in GameScreen automatisch ge�ndert.
		setDirection(new Point2D.Double(0, 0));		
		setColor(Color.WHITE);
	}
}
