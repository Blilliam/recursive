import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.event.MouseEvent;

public class RunState extends State {
    private Dvd rootDvd;
    private boolean isZooming = false;
    private boolean isAuto = false; // Toggle for automatic zooming
    private double zoomProgress = 0;
    private double zoomSpeed = 0.04; 

    public RunState(GameObject gameObj) {
        super(gameObj);
        rootDvd = new Dvd(AppPanel.WIDTH, AppPanel.HEIGHT, 3.0, 5, AppPanel.WIDTH, AppPanel.HEIGHT);
    }

    @Override
    public void update() {
    	double speedFromSlider = Main.getDelay() / 1000.0;
        
        // Prevent the speed from being exactly 0 so it doesn't freeze
        if (speedFromSlider <= 0) {
            speedFromSlider = 0.001;
        }
        
        this.zoomSpeed = speedFromSlider;
    	
        // Toggle Auto-Mode on Right Click
        if (MouseInput.rightClicked) {
            isAuto = !isAuto;
        }

        // Trigger zoom if: Manual left click OR Auto-mode is on
        if ((MouseInput.mousePressed || isAuto) && !isZooming) {
            isZooming = true;
            zoomProgress = 0;
        }

        rootDvd.update(AppPanel.WIDTH, AppPanel.HEIGHT, false);

        if (isZooming) {
            zoomProgress += zoomSpeed;

            if (zoomProgress >= 1.0) {
                Dvd child = rootDvd.getChild();
                if (child != null) {
                    rootDvd = child;
                    rootDvd.promoteToRoot(AppPanel.WIDTH, AppPanel.HEIGHT);
                }
                isZooming = false;
                zoomProgress = 0;
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);

        AffineTransform original = g.getTransform();

        if (isZooming && rootDvd.getChild() != null) {
            Dvd target = rootDvd.getChild();
            double currentScale = 1.0 + zoomProgress; 

            double tx = -target.getPos().getX() * zoomProgress;
            double ty = -target.getPos().getY() * zoomProgress;

            g.translate(tx * currentScale, ty * currentScale);
            g.scale(currentScale, currentScale);
        }

        rootDvd.draw(g);
        g.setTransform(original);
        
        // Optional: Visual indicator for Auto Mode
        if (isAuto) {
            g.setColor(Color.WHITE);
            g.drawString("AUTO MODE: ON", 10, 20);
        }
    }
}