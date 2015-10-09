package de.hsh;

public class Level {
	private int levelNummer;
	public Level(int nr){
		setLevelNummer(nr);
	}
	public int getLevelNummer() {
		return levelNummer;
	}
	public void setLevelNummer(int levelNummer) {
		this.levelNummer = levelNummer;
	}
}
