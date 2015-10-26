package de.hsh;

import java.awt.Point;
import java.awt.event.KeyEvent;

public class Player {

	
	public static void main(String[] args) {
		System.out.println("sieg ranz");
	}	
	
	public Player(){
		
	}

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            //setDirection(new Point(-1.0,0.0);
        }

        if (key == KeyEvent.VK_RIGHT) {
        	//setDirection(new Point(-1.0,0.0);
        }

        if (key == KeyEvent.VK_UP) {
        	//setDirection(new Point(-1.0,0.0);
        }

        if (key == KeyEvent.VK_DOWN) {
        	//setDirection(new Point(-1.0,0.0);
        }
    }

    public void keyReleased(KeyEvent e) {
        
        int key = e.getKeyCode();
        //setDirection(new Point(0.0,0.0);
        
    }
	
}