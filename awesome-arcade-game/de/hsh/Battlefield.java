package de.hsh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

public class Battlefield extends Polygon{

	
	
	public Battlefield() {
		super();
	}	
	
	
	
	public void draw(Graphics g) {
		/*if(walls.size() >= 2) {
			for(int i = 0; i<walls.size()-1; i++) {
				g.drawLine(walls.get(i).x, walls.get(i).y, walls.get(i+1).x, walls.get(i+1).y);
			}
			//Zum Schluss eine Linie vom letzten zum ersten Punkt zeichnen
			g.drawLine(walls.get(0).x, walls.get(0).y, walls.get(walls.size()-1).x, walls.get(walls.size()-1).y);
			
			*/
			
			
			g.setColor(Color.BLUE);
			g.fillPolygon(this);
			
		
	}
}
