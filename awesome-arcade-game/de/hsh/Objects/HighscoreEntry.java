package de.hsh.Objects;

import java.io.Serializable;

public class HighscoreEntry implements Serializable {
	private String name;
	private int points;
	public HighscoreEntry(String name, int points){
		if(name.length()>9){
			this.name = name.substring(0, 9);
		}else{
			this.name = name;
		}
		
		this.points = points;
	}
	public String getName(){
		return name;
	}
	public int getPoints(){
		return points;
	}
}
