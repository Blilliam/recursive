import java.awt.Color;
import java.awt.Graphics2D;

public class GameObject {
	// declaring everything

	private MouseInput mouseHandler;

	// buttons for game start/settings stuff
	private int startButtonWidth;
	private int startButtonHeight;
	private GameButton startButton;

	private int exitControlButtonWidth;
	private int exitControlButtonHeight;
	private GameButton exitControlButton;

	private int controlButtonWidth;
	private int controlButtonHeight;
	private GameButton controlButton;

	private State state;
	private State mainState;
	private State controlState;
	private State runState;
	
	private int centerPointX;
	private int centerPointY;

	public GameObject(MouseInput mouseHandler) {

		this.mouseHandler = mouseHandler;

		// actually initializes the buttons
		startButtonWidth = 300;
		startButtonHeight = 100;

		controlButtonWidth = 300;
		controlButtonHeight = 100;

		exitControlButtonWidth = 300;
		exitControlButtonHeight = 100;

		mainState = new MenuState(this);
		controlState = new ControlsState(this);
		runState = new RunState(this);

		state = mainState;

		startButtonWidth = 300;
		startButtonHeight = 100;

		controlButtonWidth = 300;
		controlButtonHeight = 100;

		exitControlButtonWidth = 300;
		exitControlButtonHeight = 100;
		
		setStartButton(new GameButton(AppPanel.WIDTH / 2 - startButtonWidth / 2,
				AppPanel.HEIGHT / 2 - startButtonHeight / 2, startButtonWidth, startButtonHeight, "START",
				() -> setState(new RunState(this)), new Color(0, 60, 60), Color.BLACK));

		setControlButton(new GameButton(AppPanel.WIDTH / 2 - startButtonWidth / 2,
				AppPanel.HEIGHT / 2 - controlButtonWidth / 2 + 230 + controlButtonHeight / 2, controlButtonWidth,
				controlButtonHeight, "CONTROLS", () -> setState(new ControlsState(this)), new Color(0, 60, 60), Color.BLACK));

		setExitControlButton(new GameButton(AppPanel.WIDTH / 2 - exitControlButtonWidth / 2,
				AppPanel.HEIGHT / 2 + exitControlButtonHeight / 2 + 50, exitControlButtonWidth, exitControlButtonHeight,
				"EXIT BACK", () -> setState(new MenuState(this))));

	}

	public void update() {
		state.update();
		MouseInput.update();
	}

	public void draw(Graphics2D g2) {
		state.draw(g2);
	}


	public boolean isOnScreen(int x, int y, int w, int h) {

		int camX = getCameraX();
		int camY = getCameraY();

		// Object bounds in world space
		int objLeft = x;
		int objRight = x + w;
		int objTop = y;
		int objBottom = y + h;

		// Screen bounds in world space
		int screenLeft = camX;
		int screenRight = camX + AppPanel.WIDTH;
		int screenTop = camY;
		int screenBottom = camY + AppPanel.HEIGHT;

		// Check if completely outside screen
		if (objRight < screenLeft)
			return false; // left of screen
		if (objLeft > screenRight)
			return false; // right of screen
		if (objBottom < screenTop)
			return false; // above screen
		if (objTop > screenBottom)
			return false; // below screen

		return true; // otherwise it's visible
	}

	public int getCameraX() {
		return centerPointX - AppPanel.WIDTH / 2;
	}

	public int getCameraY() {
		return centerPointX - AppPanel.HEIGHT / 2;
	}

	private void setState(State other) {
		state = other;
	}

	public MouseInput getMouseHandler() {
		return mouseHandler;
	}

	public void setMouseHandler(MouseInput mouseHandler) {
		this.mouseHandler = mouseHandler;
	}

	public GameButton getExitControlButton() {
		return exitControlButton;
	}

	public void setExitControlButton(GameButton exitControlButton) {
		this.exitControlButton = exitControlButton;
	}

	public GameButton getStartButton() {
		return startButton;
	}

	public void setStartButton(GameButton startButton) {
		this.startButton = startButton;
	}

	public GameButton getControlButton() {
		return controlButton;
	}

	public void setControlButton(GameButton controlButton) {
		this.controlButton = controlButton;
	}
}
