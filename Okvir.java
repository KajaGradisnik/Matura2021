package pongIgra;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Okvir extends JFrame {
	
	IgralnaPlosca plosca;
	
	Okvir(){
		plosca = new IgralnaPlosca();
		this.add(plosca);
		this.setTitle("Pong");
		this.setResizable(false);
		this.setBackground(Color.darkGray);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack(); //prilagodi velikosti okenca
		this.setVisible(true);
		this.setLocationRelativeTo(null); //postavi okno na sredino zaslona
	}

}
