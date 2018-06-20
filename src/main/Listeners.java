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
	
	public static boolean firstEventExecuted = false; /* v�rdet �ndras till sant n�r spelaren f�r f�rsta g�ngen r�r musen �ver spelf�nstret, 
															  anv�nds som en indikator f�r blobbarna att b�rja r�ra sig som bakgrundsanimation vid menyn */
	private Color orange = new Color(255, 180, 0);
	private Color gr�n = new Color(140, 255, 20);
	private Color gr� = new Color(122, 122, 122);
		
	public void init(JPanel panel) { // initialization metoden som startar alla EventListeners
		keyboardListener(panel); // startar avlyssnare f�r tangentbord
		mouseListener(panel); // startar avlyssnare f�r mus
		mouseMotionListener(panel); // startar avlyssnare f�r musr�relse
	};
	
	private void keyboardListener(JPanel panel) {
		panel.addKeyListener(new KeyListener() {
			
			@Override	public void keyTyped(KeyEvent e) {}
			@Override	public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 37 || e.getKeyCode() == 65) // 37=v�nster piltangent, 65='A'
					PacMan.direction = "left";
				else if (e.getKeyCode() == 38 || e.getKeyCode() == 87) // 38=pil upp (piltangent), 87='W'
					PacMan.direction = "up";
				else if (e.getKeyCode() == 39 || e.getKeyCode() == 68) // 39=h�ger piltangent, 68='D'
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
						if (Panel.menuBounds[0].contains(e.getPoint())) // om spelaren klickar p� den f�rsta meny-knappen (PLAY/PLAY AGAIN)
							main.Main.startRun = true; // startar en ny spelomg�ng med det aktuella spell�get
						
						else if (Panel.menuBounds[1].contains(e.getPoint())) { // om spelaren klickar p� den andra meny-knappen (GAME MODE)
							Panel.menuMode = 1; // �ndrar menyl�ge till spell�ge-menyn
							Panel.title = "GAME MODES"; // �ndrar titeln till "GAME MODES" som visas �ver spell�ge-menyn
						}
						
						else if (Panel.menuBounds[2].contains(e.getPoint())) // om spelaren klickar p� det tredje och sista knapp-alternativet (QUIT)
							System.exit(0); // avslutar applikationen
					}
					
					else if (Panel.menuMode == 1) { // om spell�ge-menyn visas
						if (Panel.menuBounds[0].contains(e.getPoint())) // om spelaren klickar p� den f�rsta meny-knappen (AGARIO)
							Main.gameMode = "agario"; // �ndrar applikationens aktuellt valda spell�ge
						
						else if (Panel.menuBounds[1].contains(e.getPoint())) // om spelaren klickar p� den andra meny-knappen (SURVIVAL)
							Main.gameMode = "survival";
						
						else if (Panel.menuBounds[2].contains(e.getPoint())) // om spelaren klickar p� den tredje meny-knappen (ESCAPE)
							Main.gameMode = "escape";
						
						else if (Panel.menuBounds[3].contains(e.getPoint())) { // om spelaren klickar p� det sista meny-alternativet (BACK)
							Panel.menuMode = 0; // �ndrar menyl�ge till huvudmenyn
							Panel.title = ""; // �terst�ller titel-texten
							Panel.playText = "PLAY"; // �terst�ller den f�rsta knappens text till "PLAY" (ifall den var "PLAY AGAIN")
						}
											
					}
					updateMenu(e.getPoint()); // uppdaterar bakgrundsf�rgerna till knapp-alternativen
				}
			}
			
		});
	}
	
	private void mouseMotionListener(JPanel panel) {
		panel.addMouseMotionListener(new MouseMotionListener() {

			@Override	public void mouseDragged(MouseEvent arg0) {}

			@Override
			public void mouseMoved(MouseEvent e) {
				firstEventExecuted = true; // g�r s� att blobbarna b�rjar r�ra sig vid menyn
				if (PacMan.showMenu())
					updateMenu(e.getPoint());
			}			
		});
	}
	
	private void updateMenu(Point mousePos) { // uppdaterar meny-knapparnas bakgrundsf�rger
		if (mousePos != null) {
			for (int i = 0; i < Panel.menuBounds.length; i++) { // loopar igenom alla meny-knapparna
				if (Panel.menuMode == 0) { // huvudmenyn
					if (Panel.menuBounds[i].contains(mousePos)) // om musen �r �ver knapp-alternativet
						Panel.menuColor[i] = orange; // st�ller in orange f�rg som knappens bakgrundsf�rg
					else // om musen inte �r �ver knapp-alternativet
						Panel.menuColor[i] = Panel.menuBase; // st�ller in Panel.menuBas (bl� f�rg) som knappens bakgrundsf�rg
				}
				else if (Panel.menuMode == 1) { // spell�ge-menyn
					if (isSelectedGameMode(i)) // om den aktuellt loopade knappen �r det aktuellt valda spell�get
						Panel.menuColor[i] = gr�n;
					else if (Panel.menuBounds[i].contains(mousePos)) // om musen �r �ver knapp-alternativet
						Panel.menuColor[i] = orange;
					else if (i == 3) // tillbaka knappen, om inte denna hovras �ver s� st�lls bakgrundsf�rgen in till meny-basf�rgen (bl�)
						Panel.menuColor[i] = Panel.menuBase;
					else // om knappen �r ett spell�ge-alternativ som inte �r aktuellt vald och inte �r hovrad �ver med musen
						Panel.menuColor[i] = gr�;
				}
			}
		}
	}
	
	private boolean isSelectedGameMode(int menuButton) { // returnerar sant om menuButton-parametern (knapp fr�n spell�ge-menyn) �r det aktuellt valda spell�get
		return  menuButton == 0 && Main.gameMode.equals("agario") || 
				menuButton == 1 && Main.gameMode.equals("survival") || 
				menuButton == 2 && Main.gameMode.equals("escape");
	}

}