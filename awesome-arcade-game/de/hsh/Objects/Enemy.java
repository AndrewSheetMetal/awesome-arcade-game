package de.hsh.Objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Random;

import de.hsh.Battlefield;

public class Enemy extends Movable 
{	
	// ALEX
	private final int HORIZONTAL = 0, VERTICAL = 1;	
	private Color mColor;
	private int mSpeed;
	private Point2D mDirection;
	
	// ALEX
		// Prï¿½fen, ob ein Gegner gegen eine Wand stï¿½ï¿½t und ggf. seine Richtung ï¿½ndern.
		public void handleIntersectionWithWall(List<Battlefield> pBattlefields, double speed)
		{
			for(Battlefield lBf : pBattlefields)
			{
				// Gegner befindet sich im aktuellen Spielfeld und berï¿½hrt eine Wand.
				if(intersectsBattlefield(lBf))
				{
					// 0: 0 = waagerecht, 1 = senkrecht; 1 : Anzahl Pixel innerhalb
					double[] lNearest = {0, 0};
					// Falls der Gegner ï¿½ber eine Wand hinausragt, wird hier angegeben, wie weit er hineinragt.
					double lDepth = 0;
					// Der Index des zweiten Punktes einer Wand.
					int lSecond = 0;
					// Gibt an, ob der Gegner ï¿½ber einen Teil der Mauer hinausragt.
					boolean lIntersected = false;
					// Das Wandobjekt, mit dem Kollisionen untersucht werden.
					Line2D lWall;
					// Alle Wï¿½nde des Schlachtfeldes durchsuchen und die passende Wand finden.
					for(int i = 0; i < lBf.npoints; i++)
					{
						lSecond = (i == (lBf.npoints - 1)) ? 0 : (i+1);			
						// Wandobjekt erstellen.
						lWall = new Line2D.Double(new Point2D.Double(lBf.xpoints[i],  lBf.ypoints[i]),
								new Point2D.Double(lBf.xpoints[lSecond], lBf.ypoints[lSecond]));
						// Gegner schneidet Wand.
						if(intersectsWall(lWall))
						{
							lDepth = 0;
							// Wand senkrecht.
							if(lBf.xpoints[i] == lBf.xpoints[lSecond])
							{
								// Ragt der Gegner oben etwas ï¿½ber die Mauer hinaus?
								if(this.getPosition().getY() < Math.min(lBf.ypoints[i], lBf.ypoints[lSecond]))
								{
									// Berechnen, wie weit der Gegner von oben innerhalb des Wandbereiches ist.
									lDepth = this.getSize().getHeight() - (Math.min(lBf.ypoints[i], lBf.ypoints[lSecond])
											- this.getPosition().getY());	
									lIntersected = true;
								}
								// Ragt der Gegner unten etwas ï¿½ber die Mauer hinaus?
								if((this.getPosition().getY() + this.getSize().getHeight()) > Math.max(lBf.ypoints[i], lBf.ypoints[lSecond]))
								{
									// Befindet sich der Gegner von unten mehr innerhalb als von oben?
									if((int)(Math.max(lBf.ypoints[i], lBf.ypoints[lSecond]) - this.getPosition().getY()) > lDepth)
									{
										// Berechnen, wie weit der Gegner von unten innerhalb des Wandbereiches ist.
										lDepth = Math.max(lBf.ypoints[i], lBf.ypoints[lSecond]) - this.getPosition().getY();	
									}
									lIntersected = true;
								}
								if(!lIntersected)
								{
									lDepth = this.getSize().getHeight();
								}
								// Mehr Pixel des Gegner innerhalb der Mauer oder Abstand kleiner?
								if(lDepth > lNearest[1])
								{
									lNearest[0] = VERTICAL;
									lNearest[1] = lDepth;
								}
							}
							// Wand waagerecht.
							else
							{
								// Ragt der Gegner links etwas ï¿½ber die Mauer hinaus?
								if(this.getPosition().getX() < Math.min(lBf.xpoints[i], lBf.xpoints[lSecond]))
								{
									// Berechnen, wie weit der Gegner von links innerhalb des Wandbereiches ist.
									lDepth = this.getSize().getWidth() - (Math.min(lBf.xpoints[i], lBf.xpoints[lSecond])
											- this.getPosition().getX());	
									lIntersected = true;
								}
								// Ragt der Gegner rechts etwas ï¿½ber die Mauer hinaus?
								if((this.getPosition().getX() + this.getSize().getWidth()) > Math.max(lBf.xpoints[i], lBf.xpoints[lSecond]))
								{
									// Befindet sich der Gegner von rechts mehr innerhalb als von links?
									if((int)(Math.max(lBf.xpoints[i], lBf.xpoints[lSecond]) - this.getPosition().getX()) > lDepth)
									{
										// Berechnen, wie weit der Gegner von rechts innerhalb des Wandbereiches ist.
										lDepth = Math.max(lBf.xpoints[i], lBf.xpoints[lSecond]) - this.getPosition().getX();	
									}
									lIntersected = true;
								}
								if(!lIntersected)
								{
									lDepth = this.getSize().getWidth();
								}
								// Mehr Pixel des Gegner innerhalb der Mauer als bei der vorigen?
								if(lDepth > lNearest[1])
								{
									lNearest[0] = HORIZONTAL;
									lNearest[1] = lDepth;
								}
							}
						}
					}
					changeDirectionFromWall((int)lNearest[0], speed);				
				}
			}		
		}
	
