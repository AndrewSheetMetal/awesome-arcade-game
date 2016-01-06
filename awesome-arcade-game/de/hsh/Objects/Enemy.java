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
import de.hsh.GameScreen;
import de.hsh.Main;



public abstract class Enemy extends Movable 
{	
	private final int DEBUG = 1;
	
	private static int sColCounter = 0;
	
	private final int HORIZONTAL = 0, VERTICAL = 1;	
	private Color mColor;
	private int mSpeed;
	protected Class mType;
	// Gibt an, ob bereits auf Kollision fï¿½r diesen Zyklus ï¿½berprï¿½ft wurde. 
	private boolean mHandled;
	
	// Konstruktor.
	public Enemy()
	{
		// Muss am Anfang 0 sein. Wird in GameScreen automatisch geï¿½ndert.
		setDirection(new Point2D.Double(0, 0));	
	}
	
	// Prï¿½fen, ob ein Gegner gegen eine Wand stï¿½ï¿½t und ggf. seine Richtung ï¿½ndern.
	public void handleIntersectionWithWall(List<Battlefield> pBattlefields, double speed)
	{
		for(Battlefield lBf : pBattlefields)
		{
			// Gegner befindet sich im aktuellen Spielfeld und berï¿½hrt eine Wand.
			if(hitsBattlefieldBorder(lBf))
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
	
	// ALEX: setDirection gelï¿½scht und in Player und Enemy eingefï¿½gt.	
	public void setRandomDirection()
	{
		Random lRandom = new Random();
		// Zufï¿½lligen Winkel erzeugen.
		double lDir = 360 * lRandom.nextDouble();
		// Winkel in Richtung umwandeln und setzen.
		setDirection(angleToDirection(lDir));
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
		
		g2d.fill(circle2D);	

	}
	
	@Override
	public Point getCenter() {
		return new Point((int)getPosition().getX()+getSize().width/2,
				(int)getPosition().getY()+getSize().height/2);
	}
	
	@Override
	public Dimension getSize() {
		Dimension toReturn = new Dimension();
		toReturn.setSize(50, 50);
		return toReturn;
	}	
		
	// Liefert true, wenn der Gegner den Rand eines Schlachtfeldes berï¿½hrt.
	protected boolean hitsBattlefieldBorder(Battlefield pBattlefield)
	{
		return intersectsBattlefield(pBattlefield) && !insideBattlefield(pBattlefield);
	}
	
	// Liefert true, wenn der Gegner das Schlachtfeld berï¿½hrt.
	private boolean intersectsBattlefield(Battlefield pBattlefield)
	{
		return pBattlefield.intersects(this.getPosition().getX(), this.getPosition().getY(),
				this.getSize().getWidth(), this.getSize().getHeight());
	}
	
	// Liefert true, wenn der Gegner komplett innerhalb des Schlachtfeldes ist.
	protected boolean insideBattlefield(Battlefield pBattlefield)
	{
		return pBattlefield.contains(this.getPosition().getX(), this.getPosition().getY(),
				this.getSize().getWidth(), this.getSize().getHeight());
	}
	
	// Liefert true, wenn der Gegner die Linie berï¿½hrt.
	private boolean intersectsWall(Line2D pWall)
	{
		return pWall.intersects(this.getPosition().getX(), this.getPosition().getY(),
							this.getSize().getWidth(), this.getSize().getHeight());
	}	
	
	// ï¿½ndert die Richtung des Gegners abhï¿½ngig von der Art der Wand. (0 = horizontal; 1 = vertikal)
	protected void changeDirectionFromWall(int pWallType, double speed)
	{
		// Nahste Wand ist waagerecht.
		if(pWallType == HORIZONTAL)
		{	
			// Gegner bewegte sich nach unten.
			if(this.getDirection().getY() > 0)
			{
				this.setPosition(new Point2D.Double(this.getPosition().getX(), 
						this.getPosition().getY() - speed));
			}
			// Gegner bewegte sich nach oben.
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
			// Gegner bewegte sich nach rechts.
			if(this.getDirection().getX() > 0)
			{
				this.setPosition(new Point2D.Double(this.getPosition().getX() - speed, 
					this.getPosition().getY()));							
			}
			// Gegner bewegte sich nach links
			else
			{
				this.setPosition(new Point2D.Double(this.getPosition().getX() + speed, 
					this.getPosition().getY()));							
			}
			this.setDirection(new Point2D.Double(-this.getDirection().getX(), 
				this.getDirection().getY()));								
		}
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
	
	public abstract void spawn(List<Battlefield> pBattlefields);
	
	// Erzeugt eine zufï¿½llig gewï¿½hlte gï¿½ltitge Position. 
	// TODO: Ohne zweiten Parameter lï¿½sen.
	public void createSpawnPosition(List<Battlefield> pBattlefields, int pType)
	{
		boolean lCorrectSpawn, lInBattlefield, lInsideArea;
		
		lInsideArea = (pType == 0);
		
		do
		{
			// TODO: ok?
			lCorrectSpawn = true;
			lInBattlefield = false;
			this.setPosition(getRandomSpawnPoint());
			
			//boolean test1, test2;
			
			if(this.intersectsFriend() == null)
			{
				for(Battlefield lBf : pBattlefields)
				{
					if(this.insideBattlefield(lBf))
					{
						lInBattlefield = true;
					}
					if(this.hitsBattlefieldBorder(lBf))
					{
						lCorrectSpawn = false;
						break;
					}
				}
			}
			else
			{
				lCorrectSpawn = false;
			}
		}while(!lCorrectSpawn || (lInBattlefield != lInsideArea));
	}
		
	// Liefert einen zufï¿½llig erzeugten Spawnpoint.
	protected Point2D getRandomSpawnPoint()
	{
		Point2D lCoordinates = new Point2D.Double();
		lCoordinates.setLocation(getRandomCoordinate(true), getRandomCoordinate(false));
		return lCoordinates;
	}
	
	// Liefert eine zufï¿½llige Koordinate innerhalb des Spielfeldes.
	private double getRandomCoordinate(boolean pWidth)
	{
		Random lRandom = new Random();
		double lDiff = pWidth ? this.getSize().getWidth() : this.getSize().getHeight();
		return lRandom.nextDouble() * (Main.SIZE - lDiff);
	}

	// Liefert ggf. einen Gegner, der vom aktuellen Gegner berï¿½hrt wird.
	protected Enemy intersectsFriend() 
	{
		for(Enemy lEnemy : GameScreen.EnemyList)
		{
			// TODO: Auf Typgleichheit prï¿½fen.
			// Vorsicht: Hier wird angenommen, dass die Gegner kreisrund sind.
			if((lEnemy != this) && (lEnemy.getPosition() != null) 
					&& intersectsWithMovable(lEnemy))
			{
				if (DEBUG == 1)
				{
					sColCounter++;
					System.out.println("Collision #" + sColCounter + "\nthis.position.x = " + this.getPosition().getX() + " this.position.y = "
							+ this.getPosition().getY() + "\nother.position.x = " + lEnemy.getPosition().getX()
							+ " other.position.y = " + lEnemy.getPosition().getY() + "\n");
				}
				return lEnemy;
			}
		}
		return null;
	}
	
	// Liefert true, wenn der Gegner den übergebenen Gegner berührt.
	public boolean intersectsWithMovable(Movable pMovable)
	{
		return (this.getCenter().distance(pMovable.getCenter()) 
				<= ((this.getSize().getWidth() / 2) + (pMovable.getSize().getWidth() / 2)));
	}
	
	public void setHandled(boolean pHandled)
	{
		mHandled = pHandled;
	}
	
	public boolean getHandled()
	{
		return mHandled;
	}
	
	// Prüft, ob sich Gegner berühren und lässt sie ggf. abprallen.
	public void handleIntersectionWithFriends()
	{
		// Alle Handles auf false setzen.
		for(Enemy lEnemy : GameScreen.EnemyList)
		{
			lEnemy.setHandled(false);
		}
		// Auf Kollisionen ï¿½berprï¿½fen.
		Enemy lEnemy = this.intersectsFriend();
		if((lEnemy != null) && (!this.getHandled() || !lEnemy.getHandled()))
		{
			double lX, lY, lAngle;
			lX = Math.abs(lEnemy.getPosition().getX() - this.getPosition().getX());
			lY = Math.abs(lEnemy.getPosition().getY() - this.getPosition().getY());
			lAngle = getAngle(lX, lY);
			// Zwischen 0ï¿½ und 180ï¿½.
			if(lX > 0)
			{
				// Zwischen 0ï¿½ und 90ï¿½.
				if(lY < 0)
				{
					lAngle = 90 - lAngle;
				}
				// Zwischen 90ï¿½ und 180ï¿½.
				else
				{
					lAngle += 90;
				}
			}
			// Zwischen 180ï¿½ und 360ï¿½
			else
			{
				// Zwischen 180ï¿½ und 270ï¿½.
				if(lY > 0)
				{
					lAngle = 270 - lAngle;
				}
				// Zwischen 270ï¿½ und 360ï¿½.
				else
				{
					lAngle += 270;
				}
			}
			// Diesen Gegner auf aktuellen Winkel setzen.
			this.setDirection(angleToDirection(lAngle));
			// Anderen Gegner auf gegenï¿½berliegenden Winkel setzen.
			lAngle += 180;
			lAngle -= (int)(lAngle / 360);
			lEnemy.setDirection(angleToDirection(lAngle));
			
			this.setHandled(true);
			lEnemy.setHandled(true);
			
			// TODO: Zum Testen eingebaut.
			/*
			while(intersectsWithFriend(lEnemy))
			{
				this.setPosition(new Point2D.Double(this.getPosition().getX() + (0.1 * this.getDirection().getX()),
					this.getPosition().getY() + (0.1 * this.getDirection().getY())));
				lEnemy.setPosition(new Point2D.Double(lEnemy.getPosition().getX() + (0.1 * lEnemy.getDirection().getX()),
					lEnemy.getPosition().getY() + (0.1 * lEnemy.getDirection().getY())));
			}
			*/
			this.setPosition(new Point2D.Double(this.getPosition().getX() + (this.getSpeed() * this.getDirection().getX()),
					this.getPosition().getY() + (this.getSpeed() * this.getDirection().getY())));
				lEnemy.setPosition(new Point2D.Double(lEnemy.getPosition().getX() + (lEnemy.getSpeed() * lEnemy.getDirection().getX()),
					lEnemy.getPosition().getY() + (lEnemy.getSpeed() * lEnemy.getDirection().getY())));
		}
	}
	
	// Berechnet den Winkel zwischen Gegen- und Ankathete.
	private double getAngle(double lAn, double lGeg)
	{
		return (Math.atan(lGeg / lAn) * 360) / (2*Math.PI);
	}
		
	// Wandelt einen ï¿½bergebenen Winkel in einen Punkt-Richtung um.
	private Point2D angleToDirection(double pAngle)
	{
		// Passenden Quadranten bestimmen.
		int lQuarter = (int)(pAngle / 90);
		pAngle -= lQuarter * 90;
		pAngle /= 90;
		switch(lQuarter)
		{
			// Bewegung nach oben rechts.
			case 0:
				return new Point2D.Double(pAngle, -(1 - pAngle));
			// Bewegung nach unten rechts.
			case 1:
				return new Point2D.Double(1 - pAngle, pAngle);
			// Bewegung nach unten links.
			case 2:
				return new Point2D.Double(-pAngle, 1 - pAngle);
			// Bewegung nach oben links.
			case 3:
				return new Point2D.Double(-(1 - pAngle), -pAngle);
			// Sollte nie erreicht werden.
			default:
				return new Point2D.Double(pAngle, -(1 - pAngle));
		}
	}
}
