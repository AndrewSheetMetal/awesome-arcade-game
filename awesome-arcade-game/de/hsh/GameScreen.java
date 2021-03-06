package de.hsh;


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
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
import de.hsh.Objects.PowerUp;

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
	
	private double restflaeche;
	private double anfangsflaeche;
	private double zielflaeche = 50000;
	
	private double playerAdditionalSpeedTime = 0; //Zeit, in der der Spieler noch erhöhte Geschwindigkeit hat.
	private double enemyAdditionalSpeedTime = 0; //Zeit, in der die Enemys noch erhöhte Geschwindigkeit haben.
	private double enemyReducedSpeedTime = 0; //Zeit, in der der Spieler noch verringerte Geschwindigkeit hat.
	private double playerReducedSpeedTime = 0; //Siehe enemyReducedSpeedTime nur für Player
	private double playerNormalSpeed = 1;
	private double enemyNormalSpeed = 1;
	
	// ALEX
	public static List<Enemy> EnemyList;
	public List<PowerUp> mPowerUpList;
	private Polygon gamearea = new Polygon();
	
	private int level;
	private Main main;
	private Point2D.Double center = new Point2D.Double(0, 0);
	private Point2D.Double bfInitialSize = new Point2D.Double();
	
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
		timeout = 60+level*10;
		battlefields = pBattlefields;
		EnemyList = new ArrayList<Enemy>();
		for(int i=0; i< level;i++){
			EnemyList.add(new Ball());
			if(i%2 == 0){
				EnemyList.add(new Porcupine());
			}
		}
		for(Enemy e : EnemyList){
			e.spawn(battlefields);
			e.setSpeed((int)speed);
		}
		
		// ALEX
		mPowerUpList = new ArrayList<PowerUp>();
		
		//SVEN: Größe des Felds abspeichern
		bfInitialSize.x = battlefields.get(0).getBounds().getWidth();
		bfInitialSize.y = battlefields.get(0).getBounds().getHeight();
		
		player = new de.hsh.Objects.Player();
		prototypeWall = new PrototypeWall();
		// Goldbeck
		this.setLayout(new BorderLayout());
		this.add(timebox, BorderLayout.PAGE_END);
		timebox.setVisible(true);
		
		// Goldbeck: starten der TimerTask run() methode
		this.start();
		
		// ALEX
		player.setDirection(new Point2D.Double(0,0));

		//player.setPosition(new Point2D.Double(50,150));

		player.setPosition(new Point2D.Double(-75, Main.SIZE/2-player.getSize().height/2));
		
		player.setSpeed(1);		
		
		anfangsflaeche = getTotalArea();
		restflaeche = anfangsflaeche;
		// TODO sollte sp�ter dynamisch erzeugt werden.
		
		this.addKeyListener(new TAdapter());
		System.out.println("Keylistener"+this.getKeyListeners());
		
		
		addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {}
			
			@Override
			public void componentResized(ComponentEvent e) {
				//bg 
				center.x = getWidth()/2;
				center.y = getHeight()/2;
				gamearea.reset();
				gamearea.addPoint((int)center.x-((Main.MARGIN+Main.SIZE)/2), (int)center.y-((Main.MARGIN+Main.SIZE)/2));
				gamearea.addPoint((int)center.x+((Main.MARGIN+Main.SIZE)/2), (int)center.y-((Main.MARGIN+Main.SIZE)/2));
				gamearea.addPoint((int)center.x+((Main.MARGIN+Main.SIZE)/2), (int)center.y+((Main.MARGIN+Main.SIZE)/2));
				gamearea.addPoint((int)center.x-((Main.MARGIN+Main.SIZE)/2), (int)center.y+((Main.MARGIN+Main.SIZE)/2));
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {}
			
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
		
		
		running = true;
		new Thread(this).start();
		//battlefieldHitPoint = new Point(-1,-1); //Initialisiere den Hitpoint mit negativen Werten
	}
	
	private void update(float pDeltaTime)
	{
		time += pDeltaTime;
		
		//Andreas
		float realPlayerSpeed = 1;
		if(playerAdditionalSpeedTime > 0) {
			playerAdditionalSpeedTime -= pDeltaTime;
			realPlayerSpeed *= 2;
		}
		player.updatePosition(speed,pDeltaTime*realPlayerSpeed);
		
		// ALEX
		// Gegner bewegen.
		refreshEnemyPositions(pDeltaTime);
		// Power-Up ggf. spawnen oder verschwinden lassen.
		randomSpawnPowerUps();
		
		/*Checken, ob Player im Battlefield ist*/
		//player.setColor(Color.BLUE);
		for(int i = 0; i<battlefields.size(); i++) 
		{
			Battlefield b = battlefields.get(i);
			
			// ALEX			
			// Kollisionen der Gegner mit W�nden oder anderen Gegnern managen.
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
				//player.setColor(Color.RED);
			}
			else if(b.intersects(player.getPosition().getX(),player.getPosition().getY(),player.getSize().getWidth(),player.getSize().getHeight())) {
				/*Der Spieler schneidet das Battlefield*/
				//player.setColor(Color.GREEN);				
				
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
		//if(prototypeWall.intersects(player.getBounds()) >= 0) {
		if(prototypeWall.playerHitsPrototypeWall(player)) {
			//JOptionPane.showMessageDialog(null, "Haha, Leben verloren");
			lostLife("Player hat PrototypeWall geschnitten");
			//player.setColor(Color.PINK);
		}
		
		// ALEX: Kollisionen managen.
		for(Enemy lEnemy : EnemyList)
		{
			// Kollisionen mit Prototyp-Wand.
			if(lEnemy instanceof  Ball)
			{
				if(prototypeWall.intersects((( Ball)lEnemy).getBounds(),player.getCenter())) {
					//JOptionPane.showMessageDialog(null, "Haha, Leben verloren");
					//lostLife();
				}
			}
			// Kollisionen mit Spieler.
			if(lEnemy.intersectsWithObject(player.getPosition(), 
					new Point2D.Double(player.getSize().getWidth(),  player.getSize().getHeight())))
			{
				lostLife("Spieler hat Gegner ber�hrt");
			}
			// Kollisionen mit anderen Gegnern.
			lEnemy.handleIntersectionWithFriends();
			// Kollisionen mit Power-Ups.
			for(int i = 0; i < mPowerUpList.size(); i++)
			{
			 	if(lEnemy.intersectsWithObject(mPowerUpList.get(i).getPosition(), 
						new Point2D.Double(mPowerUpList.get(i).getSize().getWidth(), 
								mPowerUpList.get(i).getSize().getHeight())))
				{
					 mPowerUpList.remove(i);
					i--;
				}
			}
		}
		// Kollision von Spieler und Power-Up.
		for(int i = 0; i < mPowerUpList.size();  i++)
		{
			if(player.intersectsWithPowerUp(mPowerUpList.get(i)))
			{
				System.out.println("Spieler hat Power-Up eingesammelt.");
				// TODO: Andi kann hier einf�gen.
				mPowerUpList.remove(i);
				i--;
			}
		}
		
		
		prototypeWall.update(pDeltaTime);
		
		/*Wurde die PrototypeWall von einem Ball berührt, so bilden sich Kreise um den
		Berührungspunkt. Ist der Player zu langsam, so fängt ihn der Kreis ein und er verliert ein Leben
		*/
		if(!prototypeWall.isValid()) {
			if(prototypeWall.isPlayerCatched(player)) {
				lostLife("PrototypeWall wurde zerstört");
			}
			
		}
		
		//updateUI();
	}
	
	public void enterBattlefield(Battlefield b) {
		//player.setColor(Color.BLACK);
		lines.clear();
		Point2D tmp = (Point2D) player.getPosition().clone();
		tmp.setLocation(tmp.getX()+player.getSize().getWidth()/2, tmp.getY()+player.getSize().getHeight()/2);
		//lines.add(tmp);
		prototypeWall.addEdge(tmp);
	}
	
	/*Verlaesst der Spieler das Spielfeld, so wird entweder das Battlefield kleiner, oder es wird in zwei Battlefield zerteilt.*/   
	public void leaveBattlefield(Battlefield b) {
		//player.setColor(Color.BLACK);
		
		Point2D tmp = (Point2D) player.getPosition().clone();
		tmp.setLocation(tmp.getX()+player.getSize().getWidth()/2, tmp.getY()+player.getSize().getHeight()/2);
		//lines.add(tmp);
		prototypeWall.addEdge(tmp);
				
		/*Erstmal wird er nur in zwei Battlefield zerteilt, da es noch keine Gegner gibt, anhand der man bestimmen koennte wie das Battlefield schrumpft*/

		//b.splitByPrototypeWall(prototypeWall,battlefields);
		
		
		//Nur wenn die prototypeWall nicht von Bällen berührt wurde, darf das Battlefield verkleinert werden
		if(prototypeWall.isValid()) {
		
			
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
			
			double totalArea = getTotalArea();
			restflaeche = totalArea;
			if(totalArea <= zielflaeche) {
				//TODO Level beendet
				System.out.println("Spiel gewonnen!!!");
				
				double prozentGefuellt = ((int)(((anfangsflaeche-restflaeche)/(anfangsflaeche-zielflaeche))*1000)/10.0);
				
				ScoreScreen scoreScreen = new ScoreScreen(main, level, prozentGefuellt);
				
				this.running = false;
				main.remove(this);
				main.setScreen(scoreScreen);
				
				scoreScreen.setFocusable(true);
				scoreScreen.requestFocus();
				scoreScreen.updateUI();
			}
			
			System.out.println("Gesamtfläche: "+totalArea);
			
		}
		prototypeWall.clear();		
	}
	
	public double getTotalArea() {
		double totalArea = 0;
		for(Battlefield bat : battlefields) {
			totalArea += Math.abs(bat.getArea());
			//System.out.println("Fläche: "+Math.abs(polygonArea(b)));
		}
		return totalArea;
	}
	
	/*Diese Methode wird aufgerufen, wenn der Spieler ein Leben verliert.
	 * Hier wird er z.B. auf die Startposition gesetzt*/
	public void lostLife(String reason) {
		System.out.println("Leben verloren: "+reason);
		
		if(player.getLifePoints() <= 1) {
			//Game over - keine Leben mehr
			System.out.println("Game over :(");
			GameoverScreen gameoverScreen = new GameoverScreen(main, level);
			
			this.running = false;
			main.remove(this);
			
			main.setScreen(gameoverScreen);
			
			gameoverScreen.setFocusable(true);
			gameoverScreen.requestFocus();
		}
		else {
			//Spieler wird zurückgesetzt und ein Leben wird abgezogen
			Point p = new Point();
			player.setDirection(p);
			player.setLifePoints(player.getLifePoints()-1);
			prototypeWall.clear();
			player.setPosition(new Point2D.Double(-75, Main.SIZE/2-player.getSize().height/2));	
			System.out.println("Leben verloren");
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		

		//Spielfeld skalierbar
		// Verhältnis wird beibehalten
		// TODO Spieler, Bälle und Spielfelder zentrieren
		Graphics2D gT = (Graphics2D) g;


		
		//if(center.x != 0)gT.translate(center.x-bfInitialSize.x/2 , center.y-bfInitialSize.y/2);
		g.setColor(Color.YELLOW);
		g.fillPolygon(gamearea);
		
		drawHUD(gT);
		
		
		//gT.translate((getWidth()-700)/2, (getHeight()-700)/2);
		if(center.x !=0)gT.translate(center.x-bfInitialSize.x/2 , center.y-bfInitialSize.y/2);
		
		//(getWidth()-500)/2, (getHeight()-500)/2
	
		
		this.setBackground(Color.BLACK);
		
		for(Battlefield b : battlefields) {
			b.draw(g);	
		}
		
		player.draw(g);
		
		// ALEX
		drawEnemies(g);
		drawPowerUps(g);
		
		prototypeWall.draw(g,player.getCenter());
		
	}
		

	private void drawHUD(Graphics2D gT) {
		gT.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		gT.setColor(Color.GREEN);
		
		Polygon background = new Polygon();
		background.addPoint(0, getHeight()-50);
		background.addPoint(100, getHeight() -50);
		background.addPoint(150,getHeight() -25);
		background.addPoint(getWidth()-150, getHeight()-25);
		background.addPoint(getWidth()-100, getHeight()-50);
		background.addPoint(getWidth(), getHeight()-50);
		background.addPoint(getWidth(), getHeight());
		background.addPoint(0, getHeight());
		
		gT.fillPolygon(background);
		
		gT.setColor(Color.BLACK);
		
		gT.drawString("Zeit: "+(int)timeout, 10, getHeight()-30);
		
		
		gT.drawString("Leben: "+player.getLifePoints(), getWidth()-75, getHeight()-10);
	
		gT.drawString("Level: "+level, getWidth()-75, getHeight()-30);
		
		
		double prozentGefuellt = ((int)(((anfangsflaeche-restflaeche)/(anfangsflaeche-zielflaeche))*1000)/10.0);
		
		gT.drawString("Fortschritt: "+ prozentGefuellt,10,getHeight()-10);
		
		//gT.drawPolygon(background);
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
    			System.out.println("Breite: "+getWidth()+"\nHöhe: "+getHeight());
    			System.out.println("Spielerleben:"+player.getLifePoints());
    			
    		}
    		else if(e.getKeyCode() == KeyEvent.VK_S) {
    			playerAdditionalSpeedTime = 100;//speed /= 10;
    		}
    		else if(e.getKeyCode() == KeyEvent.VK_PLUS) {
    			speed *= 2.0;
    		}
    		else if(e.getKeyCode() == KeyEvent.VK_MINUS) {
    			speed /= 2.0;
    		}
    		else if(e.getKeyCode() == KeyEvent.VK_P) {
    			running = !running;
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
				
				//this.cancel();
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
		while(true) {
			delta = 0;
			lastTime = System.nanoTime();
			while(running){
				long now = System.nanoTime();
				delta +=  (now-lastTime)/nsPerTick;
				lastTime = now;			
				
				if(delta >= 1) {
					//if(running) {
						update(delta);
	
					//}
					updateUI();
					delta = 0;
	
				}
				else {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
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
	
	// ALEX
	private void drawPowerUps(Graphics g)
	{
		for(PowerUp lPowerUp : mPowerUpList)
		{
			lPowerUp.draw(g);
		}
	}
	
	// ALEX
	private void randomSpawnPowerUps()
	{
		// Zuf�llig spawnen (P = 1/400).
		Random lRandom = new Random();
		if(lRandom.nextInt(400) == 399)
		{
			PowerUp lPowerUp = new PowerUp(0,0);
			lPowerUp.setPosition(new Point2D.Double(lRandom.nextInt(Main.SIZE - (int)(lPowerUp.getSize().getWidth())),
					lRandom.nextInt(Main.SIZE - (int)(lPowerUp.getSize().getHeight()))));
			mPowerUpList.add(lPowerUp);
		}
	}	
}

