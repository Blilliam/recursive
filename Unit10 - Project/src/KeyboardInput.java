import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {
	public boolean up = false, down = false, left = false, right = false;

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W)
			up = true;
		if (e.getKeyCode() == KeyEvent.VK_S)
			down = true;
		if (e.getKeyCode() == KeyEvent.VK_A)
			left = true;
		if (e.getKeyCode() == KeyEvent.VK_D)
			right = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W)
			up = false;
		if (e.getKeyCode() == KeyEvent.VK_S)
			down = false;
		if (e.getKeyCode() == KeyEvent.VK_A)
			left = false;
		if (e.getKeyCode() == KeyEvent.VK_D)
			right = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}