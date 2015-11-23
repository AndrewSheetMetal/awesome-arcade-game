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
import java.awt.Polygon;

import de.hsh.Battlefield;

public class Ball extends Enemy {	

	private Color mColor;
	
	// ALEX
	private int mSpeed;
		
	public Ball() {
		
		// Muss am Anfang 0 sein. Wird in GameScreen automatisch geändert.
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
		return new Point((int)getPosition().getX()+getSize().width/2,
				(int)getPosition().getY()+getSize().height/2);
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
	// Prüfen, ob ein Ball gegen eine Wand stößt und ggf. seine Richtung ändern.
	public void handleIntersection(List<Battlefield> pBattlefields, int pSpeed)
	{
		for(Battlefield lBf : pBattlefields)
		{
			// Ball befindet sich im aktuellen Spielfeld und berührt eine Wand.
			if((lBf.intersects(this.getPosition().getX(), this.getPosition().getY(),
					this.getSize().getWidth(), this.getSize().getHeight()))
				&& (!lBf.contains(this.getPosition().getX(), this.getPosition().getY(),
					this.getSize().getWidth(), this.getSize().getHeight())))
			{
				// 0: Abstand; 1: 0 = waagerecht, 1 = senkrecht; 2 = Anzahl Pixel innerhalb
				int[] lNearest = new int[3];
				int lPixelInside = 0;
				// Der Index des zweiten Punktes einer Wand.
				int lSecond = 0;
				// Gibt an, ob der Ball über einen Teil der Mauer hinausragt.
				boolean lIntersected = false;
				lNearest[0] = Integer.MAX_VALUE;
				lNearest[1] = 0;
				lNearest[2] = 0;
				// Alle Wände des Schlachtfeldes durchsuchen und die passende Wand finden.
				for(int i = 0; i < lBf.npoints; i++)
				{
					lSecond = (i == (lBf.npoints - 1)) ? 0 : (i+1);					
					// Wand senkrecht.
					if(lBf.xpoints[i] == lBf.xpoints[lSecond])
					{
						// Ball im Bereich der Wand und berührt sie.
						if((this.getPosition().getY() + this.getSize().getHeight()) > Math.min(lBf.ypoints[i], (lBf.ypoints[lSecond]))
								&& (this.getPosition().getY() <= Math.max(lBf.ypoints[i], lBf.ypoints[lSecond]))
								&& ((Math.abs(lBf.xpoints[i] - this.getCenter().getX())) <= (this.getSize().getWidth() / 2)))
						{
							lIntersected = false;
							lPixelInside = 0;
							// Ragt der Ball oben etwas über die Mauer hinaus?
							if(this.getPosition().getY() < Math.min(lBf.ypoints[i], lBf.ypoints[lSecond]))
							{
								// Berechnen, wie weit der Ball von oben innerhalb des Wandbereiches ist.
								lPixelInside = (int) (this.getSize().getHeight() - (Math.min(lBf.ypoints[i], lBf.ypoints[lSecond])
										- this.getPosition().getY()));	
								lIntersected = true;
							}
							// Ragt der Ball unten etwas über die Mauer hinaus?
							if((this.getPosition().getY() + this.getSize().getHeight()) > Math.max(lBf.ypoints[i], lBf.ypoints[lSecond]))
							{
								// Befindet sich der Ball von unten mehr innerhalb als von oben?
								if((int)(Math.max(lBf.ypoints[i], lBf.ypoints[lSecond]) - this.getPosition().getY()) > lPixelInside)
								{
									// Berechnen, wie weit der Ball von unten innerhalb des Wandbereiches ist.
									lPixelInside = (int)(Math.max(lBf.ypoints[i], lBf.ypoints[lSecond]) - this.getPosition().getY());	
									lIntersected = true;
								}
							}
							if(!lIntersected)
							{
								lPixelInside = 50;
							}
							// Mehr Pixel des Balls innerhalb der Mauer oder Abstand kleiner?
							if((lPixelInside > lNearest[2]))
								//	|| (Math.abs(lBf.xpoints[i] - this.getCenter().getX()) < lNearest[0]))
							{
								lNearest[0] = (int)Math.abs(lBf.xpoints[i] - this.getCenter().getX());
								lNearest[1] = 1;
								lNearest[2] = lPixelInside;
							}
						}		
					}
					// Wand waagerecht.
					else
					{
						// Ball im Bereich der Wand und berührt sie.
						if((this.getPosition().getX() + this.getSize().getWidth()) > Math.min(lBf.xpoints[i], (lBf.xpoints[lSecond]))
								&& (this.getPosition().getX() <= Math.max(lBf.xpoints[i], lBf.xpoints[lSecond]))
								&& ((Math.abs(lBf.ypoints[i] - this.getCenter().getY())) <= (this.getSize().getWidth() / 2)))
						{
							lIntersected = false;
							lPixelInside = 0;
							// Ragt der Ball links etwas über die Mauer hinaus?
							if(this.getPosition().getX() < Math.min(lBf.xpoints[i], lBf.xpoints[lSecond]))
							{
								// Berechnen, wie weit der Ball von links innerhalb des Wandbereiches ist.
								lPixelInside = (int) (this.getSize().getWidth() - (Math.min(lBf.xpoints[i], lBf.xpoints[lSecond])
										- this.getPosition().getX()));	
								lIntersected = true;
							}
							// Ragt der Ball rechts etwas über die Mauer hinaus?
							if((this.getPosition().getX() + this.getSize().getWidth()) > Math.max(lBf.xpoints[i], lBf.xpoints[lSecond]))
							{
								// Befindet sich der Ball von rechts mehr innerhalb als von links?
								if((int)(Math.max(lBf.xpoints[i], lBf.xpoints[lSecond]) - this.getPosition().getX()) > lPixelInside)
								{
									// Berechnen, wie weit der Ball von rechts innerhalb des Wandbereiches ist.
									lPixelInside = (int)(Math.max(lBf.xpoints[i], lBf.xpoints[lSecond]) - this.getPosition().getX());	
									lIntersected = true;
								}
							}
							if(!lIntersected)
							{
								lPixelInside = 50;
							}
							// Mehr Pixel des Balls innerhalb der Mauer als bei der vorigen?
							if((lPixelInside > lNearest[2]))
									//|| (Math.abs(lBf.ypoints[i] - this.getPosition().getY()) < lNearest[0]))
							{
								lNearest[0] = (int)Math.abs(lBf.ypoints[i] - this.getCenter().getY());
								lNearest[1] = 0;
								lNearest[2] = lPixelInside;
							}
						}
					}
				}
				// Nahste Wand ist waagerecht.
				if(lNearest[1] == 0)
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
