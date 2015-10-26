package de.hsh.Objects;

import java.awt.Point;

public abstract class Actor {
	private Point position;
	
	public void setPosition(Point position) {
		this.position = position;
	}
	
	public Point getPosition() {
		return this.position;
	}
	
}
