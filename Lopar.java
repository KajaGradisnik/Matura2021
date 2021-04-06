package pongIgra;

import java.awt.*;
import java.awt.event.*;

public class Lopar extends Rectangle {
	
	int id; //igralec 1 ali 2
	int yHitrost;
	int hitrost = 8;
	
	Lopar(int x, int y, int LOPAR_SIRINA, int LOPAR_VISINA, int id){
		super(x, y, LOPAR_SIRINA, LOPAR_VISINA);
		this.id = id;
	}
	
	public void keyPressed(KeyEvent e) {
		switch(id) {
		case 1: 
			if(e.getKeyCode()==KeyEvent.VK_W) {
				setYSmer(-hitrost);
				move();
			}
			if(e.getKeyCode()==KeyEvent.VK_S) {
				setYSmer(hitrost);
				move();
			}
			break;
		case 2: 
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				setYSmer(-hitrost);
				move();
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN) {
				setYSmer(hitrost);
				move();
			}
			break;
		}
	}
	public void keyReleased(KeyEvent e) {
		switch(id) {
		case 1: 
			if(e.getKeyCode()==KeyEvent.VK_W) {
				setYSmer(0);
				move();
			}
			if(e.getKeyCode()==KeyEvent.VK_S) {
				setYSmer(0);
				move();
			} 
			break;
		case 2: 
			if(e.getKeyCode()==KeyEvent.VK_UP) {
				setYSmer(0);
				move();
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN) {
				setYSmer(0);
				move();
			}
			break;
		}
	}
	public void setYSmer(int ySmer) {
		yHitrost = ySmer;
	}
	public void move() {
		y += yHitrost;
	}
	public void draw(Graphics g) {
		if(id==1) {
			g.setColor(Color.CYAN);
		}
		else {
			g.setColor(Color.PINK);
		}
		g.fillRect(x, y, width, height);
	}

}
