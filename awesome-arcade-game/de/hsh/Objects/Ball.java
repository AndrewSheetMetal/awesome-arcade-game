package de.hsh.Objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.util.List;
import java.awt.Polygon;

import de.hsh.Battlefield;

public class Ball extends Enemy {	

	// ALEX
	private final int HORIZONTAL = 0, VERTICAL = 1;
	
	private Color mColor;
	
	// ALEX
	private int mSpeed;
		
	public Ball() {
		
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
	// Pr�fen, ob ein Ball gegen eine Wand st��t und ggf. seine Richtung �ndern.
	public void handleIntersection(List<Battlefield> pBattlefields, double speed)
	{
		for(Battlefield lBf : pBattlefields)
		{
			// Ball befindet sich im aktuellen Spielfeld und ber�hrt eine Wand.
			if(intersectsBattlefield(lBf))
			{
				// 0: 0 = waagerecht, 1 = senkrecht; 1 : Anzahl Pixel innerhalb
				double[] lNearest = {0, 0};
				// Falls der Ball �ber eine Wand hinausragt, wird hier angegeben, wie weit er hineinragt.
				double lDepth = 0;
				// Der Index des zweiten Punktes einer Wand.
				int lSecond = 0;
				// Gibt an, ob der Ball �ber einen Teil der Mauer hinausragt.
				boolean lIntersected = false;
				// Das Wandobjekt, mit dem Kollisionen untersucht werden.
				Line2D lWall;
				// Alle W�nde des Schlachtfeldes durchsuchen und die passende Wand finden.
				for(int i = 0; i < lBf.npoints; i++)
				{
					lSecond = (i == (lBf.npoints - 1)) ? 0 : (i+1);			
					// Wandobjekt erstellen.
					lWall = new Line2D.Double(new Point2D.Double(lBf.xpoints[i],  lBf.ypoints[i]),
							new Point2D.Double(lBf.xpoints[lSecond], lBf.ypoints[lSecond]));
					// Ball schneidet Wand.
					if(intersectsWall(lWall))
					{
						lDepth = 0;
						// Wand senkrecht.
						if(lBf.xpoints[i] == lBf.xpoints[lSecond])
						{
							// Ragt der Ball oben etwas �ber die Mauer hinaus?
							if(this.getPosition().getY() < Math.min(lBf.ypoints[i], lBf.ypoints[lSecond]))
							{
								// Berechnen, wie weit der Ball von oben innerhalb des Wandbereiches ist.
								lDepth = this.getSize().getHeight() - (Math.min(lBf.ypoints[i], lBf.ypoints[lSecond])
										- this.getPosition().getY());	
								lIntersected = true;
							}
							// Ragt der Ball unten etwas �ber die Mauer hinaus?
							if((this.getPosition().getY() + this.getSize().getHeight()) > Math.max(lBf.ypoints[i], lBf.ypoints[lSecond]))
							{
								// Befindet sich der Ball von unten mehr innerhalb als von oben?
								if((int)(Math.max(lBf.ypoints[i], lBf.ypoints[lSecond]) - this.getPosition().getY()) > lDepth)
								{
									// Berechnen, wie weit der Ball von unten innerhalb des Wandbereiches ist.
									lDepth = Math.max(lBf.ypoints[i], lBf.ypoints[lSecond]) - this.getPosition().getY();	
								}
								lIntersected = true;
							}
							if(!lIntersected)
							{
								lDepth = this.getSize().getHeight();
							}
							// Mehr Pixel des Balls innerhalb der Mauer oder Abstand kleiner?
							if(lDepth > lNearest[1])
							{
								lNearest[0] = VERTICAL;
								lNearest[1] = lDepth;
							}
						}
						// Wand waagerecht.
						else
						{
							// Ragt der Ball links etwas �ber die Mauer hinaus?
							if(this.getPosition().getX() < Math.min(lBf.xpoints[i], lBf.xpoints[lSecond]))
							{
								// Berechnen, wie weit der Ball von links innerhalb des Wandbereiches ist.
								lDepth = this.getSize().getWidth() - (Math.min(lBf.xpoints[i], lBf.xpoints[lSecond])
										- this.getPosition().getX());	
								lIntersected = true;
							}
							// Ragt der Ball rechts etwas �ber die Mauer hinaus?
							if((this.getPosition().getX() + this.getSize().getWidth()) > Math.max(lBf.xpoints[i], lBf.xpoints[lSecond]))
							{
								// Befindet sich der Ball von rechts mehr innerhalb als von links?
								if((int)(Math.max(lBf.xpoints[i], lBf.xpoints[lSecond]) - this.getPosition().getX()) > lDepth)
								{
									// Berechnen, wie weit der Ball von rechts innerhalb des Wandbereiches ist.
									lDepth = Math.max(lBf.xpoints[i], lBf.xpoints[lSecond]) - this.getPosition().getX();	
								}
								lIntersected = true;
							}
							if(!lIntersected)
							{
								lDepth = this.getSize().getWidth();
							}
							// Mehr Pixel des Balls innerhalb der Mauer als bei der vorigen?
							if(lDepth > lNearest[1])
							{
								lNearest[0] = HORIZONTAL;
								lNearest[1] = lDepth;
							}
						}
					}
				}
				changeDirectionFromIntersection((int)lNearest[0], speed);				
			}
		}		
	}
	
	
	
	// Liefert true, wenn der Ball den Rand eines Schlachtfeldes ber�hrt.
	private boolean intersectsBattlefield(Battlefield pBattlefield)
	{
		return pBattlefield.intersects(this.getPosition().getX(), this.getPosition().getY(),
					this.getSize().getWidth(), this.getSize().getHeight())
				&& (!pBattlefield.contains(this.getPosition().getX(), this.getPosition().getY(),
					this.getSize().getWidth(), this.getSize().getHeight()));
	}
	
	
	
	// Liefert true, wenn der Ball die Linie ber�hrt.
	private boolean intersectsWall(Line2D pWall)
	{
		return pWall.intersects(this.getPosition().getX(), this.getPosition().getY(),
							this.getSize().getWidth(), this.getSize().getHeight());
	}
	
	
	
	// �ndert die Richtung des Balls abh�ngig von der Art der Wand. (0 = horizontal; 1 = vertikal)
	private void changeDirectionFromIntersection(int pWallType, double speed)
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
	
	private boolean isOverBoarder()
	{
		return false;
	}
}
