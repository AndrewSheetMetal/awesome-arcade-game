package de.hsh;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import de.hsh.Objects.PrototypeWall;

// ALEX
import de.hsh.Objects.Ball;
import de.hsh.Objects.Player;
import java.util.Random;

public class GameScreen extends Screen implements Runnable {
	
	private static final long serialVersionUID = 1L;
	private List<Battlefield> battlefields;
	private float time;
	private Player player;
	
	private List<Ball> mBallList;
	
	private boolean running;
	// ALEX: speed entfernt.
	private boolean painting = false;
	private PrototypeWall prototypeWall; //Die Linie, die der Player innerhalb des Battlefields zeichnet
	private ArrayList<Point2D> lines = new ArrayList<Point2D>();
	//private Point battlefieldHitPoint; //Koordinate an der der Player das Battlefield betritt. Referenz ist der Mittelpunkt des Players
	private boolean playerIsInBattlefield = false;
	
	public GameScreen(List<Battlefield> pBattlefields){
		
		battlefields = pBattlefields;
		
		player = new de.hsh.Objects.Player();

		prototypeWall = new PrototypeWall();


		// ALEX
		player.setDirection(new Point2D.Double(0,0));
		player.setPosition(new Point2D.Double(50,50));
		player.setSpeed(2);
		
		mBallList = new ArrayList<Ball>();
		// Muss sp�ter dynamisch erzeugt werden.
		mBallList.add(new Ball());
		Random lRandom = new Random();
		for(Ball lBall : mBallList)
		{
			// Zuf�lligen Winkel erzeugen.
			double lDir = 360 * lRandom.nextDouble();
			// Passenden Quadranten bestimmen.
			int lQuarter = (int)(lDir / 90);
			lDir -= lQuarter * 90;
			lDir /= 90;
			switch(lQuarter)
			{
				// Ball bewegt sich nach oben rechts.
				case 0:
					lBall.setDirection(new Point2D.Double(lDir, -(1 - lDir)));
					break;
				// Ball bewegt sich nach unten rechts.
				case 1:
					lBall.setDirection(new Point2D.Double(1 - lDir, lDir));
					break;
				// Ball bewegt sich nach unten links.
				case 2:
					lBall.setDirection(new Point2D.Double(-lDir, 1 - lDir));
					break;
				// Ball bewegt sich nach oben links.
				case 3:
					lBall.setDirection(new Point2D.Double(-(1 - lDir), -lDir));
					break;
			}
			lBall.setSpeed(1);
		}
		
		this.addKeyListener(new TAdapter());
		System.out.println("Keylistener"+this.getKeyListeners());
		new Thread(this).start();
		running = true;
		
		//battlefieldHitPoint = new Point(-1,-1); //Initialisiere den Hitpoint mit negativen Werten
	}
	
	private void update(float pDeltaTime){
		time += pDeltaTime;
		
		Point2D pos = player.getPosition();
		Point2D direction = player.getDirection();
		pos.setLocation(pos.getX() + player.getSpeed()*(direction.getX()*pDeltaTime), 
				pos.getY() + player.getSpeed()*(direction.getY()*pDeltaTime));
		player.setPosition(pos);
		
		/*Checken, ob Player im Battlefield ist*/
		// ALEX
		for(Ball lBall : mBallList)
		{
			pos = lBall.getPosition();
			direction = lBall.getDirection();
			pos.setLocation(pos.getX() + lBall.getSpeed()*(direction.getX()*pDeltaTime), 
					pos.getY() + lBall.getSpeed()*(direction.getY()*pDeltaTime));
			lBall.setPosition(pos);
		}
				
		/*Checken, ob Player im Battlefield ist*/		
		

		player.setColor(Color.BLUE);
		for(Battlefield b : battlefields) {
			if(b.contains(player.getPosition().getX(),player.getPosition().getY(),
					player.getSize().getWidth(),player.getSize().getHeight())) {
				/*Der Spieler ist innerhalb eines Battlefields*/
				player.setColor(Color.RED);
			}
			else if(b.intersects(player.getPosition().getX(),player.getPosition().getY(),
					player.getSize().getWidth(),player.getSize().getHeight())) {
				/*Der Spieler schneidet das Battlefield*/
				player.setColor(Color.GREEN);
				
				
				if(!playerIsInBattlefield && b.contains(player.getCenter())) {
					playerIsInBattlefield = true;
					enterBattlefield(b);
				}
				else if(playerIsInBattlefield && !b.contains(player.getCenter())) {
					playerIsInBattlefield = false;
					leaveBattlefield(b);
				}
			}
			for(Ball lBall : mBallList)
			{
				// Ball schneidet das Spielfeld.
				if(!b.contains(lBall.getPosition().getX(), lBall.getPosition().getY(),
						lBall.getSize().getWidth(), lBall.getSize().getHeight()))
				{
					lBall.setDirection(new Point2D.Double(1,0));
				}
			}
							

		}
		
		/*Schauen, ob der Player seine eigene Linie kreuzt*/
		if(prototypeWall.intersects(player.getBounds())) {
			lostLife();
			//player.setColor(Color.PINK);
		}
		
		updateUI();
	}
	
