package npcs;

import java.awt.Color;
import java.awt.Point;

import main.Listeners;
import main.Main;
import main.Metoder;
import main.Panel;

public class Blobs implements NPC {
	
	public static int amount = 10; // antal blobbar, ändras beroende på spelläge
	public static double[] x = new double[1000]; // blobbarnas x-koordinationer på spelfönstret
	public static double[] y = new double[1000]; // blobbarnas y-koordinationer på spelfönstret
	public double[] speed = new double[1000]; // blobbarnas rörelse hastigheter
	public static int[] size = new int[1000]; // blobbarnas storlekar
	public int[] rotation = new int[1000]; // blobbarnas spawn-platser (över/höger/nedanför/vänster om spelrutan)
	public static Color[] color = new Color[1000]; // blobbarnas bakgrundsfärger
	public static Boolean[] alive = new Boolean[1000]; // sant om blobben lever och är spawnad, falskt om blobben är död, null från start
	public static Point[] middlePoint = new Point[1000]; // mittpunkten i blobbarnas cirklar
		
	public void logic() { // blobbarnas huvudlogik
		for (int element = 0; amount > element; element++) { // loopar igenom alla aktuella blobbar
			
			if (Listeners.firstEventExecuted && // om spelaren har rört musen över spelrutan
				(alive[element] == null || !alive[element] || rotation[element] == 0 || // om blobben är död eller än inte blivit spawnad
				!isOnScreen(element) && (PacMan.showMenu() || !Main.gameMode.equals("survival")))) // om blobben är utanför skärmen & menyn visas/spelläget inte är survival
					spawn(element); // spawnar ("livar upp") den aktuellt loopade blobben utanför spelrutan med slumpmässig data
			
			move(element); // rör blobben framåt, horisontellt eller vertikalt över spelrutan
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
		color[element] = Metoder.randomColor(); // slumpar blobbens färg
		if (Main.gameMode.equals("survival") && blobsOnScreen() == 1)
			speed[element] = Metoder.randomDouble(PacMan.speed*0.8, PacMan.speed*1.8); // slumpar blobbens hastighet
		else if (PacMan.showMenu())
			speed[element] = Metoder.randomDouble(PacMan.speed/4, PacMan.speed*0.8);
		else
			speed[element] = Metoder.randomDouble(PacMan.speed/4, PacMan.speed*1.8);
		int r = Metoder.randomInt(1, 4); // slumpar blobbens spawn (höger/vänster/ovanför/nedanför spelrutan)
		switch (r) {
		case 1: // spawnar vänster om spelfönstret
			x[element] = -size[element];
			y[element] = Metoder.randomInt(0, Panel.gfxHeight-size[element]);
			break;
		case 2: // spawnar höger om spelfönstret
			x[element] = Panel.gfxWidth;
			y[element] = Metoder.randomInt(0, Panel.gfxHeight-size[element]);
			break;
		case 3: // spawnar ovanför spelfönstret
			x[element] = Metoder.randomInt(0, Panel.gfxWidth-size[element]);
			y[element] = -size[element];
			break;
		case 4: // spawnar nedanför spelfönstret
			x[element] = Metoder.randomInt(0, Panel.gfxWidth-size[element]);
			y[element] = Panel.gfxWidth;
			break;
		}
		rotation[element] = r; // lagrar blobbens rotation så blobben vet åt vilket håll den ska åka mot
		alive[element] = true;
	}
	
	private void move(int element) {
		switch (rotation[element]) {
		case 1: // spawnade vänster om spelrutan = åker höger med blobbens slumpade hastighet
			x[element] += speed[element];
			break;
		case 2: // spawnade höger om spelrutan = åker vänster med blobbens slumpade hastighet
			x[element] -= speed[element];
			break;
		case 3: // spawnade ovanför spelrutan = åker neråt med blobbens slumpade hastighet
			y[element] += speed[element];
			break;
		case 4: // spawnade nedanför spelrutan = åker uppåt med blobbens slumpade hastighet
			y[element] -= speed[element];
			break;
		}
	}
	
	public void reset() { // ändrar blobb-antalet inför nya omgångar, talet är olika beroende på spelläge
		deSpawn(); // "dödar" alla aktuella blobbar och flyttar de utanför spelrutan
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
	
	public boolean allPassedOverScreen() { // returnerar sant om det inte finns några blobbar på spelskärmen, används för survival-spelläget
		return blobsOnScreen() == 0;
	}
	
	private int blobsOnScreen() { // returnerar antal blobbar på skärmen
		int r = 0;
		for (int element = 0; amount > element; element++) {
			if (isOnScreen(element))
				r++;
		}
		return r;
	}
	
	private boolean isOnScreen(int element) { // returnerar sant om blobb-elementet finns och är synlig på spelrutan
		return  rotation[element] == 1 && x[element] < Panel.gfxWidth || 
				rotation[element] == 2 && x[element] > 0-size[element] || 
				rotation[element] == 3 && y[element] < Panel.gfxHeight || 
				rotation[element] == 4 && y[element] > 0-size[element];
	}

	@Override	public void move() {} // oanvänd metod (denna klass använder istället move(int element)) men krävs p.g.a. gränsnitt-implenteringen
	
}