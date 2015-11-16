package de.hsh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hsh.Objects.PrototypeWall;

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
		
		/*Polygon tmp = new Polygon();
		for(int i=0; i<xpoints.length;i++){
			tmp.addPoint((int)(xpoints[i]*scale), (int)(ypoints[i]*scale));
		}*/
		
		//System.out.println("XPoints: "+Arrays.toString(tmp.xpoints));
		//System.out.println("YPoints: "+Arrays.toString(tmp.ypoints));
		g.setColor(Color.BLUE);
		g.fillPolygon(this);
			
		
	}


	/* Diese Methode zerteilt das Battlefield basierend auf der prototypeWall und gibt ein neues Battlefield zurück
	 * */
	public Battlefield splitByPrototypeWall(PrototypeWall prototypeWall) {
		Battlefield newB = new Battlefield();
		
		
		
		
		return null;
	}
}
