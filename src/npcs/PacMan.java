package npcs;

import java.awt.Point;

import main.Main;
import main.Panel;

public class PacMan implements NPC {
	
	public static double size = 50; // PacMans storlek
	public static double x = (Panel.gfxWidth-size)/2; // PacMans x-position i grafiken, 
	public static double y = (Panel.gfxHeight-size)/2; // PacMans y-position i grafiken
	public static double speed = 3; // PacMans rörselse hastighet
	private double mouthAngleChange = 6; // PacMans mun-animation hastighet
	public Point middlePoint = new Point((int) (x+size/2), (int) (y+size/2)); // PacMan-cirkelns mittpunkt
	
	public static int arcAngle = 270; // arcAngle parametern för PacMans mun-animation-utmålning med #fillArc-metoden
	public static int startAngle = 225; // startAngle parametern för PacMans mun-animation-utmålning med #fillArc-metoden, 225 = PacMans mun startar riktad åt vänster
	private int maxStartAngle = startAngle; /* startAngle-variabelns max-värde som inte ska överskridas när PacMans
													 #fillArc-metod-parametrar modifieras */
	
	public static String direction = "left"; // riktningen som PacMan är riktad mot, startar riktad åt vänster som standard
	private String lastDirection = direction; // riktningen som användaren sist ville åka mot, används för att rotera PacMans mun mot den nya destinationen
		
	public static boolean alive = false; // sant om PacMan lever och en spelomgång är igång, falskt annars
	public static boolean spawned = false; // sant efter den första spelomgången har startats, används som en indikator för att måla PacMan
	
	public int nearestBlobElement = 0; // variabel som håller koll på blobben (elementet från blobb-arrayen) som är närmast PacMan-figuren
		
	public void move() {
		direction(); // roterar PacMan åt korrekt riktning som användaren vill åka mot
		animateMouth(false, null, null); // animerar PacMans mun så den öppnas och stängs när figuren åker framåt
		if (canWalkForward()) { // om PacMan kan åka framåt, sant om PacMan-figuren inte försöker åka utanför spelfönstret
			if (isFacedLeft()) // om PacMan är riktad mot vänster riktning
				goLeft(); // åker vänster
			else if (isFacedUp())
				goUp();
			else if (isFacedRight())
				goRight();
			else if (isFacedDown())
				goDown();
		}
		middlePoint = new Point((int) (x+size/2), (int) (y+size/2)); // uppdaterar PacMan-cirkelns mittpunkt
	}
	
	public void direction() {
		if (!lastDirection.equals(direction) && alive) // om användaren vill åka mot en annan riktning
			animateMouth(true, lastDirection, direction); // roterar PacMans mun mot den nya riktningen
		lastDirection = direction; // lagrar den nya riktningen
	}
	
	private boolean canWalkForward() {
		if (alive) {
			if (isFacedLeft())
				return canWalkLeft(); // returnerar sant om PacMans cirkel kan åka vänster utan att cirkeln åker utanför spelrutan
			if (isFacedUp())
				return canWalkUp();
			else if (isFacedRight())
				return canWalkRight();
			else if (isFacedDown())
				return canWalkDown();
		}
		return false;
	}
	
	public void center() { // ställer in PacMans x- och y-koordinationer så att PacMan-figuren centreras i mitten av spelfönstret
		x = (Panel.gfxWidth-size)/2;
		y = (Panel.gfxHeight-size)/2;
	}
	
	private boolean openMouth = false; // falskt när PacMan-munnen ska stängas, sant när munnen ska öppnas
	public void animateMouth(boolean rotateMouth, String lastDirection, String newDirection) { // roterar/animerar PacMans mun genom att modifiera utmålning-metodens parametrar
		if (!rotateMouth && (canWalkForward() || !alive && !isEaten() || startAngle != maxStartAngle)) { // sant om munnen ska animeras (öppnas/stängas)
			if (openMouth || !alive && !isEaten()) { // om munnen ska öppnas
				startAngle += mouthAngleChange;
				if (alive || !alive && arcAngle >= mouthAngleChange * 2)
					arcAngle -= mouthAngleChange * 2;
				else if (!alive && mouthAngleChange * 2 > arcAngle) // om PacMan har dött och är i slutet av sin äta-upp-sig-själv animationen
					arcAngle = 0;
				if (startAngle >= maxStartAngle || arcAngle <= 270) // om munnen är fullt öppnad
					openMouth = false; // ändrar värdet för att börja stänga munnen
			} else if (alive) { // om munnen ska stängas
				startAngle -= mouthAngleChange;
				arcAngle += mouthAngleChange * 2;
				if (startAngle <= maxStartAngle - 45 || arcAngle >= 360) // om munnen är stängd
					openMouth = true; // ändrar värdet för att börja öppna munnen
			}
		}
		else if (rotateMouth) { // roterar PacMans mun för att visa att riktningen är bytt
			maxStartAngle += (getDirectionValue(lastDirection)-getDirectionValue(newDirection)) * 90;
			startAngle += (getDirectionValue(lastDirection)-getDirectionValue(newDirection)) * 90;
		}
	}
	
	private int getDirectionValue(String direction) { // returnerar ett värde beroende på riktning, vilket används i de två överstående rotatera-mun-algoritmerna
		if (direction.equals("left"))
			return 0;
		else if (direction.equals("up"))
			return 1;
		else if (direction.equals("right"))
			return 2;
		else // "down"
			return 3;
	}
	
	private void goLeft() {
		if (x-speed >= 0) // om PacMan inte kommer hamna utanför spelfönstret efter förflyttelse
			x -= speed;
		else // flytta PacMan mot spelfönstrets kant
			x += 0-x;
	}
	
