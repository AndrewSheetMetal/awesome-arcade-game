package de.hsh.Objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D.Double;
import java.util.ArrayList;
import java.util.List;

public class PrototypeWall extends Unmovable {
	private ArrayList<Point2D> lines = new ArrayList<Point2D>();
	
	private ArrayList<Point2D> collisionPoints = new ArrayList<Point2D>(); //Der Punkt, an dem die PrototypeWall von einem Ball berührt wurde. Das muss und sollte nicht passieren
	private ArrayList<Integer> collisionIndexs = new ArrayList<Integer>(); //Index des Anfangspunkt der Linie, die vom Ball berührt wurde
	private ArrayList<Float>  collisionTimes = new ArrayList<Float>(); //Speichert die Zeiten seit dem die Kollisionen passiert sind
	//private double collisionDistanceToStart = 0;
	//private double collisionDistanceToEnd = 0;
	
	
	public PrototypeWall() {
		lines = new ArrayList<Point2D>();
	}
	
	public void draw(Graphics g, Point2D p) { //Der Punkt p ist der Teil der prototypeWall, der sich noch ändert. Also die aktuelle Position des Player
		Graphics2D g2d = (Graphics2D) g;
		
		/* Point of Collision zeichnen, falls er existiert
		 * */
		g2d.setColor(Color.MAGENTA);
		for(int i=0; i<collisionPoints.size(); i++) {
			
			float radius = collisionTimes.get(i);
			
			g2d.drawOval((int)(collisionPoints.get(i).getX()-radius), (int)(collisionPoints.get(i).getY()-radius), (int)radius*2, (int)radius*2);
			
		}
		
		for(Point2D collPoint : collisionPoints) {
			g2d.setColor(Color.MAGENTA);
			
			g2d.drawOval((int)collPoint.getX()-3, (int)collPoint.getY()-3, 6, 6);
			
		}
		
		if(collisionPoints.size() >= 1) {
			Ellipse2D e = new Ellipse2D.Double(collisionPoints.get(0).getX()-collisionTimes.get(0), collisionPoints.get(0).getY()-collisionTimes.get(0), collisionTimes.get(0)*2, collisionTimes.get(0)*2);
			Area inside = new Area(e);
			
			
			Area outside = new Area(new Rectangle2D.Double(0, 0, 500, 500));
			outside.subtract(inside);
			
			g2d.setClip(outside);
		}
		float dash[] = { 10.0f };
		g2d.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
		        BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
		
		
		g2d.setColor(getColor());
		
		if(lines.size()>0) {
			g2d.drawLine((int)lines.get(lines.size()-1).getX(), (int)lines.get(lines.size()-1).getY(),(int)p.getX(), (int)p.getY());
			
			if(lines.size() >= 2) {
				for(int i = 0; i<lines.size()-1; i++) {
					g2d.drawLine((int)lines.get(i).getX(), (int)lines.get(i).getY(), (int)lines.get(i+1).getX(), (int)lines.get(i+1).getY());
				}
			}
		}
		
		//g2d.setClip(0, 0, 500, 500);
	}
	
	public Color getColor() {
		return Color.CYAN;
	}
	
	public boolean isDrawn() {
		return lines.size() > 0;
	}
	
	public void addEdge(Point2D edge) {
		lines.add(edge);
	}
	
	public void clear() {
		lines.clear();
		
		//Auch die Kollisionspunkte müssen zurückgesetzt werden
		collisionPoints.clear();
		collisionIndexs.clear();
		collisionTimes.clear();
	}
	
	public Point2D getEnd() {
		return lines.get(lines.size()-1);
	}
	
	public Point2D getStart() {
		return lines.get(0);
	}
	
	public int getEdgeCount() {
		return lines.size();
	}
	
	public Point2D getEdge(int index) {
		return lines.get(index);	
	}
	
