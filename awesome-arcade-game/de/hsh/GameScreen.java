package de.hsh;


import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class GameScreen extends Screen {
	
	private static final long serialVersionUID = 1L;
	private List<Battlefield> battlefields;
	private float time;
	private de.hsh.Objects.Player player;
	
	public GameScreen(List<Battlefield> pBattlefields){
		
		battlefields = pBattlefields;
		
		player = new de.hsh.Objects.Player();
		
		this.addKeyListener(new TAdapter());
		System.out.println("Keylistener"+this.getKeyListeners());
	}
	
	private void update(float pDeltaTime){
		
		time += pDeltaTime;
		
		Point pos = player.getPosition();
		Point direction = player.getDirection();
		pos.x += direction.x*pDeltaTime;
		pos.y += direction.y*pDeltaTime;
		player.setPosition(pos);
		
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
}
