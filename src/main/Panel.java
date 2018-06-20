package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import npcs.Blobs;
import npcs.PacMan;

@SuppressWarnings("serial")
public class Panel extends JPanel {
	
	private Graphics2D g2d = null; // applikationens grafikobjekt d�r all aktuell ritning sker
	private Rectangle gfxBounds = null; // grafikbounds f�r applikationsf�nstret, anv�nds f�r att st�lla in grafikens aktuella gfxWidth och gfxHeight v�rden
	
	public static int gfxWidth = 584; // applikationens aktuella bredd (i pixlar)
	public static int gfxHeight = 561; // applikationens aktuella bredd (i pixlar)
		
	public static Rectangle[] menuBounds = { // rektangel-bounds f�r meny-knapparna
			new Rectangle(gfxWidth/3, gfxHeight/3                , gfxWidth/3, gfxHeight/10), 
			new Rectangle(gfxWidth/3, gfxHeight/3 + gfxHeight/7  , gfxWidth/3, gfxHeight/10), 
			new Rectangle(gfxWidth/3, gfxHeight/3 + gfxHeight/7*2, gfxWidth/3, gfxHeight/10), 
			new Rectangle(gfxWidth/3, gfxHeight/3 + gfxHeight/7*3, gfxWidth/3, gfxHeight/10)};

	public static int menuMode = 0; // variabel som h�ller koll p� meny-l�ge, 0 = huvudmeny, 1 = spell�ge-meny
	public static Color menuBase = new Color(0, 100, 255); // basbakgrundf�rg f�r meny-knapparna - (0, 100, 255) = bl�
	public static Color[] menuColor = {menuBase, menuBase, menuBase, menuBase}; // bakgrundsf�rgerna f�r meny-knapparna
	
	public static String title = ""; // textvariabel som anv�nds vid start-nedr�kning, f�rlust m.m.
	public static String playText = "PLAY"; // textvariabel som anv�nds f�r f�rsta knappen i huvudmenyn (�ndras till "PLAY AGAIN" n�r spelaren d�r)

	@Override
	public void paint(Graphics g) {
		/* Uppdaterar grafikkomponenterna utifr�n f�nstrets aktuella v�rden */
		g2d = (Graphics2D) g;
		gfxBounds = g.getClipBounds();
		if (gfxBounds != null) {
			gfxWidth = gfxBounds.width;
			gfxHeight = gfxBounds.height;
			menuBounds[0] = new Rectangle(gfxWidth/3, gfxHeight/3                , gfxWidth/3, gfxHeight/10);
			menuBounds[1] = new Rectangle(gfxWidth/3, gfxHeight/3 + gfxHeight/7  , gfxWidth/3, gfxHeight/10);
			menuBounds[2] = new Rectangle(gfxWidth/3, gfxHeight/3 + gfxHeight/7*2, gfxWidth/3, gfxHeight/10);
			menuBounds[3] = new Rectangle(gfxWidth/3, gfxHeight/3 + gfxHeight/7*3, gfxWidth/3, gfxHeight/10);
		}
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // s�tter p� antialiasing f�r b�ttre grafikdetalj
		resetBG(); // �terstr�ller grafiken genom att m�la hela spelf�nstret vitt
		drawNPCs(); // ritar spelets NPC:er (blobbar och PacMan)
		drawTitle(); // ritar upp en titeln med texten som �r tilldelad title-variabeln (t.ex. "Game Over" & start-nedr�kning)
		drawScore(); // ritar upp spelarens po�ng
		if (PacMan.showMenu()) // om menyn b�r visas
			drawMenu(); // m�lar upp meny-knapparna
		GameWindow.panel.updateUI(); // uppdaterar panelen med den aktuellt uppdaterade grafiken
	}

