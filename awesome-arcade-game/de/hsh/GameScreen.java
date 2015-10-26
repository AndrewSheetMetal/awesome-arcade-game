package de.hsh;

import java.util.List;

public class GameScreen extends Screen {
	
	private static final long serialVersionUID = 1L;
	private List<Battlefield> battlefields;
	private float time;
	
	public GameScreen(List<Battlefield> pBattlefields){
		
		battlefields = pBattlefields;
	}
	
	private void update(float pDeltaTime){
		
		time += pDeltaTime;
	}
}
