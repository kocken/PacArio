package npcs;

import java.awt.Point;

import main.Main;
import main.Panel;

public class PacMan implements NPC {
	
	public static double size = 50; // PacMans storlek
	public static double x = (Panel.gfxWidth-size)/2; // PacMans x-position i grafiken, 
	public static double y = (Panel.gfxHeight-size)/2; // PacMans y-position i grafiken
	public static double speed = 3; // PacMans r�rselse hastighet
	private double mouthAngleChange = 6; // PacMans mun-animation hastighet
	public Point middlePoint = new Point((int) (x+size/2), (int) (y+size/2)); // PacMan-cirkelns mittpunkt
	
	public static int arcAngle = 270; // arcAngle parametern f�r PacMans mun-animation-utm�lning med #fillArc-metoden
	public static int startAngle = 225; // startAngle parametern f�r PacMans mun-animation-utm�lning med #fillArc-metoden, 225 = PacMans mun startar riktad �t v�nster
	private int maxStartAngle = startAngle; /* startAngle-variabelns max-v�rde som inte ska �verskridas n�r PacMans
													 #fillArc-metod-parametrar modifieras */
	
	public static String direction = "left"; // riktningen som PacMan �r riktad mot, startar riktad �t v�nster som standard
	private String lastDirection = direction; // riktningen som anv�ndaren sist ville �ka mot, anv�nds f�r att rotera PacMans mun mot den nya destinationen
		
	public static boolean alive = false; // sant om PacMan lever och en spelomg�ng �r ig�ng, falskt annars
	public static boolean spawned = false; // sant efter den f�rsta spelomg�ngen har startats, anv�nds som en indikator f�r att m�la PacMan
	
	public int nearestBlobElement = 0; // variabel som h�ller koll p� blobben (elementet fr�n blobb-arrayen) som �r n�rmast PacMan-figuren
		
