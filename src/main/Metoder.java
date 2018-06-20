package main;

import java.awt.Color;
import java.util.Random;

public class Metoder {
	
    /**
     * Orsakar den aktuella tråden att sova (tillfälligt upphöra utföranden) för det angivna antalet millisekunder
     *
     * @param ms    Hur lång tid applikationen ska sova i millisekunder
     */
	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}
	
    /**
     * Genererar ett slumpmässigt integer-tal mellan min och max värdet
     *
     * @param min   Minsta tal
     * @param max   Högsta tal
     */
	public static int randomInt(double min, double max) {
	    Random rand = new Random();
	    return (int) (rand.nextInt((int) ((max - min) + 1)) + min);
	}
	
    /**
     * Genererar ett slumpmässigt double-tal mellan min och max värdet
     *
     * @param min   Minsta tal
     * @param max   Högsta tal
     */
	public static double randomDouble(double min, double max) {
	    Random rand = new Random();
	    return (rand.nextInt((int) ((max - min) + 1)) + min);
	}
	
    /**
     * Genererar en slumpmässig färg
     *
     */
	public static Color randomColor() {
		Color c = new Color(randomInt(0, 255), randomInt(0, 255), randomInt(0, 255)); // genererar slumpmässig färg med RGB-systemet (RGB = Red, Green, Blue)
		while (c.getRed() >= 240 && c.getGreen() >= 240 && c.getBlue() >= 240) // om den genererade färgen är vit (så att det blir svårt att se p.g.a. den vita bakgrunden)
			c = new Color(randomInt(0, 255), randomInt(0, 255), randomInt(0, 255)); // genererar om färgen
		return c;
	}
	
}