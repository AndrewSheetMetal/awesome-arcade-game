package de.hsh;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import de.hsh.Objects.PrototypeWall;

public class BattlefieldSeperator {
	private Battlefield bat; //Das Battlefield, das Zerteilt werden soll
	private Battlefield b1;
	private Battlefield b2;
	
	private int startIndex; //Dies ist der Index, an dem der Beginn der PrototypeWall das Battlefield schneidet.
	private int endIndex; //Sie startIndex, bezogen auf das Ende der PrototypeWall
	
	//Unused
	//private boolean isStartVertical; //Speichert, ob die Linie, in der die PrototypeWall beginnt vertikal oder horizontal ist
	//private boolean isEndVertical;
	
	private ArrayList<Point2D> prototypePoints;
	
	public BattlefieldSeperator(Battlefield toSplit) {
		bat = toSplit;
	}
	
	/*
	 * Kalkuliert die Battlefield Hälften, die entstehen, wenn man das Battlefield durch die übergebene prototypeWall teilen würde
	 * */
	public void calculateBattlefieldHalfs(PrototypeWall wall) {
		calculateStartAndEndIndex(wall);
		
		b1 = getFirstBattlefield();
		b2 = getSecondBattlefield();
		
		//calculateSplittedBattlefields();
	}
	
	/*
	 * Dies ist die zweite Methode, die aufgerufen wird, wenn in beiden zuvor berechneten Battlefield Hälften Kugeln sind.
	 * */
	public void calculateSplittedBattlefields() {
		b1 = calculateFirstSplittedBattlefield();
		b2 = calculateSecondSplittedBattlefield();
		
	}
	
