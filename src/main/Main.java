package main;

import npcs.Blobs;
import npcs.PacMan;

public class Main {
	
	private int sleep = 20; // tid (i millisekunder) som programmet sover efter varje loop
	public static int score = 0; // spelarens po�ng
	
	public static boolean startRun = false; // startar en ny omg�ng n�r v�rdet �r sant
	
	public static String gameMode = "agario"; // det aktuella spell�get
	
	PacMan pacMan = new PacMan(); // skapar ett objekt av PacMan-klassen
	Blobs blobs = new Blobs(); // skapar ett objekt av Blobs-klassen
		
	public static void main(String[] args) {
		
		GameWindow gameWindow = new GameWindow(); // startar ett objekt av det grafiska applikationsf�nstret
		gameWindow.setVisible(true); // st�ller in f�nstret s� det blir synligt
		
		Listeners listeners = new Listeners(); // skapar ett objekt av Listeners-klassen s� att klassens flesta variabler och metoder inte beh�ver vara statiska
		listeners.init(GameWindow.panel); // startar listeners (avlyssnare) f�r Keyboard, Mouse och MouseMotion
		
		Main main = new Main(); // skapar ett objekt av denna klass f�r att f� till�telse till att k�ra och h�mta o-statisk kod
		
		while(true) // huvudloop
			Metoder.sleep(main.logic()); // utf�r spelets huvudlogik i "loop()" metoden och sover sedan 20 ms, det v�rde som �r tilldelat "sleep" variabeln
		
	}
	
	private int logic() {
		if (startRun) // om en ny omg�ng b�r startas
			startRun(); // startar ny omg�ng
		if (PacMan.alive) { // om PacMan lever
			blobs.logic(); // hanterar blobbarna; spawnar och r�r de fram�t
			pacMan.move(); // hanterar spelarens PacMan-figur; animerar munnen och r�r figuren fram�t mot riktningen som spelaren sist ville �ka mot
			gameMode(); // utf�r spelets aktuellt valda spell�ge-logik
		}
		else { // om PacMan �r d�d
			blobs.logic();
			if (PacMan.spawned && !PacMan.isEaten()) // om PacMans �t-upp-sig-sj�lv animation inte �r f�rdig �n
				pacMan.animateMouth(false, null, null); // utf�r PacMans �t-upp-sig-sj�lv animation
		}
		
		return sleep;
	}
	
	private int counter = 0; // variabel som anv�nds f�r "escape" spell�gets po�nginsamling
	private void gameMode() {
		switch (gameMode) {
		case "agario":
			if (pacMan.isOnBlob()) { // om PacMan �r �ver en blobb
				if (PacMan.size > Blobs.size[pacMan.nearestBlobElement]) // Om PacMan �r st�rre �n den n�rmsta Blobben
					pacMan.eatBlob(); // PacMan �ter upp (d�dar) den n�rmsta blobben, PacMan-figuren v�xer i storlek och spelaren f�r po�ng
				else // Om den n�rmsta Blobben �r st�rre �n PacMan
					pacMan.lose(); // spelaren f�rlorar = game over
			}
			break;
			
		case "survival":
			if (pacMan.isOnBlob())
				pacMan.eatBlob();
			else if (blobs.allPassedOverScreen()) // om inga blobbar finns synliga p� sk�rmen
				pacMan.lose();
			break;

		case "escape":
			if (pacMan.isOnBlob())
				pacMan.lose();
			else if (PacMan.alive) {
				counter++;
				if (counter >= 10) { // sant varje 200ms (10 * sleep (20) =200ms)
					score++; // ger spelaren +1 po�ng
					if (score%16 == 0) // sant varje 3200ms (200*16)
						Blobs.amount++; // l�gger till +1 blobb till den p�goende spelomg�ngen
					counter = 0; // �terst�ller variabeln
				}
			}
			break;
		}
	}
	
	private void startRun() {
		startRun = false; // �terst�ller variabeln f�r att undvika en o�ndlig loop av omstartningar
		score = 0; // nollst�ller po�ngtalet
		pacMan.reset(); // �terst�ller PacMans v�rden (storlek, position, mun etc)
		blobs.reset(); // flyttar blobbarna utanf�r spelrutan och �ndrar antalet blobbar till ett specifikt v�rde beroende p� det aktuella spell�get
		for (int i = 0; Panel.menuColor.length > i; i++) // loopar igenom alla meny-knappar
			Panel.menuColor[i] = Panel.menuBase; // �terst�ller menyknapparnas bakgrundsf�rg till basf�rgen (bl�)
		for (int i = 0; 40 > i; i++) { // loopar ("sover") i 4 sekunder, f�re omg�ngen startar
			pacMan.center(); // placerar PacMan-figuren i mitten av spelsk�rmen
			pacMan.direction(); // roterar PacMans mun �t h�llet som spelaren sist f�rs�kte �ka mot
			if (i < 10) // 0-1000ms efter start
				Panel.title = "Starting in 3 seconds"; // visar titeln "Starting in 3 seconds"
			else if (i >= 10 && i < 20) // 1000-1900ms efter start
				Panel.title = "Starting in 2 seconds";
			else if (i >= 20 && i < 30) // 2000-2900ms efter start
				Panel.title = "Starting in 1 second";
			else if (i >= 30 && i < 39) // 3000-3900ms efter start
				Panel.title = "GO!";
			else
				Panel.title = ""; // �terst�ller variabelns text
			Metoder.sleep(100); // g�r s� att applikationen sover i 100ms
		}
	}

}