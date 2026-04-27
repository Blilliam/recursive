import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

public class MouseInput extends MouseAdapter {

	public static int mouseX;
	public static int mouseY;

	public static boolean mousePressed = false;
	public static boolean rightClicked = false;

	@Override
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			mousePressed = true;
		} else if (SwingUtilities.isRightMouseButton(e)) {
			rightClicked = true;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	public static void update() {
		mousePressed = false;
		rightClicked = false;
	}
}