package de.hsh.Objects;

import java.awt.geom.Point2D;
import java.util.Random;

public class Enemy extends Movable 
{	
	private Point2D mDirection;
	
	// ALEX: setDirection gelöscht und in Player und Ball eingefügt.	
	public void setRandomDirection()
	{
		Random lRandom = new Random();
		// Zufälligen Winkel erzeugen.
		double lDir = 360 * lRandom.nextDouble();
		// Passenden Quadranten bestimmen.
		int lQuarter = (int)(lDir / 90);
		lDir -= lQuarter * 90;
		lDir /= 90;
		switch(lQuarter)
		{
			// Ball bewegt sich nach oben rechts.
			case 0:
				setDirection(new Point2D.Double(lDir, -(1 - lDir)));
				break;
			// Ball bewegt sich nach unten rechts.
			case 1:
				setDirection(new Point2D.Double(1 - lDir, lDir));
				break;
			// Ball bewegt sich nach unten links.
			case 2:
				setDirection(new Point2D.Double(-lDir, 1 - lDir));
				break;
			// Ball bewegt sich nach oben links.
			case 3:
				setDirection(new Point2D.Double(-(1 - lDir), -lDir));
				break;
		}
	}
	
	// ALEX
	public Point2D getDirection()
	{
		return mDirection;
	}
	
	// ALEX
	public void setDirection(Point2D pDirection) {
		mDirection = pDirection;		
	}

}