	private Battlefield calculateSecondSplittedBattlefield() {
		int t = 10;
		Battlefield original = b2;
		
		Battlefield toReturn = new Battlefield();
		/*for(int i = 0; i<=startIndex; i++) {
			toReturn.addPoint(bat.xpoints[i], bat.ypoints[i]);
		}*/
		
		/*
		 * Wo beim ersten Pixel hinzugefügt wurden, müssen sie beim zweiten abgezogen werden.
		 * Habe hier deshalb nur die Vergleichsoperatoren der inneren if-Bedingungen umgedreht
		 * */
		if(Math.abs(bat.xpoints[startIndex] - bat.xpoints[(startIndex+1)%bat.npoints]) < Math.abs(bat.ypoints[startIndex] - bat.ypoints[(startIndex+1)%bat.npoints])) {
			System.out.println("Startet in Vertikaler Linie");
			if(bat.ypoints[startIndex] > prototypePoints.get(0).getY()) {
				System.out.println("Ziehe y ab");
				toReturn.addPoint(bat.xpoints[startIndex], (int)prototypePoints.get(0).getY()-t);
			}
			else {
				System.out.println("füge y hinzu");
				toReturn.addPoint(bat.xpoints[startIndex], (int)prototypePoints.get(0).getY()+t);
			}
		}
		else {
			System.out.println("Startet in Horizontaler Linie");
			if(bat.xpoints[startIndex] > prototypePoints.get(0).getX()) {
				System.out.println("Ziehe x ab");
				toReturn.addPoint((int)prototypePoints.get(0).getX()-t, bat.ypoints[startIndex]);
			}
			else {
				System.out.println("Füge x hinzu");
				toReturn.addPoint((int)prototypePoints.get(0).getX()+t, bat.ypoints[startIndex]);
			}
			
			//b1.addPoint((int)prototypeWall.getEdge(0).getX()-5, (int)prototypeWall.getEdge(0).getY());*/
		}
		
		/*
		 * Iteriere durch die Knoten der PrototypeWall, die zwischen Start und Ende liegen
		 * */
		for(int i = 1; i<prototypePoints.size()-1; i++) {
			/* Okay, also es gibt vier Möglichkeiten für den neuen Knoten. +5 Pixel bzw. -5 Pixel in X bzw. Y Richtung vom eigentlichen Edge der prototypeWall verschoben.
			 * Zwei davon kann man ausschließen, wenn man sich den vorangehenden bzw. nächsten Edge der PrototypeWall ansieht. Wenn von einem zum anderen beide Koordinaten sowohl
			 * in X als auch in Y Richtung größer oder kleiner werden, so liegt der Edge dazwischen entweder -5 in x und +5 y oder -5 in y und +5 in x Richtung vom eigentlichen Edge der PrototypeWall
			 * entfernt. Ist diese Bedingung nicht gegeben sind es entsprechend +5 in x und +5 in y bzw. -5 in x und -5 in y.
			 * Welche dieser zwei Optionen dann die richtige ist kann man nur bestimmen, indem man schaut ob der Punkt im originalen Battlefield läge oder nicht
			 * */
			if((prototypePoints.get(i-1).getX()-prototypePoints.get(i+1).getX()) * (prototypePoints.get(i-1).getY() - prototypePoints.get(i+1).getY()) > 0) {
				//Entweder x+5 und y+5 oder x-5 und y-5
				if(original.contains(prototypePoints.get(i).getX()+t, prototypePoints.get(i).getY()-t)) {
					toReturn.addPoint((int)prototypePoints.get(i).getX()+t, (int)prototypePoints.get(i).getY()-t); 
				}
				else {
					toReturn.addPoint((int)prototypePoints.get(i).getX()-t, (int)prototypePoints.get(i).getY()+t); 
				}
			}
			else {
				//Entweder x+5 und y-5 oder x-5 und y+5
				if(original.contains(prototypePoints.get(i).getX()+t, prototypePoints.get(i).getY()+t)) {
					toReturn.addPoint((int)prototypePoints.get(i).getX()+t, (int)prototypePoints.get(i).getY()+t); 
				}
				else {
					toReturn.addPoint((int)prototypePoints.get(i).getX()-t, (int)prototypePoints.get(i).getY()-t); 
				}
			}
			
			
			//toReturn.addPoint((int)prototypeWall.getEdge(i).getX()-5, (int)prototypeWall.getEdge(i).getY()-5);
		}
		
		/*
		 * Wo beim ersten Pixel hinzugefügt wurden, müssen sie beim zweiten abgezogen werden.
		 * Habe hier deshalb nur die Vergleichsoperatoren der inneren if-Bedingungen umgedreht
		 * */
		if(Math.abs(bat.xpoints[(endIndex+1)%bat.npoints] - prototypePoints.get(prototypePoints.size()-1).getX()) < Math.abs(bat.ypoints[(endIndex+1)%bat.npoints] - prototypePoints.get(prototypePoints.size()-1).getY())) {
			System.out.println("Endet in Vertikaler Linie");
			if(bat.ypoints[(endIndex+1)%bat.npoints] > prototypePoints.get(prototypePoints.size()-1).getY()) {
				System.out.println("Ziehe y ab");
				toReturn.addPoint(bat.xpoints[(endIndex+1)%bat.npoints], (int)prototypePoints.get(prototypePoints.size()-1).getY()-t);
			}
			else {
				System.out.println("Füge y hinzu");
				toReturn.addPoint(bat.xpoints[(endIndex+1)%bat.npoints], (int)prototypePoints.get(prototypePoints.size()-1).getY()+t);
			}
		}
		else {
			System.out.println("Endet in Horizontaler Linie");
			if(bat.xpoints[(endIndex+1)%bat.npoints] > prototypePoints.get(prototypePoints.size()-1).getX()) {
				System.out.println("Ziehe x ab");
				toReturn.addPoint((int)prototypePoints.get(prototypePoints.size()-1).getX()-t, bat.ypoints[(endIndex+1)%bat.npoints]);
			}
			else {
				System.out.println("Füge x hinzu");
				toReturn.addPoint((int)prototypePoints.get(prototypePoints.size()-1).getX()+t, bat.ypoints[(endIndex+1)%bat.npoints]);
			}
			
		}
		for(int i = endIndex; i>startIndex; i--) {
			toReturn.addPoint(bat.xpoints[i], bat.ypoints[i]);
		}
		return toReturn;
		
	}
	
