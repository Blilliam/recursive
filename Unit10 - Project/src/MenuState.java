

import java.awt.Graphics2D;


public class MenuState extends State{

	public MenuState(GameObject gameObj) {
		super(gameObj);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics2D g2) {
		// TODO Auto-generated method stub
		gameObj.getStartButton().draw(g2);
		gameObj.getControlButton().draw(g2);
	}

	@Override
	public void update() {
		gameObj.getStartButton().update();
		gameObj.getControlButton().update();
		
	}

}
