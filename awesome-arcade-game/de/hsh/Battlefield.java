package de.hsh;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
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
			
			
			g.setColor(Color.BLUE);
			g.fillPolygon(this);
			
		
	}


	/* Diese Methode zerteilt das Battlefield basierend auf der prototypeWall und gibt ein neues Battlefield zurück
	 * */
	public void splitByPrototypeWall(PrototypeWall prototypeWall, List<Battlefield> battlefields) {
		Battlefield newB = new Battlefield();
		//this.npoints = this.npoints -1;
		
		Line2D line = new Line2D.Double();
		double smallestDistanceStart = Double.MAX_VALUE;
		int intersectIndexStart = -1;
		
		double smallestDistanceEnd = Double.MAX_VALUE;
		int intersectIndexEnd = -1;
		
		Point2D start = prototypeWall.getStart();
		Point2D end = prototypeWall.getEnd();
		
		//Finde die Linien des Polygons mit den kürzesten entfernungen zum end und start punkt der prototypeWall
		for(int i = 1; i <= this.npoints; i++) {
			line.setLine(this.xpoints[(i-1)%npoints], this.ypoints[(i-1)%npoints], this.xpoints[i%npoints], this.ypoints[i%npoints]);
			double distStart = line.ptSegDist(start.getX(), start.getY());
			if(distStart < smallestDistanceStart) {
				smallestDistanceStart = distStart;
				intersectIndexStart = i; //Bei 1 wäre es die Linie zwischen 0 und 1, die am nächsten am Start der Prototype Wall liegt
			}
			
			double distEnd = line.ptSegDist(end.getX(), end.getY());
			if(distEnd < smallestDistanceEnd) {
				smallestDistanceEnd = distEnd;
				intersectIndexEnd = i;
			}
		}
		
		System.out.println(intersectIndexStart);
		System.out.println(intersectIndexEnd);
		
		
		//Es gibt zwei Optionen, beim Zerteilen eines Battlefields. Entweder es wird in zwei Battlefields zerteilt oder es wird vom vorhandenen Battlefield ein Teil abgeschnitten.
		//Welcher Teil gefüllt wird muss seperat entschieden werden. Basierend darauf in welchen Teilen Kugeln sind. Die beiden Optionen werden hier berechnet
		Battlefield b1 = getFirstBattlefield(intersectIndexStart, intersectIndexEnd, prototypeWall);
		
		Battlefield b2 = getSecondBattlefield(intersectIndexStart, intersectIndexEnd, prototypeWall);
		
		/*
		 * Sind in beiden Teil-Battlefields Kugeln, so wird das bestehende Battlefield in zwei Battlefield aufgeteilt. Dafür können nicht die beiden vorher berechneten verwendet werden, da diese keine Mauer
		 * berücksichtigen und man folglich gar nicht sehen würde, dass es sich um zwei Battlefield handelt. Sie würden in einander über gehen
		 * */
		
		
		//return b2;
		boolean zerteilen = true; //TODO Code zum Detektieren der Bälle
		if(zerteilen == true) {
			//Falls das Schlachtfeld zerteilt werden soll, so müssen b1 und b2 leider neu berechnet werden, damit man die Zertrennungslinie überhaupt sehen kann
			b1 = getFirstSplittedBattlefield(intersectIndexStart, intersectIndexEnd, prototypeWall,b1);
			//b2 = new Battlefield(); //Fürs erste wird b2 beim Alten gelassen
		}
		
		battlefields.remove(this);
		battlefields.add(b1);
		battlefields.add(b2);
		for(int i = 0; i< b1.npoints; i++) {
			System.out.println("b1: x: "+b1.xpoints[i]+" y: "+b1.ypoints[i]);
		}
		
		for(int i = 0; i<b2.npoints; i++) {
			System.out.println("b2: x: "+b2.xpoints[i]+" y: "+b2.ypoints[i]);
			
		}
		
		
	}
	
	/*
	 * Liefert eine Erste Option ein Battlefield durch die Linie zu trennen
	 * */
	private Battlefield getFirstBattlefield(int intersectIndexStart, int intersectIndexEnd, PrototypeWall prototypeWall) {
		Battlefield toReturn = new Battlefield();		
	
		if(intersectIndexStart < intersectIndexEnd) {
			System.out.println("start < end");
			
			for(int i = 0; i< intersectIndexStart; i++) {
				toReturn.addPoint(this.xpoints[i], this.ypoints[i]);
			}
			
			for(int i = 0; i<prototypeWall.getEdgeCount(); i++) {
				toReturn.addPoint((int)prototypeWall.getEdge(i).getX(), (int)prototypeWall.getEdge(i).getY());
			}
			
			for(int i = intersectIndexEnd; i<this.npoints; i++) {
				toReturn.addPoint(xpoints[i], ypoints[i]);
			}
		}
		else {
			System.out.println("end < start");
			for(int i = 0; i< intersectIndexEnd; i++) {
				toReturn.addPoint(this.xpoints[i], this.ypoints[i]);
			}
			
			for(int i = prototypeWall.getEdgeCount()-1; i>=0; i--) {
				toReturn.addPoint((int)prototypeWall.getEdge(i).getX(), (int)prototypeWall.getEdge(i).getY());
			}
			
			for(int i = intersectIndexStart; i<this.npoints; i++) {
				toReturn.addPoint(xpoints[i], ypoints[i]);
			}
		}
		
		return toReturn;
	}
	
	/*
	 * Liefert die zweite Option ein Battlefield durch eine Linie zu trennen
	 * */
	private Battlefield getSecondBattlefield(int intersectIndexStart, int intersectIndexEnd, PrototypeWall prototypeWall) {
		Battlefield toReturn = new Battlefield();		
		
		if(intersectIndexStart < intersectIndexEnd) {
			System.out.println("start < end");
			for(int i = 0; i<prototypeWall.getEdgeCount(); i++) {
				toReturn.addPoint((int)prototypeWall.getEdge(i).getX(), (int)prototypeWall.getEdge(i).getY());
			}
			
			for(int i = intersectIndexEnd-1; i >= intersectIndexStart; i--) {
				toReturn.addPoint(xpoints[i], ypoints[i]);
			}
		}
		else {
			System.out.println("end < start");
			
			for(int i = prototypeWall.getEdgeCount()-1; i>=0; i--) {
				toReturn.addPoint((int)prototypeWall.getEdge(i).getX(), (int)prototypeWall.getEdge(i).getY());
			}
			
			for(int i = intersectIndexStart-1; i >= intersectIndexEnd; i--) {
				toReturn.addPoint(xpoints[i], ypoints[i]);
			}
		}
		
		return toReturn;
	}
	
	/*
	 * Liefert das eine Battlefield, das entsteht, wenn das bestehende Battlefield durch die PrototypeWall zerteilt wird. 
	 * Es berücksichtigt die prototypeWall und spendiert dieser eine gewisse Breite, sodass man die beiden Teilbattlefield auseinanderhalten kann
	 * */
	private Battlefield getFirstSplittedBattlefield(int intersectIndexStart, int intersectIndexEnd, PrototypeWall prototypeWall, Battlefield original) {
		System.out.println("Get first splitted Battlefield");
		Battlefield toReturn = new Battlefield();
		
		if(intersectIndexStart < intersectIndexEnd) {
			System.out.println("start < end");
			
			for(int i = 0; i< intersectIndexStart; i++) {
				toReturn.addPoint(this.xpoints[i], this.ypoints[i]);
			}
			
			if(Math.abs(this.xpoints[intersectIndexStart-1] - prototypeWall.getEdge(0).getX()) < Math.abs(this.ypoints[intersectIndexStart-1] - prototypeWall.getEdge(0).getY())) {
				System.out.println("Startet in Vertikaler Linie");
				if(this.ypoints[intersectIndexStart-1] < prototypeWall.getEdge(0).getY()) {
					toReturn.addPoint((int)prototypeWall.getEdge(0).getX(), (int)prototypeWall.getEdge(0).getY()-5);
				}
				else {
					toReturn.addPoint((int)prototypeWall.getEdge(0).getX(), (int)prototypeWall.getEdge(0).getY()+5);
				}
			}
			else {
				System.out.println("Startet in Horizontaler Linie");
				if(this.xpoints[intersectIndexStart-1] < prototypeWall.getEdge(0).getX()) {
					toReturn.addPoint((int)prototypeWall.getEdge(0).getX()-5, (int)prototypeWall.getEdge(0).getY());
				}
				else {
					toReturn.addPoint((int)prototypeWall.getEdge(0).getX()+5, (int)prototypeWall.getEdge(0).getY());
				}
				
				//b1.addPoint((int)prototypeWall.getEdge(0).getX()-5, (int)prototypeWall.getEdge(0).getY());
			}
			
			/*
			 * Iteriere durch die Knoten der PrototypeWall, die zwischen Start und Ende liegen
			 * */
			for(int i = 1; i<prototypeWall.getEdgeCount()-1; i++) {
				/* Okay, also es gibt vier Möglichkeiten für den neuen Knoten. +5 Pixel bzw. -5 Pixel in X bzw. Y Richtung vom eigentlichen Edge der prototypeWall verschoben.
				 * Zwei davon kann man ausschließen, wenn man sich den vorangehenden bzw. nächsten Edge der PrototypeWall ansieht. Wenn von einem zum anderen beide Koordinaten sowohl
				 * in X als auch in Y Richtung größer oder kleiner werden, so liegt der Edge dazwischen entweder -5 in x und +5 y oder -5 in y und +5 in x Richtung vom eigentlichen Edge der PrototypeWall
				 * entfernt. Ist diese Bedingung nicht gegeben sind es entsprechend +5 in x und +5 in y bzw. -5 in x und -5 in y.
				 * Welche dieser zwei Optionen dann die richtige ist kann man nur bestimmen, indem man schaut ob der Punkt im originalen Battlefield läge oder nicht
				 * */
				if((prototypeWall.getEdge(i-1).getX()-prototypeWall.getEdge(i+1).getX()) * (prototypeWall.getEdge(i-1).getY() - prototypeWall.getEdge(i+1).getY()) > 0) {
					//Entweder x+5 und y+5 oder x-5 und y-5
					if(original.contains(prototypeWall.getEdge(i).getX()+5, prototypeWall.getEdge(i).getY()-5)) {
						toReturn.addPoint((int)prototypeWall.getEdge(i).getX()+5, (int)prototypeWall.getEdge(i).getY()-5); 
					}
					else {
						toReturn.addPoint((int)prototypeWall.getEdge(i).getX()-5, (int)prototypeWall.getEdge(i).getY()+5); 
					}
				}
				else {
					//Entweder x+5 und y-5 oder x-5 und y+5
					if(original.contains(prototypeWall.getEdge(i).getX()+5, prototypeWall.getEdge(i).getY()+5)) {
						toReturn.addPoint((int)prototypeWall.getEdge(i).getX()+5, (int)prototypeWall.getEdge(i).getY()+5); 
					}
					else {
						toReturn.addPoint((int)prototypeWall.getEdge(i).getX()-5, (int)prototypeWall.getEdge(i).getY()-5); 
					}
				}
				
				
				//toReturn.addPoint((int)prototypeWall.getEdge(i).getX()-5, (int)prototypeWall.getEdge(i).getY()-5);
			}
			
			if(Math.abs(this.xpoints[intersectIndexEnd] - prototypeWall.getEdge(prototypeWall.getEdgeCount()-1).getX()) < Math.abs(this.ypoints[intersectIndexEnd] - prototypeWall.getEdge(prototypeWall.getEdgeCount()-1).getY())) {
				System.out.println("Endet in Vertikaler Linie");
				if(this.ypoints[intersectIndexEnd] < prototypeWall.getEdge(prototypeWall.getEdgeCount()-1).getY()) {
					toReturn.addPoint((int)prototypeWall.getEdge(prototypeWall.getEdgeCount()-1).getX(), (int)prototypeWall.getEdge(prototypeWall.getEdgeCount()-1).getY()-5);
				}
				else {
					toReturn.addPoint((int)prototypeWall.getEdge(prototypeWall.getEdgeCount()-1).getX(), (int)prototypeWall.getEdge(prototypeWall.getEdgeCount()-1).getY()+5);
				}
			}
			else {
				System.out.println("Endet in Horizontaler Linie");
				if(this.xpoints[intersectIndexEnd] < prototypeWall.getEdge(prototypeWall.getEdgeCount()-1).getX()) {
					toReturn.addPoint((int)prototypeWall.getEdge(prototypeWall.getEdgeCount()-1).getX()-5, (int)prototypeWall.getEdge(prototypeWall.getEdgeCount()-1).getY());
				}
				else {
					toReturn.addPoint((int)prototypeWall.getEdge(prototypeWall.getEdgeCount()-1).getX()+5, (int)prototypeWall.getEdge(prototypeWall.getEdgeCount()-1).getY());
				}
				
			}
			for(int i = intersectIndexEnd; i<this.npoints; i++) {
				toReturn.addPoint(xpoints[i], ypoints[i]);
			}
		}
		else {
			System.out.println("end < start");
			ArrayList<Point2D> prototypePoints = prototypeWall.reverse();
			
			int tmp = intersectIndexStart;
			intersectIndexStart = intersectIndexEnd;
			intersectIndexEnd = tmp;
			
			//prototypeWall.reverse();
			
			
			for(int i = 0; i< intersectIndexStart; i++) {
				toReturn.addPoint(this.xpoints[i], this.ypoints[i]);
			}
			
			if(Math.abs(this.xpoints[intersectIndexStart-1] - prototypePoints.get(0).getX()) < Math.abs(this.ypoints[intersectIndexStart-1] - prototypePoints.get(0).getY())) {
				System.out.println("Startet in Vertikaler Linie");
				if(this.ypoints[intersectIndexStart-1] < prototypeWall.getEdge(0).getY()) {
					toReturn.addPoint((int)prototypePoints.get(0).getX(), (int)prototypePoints.get(0).getY()-5);
				}
				else {
					toReturn.addPoint((int)prototypePoints.get(0).getX(), (int)prototypePoints.get(0).getY()+5);
				}
			}
			else {
				System.out.println("Startet in Horizontaler Linie");
				if(this.xpoints[intersectIndexStart-1] < prototypePoints.get(0).getX()) {
					toReturn.addPoint((int)prototypePoints.get(0).getX()-5, (int)prototypePoints.get(0).getY());
				}
				else {
					toReturn.addPoint((int)prototypePoints.get(0).getX()+5, (int)prototypePoints.get(0).getY());
				}
				
				//b1.addPoint((int)prototypeWall.getEdge(0).getX()-5, (int)prototypeWall.getEdge(0).getY());
			}
			
			/*
			 * Iteriere durch die Knoten der PrototypeWall, die zwischen Start und Ende liegen
			 * */
			for(int i = 1; i<prototypePoints.size()-1; i++) {
				/* Okay, also es gibt vier Möglichkeiten für den neuen Knoten. +5 Pixel bzw. -5 Pixel in X bzw. Y Richtung vom eigentlichen Edge der prototypeWall verschoben.
				 * Zwei davon kann man ausschließen, wenn man sich den vorangehenden bzw. nächsten Edge der PrototypeWall ansieht. Wenn von einem zum anderen beide Koordinaten sowohl
				 * in X als auch in Y Richtung größer oder kleiner werden, so liegt der Edge dazwischen entweder -5 in x und +5 y oder -5 in y und +5 in x Richtung vom eigentlichen Edge der PrototypeWall
				 * entfernt. Ist diese Bedingung nicht gegeben sind es entsprechend +5 in x und +5 in y bzw. -5 in x und -5 in y.
				 * Welche dieser zwei Optionen dann die richtige ist kann man nur bestimmen, indem man schaut ob der Punkt im originalen Battlefield läge oder nicht
				 * */
				if((prototypePoints.get(i-1).getX()-prototypePoints.get(i+1).getX()) * (prototypePoints.get(i-1).getY() - prototypePoints.get(i+1).getY()) > 0) {
					//Entweder x+5 und y+5 oder x-5 und y-5
					if(original.contains(prototypePoints.get(i).getX()+5, prototypePoints.get(i).getY()-5)) {
						toReturn.addPoint((int)prototypePoints.get(i).getX()+5, (int)prototypePoints.get(i).getY()-5); 
					}
					else {
						toReturn.addPoint((int)prototypePoints.get(i).getX()-5, (int)prototypePoints.get(i).getY()+5); 
					}
				}
				else {
					//Entweder x+5 und y-5 oder x-5 und y+5
					if(original.contains(prototypePoints.get(i).getX()+5, prototypePoints.get(i).getY()+5)) {
						toReturn.addPoint((int)prototypePoints.get(i).getX()+5, (int)prototypePoints.get(i).getY()+5); 
					}
					else {
						toReturn.addPoint((int)prototypePoints.get(i).getX()-5, (int)prototypePoints.get(i).getY()-5); 
					}
				}
				
				
				//toReturn.addPoint((int)prototypeWall.getEdge(i).getX()-5, (int)prototypeWall.getEdge(i).getY()-5);
			}
			
			if(Math.abs(this.xpoints[intersectIndexEnd] - prototypePoints.get(prototypePoints.size()-1).getX()) < Math.abs(this.ypoints[intersectIndexEnd] - prototypePoints.get(prototypePoints.size()-1).getY())) {
				System.out.println("Endet in Vertikaler Linie");
				if(this.ypoints[intersectIndexEnd] < prototypePoints.get(prototypePoints.size()-1).getY()) {
					toReturn.addPoint((int)prototypePoints.get(prototypePoints.size()-1).getX(), (int)prototypePoints.get(prototypePoints.size()-1).getY()-5);
				}
				else {
					toReturn.addPoint((int)prototypePoints.get(prototypePoints.size()-1).getX(), (int)prototypePoints.get(prototypePoints.size()-1).getY()+5);
				}
			}
			else {
				System.out.println("Endet in Horizontaler Linie");
				if(this.xpoints[intersectIndexEnd] < prototypeWall.getEdge(prototypePoints.size()-1).getX()) {
					toReturn.addPoint((int)prototypePoints.get(prototypePoints.size()-1).getX()-5, (int)prototypePoints.get(prototypePoints.size()-1).getY());
				}
				else {
					toReturn.addPoint((int)prototypePoints.get(prototypePoints.size()-1).getX()+5, (int)prototypePoints.get(prototypePoints.size()-1).getY());
				}
				
			}
			for(int i = intersectIndexEnd; i<this.npoints; i++) {
				toReturn.addPoint(xpoints[i], ypoints[i]);
			}
		}

		
		return toReturn;
	}
	
	/*
	 * Liefert das andere Battlefield, das entsteht, wenn das bestehende Battlefield durch die PrototypeWall zerteilt wird. 
	 * Es berücksichtigt die prototypeWall und spendiert dieser eine gewisse Breite, sodass man die beiden Teilbattlefield auseinanderhalten kann
	 * */
	private Battlefield getSecondSplittedBattlefield(int intersectIndexStart, int intersectIndexEnd, PrototypeWall prototypeWall) {
		Battlefield toReturn = new Battlefield();
		
		//Zweite Option für das Abtrennen
		/*
		if(intersectIndexStart < intersectIndexEnd) {
			System.out.println("start < end");
			for(int i = 0; i<prototypeWall.getEdgeCount(); i++) {
				b2.addPoint((int)prototypeWall.getEdge(i).getX(), (int)prototypeWall.getEdge(i).getY());
			}
			
			for(int i = intersectIndexEnd-1; i >= intersectIndexStart; i--) {
				b2.addPoint(xpoints[i], ypoints[i]);
			}
		}
		else {
			System.out.println("end < start");
			
			for(int i = prototypeWall.getEdgeCount()-1; i>=0; i--) {
				b2.addPoint((int)prototypeWall.getEdge(i).getX(), (int)prototypeWall.getEdge(i).getY());
			}
			
			for(int i = intersectIndexStart-1; i >= intersectIndexEnd; i--) {
				b2.addPoint(xpoints[i], ypoints[i]);
			}
		}*/
		
		return toReturn;
	}
}
