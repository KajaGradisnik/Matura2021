public class PongIgra {
	
	public static void main(String[] args) {
		
		Okvir okvir = new Okvir();
		
	}
 }

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
