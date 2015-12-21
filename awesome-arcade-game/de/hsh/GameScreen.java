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
import de.hsh.Objects.Player;
import de.hsh.Objects.Enemy;
import de.hsh.Objects.Ball;
import de.hsh.Objects.Porcupine;

import java.util.Random;

//Cocken
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.util.Timer;
import java.util.TimerTask;


public class GameScreen extends Screen implements Runnable {
	
	private static final long serialVersionUID = 1L;
	private List<Battlefield> battlefields;
	private float time;
	private int timeout;
	private Player player;
	private JLabel timebox = new JLabel();
	
	// ALEX
	public static List<Enemy> EnemyList;
	
	private double scaleX  = 1;
	private double scaleY = 1;
	private Point2D.Double center;
	private double bfWidth=0;
	private double bfHight=0;
	
	private int level;
	private Main main;
	
	private boolean running;
    private double speed = 2;
	private PrototypeWall prototypeWall; //Die Linie, die der Player innerhalb des Battlefields zeichnet
	private ArrayList<Point2D> lines = new ArrayList<Point2D>();
	//private Point battlefieldHitPoint; //Koordinate an der der Player das Battlefield betritt. Referenz ist der Mittelpunkt des Players
	private Battlefield playerIsInBattlefield = null;
	//Timer-Objekt f�r den Countdown
	private Timer timer = new Timer();
	
	
	public GameScreen(List<Battlefield> pBattlefields, int level, Main main){
		this.main = main;
		//SVEN: Zeit, Anzahl Gegner anhand des Levels initialisieren
		this.level = level;
		timeout = level*40;
		
		
		
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
		EnemyList = new ArrayList<Enemy>();
		// Muss sp�ter dynamisch erzeugt werden.
		EnemyList.add(new Ball());
		EnemyList.add(new Ball());
		EnemyList.add(new Ball()); 
		EnemyList.add(new Porcupine());
		EnemyList.add(new Porcupine());
		
		for(Enemy lEnemy : EnemyList)
		{
			lEnemy.spawn(battlefields);
			lEnemy.setSpeed((int)speed);
		}	

		this.addKeyListener(new TAdapter());
		System.out.println("Keylistener"+this.getKeyListeners());
		new Thread(this).start();
		running = true;
		addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
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
		refreshEnemyPositions(pDeltaTime);
		
		/*Checken, ob Player im Battlefield ist*/
		player.setColor(Color.BLUE);
		for(int i = 0; i<battlefields.size(); i++) 
		{
			Battlefield b = battlefields.get(i);
			
			// ALEX
			// Pr�fen, ob ein Gegner den Rand eines Schlachtfeldes erreicht hat.
			for(Enemy lEnemy : EnemyList)
			{
				lEnemy.handleIntersectionWithWall(battlefields, speed);
				if(lEnemy instanceof Porcupine)
				{
					((Porcupine) lEnemy).handleIntersectionWithBorder(Main.SIZE, Main.SIZE);
				}
			}
			
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
			JOptionPane.showMessageDialog(null, "Haha, Leben verloren");
			lostLife();
			//player.setColor(Color.PINK);
		}
		// ALEX: An Enemy angepasst.
		for(Enemy lEnemy : EnemyList)
		{
			if(lEnemy instanceof Ball)
			{
				if(prototypeWall.intersects(((Ball)lEnemy).getBounds(),player.getCenter())) {
					JOptionPane.showMessageDialog(null, "Haha, Leben verloren");
					lostLife();
				}
			}
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
		
		// ALEX: Angepasst an Enemy.
		//for(Ball lBall : mBallList)
		for(Enemy lEnemy : EnemyList)
		{
			if(lEnemy instanceof Ball)
			{
				if(bs.getB1().contains(lEnemy.getPosition())) {
					fillb1 = false;
				}
				
				if(bs.getB2().contains(lEnemy.getPosition())) {
					fillb2 = false;
				}
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
			GameScreen newLevel = new GameScreen(main.createBattlefields(), level+1, main);
			main.setScreen(newLevel);
	
			main.remove(this);
			newLevel.setFocusable(true);
			newLevel.requestFocus();
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
		/*if(scaleY<scaleX){
			gT.scale(scaleY, scaleY);
			
		}else{
			gT.scale(scaleX,scaleX);
		}*/		
	
		
		this.setBackground(Color.YELLOW);
		for(Battlefield b : battlefields) {
			b.draw(g);	
		}
		
		player.draw(g);
		
		// ALEX
		drawEnemies(g);
		
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
	
	// ALEX
	// Gegnerpositionen aktualisieren.
	private void refreshEnemyPositions(float pDeltaTime)
	{
		Point2D pos, direction;
		for(Enemy lEnemy : EnemyList)
		{
			pos = lEnemy.getPosition();
			direction = lEnemy.getDirection();
			pos.setLocation(pos.getX() + lEnemy.getSpeed()*(direction.getX()*pDeltaTime), 
					pos.getY() + lEnemy.getSpeed()*(direction.getY()*pDeltaTime));
			lEnemy.setPosition(pos);
		}
	}			

	// ALEX
	private void drawEnemies(Graphics g)
	{
		for(Enemy lEnemy : EnemyList)
		{
			lEnemy.draw(g);
		}
	}
}

