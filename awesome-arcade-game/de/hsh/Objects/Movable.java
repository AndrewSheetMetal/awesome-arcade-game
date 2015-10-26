package de.hsh.Objects;

import java.awt.Point;


public abstract class Movable extends Actor{
	protected Point direction;
	
	public Point getDirection() {
		return this.direction;
	}
	
	public abstract void setDirection(Point direction);
}

