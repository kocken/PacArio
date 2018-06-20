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
	
	private Graphics2D g2d = null; // applikationens grafikobjekt där all aktuell ritning sker
	private Rectangle gfxBounds = null; // grafikbounds för applikationsfönstret, används för att ställa in grafikens aktuella gfxWidth och gfxHeight värden
	
	public static int gfxWidth = 584; // applikationens aktuella bredd (i pixlar)
	public static int gfxHeight = 561; // applikationens aktuella bredd (i pixlar)
		
	public static Rectangle[] menuBounds = { // rektangel-bounds för meny-knapparna
			new Rectangle(gfxWidth/3, gfxHeight/3                , gfxWidth/3, gfxHeight/10), 
			new Rectangle(gfxWidth/3, gfxHeight/3 + gfxHeight/7  , gfxWidth/3, gfxHeight/10), 
			new Rectangle(gfxWidth/3, gfxHeight/3 + gfxHeight/7*2, gfxWidth/3, gfxHeight/10), 
			new Rectangle(gfxWidth/3, gfxHeight/3 + gfxHeight/7*3, gfxWidth/3, gfxHeight/10)};

	public static int menuMode = 0; // variabel som håller koll på meny-läge, 0 = huvudmeny, 1 = spelläge-meny
	public static Color menuBase = new Color(0, 100, 255); // basbakgrundfärg för meny-knapparna - (0, 100, 255) = blå
	public static Color[] menuColor = {menuBase, menuBase, menuBase, menuBase}; // bakgrundsfärgerna för meny-knapparna
	
	public static String title = ""; // textvariabel som används vid start-nedräkning, förlust m.m.
	public static String playText = "PLAY"; // textvariabel som används för första knappen i huvudmenyn (ändras till "PLAY AGAIN" när spelaren dör)

	@Override
	public void paint(Graphics g) {
		/* Uppdaterar grafikkomponenterna utifrån fönstrets aktuella värden */
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
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // sätter på antialiasing för bättre grafikdetalj
		resetBG(); // återsträller grafiken genom att måla hela spelfönstret vitt
		drawNPCs(); // ritar spelets NPC:er (blobbar och PacMan)
		drawTitle(); // ritar upp en titeln med texten som är tilldelad title-variabeln (t.ex. "Game Over" & start-nedräkning)
		drawScore(); // ritar upp spelarens poäng
		if (PacMan.showMenu()) // om menyn bör visas
			drawMenu(); // målar upp meny-knapparna
		GameWindow.panel.updateUI(); // uppdaterar panelen med den aktuellt uppdaterade grafiken
	}

	private void resetBG() { // återställer applikationens grafik så att gammal grafik inte stannar kvar
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, getWidth(), getHeight()); // fyller hela grafikrutan med vit färg
	}
	
	private void drawNPCs() { // målar spelets NPC:er (blobbarna) och PacMan
		drawBlobs();
		if (PacMan.spawned && (PacMan.alive || !PacMan.isEaten())) // om PacMan bör ritas upp
			drawPacMan();
	}
	
	private void drawBlobs() {
		for (int i = 0; Blobs.amount > i; i++) { // loopar igenom de aktuella blobbarna
			g2d.setColor(Blobs.color[i]);
			g2d.fillArc((int) Blobs.x[i], (int) Blobs.y[i], // målar en cirkel med blobbens värden
					Blobs.size[i], Blobs.size[i], 0, 360);
		}
	}

	private void drawPacMan() {
		g2d.setColor(Color.YELLOW);
		g2d.fillArc((int) PacMan.x, (int) PacMan.y, (int) PacMan.size,  // målar en cirkel med PacMans värden
				(int) PacMan.size, PacMan.startAngle, PacMan.arcAngle);
	}
	
	private void drawTitle() { // ritar upp spelets huvudtitel
		if (title.equals("GAME MODES")) {
			g2d.setColor(menuBase); // blå färg
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
	
	private void drawScore() { // ritar upp spelarens poäng (score-variabeln)
		if (!PacMan.showMenu() || Main.score > 0) { // om inte menyn visas (d.v.s. en spelomgång är igång), eller om spelaren har poäng från förra spelomgången
			if (Main.score <= 255) // om spelarens poäng är lika med eller mindre än RGB-systemets maxvärde 255 (RGB = Red, Green, Blue)
				g2d.setColor(new Color((float) Main.score/255, 0f, 0f, .75f)); // 25% transparency med svart färg från början, blir mer röd desto mer poäng spelaren samlar
			else // om spelarens poäng är högre än RGB-systemets maxvärde
				g2d.setColor(new Color(1f, 0f, 0f, .75f)); // röd färg med 25% transparency
			drawCenteredString("Score: " + Main.score, g2d, 50, new Font("Arial", Font.BOLD, gfxHeight/25));
		}
		
	}
	
	private void drawMenu() { // målar upp menyernas knapp-alternativ
		if (menuMode == 0) { // om huvudmenyn ska visas
			String[] knappText = {playText, "GAME MODE", "QUIT"};
			for (int i = 0; i <= 2; i++)
				drawButton(knappText[i], menuBounds[i], menuColor[i]); // målar upp en meny-knapp med metodens inmatade värden
		}
		else if (menuMode == 1) { // om spelläge-menyn ska visas
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
		g2d.fillRoundRect(rectX, rectY, rectWidth, rectHeight, 10, 10); // målar knappen
		g2d.setColor(Color.BLACK);
		g2d.drawRoundRect(rectX, rectY, rectWidth, rectHeight, 10, 10); // ritar svart outline på knappen
		g2d.setColor(Color.WHITE);
		drawCenteredString(msg, g2d, (int) (rectY+(rectHeight/2)+rectHeight*.2), // ritar centrerad knapp-text
				new Font("Comic Sans MS", Font.BOLD, (int) (gfxHeight/20)));
	}
	
	private void drawCenteredString(String text, Graphics g, int y, Font font) {
		Rectangle rect = g.getClipBounds(); // grafikbounds för grafikområdet
	    FontMetrics metrics = g.getFontMetrics(font); // storleksvärden för grafikens text
	    int x = (rect.width - metrics.stringWidth(text)) / 2; // x-värdet där texten ska börja ritas för att bli centrerat på grafikområdet
	    g.setFont(font); // sätter metodens angivna textsnitt
	    g.drawString(text, x, y); // ritar upp texten centerat på det grafiska området
	}
	
}