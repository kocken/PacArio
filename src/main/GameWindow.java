package main;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameWindow extends JFrame {

	public static JPanel panel; // huvudpanelen som grafiken ritas p�

	private int windowWidth = 600; // applikationens f�redragna bredd (i pixlar)
	private int windowHeight = 600; // applikationens f�redragna h�jd (i pixlar)
	
	public GameWindow() {
		setTitle("PacArio"); // titeln f�r det grafiska f�nstret
		setBounds(350, 75, windowWidth, windowHeight); // storlek och placering p� f�nstret
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // avslutar applikationen n�r anv�ndaren klickar p� f�nstrets kryss
		setResizable(false); // st�nger av m�jligheten att �ndra f�nster-uppl�sning
		panel = new Panel(); // skapar ett nytt objekt av JPanel-klassen (som grafiken ritas p�), vilket lagras i panel-variabeln
		panel.setFocusable(true); panel.requestFocusInWindow(); // s�tter fokus p� grafikf�nstret s� f�nstret dyker upp l�ngst fram p� sk�rmen, framf�r andra potentiella grafiska f�nster
		add(BorderLayout.CENTER, panel); // l�gger till en borderlayout till panelen
	}

}