

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JPanel;


public class AppPanel extends JPanel implements Runnable {
	static Toolkit tk = Toolkit.getDefaultToolkit();
	//1024 × 768
	public static final int WIDTH = 1024; // or ((int) tk.getScreenSize().getWidth()); // sets width to screen width
	public static final int HEIGHT = 768; // or ((int)tk.getScreenSize().getHeight()) - 38; // sets height to screen height
	public Dimension d = new Dimension(WIDTH, HEIGHT); //creates dementions with screen dimentions
	
	public Thread t = new Thread(this); 
	
	MouseInput mouseHandler = new MouseInput(); //mouse input
	
	GameObject gameObj = new GameObject(mouseHandler); // creates game object and passes teh inputs
	
	// Constructor
	public AppPanel() {
		setPreferredSize(d); // sets preffered panel size
		setFocusable(true); // alows you to zoom in 
        addMouseListener(mouseHandler); // listens for mouse clicks
        addMouseMotionListener(mouseHandler); // listens for mouse movement
		t.start(); // starts the thread
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g); // just runs everything before

        Graphics2D g2 = (Graphics2D) g; // greates graphics object

        // Enable anti-aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        gameObj.draw(g2); // draws basically everything
	}

	@Override
	public void run() {
		while (true) {
			repaint();
			gameObj.update(); // updates game
			try {
				Thread.sleep(17); // 1000/17 = 58.8 so about 60 fps
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}