	// ALEX: setDirection gelöscht und in Player und Enemy eingefügt.	
	public void setRandomDirection()
	{
		Random lRandom = new Random();
		// Zufälligen Winkel erzeugen.
		double lDir = 360 * lRandom.nextDouble();
		// Passenden Quadranten bestimmen.
		int lQuarter = (int)(lDir / 90);
		lDir -= lQuarter * 90;
		lDir /= 90;
		switch(lQuarter)
		{
			// Gegner bewegt sich nach oben rechts.
			case 0:
				setDirection(new Point2D.Double(lDir, -(1 - lDir)));
				break;
			// Gegner bewegt sich nach unten rechts.
			case 1:
				setDirection(new Point2D.Double(1 - lDir, lDir));
				break;
			// Gegner bewegt sich nach unten links.
			case 2:
				setDirection(new Point2D.Double(-lDir, 1 - lDir));
				break;
			// Gegner bewegt sich nach oben links.
			case 3:
				setDirection(new Point2D.Double(-(1 - lDir), -lDir));
				break;
		}
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
		
	// Liefert true, wenn der Ball den Rand eines Schlachtfeldes berï¿½hrt.
	private boolean intersectsBattlefield(Battlefield pBattlefield)
	{
		return pBattlefield.intersects(this.getPosition().getX(), this.getPosition().getY(),
					this.getSize().getWidth(), this.getSize().getHeight())
				&& (!pBattlefield.contains(this.getPosition().getX(), this.getPosition().getY(),
					this.getSize().getWidth(), this.getSize().getHeight()));
	}
	
	// Liefert true, wenn der Ball die Linie berï¿½hrt.
	private boolean intersectsWall(Line2D pWall)
	{
		return pWall.intersects(this.getPosition().getX(), this.getPosition().getY(),
							this.getSize().getWidth(), this.getSize().getHeight());
	}	
	
	// ï¿½ndert die Richtung des Balls abhï¿½ngig von der Art der Wand. (0 = horizontal; 1 = vertikal)
	protected void changeDirectionFromWall(int pWallType, double speed)
	{
		// Nahste Wand ist waagerecht.
		if(pWallType == HORIZONTAL)
		{	
			// Ball bewegte sich nach unten.
			if(this.getDirection().getY() > 0)
			{
				this.setPosition(new Point2D.Double(this.getPosition().getX(), 
						this.getPosition().getY() - speed));
			}
			// Ball bewegte sich nach oben.
			else
			{
				this.setPosition(new Point2D.Double(this.getPosition().getX(), 
						this.getPosition().getY() + speed));							
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
				this.setPosition(new Point2D.Double(this.getPosition().getX() - speed, 
					this.getPosition().getY()));							
			}
			// Ball bewegte sich nach links
			else
			{
				this.setPosition(new Point2D.Double(this.getPosition().getX() + speed, 
					this.getPosition().getY()));							
			}
			this.setDirection(new Point2D.Double(-this.getDirection().getX(), 
				this.getDirection().getY()));								
		}
	}
	
	// ALEX
	public Point2D getDirection()
	{
		return mDirection;
	}
	
	// ALEX
	public void setDirection(Point2D pDirection) {
		mDirection = pDirection;		
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

}
