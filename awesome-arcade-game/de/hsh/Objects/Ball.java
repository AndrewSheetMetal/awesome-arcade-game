package de.hsh.Objects;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.List;

import de.hsh.Battlefield;
import de.hsh.GameScreen;

public class Ball extends Enemy 
{			
	public Ball() 
	{		
		super();
		setColor(Color.WHITE);
		// TODO: Unschön?
		this.mType = this.getClass();
	}

	@Override
	public void spawn(List<Battlefield> pBattlefields) 
	{
		this.createSpawnPosition(pBattlefields, 0);
		this.setRandomDirection();
	}
}