	private Battlefield calculateFirstSplittedBattlefield() {
		int t = 10;
		Battlefield original = b1;
		
		Battlefield toReturn = new Battlefield();
		for(int i = 0; i<=startIndex; i++) {
			toReturn.addPoint(bat.xpoints[i], bat.ypoints[i]);
		}
		
		if(Math.abs(bat.xpoints[startIndex] - bat.xpoints[(startIndex+1)%bat.npoints]) < Math.abs(bat.ypoints[startIndex] - bat.ypoints[(startIndex+1)%bat.npoints])) {
			System.out.println("Startet in Vertikaler Linie");
			if(bat.ypoints[startIndex] < prototypePoints.get(0).getY()) {
				System.out.println("Ziehe y ab");
				toReturn.addPoint(bat.xpoints[startIndex], (int)prototypePoints.get(0).getY()-t);
			}
			else {
				System.out.println("füge y hinzu");
				toReturn.addPoint(bat.xpoints[startIndex], (int)prototypePoints.get(0).getY()+t);
			}
		}
		else {
			System.out.println("Startet in Horizontaler Linie");
			if(bat.xpoints[startIndex] < prototypePoints.get(0).getX()) {
				System.out.println("Ziehe x ab");
				toReturn.addPoint((int)prototypePoints.get(0).getX()-t, bat.ypoints[startIndex]);
			}
			else {
				System.out.println("Füge x hinzu");
				toReturn.addPoint((int)prototypePoints.get(0).getX()+t, bat.ypoints[startIndex]);
			}
			
			//b1.addPoint((int)prototypeWall.getEdge(0).getX()-5, (int)prototypeWall.getEdge(0).getY());*/
		}
		
		/*
		 * Iteriere durch die Knoten der PrototypeWall, die zwischen Start und Ende liegen
		 * */
		for(int i = 1; i<prototypePoints.size()-1; i++) {
			/* Okay, also es gibt vier Möglichkeiten für den neuen Knoten. +5 Pixel bzw. -5 Pixel in X bzw. Y Richtung vom eigentlichen Edge der prototypeWall verschoben.
			 * Zwei davon kann man ausschließen, wenn man sich den vorangehenden bzw. nächsten Edge der PrototypeWall ansieht. Wenn von einem zum anderen beide Koordinaten sowohl
			 * in X als auch in Y Richtung größer oder kleiner werden, so liegt der Edge dazwischen entweder -5 in x und +5 y oder -5 in y und +5 in x Richtung vom eigentlichen Edge der PrototypeWall
			 * entfernt. Ist diese Bedingung nicht gegeben sind es entsprechend +5 in x und +5 in y bzw. -5 in x und -5 in y.
			 * Welche dieser zwei Optionen dann die richtige ist kann man nur bestimmen, indem man schaut ob der Punkt im originalen Battlefield läge oder nicht
			 * */
			if((prototypePoints.get(i-1).getX()-prototypePoints.get(i+1).getX()) * (prototypePoints.get(i-1).getY() - prototypePoints.get(i+1).getY()) > 0) {
				//Entweder x+5 und y+5 oder x-5 und y-5
				if(original.contains(prototypePoints.get(i).getX()+t, prototypePoints.get(i).getY()-t)) {
					toReturn.addPoint((int)prototypePoints.get(i).getX()+t, (int)prototypePoints.get(i).getY()-t); 
				}
				else {
					toReturn.addPoint((int)prototypePoints.get(i).getX()-t, (int)prototypePoints.get(i).getY()+t); 
				}
			}
			else {
				//Entweder x+5 und y-5 oder x-5 und y+5
				if(original.contains(prototypePoints.get(i).getX()+t, prototypePoints.get(i).getY()+t)) {
					toReturn.addPoint((int)prototypePoints.get(i).getX()+t, (int)prototypePoints.get(i).getY()+t); 
				}
				else {
					toReturn.addPoint((int)prototypePoints.get(i).getX()-t, (int)prototypePoints.get(i).getY()-t); 
				}
			}
			
			
			//toReturn.addPoint((int)prototypeWall.getEdge(i).getX()-5, (int)prototypeWall.getEdge(i).getY()-5);
		}
		
		if(Math.abs(bat.xpoints[(endIndex+1)%bat.npoints] - prototypePoints.get(prototypePoints.size()-1).getX()) < Math.abs(bat.ypoints[(endIndex+1)%bat.npoints] - prototypePoints.get(prototypePoints.size()-1).getY())) {
			System.out.println("Endet in Vertikaler Linie");
			if(bat.ypoints[(endIndex+1)%bat.npoints] < prototypePoints.get(prototypePoints.size()-1).getY()) {
				System.out.println("Ziehe y ab");
				toReturn.addPoint(bat.xpoints[(endIndex+1)%bat.npoints], (int)prototypePoints.get(prototypePoints.size()-1).getY()-t);
			}
			else {
				System.out.println("Füge y hinzu");
				toReturn.addPoint(bat.xpoints[(endIndex+1)%bat.npoints], (int)prototypePoints.get(prototypePoints.size()-1).getY()+t);
			}
		}
		else {
			System.out.println("Endet in Horizontaler Linie");
			if(bat.xpoints[(endIndex+1)%bat.npoints] < prototypePoints.get(prototypePoints.size()-1).getX()) {
				System.out.println("Ziehe x ab");
				toReturn.addPoint((int)prototypePoints.get(prototypePoints.size()-1).getX()-t, bat.ypoints[(endIndex+1)%bat.npoints]);
			}
			else {
				System.out.println("Füge x hinzu");
				toReturn.addPoint((int)prototypePoints.get(prototypePoints.size()-1).getX()+t, bat.ypoints[(endIndex+1)%bat.npoints]);
			}
			
		}
		for(int i = endIndex+1; i<bat.npoints; i++) {
			toReturn.addPoint(bat.xpoints[i], bat.ypoints[i]);
		}
		return toReturn;
	}

