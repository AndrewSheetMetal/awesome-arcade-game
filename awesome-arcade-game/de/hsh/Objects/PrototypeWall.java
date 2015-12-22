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
	
	
	private class Collision {
		//Public, da die Klasse nur der internen Strukturierung dient
		public Point2D collisionPoint; //Der Punkt, an dem die PrototypeWall von einem Ball berührt wurde
		public int index; //Der Index des Anfangspunkts der Linie, die vom Ball berührt wurde.
		public float radius; //Der Radius des Kreises, der sich um den Kollisionspunkt ausbreitet
		
		Collision(int index, Point2D collPoint) {
			this.index = index;
			this.collisionPoint = collPoint;
			radius = 0.0f;
		}
		
		public void update(float deltaTime) {
			radius += deltaTime;
		}
		
		public void draw(Graphics2D g) {
			//Ein kleiner Kreis
			g.drawOval((int)collisionPoint.getX()-3, (int)collisionPoint.getY()-3, 6, 6);
			
			
			g.draw(getCollisionImpact());
			//Ein großer Kreis, der sich vergrößert
			g.drawOval((int)(collisionPoint.getX()-radius), (int)(collisionPoint.getY()-radius), (int)radius*2, (int)radius*2);
		}
		
		//Liefert einen Umkreis um den Collisionpoint, der immer größer wird
		public Ellipse2D getCollisionImpact() {
			Ellipse2D e = new Ellipse2D.Double(collisionPoint.getX()-radius, collisionPoint.getY()-radius, radius*2, radius*2);
			return e;
		}
		
	}
	
	private ArrayList<Collision> collisions = new ArrayList<Collision>();	
	
	public PrototypeWall() {
		lines = new ArrayList<Point2D>();
	}
	
	public void draw(Graphics g, Point2D p) { //Der Punkt p ist der Teil der prototypeWall, der sich noch ändert. Also die aktuelle Position des Player
		Graphics2D g2d = (Graphics2D) g;
		
		/* Point of Collision zeichnen, falls er existierts
		 * */
		Area outside = new Area(new Rectangle2D.Double(0, 0, 500, 500));
		g2d.setColor(Color.MAGENTA);
		for(int i=0; i<collisions.size(); i++) {
			collisions.get(i).draw(g2d);
			
			Area inside = new Area(collisions.get(i).getCollisionImpact());
			outside.subtract(inside);
		}
		g2d.setClip(outside);
		
		
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
		collisions.clear();
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
		if(lines.size() >= 1 && collisions.size() == 0) {
			
			Line2D lastLine = new Line2D.Double(lines.get(lines.size()-1),playerCenter);
			if(rect.intersectsLine(lastLine)) {
				Collision e = new Collision(lines.size()-1,getPointOfCollision(rect,lastLine));
				collisions.add(e);
				return true;
			}
			
			if(lines.size() >= 1) {
				for(int i = 0; i<lines.size()-1; i++) {
					Line2D line = new Line2D.Double(lines.get(i),lines.get(i+1));
					if(rect.intersectsLine(line)) {
						Collision e = new Collision(i,getPointOfCollision(rect,line));
						collisions.add(e);
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
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
	
	/*
	 * Gibt eine ArrayList mit der umgekehrten Reihenfolge der Punkt aus
	 * */
	public ArrayList<Point2D> reverse() {
		ArrayList<Point2D> toReturn = new ArrayList<Point2D>();
		for(Point2D point : lines) {
			toReturn.add(0, point);
		}
		return toReturn;
	}

	
	/*
	 * Aktualisiert die Zeiten/Radien der CollisionPoints
	 * */
	public void update(float pDeltaTime) {
		pDeltaTime *= 2f;
		
		for(int i = 0; i<collisions.size(); i++) 
		{
			collisions.get(i).update(pDeltaTime);
		}
		// TODO Auto-generated method stub
		
	}

	public boolean isValid() {
		return collisions.size()==0;
	}

	public boolean isPlayerCatched(Player player) {
		for(int i=0; i<collisions.size(); i++) {
			if(collisions.get(i).collisionPoint.distance(player.getPosition()) < collisions.get(i).radius + player.getBounds().getWidth()) {
				return true;
				
			}
			
		}
		
		
		return false;
	}
}