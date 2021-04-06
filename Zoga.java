package pongIgra;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Zoga extends Rectangle{
	
	Random random;
	int xHitrost;
	int yHitrost;
	int zacetnaHitrost = 5;
	
	Zoga(int x, int y, int sirina, int visina){
		super(x, y, sirina, visina);
		random = new Random();
		int randomXSmer = random.nextInt(2);
		if(randomXSmer == 0) randomXSmer--;
		setXSmer(randomXSmer*zacetnaHitrost);
		int randomYSmer = random.nextInt(2);
		if(randomYSmer == 0) randomYSmer--;
		setYSmer(randomYSmer*zacetnaHitrost);
	}
	
	public void setXSmer(int randomXSmer) {
		xHitrost = randomXSmer;
	}
	public void setYSmer(int randomYSmer) {
		yHitrost = randomYSmer;
	}
	public void move() {
		x += xHitrost;
		y += yHitrost;
	}
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(x, y, height, width);
	}

}