	public void move() {
		direction(); // roterar PacMan �t korrekt riktning som anv�ndaren vill �ka mot
		animateMouth(false, null, null); // animerar PacMans mun s� den �ppnas och st�ngs n�r figuren �ker fram�t
		if (canWalkForward()) { // om PacMan kan �ka fram�t, sant om PacMan-figuren inte f�rs�ker �ka utanf�r spelf�nstret
			if (isFacedLeft()) // om PacMan �r riktad mot v�nster riktning
				goLeft(); // �ker v�nster
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
		if (!lastDirection.equals(direction) && alive) // om anv�ndaren vill �ka mot en annan riktning
			animateMouth(true, lastDirection, direction); // roterar PacMans mun mot den nya riktningen
		lastDirection = direction; // lagrar den nya riktningen
	}
	
	private boolean canWalkForward() {
		if (alive) {
			if (isFacedLeft())
				return canWalkLeft(); // returnerar sant om PacMans cirkel kan �ka v�nster utan att cirkeln �ker utanf�r spelrutan
			if (isFacedUp())
				return canWalkUp();
			else if (isFacedRight())
				return canWalkRight();
			else if (isFacedDown())
				return canWalkDown();
		}
		return false;
	}
	
	public void center() { // st�ller in PacMans x- och y-koordinationer s� att PacMan-figuren centreras i mitten av spelf�nstret
		x = (Panel.gfxWidth-size)/2;
		y = (Panel.gfxHeight-size)/2;
	}
	
	private boolean openMouth = false; // falskt n�r PacMan-munnen ska st�ngas, sant n�r munnen ska �ppnas
	public void animateMouth(boolean rotateMouth, String lastDirection, String newDirection) { // roterar/animerar PacMans mun genom att modifiera utm�lning-metodens parametrar
		if (!rotateMouth && (canWalkForward() || !alive && !isEaten() || startAngle != maxStartAngle)) { // sant om munnen ska animeras (�ppnas/st�ngas)
			if (openMouth || !alive && !isEaten()) { // om munnen ska �ppnas
				startAngle += mouthAngleChange;
				if (alive || !alive && arcAngle >= mouthAngleChange * 2)
					arcAngle -= mouthAngleChange * 2;
				else if (!alive && mouthAngleChange * 2 > arcAngle) // om PacMan har d�tt och �r i slutet av sin �ta-upp-sig-sj�lv animationen
					arcAngle = 0;
				if (startAngle >= maxStartAngle || arcAngle <= 270) // om munnen �r fullt �ppnad
					openMouth = false; // �ndrar v�rdet f�r att b�rja st�nga munnen
			} else if (alive) { // om munnen ska st�ngas
				startAngle -= mouthAngleChange;
				arcAngle += mouthAngleChange * 2;
				if (startAngle <= maxStartAngle - 45 || arcAngle >= 360) // om munnen �r st�ngd
					openMouth = true; // �ndrar v�rdet f�r att b�rja �ppna munnen
			}
		}
		else if (rotateMouth) { // roterar PacMans mun f�r att visa att riktningen �r bytt
			maxStartAngle += (getDirectionValue(lastDirection)-getDirectionValue(newDirection)) * 90;
			startAngle += (getDirectionValue(lastDirection)-getDirectionValue(newDirection)) * 90;
		}
	}
	
	private int getDirectionValue(String direction) { // returnerar ett v�rde beroende p� riktning, vilket anv�nds i de tv� �verst�ende rotatera-mun-algoritmerna
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
		if (x-speed >= 0) // om PacMan inte kommer hamna utanf�r spelf�nstret efter f�rflyttelse
			x -= speed;
		else // flytta PacMan mot spelf�nstrets kant
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
	
	private int getNearestBlobElement() { // returnerar det blobb-element (i blobb-arrayen) som �r n�rmast PacMan-figuren
		int nearestElement = 0;
		for (int i = 0; Blobs.amount > i; i++) { // loopar igenom alla aktuella blobbar
			if (Blobs.middlePoint[i].distance(middlePoint) < Blobs.middlePoint[nearestElement].distance(middlePoint)) // om det loopade blobb-elementet �r n�mre PacMan �n den lagrade elementet
				nearestElement = i; // lagrar det n�mre elementet
		}
		return nearestElement;
	}
	
	public boolean isOnBlob() { // returnerar sant om PacMan �r �ver den n�rmsta blobben
		nearestBlobElement = getNearestBlobElement();
		if (alive && Blobs.alive[nearestBlobElement]) { // om b�de PacMan och den n�rmsta blobben lever
			double radius = 0;
			if (Blobs.size[nearestBlobElement] >= size) // Om den n�rmsta blobben �r st�rre �n (eller har samma storlek som) PacMan
				radius = Blobs.size[nearestBlobElement] / 2; // blobbens radie
			else // Om PacMan �r st�rre �n den n�rmsta blobben
				radius = size / 2; // PacMans radie
			double distance = middlePoint.distance(Blobs.middlePoint[nearestBlobElement]); // distansen mellan PacMans mittpunkt och den n�rmsta blobbens mittpunkt
			return radius >= distance; // sant om en av de tv� cirklarnas mittpunkter finns inom den andra cirkelns omr�de, d.v.s. om PacMan �r �ver en blobb
		}
		return false;
	}
	
	public void eatBlob() { // anv�nds f�r PacMan att �ta up den n�rmsta blobben
		Blobs.alive[nearestBlobElement] = false;
		if (150 > size) { // om PacMan's l�ngd �r mindre �n 150 pixlar (begr�nsning f�r att PacMan inte ska bli f�r stor)
			if (Main.gameMode.equals("survival"))
				size += Blobs.size[nearestBlobElement] / 25; // �kar PacMans storlek med blobbens storlek dividerat med 25
			else
				size += Blobs.size[nearestBlobElement] / 15; // �kar PacMans storlek med blobbens storlek dividerat med 15
		}
		if (Main.gameMode.equals("survival")) {
			int s = Blobs.size[nearestBlobElement] / 10;
			if (s >= 1)
				main.Main.score += s; // �kar spelarens po�ng med blobbens storlek dividerat med 10
			else
				main.Main.score++; // �kar spelarens po�ng med +1 om blobben �r tillr�ckligt liten
		}
		else {
			int s = Blobs.size[nearestBlobElement] / 4;
			if (s >= 1)
				main.Main.score += s; // �kar spelarens po�ng med blobbens storlek dividerat med 4
			else
				main.Main.score++;
		}
	}
	
	public void reset() { // �terst�ller alla PacMans variabler till standard-v�rden
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
	
	public static boolean isEaten() { // returnerar sant om PacMans �t-upp-sig-sj�lv animation �r slutf�rd (efter f�rlust)
		return 0 >= arcAngle || !spawned;
	}
	
	public void lose() { // kallas n�r spelaren f�rlorar, innan PacMan utf�r sin �t-upp-sig-sj�lv animation
		Panel.title = "Game Over"; // g�r s� att titeln "Game Over" ritas upp p� spelsk�rmen
		Panel.playText = "PLAY AGAIN"; // �ndrar huvudmenyns f�rsta knapp-text fr�n "PLAY" till "PLAY AGAIN"
		mouthAngleChange = 3; // saktar ner PacMans mun-animatiom hastighet s� att PacMan inte �ter upp sig sj�lv s� snabbt
		alive = false; // st�lls in korrekt s� att spelet beter sig p� r�tt s�tt (t.ex. f�r att visa menyn)
		if (Main.gameMode.equals("survival"))
			Blobs.amount = 10; // tills�tter 10 blobbar som �ker runt som bakgrundsanimation, d� survival spell�get slutar med 0 blobbar n�r spelaren f�rlorar
	}
	
	public static boolean showMenu() { // returnerar sant om menyn borde visas
		return !spawned || !alive && isEaten(); // sant om PacMan har �tit upp sig sj�lv och inte �r synlig p� spelsk�rmen
	}
	
}