	public void enterBattlefield(Battlefield b) {
		player.setColor(Color.BLACK);
		lines.clear();
		Point2D tmp = (Point2D) player.getPosition().clone();
		tmp.setLocation(tmp.getX()+player.getSize().getWidth()/2, tmp.getY()+player.getSize().getHeight()/2);
		//lines.add(tmp);
		prototypeWall.addEdge(tmp);
		painting = true;
	}
	
	/*Verlässt der Spieler das Spielfeld, so wird entweder das Battlefield kleiner, oder es wird in zwei Battlefield zerteilt.*/   
	public void leaveBattlefield(Battlefield b) {
		player.setColor(Color.BLACK);
		
		Point2D tmp = (Point2D) player.getPosition().clone();
		tmp.setLocation(tmp.getX()+player.getSize().getWidth()/2, tmp.getY()+player.getSize().getHeight()/2);
		//lines.add(tmp);
		prototypeWall.addEdge(tmp);
		
		
		/*Erstmal wird er nur in zwei Battlefield zerteilt, da es noch keine Gegner gibt, anhand der man bestimmen könnte wie das Battlefield schrumpft*/
		Battlefield newBattlefield = b.splitByPrototypeWall(prototypeWall);
		
		
		prototypeWall.clear();
		painting = false;
		
	}
	
	/*Diese Methode wird aufgerufen, wenn der Spieler ein Leben verliert.
	 * Hier wird er z.B. auf die Startposition gesetzt*/
	public void lostLife() {
		prototypeWall.clear();
		player.setPosition(new Point2D.Double(50,50));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		this.setBackground(Color.YELLOW);
		
		for(Battlefield b : battlefields) {
			b.draw(g);
		}
		
		player.draw(g);

		prototypeWall.draw(g,player.getCenter());

		// ALEX (Schleife sollte vor "player.draw" stehen, damit die Linien im Anschluss
		// in der selben Farbe wie der Player gezeichnet werden.
		for(Ball lBall : mBallList)
		{
			lBall.draw(g);
		}	
		
		player.draw(g);
			
		//Zeichnet die Linen hinter dem Player, wenn der das Battlefield betritt
		if(painting){
			if(lines.size() ==1){
				g.drawLine((int)lines.get(0).getX(), (int)lines.get(0).getY(), 
						(int)(player.getPosition().getX()+player.getSize().getWidth()/2), 
						(int)(player.getPosition().getY()+player.getSize().getHeight()/2));
			}
			for(int i =0;i<lines.size()-1;i++){
					g.drawLine((int)lines.get(i).getX(), (int)lines.get(i).getY(), 
							(int)lines.get(i+1).getX(), (int)lines.get(i+1).getY());
				}
				g.drawLine((int)lines.get(lines.size()-1).getX(), (int)lines.get(lines.size()-1).getY(), 
						(int)(player.getPosition().getX()+player.getSize().getWidth()/2), 
						(int)(player.getPosition().getY()+player.getSize().getHeight()/2));
			}

			
	}
		

	private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            
        }

        @Override
        public void keyPressed(KeyEvent e) {
        	
        	//Fügt Positionen zu zur Liste für die Linen hinzu

    		if(prototypeWall.isDrawn() && (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT )){
    			Point2D tmp = (Point2D) player.getPosition().clone();
				tmp.setLocation(tmp.getX()+player.getSize().getWidth()/2, 
						tmp.getY()+player.getSize().getHeight()/2);
				
				lines.add(tmp);
				prototypeWall.addEdge(tmp);
    		}
    		if(e.getKeyCode() == KeyEvent.VK_UP) {
    			player.setDirection(new Point2D.Double(0,-1));
    		}
    		else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
    			player.setDirection(new Point2D.Double(0,1));
    		}
    		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
    			player.setDirection(new Point2D.Double(1,0));
    		}
    		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
    			player.setDirection(new Point2D.Double(-1,0));
    		}
    		
        }
    }

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D/60D;
		float delta = 0;
		
		while(running){
			long now = System.nanoTime();
			delta +=  (now-lastTime)/nsPerTick;
			lastTime = now;
				
			
			
			if(delta >= 1) {
				update(delta);
				delta = 0;
			}
			}
		}
	}
