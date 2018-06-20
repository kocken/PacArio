package npcs;

import java.awt.Color;
import java.awt.Point;

import main.Listeners;
import main.Main;
import main.Metoder;
import main.Panel;

public class Blobs implements NPC {
	
	public static int amount = 10; // antal blobbar, �ndras beroende p� spell�ge
	public static double[] x = new double[1000]; // blobbarnas x-koordinationer p� spelf�nstret
	public static double[] y = new double[1000]; // blobbarnas y-koordinationer p� spelf�nstret
	public double[] speed = new double[1000]; // blobbarnas r�relse hastigheter
	public static int[] size = new int[1000]; // blobbarnas storlekar
	public int[] rotation = new int[1000]; // blobbarnas spawn-platser (�ver/h�ger/nedanf�r/v�nster om spelrutan)
	public static Color[] color = new Color[1000]; // blobbarnas bakgrundsf�rger
	public static Boolean[] alive = new Boolean[1000]; // sant om blobben lever och �r spawnad, falskt om blobben �r d�d, null fr�n start
	public static Point[] middlePoint = new Point[1000]; // mittpunkten i blobbarnas cirklar
		
	public void logic() { // blobbarnas huvudlogik
		for (int element = 0; amount > element; element++) { // loopar igenom alla aktuella blobbar
			
			if (Listeners.firstEventExecuted && // om spelaren har r�rt musen �ver spelrutan
				(alive[element] == null || !alive[element] || rotation[element] == 0 || // om blobben �r d�d eller �n inte blivit spawnad
				!isOnScreen(element) && (PacMan.showMenu() || !Main.gameMode.equals("survival")))) // om blobben �r utanf�r sk�rmen & menyn visas/spell�get inte �r survival
					spawn(element); // spawnar ("livar upp") den aktuellt loopade blobben utanf�r spelrutan med slumpm�ssig data
			
			move(element); // r�r blobben fram�t, horisontellt eller vertikalt �ver spelrutan
			middlePoint[element] = new Point((int) x[element]+size[element]/2, (int) y[element]+size[element]/2); // lagrar blobb-cirkelns uppdaterade mittpunkt
		}
	}
	
	private void spawn(int element) {
		if (!PacMan.showMenu() && Main.gameMode.equals("agario"))
			size[element] = Metoder.randomInt(PacMan.size/5, PacMan.size*2); // slumpar blobbens storlek
		else if (PacMan.showMenu())
			size[element] = Metoder.randomInt(10, 75);
		else
			size[element] = Metoder.randomInt(PacMan.size/5, PacMan.size-1);
		color[element] = Metoder.randomColor(); // slumpar blobbens f�rg
		if (Main.gameMode.equals("survival") && blobsOnScreen() == 1)
			speed[element] = Metoder.randomDouble(PacMan.speed*0.8, PacMan.speed*1.8); // slumpar blobbens hastighet
		else if (PacMan.showMenu())
			speed[element] = Metoder.randomDouble(PacMan.speed/4, PacMan.speed*0.8);
		else
			speed[element] = Metoder.randomDouble(PacMan.speed/4, PacMan.speed*1.8);
		int r = Metoder.randomInt(1, 4); // slumpar blobbens spawn (h�ger/v�nster/ovanf�r/nedanf�r spelrutan)
		switch (r) {
		case 1: // spawnar v�nster om spelf�nstret
			x[element] = -size[element];
			y[element] = Metoder.randomInt(0, Panel.gfxHeight-size[element]);
			break;
		case 2: // spawnar h�ger om spelf�nstret
			x[element] = Panel.gfxWidth;
			y[element] = Metoder.randomInt(0, Panel.gfxHeight-size[element]);
			break;
		case 3: // spawnar ovanf�r spelf�nstret
			x[element] = Metoder.randomInt(0, Panel.gfxWidth-size[element]);
			y[element] = -size[element];
			break;
		case 4: // spawnar nedanf�r spelf�nstret
			x[element] = Metoder.randomInt(0, Panel.gfxWidth-size[element]);
			y[element] = Panel.gfxWidth;
			break;
		}
		rotation[element] = r; // lagrar blobbens rotation s� blobben vet �t vilket h�ll den ska �ka mot
		alive[element] = true;
	}
	
	private void move(int element) {
		switch (rotation[element]) {
		case 1: // spawnade v�nster om spelrutan = �ker h�ger med blobbens slumpade hastighet
			x[element] += speed[element];
			break;
		case 2: // spawnade h�ger om spelrutan = �ker v�nster med blobbens slumpade hastighet
			x[element] -= speed[element];
			break;
		case 3: // spawnade ovanf�r spelrutan = �ker ner�t med blobbens slumpade hastighet
			y[element] += speed[element];
			break;
		case 4: // spawnade nedanf�r spelrutan = �ker upp�t med blobbens slumpade hastighet
			y[element] -= speed[element];
			break;
		}
	}
	
	public void reset() { // �ndrar blobb-antalet inf�r nya omg�ngar, talet �r olika beroende p� spell�ge
		deSpawn(); // "d�dar" alla aktuella blobbar och flyttar de utanf�r spelrutan
		switch(Main.gameMode) {
		case "agario":
			amount = 10;
			break;
		case "survival":
			amount = 100;
			break;
		case "escape":
			amount = 15;
			break;
		}
	}
	
	private void deSpawn() {
		for (int i = 0; amount > i; i++) {
			alive[i] = null;
			x[i] = -1000;
			y[i] = -1000;
		}
	}
	
	public boolean allPassedOverScreen() { // returnerar sant om det inte finns n�gra blobbar p� spelsk�rmen, anv�nds f�r survival-spell�get
		return blobsOnScreen() == 0;
	}
	
	private int blobsOnScreen() { // returnerar antal blobbar p� sk�rmen
		int r = 0;
		for (int element = 0; amount > element; element++) {
			if (isOnScreen(element))
				r++;
		}
		return r;
	}
	
	private boolean isOnScreen(int element) { // returnerar sant om blobb-elementet finns och �r synlig p� spelrutan
		return  rotation[element] == 1 && x[element] < Panel.gfxWidth || 
				rotation[element] == 2 && x[element] > 0-size[element] || 
				rotation[element] == 3 && y[element] < Panel.gfxHeight || 
				rotation[element] == 4 && y[element] > 0-size[element];
	}

	@Override	public void move() {} // oanv�nd metod (denna klass anv�nder ist�llet move(int element)) men kr�vs p.g.a. gr�nsnitt-implenteringen
	
}