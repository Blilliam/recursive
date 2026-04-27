
import java.awt.Graphics2D;

public abstract class State {
	GameObject gameObj;

	public State(GameObject gameObj) {
		this.gameObj = gameObj;
	}

	public abstract void draw(Graphics2D g);

	public abstract void update();

}
