package de.hsh;


import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.List;

public class GameScreen extends Screen implements Runnable {
	
	private static final long serialVersionUID = 1L;
	private List<Battlefield> battlefields;
	private float time;
	private de.hsh.Objects.Player player;
	private boolean running;

	
	public GameScreen(List<Battlefield> pBattlefields){
		
		battlefields = pBattlefields;
		
		player = new de.hsh.Objects.Player();
		
		this.addKeyListener(new TAdapter());
		System.out.println("Keylistener"+this.getKeyListeners());
		new Thread(this).start();
		running = true;
		
	}
	
	private void update(float pDeltaTime){
		
		
		
		
		time += pDeltaTime;
		
		Point pos = player.getPosition();
		Point direction = player.getDirection();
		pos.x += direction.x*pDeltaTime/100000;
		pos.y += direction.y*pDeltaTime/100000;
		player.setPosition(pos);
		updateUI();
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		player.draw(g);
	}

	private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            
        }

        @Override
        public void keyPressed(KeyEvent e) {
        	System.out.println("Taste gedrückt "+e);
    		
    		if(e.getKeyCode() == KeyEvent.VK_UP) {
    			player.setDirection(new Point(0,1));
    		}
    		else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
    			player.setDirection(new Point(0,-1));
    		}
    		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
    			player.setDirection(new Point(1,0));
    		}
    		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
    			player.setDirection(new Point(-1,0));
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
			update(delta);
			}
		}
	}

