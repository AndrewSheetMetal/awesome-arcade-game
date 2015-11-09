package de.hsh.Objects;

import java.awt.Point;
import java.awt.geom.Point2D;

public abstract class Actor {
	private Point2D position;
	
	public void setPosition(Point2D point2d) {
		this.position = point2d;
	}
	
	public Point2D getPosition() {
		return this.position;
	}
	
}
