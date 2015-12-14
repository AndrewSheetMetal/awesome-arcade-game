package de.hsh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.hsh.Objects.PrototypeWall;

public class Battlefield extends Polygon{
	private Color c;
	
	
	public Battlefield() {
		super();
		Random rand = new Random();
		c = Color.getHSBColor(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
	}	
	
	/*
	 * Komplizierter Algorithmus zum Berechnen der Polygonfläche.
	 * Nicht selbst entwickelt
	 * Am besten nichts dran verändern :)
	 * */
	public double getArea() {
		int i, j, n = this.npoints;
		double area = 0;

		for (i = 0; i < n; i++) {
			j = (i + 1) % n;
			area += xpoints[i] * ypoints[j];
			area -= xpoints[j] * ypoints[i];
		}
		area /= 2.0;
		return (area);
	}
	
	public void draw(Graphics g) {
		
		g.setColor(c);
		
		//g.setColor(Color.BLUE);
		g.fillPolygon(this);
			
		
	}
}