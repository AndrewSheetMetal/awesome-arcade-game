package de.hsh.Objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Highscore implements Serializable{
	private List<HighscoreEntry> list;
	public Highscore(){
		list = new ArrayList<HighscoreEntry>();
	}
	public void addEntry(HighscoreEntry entry){
		if(list.size()<9)list.add(entry);
		Collections.sort(list, new EntryCompare());
		
	}
	public void restore(){
		try{
			File f = new File("hs.dat");
			if(!f.exists()){
				f.createNewFile();
				return;
			}
			FileInputStream fi = new FileInputStream(f);
			ObjectInputStream oi = new ObjectInputStream(fi);
			list = (List<HighscoreEntry>) oi.readObject();
			fi.close();
			oi.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void save(){
		try{
			File f = new File("hs.dat");
			FileOutputStream fo = new FileOutputStream(f);
			ObjectOutputStream ou = new ObjectOutputStream(fo);
			ou.writeObject(list);
			fo.close();
			ou.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public List<HighscoreEntry> getList(){
		return list;
	}
	class EntryCompare implements Comparator<HighscoreEntry>{

		@Override
		public int compare(HighscoreEntry o1, HighscoreEntry o2) {
			Integer o1int = new Integer(o1.getPoints());
			Integer o2int = new Integer(o2.getPoints());
			return o2int.compareTo(o1int);
		}
		
	}
}

