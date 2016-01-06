package de.hsh.Objects;

import java.io.Serializable;

public class HighscoreEntry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private int points;
	public HighscoreEntry(String name, int points){
		this.name = name;
		this.points = points;
	}
	public String getName(){
		return name;
	}
	public int getPoints(){
		return points;
	}
}
