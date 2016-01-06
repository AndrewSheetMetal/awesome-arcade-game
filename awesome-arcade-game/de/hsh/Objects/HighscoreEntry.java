package de.hsh.Objects;

import java.io.Serializable;

public class HighscoreEntry implements Serializable {
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
