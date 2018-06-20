package main;

import npcs.Blobs;
import npcs.PacMan;

public class Main {
	
	private int sleep = 20; // tid (i millisekunder) som programmet sover efter varje loop
	public static int score = 0; // spelarens poäng
	
	public static boolean startRun = false; // startar en ny omgång när värdet är sant
	
	public static String gameMode = "agario"; // det aktuella spelläget
	
	PacMan pacMan = new PacMan(); // skapar ett objekt av PacMan-klassen
	Blobs blobs = new Blobs(); // skapar ett objekt av Blobs-klassen
		
	public static void main(String[] args) {
		
		GameWindow gameWindow = new GameWindow(); // startar ett objekt av det grafiska applikationsfönstret
		gameWindow.setVisible(true); // ställer in fönstret så det blir synligt
		
		Listeners listeners = new Listeners(); // skapar ett objekt av Listeners-klassen så att klassens flesta variabler och metoder inte behöver vara statiska
		listeners.init(GameWindow.panel); // startar listeners (avlyssnare) för Keyboard, Mouse och MouseMotion
		
		Main main = new Main(); // skapar ett objekt av denna klass för att få tillåtelse till att köra och hämta o-statisk kod
		
		while(true) // huvudloop
			Metoder.sleep(main.logic()); // utför spelets huvudlogik i "loop()" metoden och sover sedan 20 ms, det värde som är tilldelat "sleep" variabeln
		
	}
	
	private int logic() {
		if (startRun) // om en ny omgång bör startas
			startRun(); // startar ny omgång
		if (PacMan.alive) { // om PacMan lever
			blobs.logic(); // hanterar blobbarna; spawnar och rör de framåt
			pacMan.move(); // hanterar spelarens PacMan-figur; animerar munnen och rör figuren framåt mot riktningen som spelaren sist ville åka mot
			gameMode(); // utför spelets aktuellt valda spelläge-logik
		}
		else { // om PacMan är död
			blobs.logic();
			if (PacMan.spawned && !PacMan.isEaten()) // om PacMans ät-upp-sig-själv animation inte är färdig än
				pacMan.animateMouth(false, null, null); // utför PacMans ät-upp-sig-själv animation
		}
		
		return sleep;
	}
	
	private int counter = 0; // variabel som används för "escape" spellägets poänginsamling
	private void gameMode() {
		switch (gameMode) {
		case "agario":
			if (pacMan.isOnBlob()) { // om PacMan är över en blobb
				if (PacMan.size > Blobs.size[pacMan.nearestBlobElement]) // Om PacMan är större än den närmsta Blobben
					pacMan.eatBlob(); // PacMan äter upp (dödar) den närmsta blobben, PacMan-figuren växer i storlek och spelaren får poäng
				else // Om den närmsta Blobben är större än PacMan
					pacMan.lose(); // spelaren förlorar = game over
			}
			break;
			
		case "survival":
			if (pacMan.isOnBlob())
				pacMan.eatBlob();
			else if (blobs.allPassedOverScreen()) // om inga blobbar finns synliga på skärmen
				pacMan.lose();
			break;

		case "escape":
			if (pacMan.isOnBlob())
				pacMan.lose();
			else if (PacMan.alive) {
				counter++;
				if (counter >= 10) { // sant varje 200ms (10 * sleep (20) =200ms)
					score++; // ger spelaren +1 poäng
					if (score%16 == 0) // sant varje 3200ms (200*16)
						Blobs.amount++; // lägger till +1 blobb till den pågoende spelomgången
					counter = 0; // återställer variabeln
				}
			}
			break;
		}
	}
	
	private void startRun() {
		startRun = false; // återställer variabeln för att undvika en oändlig loop av omstartningar
		score = 0; // nollställer poängtalet
		pacMan.reset(); // återställer PacMans värden (storlek, position, mun etc)
		blobs.reset(); // flyttar blobbarna utanför spelrutan och ändrar antalet blobbar till ett specifikt värde beroende på det aktuella spelläget
		for (int i = 0; Panel.menuColor.length > i; i++) // loopar igenom alla meny-knappar
			Panel.menuColor[i] = Panel.menuBase; // återställer menyknapparnas bakgrundsfärg till basfärgen (blå)
		for (int i = 0; 40 > i; i++) { // loopar ("sover") i 4 sekunder, före omgången startar
			pacMan.center(); // placerar PacMan-figuren i mitten av spelskärmen
			pacMan.direction(); // roterar PacMans mun åt hållet som spelaren sist försökte åka mot
			if (i < 10) // 0-1000ms efter start
				Panel.title = "Starting in 3 seconds"; // visar titeln "Starting in 3 seconds"
			else if (i >= 10 && i < 20) // 1000-1900ms efter start
				Panel.title = "Starting in 2 seconds";
			else if (i >= 20 && i < 30) // 2000-2900ms efter start
				Panel.title = "Starting in 1 second";
			else if (i >= 30 && i < 39) // 3000-3900ms efter start
				Panel.title = "GO!";
			else
				Panel.title = ""; // återställer variabelns text
			Metoder.sleep(100); // gör så att applikationen sover i 100ms
		}
	}

}