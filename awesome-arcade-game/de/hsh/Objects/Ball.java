package de.hsh.Objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.List;

import de.hsh.Battlefield;

public class Ball extends Enemy {	

	private Color mColor;
	
	// ALEX
	private int mSpeed;
		
	public Ball() {
		
		// ALEX: Muss sp�ter zuf�llig gew�hlt werden.
		setPosition(new Point2D.Double(200,100));
		// Muss am Anfang 0 sein. Wird in GameScreen automatisch ge�ndert.
		setDirection(new Point2D.Double(0, 0));
		
		setColor(Color.WHITE);
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		//RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		//rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		
		//g2d.setRenderingHint(null, rh);
		
		Dimension size = getSize();
		double w = size.getWidth();
		double h = size.getHeight();
		
		Ellipse2D circle2D = new Ellipse2D.Double();

		
		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(getColor());
		
		
		Point2D pos = this.getPosition();
		
		circle2D.setFrame(pos.getX(), pos.getY(), w, h);
		
		//pos.x += 1;
		//pos.y += 1;
		//pos.x = pos.x % 300;
		//pos.y = pos.y % 300;
		//System.out.println("Position: "+pos);
		
		this.setPosition(pos);
		
		g2d.draw(circle2D);	

	}
	
	public Point getCenter() {
		return new Point((int)getPosition().getX()+getSize().width/2,(int)getPosition().getY()+getSize().height/2);
	}
	
	public Dimension getSize() {
		Dimension toReturn = new Dimension();
		toReturn.setSize(50, 50);
		return toReturn;
	}

	public Color getColor() {
		return mColor;
	}
	
	public void setColor(Color pColor) {
		mColor = pColor;
	}

	public void setSpeed(int pSpeed)
	{
		mSpeed = pSpeed;
	}
	
	public int getSpeed()
	{
		return mSpeed;
	}
	
	// ALEX
	// Pr�fen, ob ein Ball gegen eine Wand st��t und ggf. seine Richtung �ndern.
	public void handleIntersection(List<Battlefield> pBattlefields, int pSpeed)
	{
		for(Battlefield lBf : pBattlefields)
		{
			// Ball befindet sich im aktuellen Spielfeld und ber�hrt eine Wand.
			if((lBf.intersects(this.getPosition().getX(), this.getPosition().getY(),
					this.getSize().getWidth(), this.getSize().getHeight()))
				&& (!lBf.contains(this.getPosition().getX(), this.getPosition().getY(),
					this.getSize().getWidth(), this.getSize().getHeight())))
			{
				// X: Abstand; Y: 0 = waagerecht, 1 = senkrecht 
				Point lNearest = new Point(Integer.MAX_VALUE, 0);	
				// Alle W�nde des Schlachtfeldes durchsuchen und die passende Wand finden.
				for(int i = 0; i < lBf.npoints - 1; i++)
				{
					// Wand senkrecht.
					if(lBf.xpoints[i] == lBf.xpoints[i+1])
					{
						// Ball im Bereich der Wand?
						if((this.getPosition().getY() /*+ 1*/) >= Math.min(lBf.ypoints[i], (lBf.ypoints[i+1]))
								&& ((this.getPosition().getY() /*- 1*/) <= Math.max(lBf.ypoints[i], lBf.ypoints[i+1])))
						{
							// X-Abstand von Ball und Wand kleiner dem aktuellen Maximum?
							if(Math.abs(lBf.xpoints[i] - this.getPosition().getX()) < lNearest.x)
							{
								lNearest.x = (int)Math.abs(lBf.xpoints[i] - this.getPosition().getX());
								lNearest.y = 1;
							}
						}						
					}
					// Wand waagerecht.
					else
					{
						// Ball im Bereich der Wand?
						if((this.getPosition().getX() /*+ 1*/) >= Math.min(lBf.xpoints[i], lBf.xpoints[i+1])
								&& ((this.getPosition().getX() /*- 1*/) <= Math.max(lBf.xpoints[i], lBf.xpoints[i+1])))
						{
							// Y-Abstand von Ball und Wand kleiner dem aktuellen Maximum?
							if(Math.abs(lBf.ypoints[i] - this.getPosition().getY()) < lNearest.x)
							{
								lNearest.x = (int)Math.abs(lBf.ypoints[i] - this.getPosition().getY());
								lNearest.y = 0;
							}
						}
					}
				}
				// Nahste Wand ist waagerecht.
				if(lNearest.y == 0)
				{
					// Ball bewegte sich nach unten.
					if(this.getDirection().getY() > 0)
					{
						this.setPosition(new Point2D.Double(this.getPosition().getX(), 
								this.getPosition().getY() - pSpeed));
					}
					// Ball bewegte sich nach oben.
					else
					{
						this.setPosition(new Point2D.Double(this.getPosition().getX(), 
								this.getPosition().getY() + pSpeed));							
					}
					this.setDirection(new Point2D.Double(this.getDirection().getX(), 
						-this.getDirection().getY()));
				}
				// Nahste Wand ist senkrecht.
				else
				{
					// Ball bewegte sich nach rechts.
					if(this.getDirection().getX() > 0)
					{
						this.setPosition(new Point2D.Double(this.getPosition().getX() - pSpeed, 
								this.getPosition().getY()));							
					}
					// Ball bewegte sich nach links
					else
					{
						this.setPosition(new Point2D.Double(this.getPosition().getX() + pSpeed, 
								this.getPosition().getY()));							
					}
					this.setDirection(new Point2D.Double(-this.getDirection().getX(), 
							this.getDirection().getY()));								
				}
			}
		}		
	}
}
