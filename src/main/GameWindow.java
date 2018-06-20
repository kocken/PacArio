package main;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GameWindow extends JFrame {

	public static JPanel panel; // huvudpanelen som grafiken ritas på

	private int windowWidth = 600; // applikationens föredragna bredd (i pixlar)
	private int windowHeight = 600; // applikationens föredragna höjd (i pixlar)
	
	public GameWindow() {
		setTitle("PacArio"); // titeln för det grafiska fönstret
		setBounds(350, 75, windowWidth, windowHeight); // storlek och placering på fönstret
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // avslutar applikationen när användaren klickar på fönstrets kryss
		setResizable(false); // stänger av möjligheten att ändra fönster-upplösning
		panel = new Panel(); // skapar ett nytt objekt av JPanel-klassen (som grafiken ritas på), vilket lagras i panel-variabeln
		panel.setFocusable(true); panel.requestFocusInWindow(); // sätter fokus på grafikfönstret så fönstret dyker upp längst fram på skärmen, framför andra potentiella grafiska fönster
		add(BorderLayout.CENTER, panel); // lägger till en borderlayout till panelen
	}

}