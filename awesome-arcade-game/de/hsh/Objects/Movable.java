package de.hsh.Objects;

import java.awt.geom.*;


public abstract class Movable extends Actor{
	protected Point2D direction;
	
	public Point2D getDirection() {
		return this.direction;
	}
	
	public abstract void setDirection(Point2D direction);
}
