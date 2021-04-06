package pongIgra;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class IgralnaPlosca extends JPanel implements Runnable {

	static final int SIRINA_IGRE = 1000;
	static final int VISINA_IGRE = (int)(SIRINA_IGRE * 0.5555); 
	static final Dimension ZASLON = new Dimension(SIRINA_IGRE, VISINA_IGRE);
	static final int PREMER_ZOGE = 20;
	static final int LOPAR_SIRINA = 20;
	static final int LOPAR_VISINA = 100;
	Thread gameThread;
	Image image;
	Graphics graphics;
	Random random;
	Lopar l1;
	Lopar l2;
	Zoga zoga;
	Rezultat rezultat;
	
	IgralnaPlosca() {
		noviLoparji();
		novaZoga();
		rezultat = new Rezultat(SIRINA_IGRE, VISINA_IGRE);
		this.setFocusable(true);
		this.addKeyListener(new AL());
		this.setPreferredSize(ZASLON);
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void novaZoga() {
		random = new Random();
		zoga = new Zoga((SIRINA_IGRE/2)-(PREMER_ZOGE/2), random.nextInt(VISINA_IGRE-PREMER_ZOGE), PREMER_ZOGE, PREMER_ZOGE);
	}
	public void noviLoparji() {
		//x, y, sirina, visina
		l1 = new Lopar(10, (VISINA_IGRE/2)-(LOPAR_VISINA/2), LOPAR_SIRINA, LOPAR_VISINA, 1);
		l2 = new Lopar(SIRINA_IGRE-LOPAR_SIRINA-10, (VISINA_IGRE/2)-(LOPAR_VISINA/2), LOPAR_SIRINA, LOPAR_VISINA, 2);
	}
	public void paint(Graphics g) {
		image = createImage(getWidth(), getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image, 0, 0, this); //slika, koordinate in "this" (JPanel)
	}
	public void draw(Graphics g) {
		//ustavi igro pri 11
		g.setColor(Color.white);
		if(rezultat.p1 >= 11 || rezultat.p2 >= 11) {
			g.setFont(new Font("Consolas", Font.PLAIN, 60));
			g.drawString("Konec igre", (SIRINA_IGRE/2)-160, VISINA_IGRE/2);
			if(rezultat.p1 >= 11) {
				g.setFont(new Font("Consolas", Font.PLAIN, 20));
				g.drawString("Igralec 1 zmaga!",(SIRINA_IGRE/2)-100, (VISINA_IGRE/2)+50);
			}
			else {
				g.setFont(new Font("Consolas", Font.PLAIN, 20));
				g.drawString("Igralec 2 zmaga!",(SIRINA_IGRE/2)-100, (VISINA_IGRE/2)+50);
			}
		}
		else {
			l1.draw(g);
			l2.draw(g);
			zoga.draw(g);
			rezultat.draw(g);
			Toolkit.getDefaultToolkit().sync(); //tekoča animacija
		}
	}
	public void move() {
		l1.move();
		l2.move();
		zoga.move();
	}
	public void preveriTrk() {
		//odbije zogo zgoraj in spodaj
		if(zoga.y <= 0) {
			zoga.setYSmer(-zoga.yHitrost);
		}
		if(zoga.y >= VISINA_IGRE-PREMER_ZOGE) {
			zoga.setYSmer(-zoga.yHitrost);
		}
		//odbije zogo od loparjev
		if(zoga.intersects(l1)) {
			zoga.xHitrost = Math.abs(zoga.xHitrost);
			zoga.xHitrost++; //pospeši po trku z loparjem
			zoga.setXSmer(zoga.xHitrost);
			zoga.setYSmer(zoga.yHitrost);
		}
		if(zoga.intersects(l2)) {
			zoga.xHitrost = Math.abs(zoga.xHitrost);
			zoga.xHitrost++; //pospeši po trku z loparjem
			zoga.setXSmer(-zoga.xHitrost);
			zoga.setYSmer(zoga.yHitrost);
		}
		//prepreči prehajanje loparjev z zaslona
		if(l1.y <= 0) l1.y = 0;
		if(l1.y >= (VISINA_IGRE-LOPAR_VISINA)) l1.y = VISINA_IGRE - LOPAR_VISINA;
		if(l2.y <= 0) l2.y = 0;
		if(l2.y >= (VISINA_IGRE-LOPAR_VISINA)) l2.y = VISINA_IGRE - LOPAR_VISINA;
		//igralcu da točko in ustvari nove loparje ter zogo
		if(zoga.x <= 0) {
			rezultat.p2++;
			noviLoparji();
			novaZoga();
		}
		if(zoga.x >= (SIRINA_IGRE - PREMER_ZOGE)) {
			rezultat.p1++;
			noviLoparji();
			novaZoga();
		}
	}
	public void run() {
		//zanka za igro
		long zadnjiCas = System.nanoTime();
		double posodobitev = 1000000000 / 60.0; //posodobitev na sekundo
		double pretekliCas = 0;
		while(true) {
			long trenutniCas = System.nanoTime();
			pretekliCas += (trenutniCas - zadnjiCas)/posodobitev;
			zadnjiCas = trenutniCas;
			if(pretekliCas >= 1) {
				move();
				preveriTrk();
				repaint();
				pretekliCas--;
			}
		}
	}
	public class AL extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			l1.keyPressed(e);
			l2.keyPressed(e);
		}
		public void keyReleased(KeyEvent e) {
			l1.keyReleased(e);
			l2.keyReleased(e);
		}
	}

}
