package pongIgra;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Rezultat extends Rectangle{
	
	static int SIRINA_IGRE;
	static int VISINA_IGRE;
	int p1;
	int p2;
	
	Rezultat(int SIRINA_IGRE, int VISINA_IGRE){
		Rezultat.SIRINA_IGRE = SIRINA_IGRE;
		Rezultat.VISINA_IGRE = VISINA_IGRE;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Consolas", Font.PLAIN, 60));
		g.drawLine(SIRINA_IGRE/2, 0, SIRINA_IGRE/2, VISINA_IGRE);
		g.drawString(String.valueOf(p1/10)+String.valueOf(p1%10), (SIRINA_IGRE/2)-85, 50);
		g.drawString(String.valueOf(p2/10)+String.valueOf(p2%10), (SIRINA_IGRE/2)+20, 50);
	}

}