	private void goUp() {
		if (y-speed >= 0)
			y -= speed;
		else
			y += 0-y;
	}
	
	private void goRight() {
		if (Panel.gfxWidth-x-size >= speed)
			x += speed;
		else 
			x += Panel.gfxWidth-x-size;
	}
	
	private void goDown() {
		if (Panel.gfxHeight-y-size >= speed)
			y += speed;
		else
			y += Panel.gfxHeight-y-size;
	}
	
	private boolean isFacedLeft() {
		return direction.equals("left");
	}
	
	private boolean isFacedUp() {
		return direction.equals("up");
	}
	
	private boolean isFacedRight() {
		return direction.equals("right");
	}
	
	private boolean isFacedDown() {
		return direction.equals("down");
	}
	
	private boolean canWalkLeft() {
		return x > 0;
	}
	
	private boolean canWalkUp() {
		return y > 0;
	}
	
	private boolean canWalkRight() {
		return x+size < Panel.gfxWidth;
	}
	
	private boolean canWalkDown() {
		return y+size < Panel.gfxHeight;
	}
	
	private int getNearestBlobElement() { // returnerar det blobb-element (i blobb-arrayen) som är närmast PacMan-figuren
		int nearestElement = 0;
		for (int i = 0; Blobs.amount > i; i++) { // loopar igenom alla aktuella blobbar
			if (Blobs.middlePoint[i].distance(middlePoint) < Blobs.middlePoint[nearestElement].distance(middlePoint)) // om det loopade blobb-elementet är nämre PacMan än den lagrade elementet
				nearestElement = i; // lagrar det nämre elementet
		}
		return nearestElement;
	}
	
	public boolean isOnBlob() { // returnerar sant om PacMan är över den närmsta blobben
		nearestBlobElement = getNearestBlobElement();
		if (alive && Blobs.alive[nearestBlobElement]) { // om både PacMan och den närmsta blobben lever
			double radius = 0;
			if (Blobs.size[nearestBlobElement] >= size) // Om den närmsta blobben är större än (eller har samma storlek som) PacMan
				radius = Blobs.size[nearestBlobElement] / 2; // blobbens radie
			else // Om PacMan är större än den närmsta blobben
				radius = size / 2; // PacMans radie
			double distance = middlePoint.distance(Blobs.middlePoint[nearestBlobElement]); // distansen mellan PacMans mittpunkt och den närmsta blobbens mittpunkt
			return radius >= distance; // sant om en av de två cirklarnas mittpunkter finns inom den andra cirkelns område, d.v.s. om PacMan är över en blobb
		}
		return false;
	}
	
	public void eatBlob() { // används för PacMan att äta up den närmsta blobben
		Blobs.alive[nearestBlobElement] = false;
		if (150 > size) { // om PacMan's längd är mindre än 150 pixlar (begränsning för att PacMan inte ska bli för stor)
			if (Main.gameMode.equals("survival"))
				size += Blobs.size[nearestBlobElement] / 25; // ökar PacMans storlek med blobbens storlek dividerat med 25
			else
				size += Blobs.size[nearestBlobElement] / 15; // ökar PacMans storlek med blobbens storlek dividerat med 15
		}
		if (Main.gameMode.equals("survival")) {
			int s = Blobs.size[nearestBlobElement] / 10;
			if (s >= 1)
				main.Main.score += s; // ökar spelarens poäng med blobbens storlek dividerat med 10
			else
				main.Main.score++; // ökar spelarens poäng med +1 om blobben är tillräckligt liten
		}
		else {
			int s = Blobs.size[nearestBlobElement] / 4;
			if (s >= 1)
				main.Main.score += s; // ökar spelarens poäng med blobbens storlek dividerat med 4
			else
				main.Main.score++;
		}
	}
	
	public void reset() { // återställer alla PacMans variabler till standard-värden
		size = 50;
		x = (Panel.gfxWidth-size)/2;
		y = (Panel.gfxHeight-size) /2;
		arcAngle = 270;
		startAngle = 225;
		maxStartAngle = startAngle;
		speed = 3;
		mouthAngleChange = 6;
		middlePoint = new Point((int) (x+size/2), (int) (y+size/2));
		direction = "left";
		lastDirection = direction;
		alive = true;
		spawned = true;
		nearestBlobElement = 0;
	}
	
	public static boolean isEaten() { // returnerar sant om PacMans ät-upp-sig-själv animation är slutförd (efter förlust)
		return 0 >= arcAngle || !spawned;
	}
	
	public void lose() { // kallas när spelaren förlorar, innan PacMan utför sin ät-upp-sig-själv animation
		Panel.title = "Game Over"; // gör så att titeln "Game Over" ritas upp på spelskärmen
		Panel.playText = "PLAY AGAIN"; // ändrar huvudmenyns första knapp-text från "PLAY" till "PLAY AGAIN"
		mouthAngleChange = 3; // saktar ner PacMans mun-animatiom hastighet så att PacMan inte äter upp sig själv så snabbt
		alive = false; // ställs in korrekt så att spelet beter sig på rätt sätt (t.ex. för att visa menyn)
		if (Main.gameMode.equals("survival"))
			Blobs.amount = 10; // tillsätter 10 blobbar som åker runt som bakgrundsanimation, då survival spelläget slutar med 0 blobbar när spelaren förlorar
	}
	
	public static boolean showMenu() { // returnerar sant om menyn borde visas
		return !spawned || !alive && isEaten(); // sant om PacMan har ätit upp sig själv och inte är synlig på spelskärmen
	}
	
}