	private void calculateStartAndEndIndex(PrototypeWall wall) {
		Line2D line = new Line2D.Double();
		double smallestDistanceStart = Double.MAX_VALUE;
		startIndex = -1;
		
		double smallestDistanceEnd = Double.MAX_VALUE;
		endIndex = -1;
		
		Point2D start = wall.getStart();
		Point2D end = wall.getEnd();
		
		//Finde die Linien des Polygons mit den kürzesten entfernungen zum end und start punkt der prototypeWall
		for(int i = 0; i < bat.npoints; i++) {
			line.setLine(bat.xpoints[i], bat.ypoints[i], bat.xpoints[(i+1)%bat.npoints], bat.ypoints[(i+1)%bat.npoints]); //Betrachte die einzelnen Linien
			double distStart = line.ptSegDist(start.getX(), start.getY());
			if(distStart < smallestDistanceStart) {
				smallestDistanceStart = distStart;
				startIndex = i; //Bei 0 wäre es die Linie zwischen 0 und 1, die am nächsten am Start der Prototype Wall liegt
			}
			
			double distEnd = line.ptSegDist(end.getX(), end.getY());
			if(distEnd < smallestDistanceEnd) {
				smallestDistanceEnd = distEnd;
				endIndex = i;
			}
		}
		
		prototypePoints = new ArrayList<Point2D>();
		
        //Falls der endIndex kleiner als der startIndex ist, dann tausche sie und kehre die Reihenfolge der PrototypeWall um
		if((endIndex < startIndex) ||(startIndex == endIndex && start.distanceSq(bat.xpoints[startIndex], bat.ypoints[startIndex]) > end.distanceSq(bat.xpoints[startIndex], bat.ypoints[startIndex]))) { 
			int tmp = endIndex;
			endIndex = startIndex;
			startIndex = tmp;
			
			for(int i = 0; i<wall.getEdgeCount(); i++) {
				prototypePoints.add(0, wall.getEdge(i));
			}
		}
		else {
			for(int i = 0; i<wall.getEdgeCount(); i++) {
				prototypePoints.add(wall.getEdge(i));
			}
		}
		
	}
	
