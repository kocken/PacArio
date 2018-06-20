package main;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import npcs.PacMan;

public class Listeners {
	
	public static boolean firstEventExecuted = false; /* värdet ändras till sant när spelaren för första gången rör musen över spelfönstret, 
															  används som en indikator för blobbarna att börja röra sig som bakgrundsanimation vid menyn */
	private Color orange = new Color(255, 180, 0);
	private Color grön = new Color(140, 255, 20);
	private Color grå = new Color(122, 122, 122);
		
	public void init(JPanel panel) { // initialization metoden som startar alla EventListeners
		keyboardListener(panel); // startar avlyssnare för tangentbord
		mouseListener(panel); // startar avlyssnare för mus
		mouseMotionListener(panel); // startar avlyssnare för musrörelse
	};
	
	private void keyboardListener(JPanel panel) {
		panel.addKeyListener(new KeyListener() {
			
			@Override	public void keyTyped(KeyEvent e) {}
			@Override	public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 37 || e.getKeyCode() == 65) // 37=vänster piltangent, 65='A'
					PacMan.direction = "left";
				else if (e.getKeyCode() == 38 || e.getKeyCode() == 87) // 38=pil upp (piltangent), 87='W'
					PacMan.direction = "up";
				else if (e.getKeyCode() == 39 || e.getKeyCode() == 68) // 39=höger piltangent, 68='D'
					PacMan.direction = "right";
				else if (e.getKeyCode() == 40 || e.getKeyCode() == 83) // 40=pil ner (piltangent), 83='S'
					PacMan.direction = "down";
			}
			
		});
	}
	
	private void mouseListener(JPanel panel) {
		panel.addMouseListener(new MouseListener() {

			@Override	public void mouseClicked(MouseEvent arg0) {}

			@Override	public void mouseEntered(MouseEvent arg0) {}

			@Override	public void mouseExited(MouseEvent arg0) {}

			@Override	public void mousePressed(MouseEvent arg0) {}

			@Override	public void mouseReleased(MouseEvent e) {
				if (PacMan.showMenu()) { // om menyn visas
					
					if (Panel.menuMode == 0) { // om huvudmenyn visas
						if (Panel.menuBounds[0].contains(e.getPoint())) // om spelaren klickar på den första meny-knappen (PLAY/PLAY AGAIN)
							main.Main.startRun = true; // startar en ny spelomgång med det aktuella spelläget
						
						else if (Panel.menuBounds[1].contains(e.getPoint())) { // om spelaren klickar på den andra meny-knappen (GAME MODE)
							Panel.menuMode = 1; // ändrar menyläge till spelläge-menyn
							Panel.title = "GAME MODES"; // ändrar titeln till "GAME MODES" som visas över spelläge-menyn
						}
						
						else if (Panel.menuBounds[2].contains(e.getPoint())) // om spelaren klickar på det tredje och sista knapp-alternativet (QUIT)
							System.exit(0); // avslutar applikationen
					}
					
					else if (Panel.menuMode == 1) { // om spelläge-menyn visas
						if (Panel.menuBounds[0].contains(e.getPoint())) // om spelaren klickar på den första meny-knappen (AGARIO)
							Main.gameMode = "agario"; // ändrar applikationens aktuellt valda spelläge
						
						else if (Panel.menuBounds[1].contains(e.getPoint())) // om spelaren klickar på den andra meny-knappen (SURVIVAL)
							Main.gameMode = "survival";
						
						else if (Panel.menuBounds[2].contains(e.getPoint())) // om spelaren klickar på den tredje meny-knappen (ESCAPE)
							Main.gameMode = "escape";
						
						else if (Panel.menuBounds[3].contains(e.getPoint())) { // om spelaren klickar på det sista meny-alternativet (BACK)
							Panel.menuMode = 0; // ändrar menyläge till huvudmenyn
							Panel.title = ""; // återställer titel-texten
							Panel.playText = "PLAY"; // återställer den första knappens text till "PLAY" (ifall den var "PLAY AGAIN")
						}
											
					}
					updateMenu(e.getPoint()); // uppdaterar bakgrundsfärgerna till knapp-alternativen
				}
			}
			
		});
	}
	
	private void mouseMotionListener(JPanel panel) {
		panel.addMouseMotionListener(new MouseMotionListener() {

			@Override	public void mouseDragged(MouseEvent arg0) {}

			@Override
			public void mouseMoved(MouseEvent e) {
				firstEventExecuted = true; // gör så att blobbarna börjar röra sig vid menyn
				if (PacMan.showMenu())
					updateMenu(e.getPoint());
			}			
		});
	}
	
	private void updateMenu(Point mousePos) { // uppdaterar meny-knapparnas bakgrundsfärger
		if (mousePos != null) {
			for (int i = 0; i < Panel.menuBounds.length; i++) { // loopar igenom alla meny-knapparna
				if (Panel.menuMode == 0) { // huvudmenyn
					if (Panel.menuBounds[i].contains(mousePos)) // om musen är över knapp-alternativet
						Panel.menuColor[i] = orange; // ställer in orange färg som knappens bakgrundsfärg
					else // om musen inte är över knapp-alternativet
						Panel.menuColor[i] = Panel.menuBase; // ställer in Panel.menuBas (blå färg) som knappens bakgrundsfärg
				}
				else if (Panel.menuMode == 1) { // spelläge-menyn
					if (isSelectedGameMode(i)) // om den aktuellt loopade knappen är det aktuellt valda spelläget
						Panel.menuColor[i] = grön;
					else if (Panel.menuBounds[i].contains(mousePos)) // om musen är över knapp-alternativet
						Panel.menuColor[i] = orange;
					else if (i == 3) // tillbaka knappen, om inte denna hovras över så ställs bakgrundsfärgen in till meny-basfärgen (blå)
						Panel.menuColor[i] = Panel.menuBase;
					else // om knappen är ett spelläge-alternativ som inte är aktuellt vald och inte är hovrad över med musen
						Panel.menuColor[i] = grå;
				}
			}
		}
	}
	
	private boolean isSelectedGameMode(int menuButton) { // returnerar sant om menuButton-parametern (knapp från spelläge-menyn) är det aktuellt valda spelläget
		return  menuButton == 0 && Main.gameMode.equals("agario") || 
				menuButton == 1 && Main.gameMode.equals("survival") || 
				menuButton == 2 && Main.gameMode.equals("escape");
	}

}