	private void resetBG() { // �terst�ller applikationens grafik s� att gammal grafik inte stannar kvar
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight()); // fyller hela grafikrutan med vit f�rg
	}
	
	private void drawNPCs() { // m�lar spelets NPC:er (blobbarna) och PacMan
		drawBlobs();
		if (PacMan.spawned && (PacMan.alive || !PacMan.isEaten())) // om PacMan b�r ritas upp
			drawPacMan();
	}
	
	private void drawBlobs() {
		for (int i = 0; Blobs.amount > i; i++) { // loopar igenom de aktuella blobbarna
			g2d.setColor(Blobs.color[i]);
			g2d.fillArc((int) Blobs.x[i], (int) Blobs.y[i], // m�lar en cirkel med blobbens v�rden
					Blobs.size[i], Blobs.size[i], 0, 360);
		}
	}

	private void drawPacMan() {
		g2d.setColor(Color.YELLOW);
		g2d.fillArc((int) PacMan.x, (int) PacMan.y, (int) PacMan.size,  // m�lar en cirkel med PacMans v�rden
				(int) PacMan.size, PacMan.startAngle, PacMan.arcAngle);
	}
	
	private void drawTitle() { // ritar upp spelets huvudtitel
		if (title.equals("GAME MODES")) {
			g2d.setColor(menuBase); // bl� f�rg
			drawCenteredString(title, g2d, (int) (gfxHeight/3.7), new Font("Comic Sans MS", Font.BOLD, gfxWidth/8));
		}
		else if (title.equals("GO!") || title.contains("Game Over")) {
			g2d.setColor(Color.RED);
			drawCenteredString(title, g2d, (int) (gfxHeight/3.7), new Font("Arial", Font.BOLD, gfxWidth/8));
		}
		else {
			g2d.setColor(Color.DARK_GRAY);
			drawCenteredString(title, g2d, (int) (gfxHeight/3.7), new Font("Arial", Font.BOLD, gfxWidth/12));
		}				
	}
	
	private void drawScore() { // ritar upp spelarens po�ng (score-variabeln)
		if (!PacMan.showMenu() || Main.score > 0) { // om inte menyn visas (d.v.s. en spelomg�ng �r ig�ng), eller om spelaren har po�ng fr�n f�rra spelomg�ngen
			if (Main.score <= 255) // om spelarens po�ng �r lika med eller mindre �n RGB-systemets maxv�rde 255 (RGB = Red, Green, Blue)
				g2d.setColor(new Color((float) Main.score/255, 0f, 0f, .75f)); // 25% transparency med svart f�rg fr�n b�rjan, blir mer r�d desto mer po�ng spelaren samlar
			else // om spelarens po�ng �r h�gre �n RGB-systemets maxv�rde
				g2d.setColor(new Color(1f, 0f, 0f, .75f)); // r�d f�rg med 25% transparency
			drawCenteredString("Score: " + Main.score, g2d, 50, new Font("Arial", Font.BOLD, gfxHeight/25));
		}
		
	}
	
	private void drawMenu() { // m�lar upp menyernas knapp-alternativ
		if (menuMode == 0) { // om huvudmenyn ska visas
			String[] knappText = {playText, "GAME MODE", "QUIT"};
			for (int i = 0; i <= 2; i++)
				drawButton(knappText[i], menuBounds[i], menuColor[i]); // m�lar upp en meny-knapp med metodens inmatade v�rden
		}
		else if (menuMode == 1) { // om spell�ge-menyn ska visas
			String[] knappText = {"AGARIO", "SURVIVAL", "ESCAPE", "BACK"};
			for (int i = 0; i <= 3; i++)
				drawButton(knappText[i], menuBounds[i], menuColor[i]);
		}
	}
	
	private void drawButton(String msg, Rectangle rect, Color c) {
		drawButton(msg, rect.x, rect.y, rect.width, rect.height, c);
	}
	
	private void drawButton(String msg, int rectX, int rectY, int rectWidth, int rectHeight, Color c) {
		g2d.setColor(c);
		g2d.fillRoundRect(rectX, rectY, rectWidth, rectHeight, 10, 10); // m�lar knappen
		g2d.setColor(Color.BLACK);
		g2d.drawRoundRect(rectX, rectY, rectWidth, rectHeight, 10, 10); // ritar svart outline p� knappen
		g2d.setColor(Color.WHITE);
		drawCenteredString(msg, g2d, (int) (rectY+(rectHeight/2)+rectHeight*.2), // ritar centrerad knapp-text
				new Font("Comic Sans MS", Font.BOLD, (int) (gfxHeight/20)));
	}
	
	private void drawCenteredString(String text, Graphics g, int y, Font font) {
		Rectangle rect = g.getClipBounds(); // grafikbounds f�r grafikomr�det
	    FontMetrics metrics = g.getFontMetrics(font); // storleksv�rden f�r grafikens text
	    int x = (rect.width - metrics.stringWidth(text)) / 2; // x-v�rdet d�r texten ska b�rja ritas f�r att bli centrerat p� grafikomr�det
	    g.setFont(font); // s�tter metodens angivna textsnitt
	    g.drawString(text, x, y); // ritar upp texten centerat p� det grafiska omr�det
	}
	
}