	private Battlefield getFirstBattlefield() {
		Battlefield toReturn = new Battlefield();		
	
		for(int i = 0; i<= startIndex; i++) {
			toReturn.addPoint(bat.xpoints[i], bat.ypoints[i]);
		}
		
		if(Math.abs(bat.xpoints[startIndex] - bat.xpoints[(startIndex+1)%bat.npoints]) < Math.abs(bat.ypoints[startIndex] - bat.ypoints[(startIndex+1)%bat.npoints])) {
			System.out.println("Startet in Vertikaler Linie");
			toReturn.addPoint(bat.xpoints[startIndex], (int)prototypePoints.get(0).getY());
		}
		else {
			System.out.println("Startet in Horizontaler Linie");
			toReturn.addPoint((int)prototypePoints.get(0).getX(), bat.ypoints[startIndex]);
		}
		
		for(int i = 1; i<prototypePoints.size()-1; i++) {
			toReturn.addPoint((int)prototypePoints.get(i).getX(), (int)prototypePoints.get(i).getY());
		}
		
		if(Math.abs(bat.xpoints[(endIndex+1)%bat.npoints] - prototypePoints.get(prototypePoints.size()-1).getX()) < Math.abs(bat.ypoints[(endIndex+1)%bat.npoints] - prototypePoints.get(prototypePoints.size()-1).getY())) {
			System.out.println("Endet in Vertikaler Linie");
			toReturn.addPoint(bat.xpoints[(endIndex+1)%bat.npoints], (int)prototypePoints.get(prototypePoints.size()-1).getY());
		}
		else {
			System.out.println("Endet in Horizontaler Linie");
			toReturn.addPoint((int)prototypePoints.get(prototypePoints.size()-1).getX(), bat.ypoints[(endIndex+1)%bat.npoints]);
		}
		
		
		for(int i = endIndex+1; i<bat.npoints; i++) {
			toReturn.addPoint(bat.xpoints[i], bat.ypoints[i]);
		}
	
		return toReturn;
	}
	
	private Battlefield getSecondBattlefield() {
		Battlefield toReturn = new Battlefield();		
		
		if(Math.abs(bat.xpoints[startIndex] - bat.xpoints[(startIndex+1)%bat.npoints]) < Math.abs(bat.ypoints[startIndex] - bat.ypoints[(startIndex+1)%bat.npoints])) {
			System.out.println("Startet in Vertikaler Linie");
			toReturn.addPoint(bat.xpoints[startIndex], (int)prototypePoints.get(0).getY());
		}
		else {
			System.out.println("Startet in Horizontaler Linie");
			toReturn.addPoint((int)prototypePoints.get(0).getX(), bat.ypoints[startIndex]);
		}
		
		for(int i = 1; i<prototypePoints.size()-1; i++) {
			toReturn.addPoint((int)prototypePoints.get(i).getX(), (int)prototypePoints.get(i).getY());
		}
		
		if(Math.abs(bat.xpoints[(endIndex+1)%bat.npoints] - prototypePoints.get(prototypePoints.size()-1).getX()) < Math.abs(bat.ypoints[(endIndex+1)%bat.npoints] - prototypePoints.get(prototypePoints.size()-1).getY())) {
			System.out.println("Endet in Vertikaler Linie");
			toReturn.addPoint(bat.xpoints[(endIndex+1)%bat.npoints], (int)prototypePoints.get(prototypePoints.size()-1).getY());
		}
		else {
			System.out.println("Endet in Horizontaler Linie");
			toReturn.addPoint((int)prototypePoints.get(prototypePoints.size()-1).getX(), bat.ypoints[(endIndex+1)%bat.npoints]);
		}
		
		for(int i = endIndex; i > startIndex; i--) {
			toReturn.addPoint(bat.xpoints[i], bat.ypoints[i]);
		}
		
		return toReturn;
	}

	public Battlefield getB1() {
		return b1;
	}
	
	public Battlefield getB2() {
		return b2;
	}
	
}
