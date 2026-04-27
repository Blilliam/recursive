import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * ControlsState manages the UI overlay that displays keybindings and
 * interaction instructions to the user.
 */
public class ControlsState extends State {

	/**
	 * Constructor: Initializes the state with a reference to the parent GameObject.
	 * 
	 * @param gameObj The central engine manager.
	 */
	public ControlsState(GameObject gameObj) {
		super(gameObj);
	}

	/**
	 * Renders the controls menu, including a background overlay and dynamic text
	 * alignment.
	 * 
	 * @param g2 The Graphics2D context for high-quality rendering.
	 */
	@Override
	public void draw(Graphics2D g2) {

		// 1. Render semi-transparent background overlay
		g2.setColor(new Color(30, 30, 80, 200));
		g2.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);

		// 2. Configure typography and font metrics for centering
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Malgun Gothic", Font.BOLD, 35));
		FontMetrics fm = g2.getFontMetrics();

		// Populate instruction set
		ArrayList<String> str = new ArrayList<String>();
		str.add("Up: W");
		str.add("Down: S");
		str.add("Left: A");
		str.add("Right: D");
		str.add("Zoom In: Left Click");
		str.add("Auto Zoom: Right Click");
		str.add("Zoom Speed: Side Bar");

		// 3. Iterative rendering with center-aligned horizontal positioning
		int startY = AppPanel.HEIGHT / 2 - ((str.size() * 80) / 2);

		for (int i = 0; i < str.size(); i++) {
			String text = str.get(i);

			// Calculate x-coordinate for center alignment
			int x = (AppPanel.WIDTH / 2) - (fm.stringWidth(text) / 2);
			int y = startY + (i * 60);

			// Render drop shadow for legibility
			g2.setColor(Color.BLACK);
			g2.drawString(text, x + 2, y + 2);

			// Render primary text
			g2.setColor(Color.WHITE);
			g2.drawString(text, x, y);
		}

		// 4. Delegate rendering to navigation button
		if (gameObj.getExitControlButton() != null) {
			gameObj.getExitControlButton().draw(g2);
		}
	}

	/**
	 * Updates the state-specific logic, primarily monitoring navigation button
	 * interactions.
	 */
	@Override
	public void update() {
		if (gameObj.getExitControlButton() != null) {
			gameObj.getExitControlButton().update();
		}
	}
}