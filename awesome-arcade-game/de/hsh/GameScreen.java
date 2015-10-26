package de.hsh;

import java.awt.Graphics;
import java.util.List;
import de.hsh.Objects.*;

public class GameScreen extends Screen {
	
	private de.hsh.Objects.Player player;
	private static final long serialVersionUID = 1L;
	private List<Battlefield> battlefields;
	private float time;
	
	public GameScreen(List<Battlefield> pBattlefields){
		player = new de.hsh.Objects.Player();
		battlefields = pBattlefields;
	}
	
	private void update(float pDeltaTime){
		
		time += pDeltaTime;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		player.draw(g);
		
	}
}
