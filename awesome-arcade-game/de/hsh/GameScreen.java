package de.hsh;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import de.hsh.Objects.Ball;
import de.hsh.Objects.Player;

public class GameScreen extends Screen implements Runnable {
	
	private static final long serialVersionUID = 1L;
	private List<Battlefield> battlefields;
	private float time;
	private Player player;
	
	private List<Ball> mBallList;
	
	private boolean running;
	private int speed = 2;
	private boolean painting = false;
	private ArrayList<Point2D> lines = new ArrayList<Point2D>();
	//private Point battlefieldHitPoint; //Koordinate an der der Player das Battlefield betritt. Referenz ist der Mittelpunkt des Players
	private boolean playerIsInBattlefield = false;
	
	public GameScreen(List<Battlefield> pBattlefields){
		
		battlefields = pBattlefields;
		
		player = new de.hsh.Objects.Player();

		// ALEX
		player.setDirection(new Point2D.Double(0,0));
		player.setPosition(new Point2D.Double(50,50));
		mBallList = new ArrayList<Ball>();
		// Muss später dynamisch erzeugt werden.
		mBallList.add(new Ball());
		for(Ball lBall : mBallList)
		{
			// Muss später zufällig generiert werden.
			lBall.setDirection(new Point2D.Double(1, 0));
		}
		
		this.addKeyListener(new TAdapter());
		System.out.println("Keylistener"+this.getKeyListeners());
		new Thread(this).start();
		running = true;
		
		//battlefieldHitPoint = new Point(-1,-1); //Initialisiere den Hitpoint mit negativen Werten
	}
	
	private void update(float pDeltaTime){
		
		
		//System.out.println("Update: pDeltaTime: "+pDeltaTime);
		
		time += pDeltaTime;
		
		Point2D pos = player.getPosition();
		Point2D direction = player.getDirection();
		pos.setLocation(pos.getX() + speed*(direction.getX()*pDeltaTime), 
				pos.getY() + speed*(direction.getY()*pDeltaTime));
		player.setPosition(pos);
		
		
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
					enterBattlefield();
				}
				else if(playerIsInBattlefield && !b.contains(player.getCenter())) {
					playerIsInBattlefield = false;
					leaveBattlefield();
				}
			}
			
		}
		updateUI();
	}
	
	public void enterBattlefield() {
		player.setColor(Color.BLACK);
		lines.clear();
		Point2D tmp = (Point2D) player.getPosition().clone();
		tmp.setLocation(tmp.getX()+player.getSize().getWidth()/2, tmp.getY()+player.getSize().getHeight()/2);
		lines.add(tmp);
		painting = true;
	}
	
	public void leaveBattlefield() {
		player.setColor(Color.BLACK);
		painting = false;
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		this.setBackground(Color.YELLOW);
		
		for(Battlefield b : battlefields) {
			b.draw(g);
		}
		
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
        	
        	//FÃ¼gt Positionen zu zur Liste fÃ¼r die Linen hinzu
    		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN 
    				|| e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT ){
    			
    			Point2D tmp = (Point2D) player.getPosition().clone();
				tmp.setLocation(tmp.getX()+player.getSize().getWidth()/2, 
						tmp.getY()+player.getSize().getHeight()/2);
				
				lines.add(tmp);
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
    		else if(e.getKeyCode() == KeyEvent.VK_ENTER){
    			
    			//Test! simuliert Eintritt in Battlefield
    			lines.clear();
    			if(!painting){
    				Point2D tmp = (Point2D) player.getPosition().clone();
    				tmp.setLocation(tmp.getX()+player.getSize().getWidth()/2, tmp.getY()+player.getSize().getHeight()/2);
    				lines.add(tmp);
    				painting = true;
    			}else{
    				painting = false;
    			}
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

