import java.awt.Color;
import java.awt.Graphics2D;

/**
 * GameObject serves as the primary controller and state manager for the
 * application. It utilizes the State Pattern to transition between the menu,
 * controls, and active gameplay.
 */
public class GameObject {
	// Input handler references for event delegation
	private MouseInput mouseHandler;
	private KeyboardInput keyHandler;

	// State references to facilitate switching between application phases
	private State state;
	private State mainState;
	private State controlState;
	private State runState;

	// Button dimensions and object declarations
	private int startButtonWidth = 300;
	private int startButtonHeight = 100;
	private GameButton startButton;

	private int controlButtonWidth = 300;
	private int controlButtonHeight = 100;
	private GameButton controlButton;

	private int exitControlButtonWidth = 300;
	private int exitControlButtonHeight = 100;
	private GameButton exitControlButton;

	/**
	 * Constructor: Initializes input handlers, creates state instances, and
	 * configures UI components with functional logic.
	 * 
	 * @param mouseHandler The MouseInput listener from the AppPanel.
	 * @param keyHandler   The KeyboardInput listener from the AppPanel.
	 */
	public GameObject(MouseInput mouseHandler, KeyboardInput keyHandler) {
		this.mouseHandler = mouseHandler;
		this.keyHandler = keyHandler;

		// Initialize concrete state objects, passing 'this' to allow for state
		// callbacks
		mainState = new MenuState(this);
		controlState = new ControlsState(this);
		runState = new RunState(this);

		// Initialize default application state
		state = mainState;

		// Configure Buttons: Using Lambda expressions (Functional Interfaces)
		// to define behavior on click events.
		setStartButton(new GameButton(AppPanel.WIDTH / 2 - startButtonWidth / 2,
				AppPanel.HEIGHT / 2 - startButtonHeight / 2, startButtonWidth, startButtonHeight, "START",
				() -> setState(new RunState(this)), new Color(0, 60, 60), Color.BLACK));

		setControlButton(new GameButton(AppPanel.WIDTH / 2 - startButtonWidth / 2, AppPanel.HEIGHT / 2 + 150,
				controlButtonWidth, controlButtonHeight, "CONTROLS", () -> setState(new ControlsState(this)),
				new Color(0, 60, 60), Color.BLACK));

		setExitControlButton(new GameButton(AppPanel.WIDTH / 2 - exitControlButtonWidth / 2, AppPanel.HEIGHT / 2 + 200,
				exitControlButtonWidth, exitControlButtonHeight, "EXIT BACK", () -> setState(new MenuState(this))));
	}

	/**
	 * Logic Tick: Delegates updates to the currently active state and refreshes
	 * global input states.
	 */
	public void update() {
		// Polymorphic call to the current state's update method
		state.update();

		// Refresh mouse state to process click/hover events globally
		MouseInput.update();
	}

	/**
	 * Render Tick: Delegates drawing operations to the currently active state.
	 * 
	 * @param g2 The Graphics2D context for rendering.
	 */
	public void draw(Graphics2D g2) {
		// Polymorphic call to the current state's draw method
		state.draw(g2);
	}

	/**
	 * Transition the application to a different execution state.
	 * 
	 * @param other The new state to activate.
	 */
	public void setState(State other) {
		state = other;
	}

	public State getState() {
		return state;
	}

	// Accessor methods for input handlers used by concrete State classes
	public KeyboardInput getKeyHandler() {
		return keyHandler;
	}

	public MouseInput getMouseHandler() {
		return mouseHandler;
	}

	// Accessor and Mutator methods for UI button components
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

	public GameButton getExitControlButton() {
		return exitControlButton;
	}

	public void setExitControlButton(GameButton exitControlButton) {
		this.exitControlButton = exitControlButton;
	}
}