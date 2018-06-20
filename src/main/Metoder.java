package main;

import java.awt.Color;
import java.util.Random;

public class Metoder {
	
    /**
     * Orsakar den aktuella tr�den att sova (tillf�lligt upph�ra utf�randen) f�r det angivna antalet millisekunder
     *
     * @param ms    Hur l�ng tid applikationen ska sova i millisekunder
     */
	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}
	
    /**
     * Genererar ett slumpm�ssigt integer-tal mellan min och max v�rdet
     *
     * @param min   Minsta tal
     * @param max   H�gsta tal
     */
	public static int randomInt(double min, double max) {
	    Random rand = new Random();
	    return (int) (rand.nextInt((int) ((max - min) + 1)) + min);
	}
	
    /**
     * Genererar ett slumpm�ssigt double-tal mellan min och max v�rdet
     *
     * @param min   Minsta tal
     * @param max   H�gsta tal
     */
	public static double randomDouble(double min, double max) {
	    Random rand = new Random();
	    return (rand.nextInt((int) ((max - min) + 1)) + min);
	}
	
    /**
     * Genererar en slumpm�ssig f�rg
     *
     */
	public static Color randomColor() {
		Color c = new Color(randomInt(0, 255), randomInt(0, 255), randomInt(0, 255)); // genererar slumpm�ssig f�rg med RGB-systemet (RGB = Red, Green, Blue)
		while (c.getRed() >= 240 && c.getGreen() >= 240 && c.getBlue() >= 240) // om den genererade f�rgen �r vit (s� att det blir sv�rt att se p.g.a. den vita bakgrunden)
			c = new Color(randomInt(0, 255), randomInt(0, 255), randomInt(0, 255)); // genererar om f�rgen
		return c;
	}
	
}