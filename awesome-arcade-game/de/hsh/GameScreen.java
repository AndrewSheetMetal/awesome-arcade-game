package de.hsh;


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hsh.Objects.PrototypeWall;

// ALEX
import de.hsh.Objects.Ball;
import de.hsh.Objects.Player;

import java.util.Random;


//Cocken
import javax.swing.JLabel;

import java.util.Timer;
import java.util.TimerTask;


public class GameScreen extends Screen implements Runnable {
	
	private static final long serialVersionUID = 1L;
	private List<Battlefield> battlefields;
	private float time;
	private int timeout;
	private Player player;
	private JLabel timebox = new JLabel();
	private List<Ball> mBallList;
	public static double scaleX  = 1;
	public static double scaleY = 1;
	private Point2D.Double center;
	private double bfWidth=0;
	private double bfHight=0;
	private int level;
	
	private boolean running;
    private double speed = 2;
	private PrototypeWall prototypeWall; //Die Linie, die der Player innerhalb des Battlefields zeichnet
	private ArrayList<Point2D> lines = new ArrayList<Point2D>();
	//private Point battlefieldHitPoint; //Koordinate an der der Player das Battlefield betritt. Referenz ist der Mittelpunkt des Players
	private Battlefield playerIsInBattlefield = null;
	//Timer-Objekt f�r den Countdown
	private Timer timer = new Timer();
	
	
	public GameScreen(List<Battlefield> pBattlefields, int level){
		
		//SVEN: Zeit, Anzahl Gegner anhand des Levels initialisieren
		this.level = level;
		timeout = level*30;
		mBallList = new ArrayList<Ball>();
		for(int i=0; i<level*2;i++){
			mBallList.add(new Ball());
		}
		for(Ball lBall : mBallList)
		{
			//lBall.setDirection(new Point2D.Double(-0.8, -0.2));
			// ALEX: Muss sp�ter zuf�llig gew�hlt werden.
			lBall.setPosition(new Point2D.Double(250,150));
			lBall.setRandomDirection();
			lBall.setSpeed(1);
		}	
		
		
		
		battlefields = pBattlefields;
		player = new de.hsh.Objects.Player();
		prototypeWall = new PrototypeWall();
		center = new Point2D.Double();
		// Goldbeck
		this.setLayout(new BorderLayout());
		this.add(timebox, BorderLayout.PAGE_END);
		timebox.setVisible(true);
		
		// Goldbeck: starten der TimerTask run() methode
		this.start();
		
		// ALEX
		player.setDirection(new Point2D.Double(0,0));

		//player.setPosition(new Point2D.Double(50,150));

		player.setPosition(new Point2D.Double(Main.SIZE/4, Main.SIZE*3/4));
		
		player.setSpeed(1);		
			

		this.addKeyListener(new TAdapter());
		System.out.println("Keylistener"+this.getKeyListeners());
		new Thread(this).start();
		running = true;
		addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				scaleX = (double)getWidth()/500;
				scaleY = (double)getHeight()/500;
				center.x = getWidth()/2;
				center.y = getHeight()/2;
				if(bfHight==0){
					bfWidth = getBattlefieldWidth();
					bfHight = getBattlefieldHeight();
				}
			}	
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//battlefieldHitPoint = new Point(-1,-1); //Initialisiere den Hitpoint mit negativen Werten
	}
	
	private void update(float pDeltaTime){
		if(player.getLifePoints()<=0){
			running = false;
		}
		time += pDeltaTime;
		
		Point2D pos = player.getPosition();
		Point2D direction = player.getDirection();
		pos.setLocation(pos.getX() + speed*(direction.getX()*pDeltaTime), pos.getY() + speed*(direction.getY()*pDeltaTime));
		player.setPosition(pos);
		
		// ALEX
		// Ballpositionen aktualisieren.
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
		for(int i = 0; i<battlefields.size(); i++) {
			Battlefield b = battlefields.get(i);
			// Pr�fen, ob ein Ball den Rand eines Schlachtfeldes erreicht hat.
			// Pr�fen, ob ein Ball den Rand eines Schlachtfeldes erreicht hat.
			for(Ball lBall : mBallList)
			{
				lBall.handleIntersection(battlefields, speed);
			}
			
			/*for(Ball lBall : mBallList)
			{
				// Ball schneidet das Spielfeld.
				if(!b.contains(lBall.getPosition().getX(), lBall.getPosition().getY(),
						lBall.getSize().getWidth(), lBall.getSize().getHeight()))
				{
					lBall.setDirection(new Point2D.Double(1,0));
				}
			}*/
			
			if(b.contains(player.getPosition().getX(),player.getPosition().getY(),player.getSize().getWidth(),player.getSize().getHeight())) {
				/*Der Spieler ist innerhalb eines Battlefields*/
				player.setColor(Color.RED);
			}
			else if(b.intersects(player.getPosition().getX(),player.getPosition().getY(),player.getSize().getWidth(),player.getSize().getHeight())) {
				/*Der Spieler schneidet das Battlefield*/
				player.setColor(Color.GREEN);				
				
				if(!(playerIsInBattlefield==b) && b.contains(player.getCenter())) {
					playerIsInBattlefield = b;
					System.out.println("Enter Battlefield  "+battlefields.size());
					enterBattlefield(b);
					break;
				}
				else if(playerIsInBattlefield==b && !b.contains(player.getCenter())) {
					playerIsInBattlefield = null;
					System.out.println("Leave Battlefield  "+battlefields.size());
					leaveBattlefield(b);
					System.out.println("Leave Battlefield  "+battlefields.size());
					break; //Verlässt der Spieler das Battlefield, so ändert sich die Battlefield Liste und die for-each Schleife wird inkonsistent
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
	}
	
	/*Verlaesst der Spieler das Spielfeld, so wird entweder das Battlefield kleiner, oder es wird in zwei Battlefield zerteilt.*/   
	public void leaveBattlefield(Battlefield b) {
		player.setColor(Color.BLACK);
		
		Point2D tmp = (Point2D) player.getPosition().clone();
		tmp.setLocation(tmp.getX()+player.getSize().getWidth()/2, tmp.getY()+player.getSize().getHeight()/2);
		//lines.add(tmp);
		prototypeWall.addEdge(tmp);
				
		/*Erstmal wird er nur in zwei Battlefield zerteilt, da es noch keine Gegner gibt, anhand der man bestimmen koennte wie das Battlefield schrumpft*/

		//b.splitByPrototypeWall(prototypeWall,battlefields);
		
		BattlefieldSeperator bs = new BattlefieldSeperator(b);
		bs.calculateBattlefieldHalfs(prototypeWall);
		
		boolean fillb1 = true;
		boolean fillb2 = true;
		
		for(Ball lBall : mBallList)
		{
			if(bs.getB1().contains(lBall.getPosition())) {
				fillb1 = false;
			}
			
			if(bs.getB2().contains(lBall.getPosition())) {
				fillb2 = false;
			}
		}	

		
		battlefields.remove(battlefields.indexOf(b));
		
		if(fillb1==false && fillb2==false) {
			bs.calculateSplittedBattlefields();

			battlefields.add(bs.getB1());
			battlefields.add(bs.getB2());
			
			//System.out.println("Battlefield 1: "+bs.getB1().getArea());
			//System.out.println("Battlefield 2: "+bs.getB2().getArea());
			
		}
		else if(fillb1 == true) {
			battlefields.add(bs.getB2());
			
			//System.out.println("Battlefield: "+bs.getB2().getArea());
		}
		else if(fillb2 == true) {
			battlefields.add(bs.getB1());
			
			//System.out.println("Battlefield: "+bs.getB1().getArea());
		}
		else {
			System.out.println("Error filling Battlefields");
		}
		
		double totalArea = 0;
		for(Battlefield bat : battlefields) {
			totalArea += Math.abs(bat.getArea());
			//System.out.println("Fläche: "+Math.abs(polygonArea(b)));
		}
		if(totalArea <= 50000) {
			//TODO Level beendet
			System.out.println("Spiel gewonnen!!!");
		}
		
		System.out.println("Gesamtfläche: "+totalArea);
		
		//battlefields.clear();
		//battlefields.add(b);
		
		prototypeWall.clear();		
	}
	
	/*Diese Methode wird aufgerufen, wenn der Spieler ein Leben verliert.
	 * Hier wird er z.B. auf die Startposition gesetzt*/
	public void lostLife() {
		player.setLifePoints(player.getLifePoints()-1);
		prototypeWall.clear();
		player.setPosition(new Point2D.Double(50,50));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		
		
		//Spielfeld skalierbar
		// Verhältnis wird beibehalten
		// TODO Spieler, Bälle und Spielfelder zentrieren
		Graphics2D gT = (Graphics2D) g;
		
		gT.translate(center.x-bfWidth/2 , center.y-bfHight/2);
	
		
		this.setBackground(Color.YELLOW);
		for(Battlefield b : battlefields) {
			b.draw(g);	
		}
		
		player.draw(g);
		
		// ALEX
		for(Ball lBall : mBallList)
		{
			lBall.draw(g);
		}
		
		prototypeWall.draw(g,player.getCenter());			
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
				tmp.setLocation(tmp.getX()+player.getSize().getWidth()/2, tmp.getY()+player.getSize().getHeight()/2);
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
    		else if(e.getKeyCode() == KeyEvent.VK_C){
    			System.out.println("ScaleX: "+scaleX+"\nScaleY: "+scaleY);
    			System.out.println("Breite: "+getWidth()+"\nHöhe: "+getHeight());
    			System.out.println("Spielerleben:"+player.getLifePoints());
    			
    		}
    		else if(e.getKeyCode() == KeyEvent.VK_S) {
    			speed /= 10;
    		}
    		else if(e.getKeyCode() == KeyEvent.VK_PLUS) {
    			speed *= 2.0;
    		}
    		else if(e.getKeyCode() == KeyEvent.VK_MINUS) {
    			speed /= 2.0;
    		}
        }
    }

	

	//Goldbeck: run() Methode des Timers, mit der im Sekundentakt hochgez�hlt wird.

	TimerTask timerTask = new TimerTask() {

		public void run() {
			timeout--;
			//System.out.println("Timer:" + timeout);
			timebox.setText("" + timeout);
			//Sven: Zum testen erstemal auskommentiert
			if (timeout == 0) {
				running = false;
				
				this.cancel();
			}
		}

	};
	
	
	/**
	 * Startet den Timer des Spiels
	 */
	
	public void start() {

		timer.scheduleAtFixedRate(timerTask, 1000, 1000);

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
	private double getBattlefieldWidth(){
		double w;
		w = battlefields.get(0).getBounds2D().getWidth();
		
		for(int i=1; i<battlefields.size();i++){
			if(battlefields.get(i).getBounds2D().getWidth()>w){
				w = battlefields.get(i).getBounds2D().getWidth();
			}
		}
		return w*scaleX;
	}
	private double getBattlefieldHeight(){
		double h;
		
		h = battlefields.get(0).getBounds2D().getHeight();
		
		for(int i=1; i<battlefields.size();i++){
			if(battlefields.get(i).getBounds2D().getHeight()>h){
				h = battlefields.get(i).getBounds2D().getHeight();
			}
		}
		return h*scaleY;
	}
}