	public boolean intersects(Rectangle2D rect) {
		if(lines.size() >= 2) {
			for(int i = 0; i<lines.size()-2; i++) { // -2 da er die letzte Linie eh nicht schneiden kann und es sonst beim Abbiegen kurz zu einem fehlerhaften true kommt
				Line2D line = new Line2D.Double(lines.get(i),lines.get(i+1));
				if(rect.intersectsLine(line)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/*Gibt zurück, ob das Rectangle die PrototypeWall überschneidet und speichert an welchem Punkt dies war.
	 * Es wird nur etwas kalkuliert, wenn die PrototypeWall zuvor noch nie berührt wurde
	 * */
	public boolean intersects(Rectangle2D rect, Point2D playerCenter) {
		if(lines.size() >= 1 && collisionPoints.size() == 0) {
			
			Line2D lastLine = new Line2D.Double(lines.get(lines.size()-1),playerCenter);
			if(rect.intersectsLine(lastLine)) {
				
				collisionIndexs.add(lines.size()-1); //Merken, an welcher Stelle die PrototypeWall berührt wurde
				collisionPoints.add(getPointOfCollision(rect,lastLine));
				collisionTimes.add(0.0f);
				return true;
			}
			
			if(lines.size() >= 1) {
				for(int i = 0; i<lines.size()-1; i++) {
					Line2D line = new Line2D.Double(lines.get(i),lines.get(i+1));
					if(rect.intersectsLine(line)) {
						collisionIndexs.add(i); //Merken, an welcher Stelle die PrototypeWall berührt wurde
						collisionPoints.add(getPointOfCollision(rect,line)); 
						collisionTimes.add(0.0f);
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	//Berechnet die Distanz des Collision Points zum Start der PrototypeWall.
	//Verfolgt dabei den Verlauf der PrototypeWall, also nicht Fluglinie
	/*private double calculateCollisionDistanceToStart() {
		if(collisionIndex < 0) {
			throw new IllegalArgumentException("Der CollisionIndex wurde noch nicht gesetzt");
		}
	}*/
	
	//Berechnet die Distanz des Collision Points zum Start der PrototypeWall.
		//Verfolgt dabei den Verlauf der PrototypeWall, also nicht Fluglinie
	/*private double calculateCollisionDistanceToEnd() {
		if(collisionIndex < 0) {
			throw new IllegalArgumentException("Der CollisionIndex wurde noch nicht gesetzt");
		}
	}*/
	
	private Point2D getPointOfCollision(Rectangle2D rect, Line2D line) {
		if(Math.abs(line.getX1() - line.getX2()) < Math.abs(line.getY1() -line.getY2())) {
			//Wenn es eine Vertikale Linie ist, dann liegt das Rectangle entweder links oder rechts von der Linie
			//In diesem Fall nutze die Y-Koordinate des Mittelpunkts des Rectangles und die X-Koordinate der Linie
			Point2D p = new Point2D.Double(line.getX1(),rect.getCenterY());
			
			return p;
		}
		else {
			//Wenn es eine horizontale Linie ist, dann liegt das Rectangle entweder über oder unter der Linie
			//In diesem Fall nutze die X-Koordinate des Mittelpunkts des Rectangles und die Y-Koordinate der Linie
			Point2D p = new Point2D.Double(rect.getCenterX(), line.getY1());
			
			return p;
		}
	}
	
	public ArrayList<Point2D> reverse() {
		ArrayList<Point2D> toReturn = new ArrayList<Point2D>();
		for(Point2D point : lines) {
			toReturn.add(0, point);
			
			//System.out.println("Punkt: "+point);
			
		}
		//lines = toReturn;
		
		/*for(Point2D point : lines) {
			
			System.out.println("Punkt: "+point);
			
		}*/
		return toReturn;
	}

	public void update(float pDeltaTime) {
		for(int i = 0; i<collisionTimes.size(); i++) 
		{
			Float d = collisionTimes.get(i);
			
			Float f = new Float(d+pDeltaTime);
			
			collisionTimes.set(i,f);
		}
		// TODO Auto-generated method stub
		
	}
}