import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * GameButton defines a UI component capable of detecting mouse interaction
 * and executing functional interfaces (Runnable) upon activation.
 */
public class GameButton {

    private int w, h;
    private Vec2 pos;
    private String buttonText;
    private Runnable clickFunc;
    private Color mainColor;
    private Color borderColor;

    /**
     * Overloaded constructor for custom color configurations.
     */
    public GameButton(int x, int y, int w, int h, String text, Runnable clickFunc, Color mainColor, Color borderColor) {
        this(x, y, w, h, text, clickFunc);
        this.mainColor = mainColor;
        this.borderColor = borderColor;
    }
    
    /**
     * Standard constructor initializing position, dimensions, and behavior.
     */
    public GameButton(int x, int y, int w, int h, String text, Runnable clickFunc) {
        this.pos = new Vec2(x, y);
        this.w = w;
        this.h = h;
        this.buttonText = text;
        this.clickFunc = clickFunc; 
        this.mainColor = new Color(60, 60, 60);
        this.borderColor = Color.WHITE;
    }

    /**
     * Performs AABB (Axis-Aligned Bounding Box) collision detection between 
     * the mouse coordinates and button bounds.
     * @return true if the mouse cursor is within the button perimeter.
     */
    public boolean isHovering() {
        return MouseInput.mouseX >= pos.getX() && MouseInput.mouseX <= pos.getX() + w &&
               MouseInput.mouseY >= pos.getY() && MouseInput.mouseY <= pos.getY() + h;
    }

    /**
     * Evaluates user interaction state and triggers the assigned Runnable if 
     * conditions for a click event are met.
     */
    public void update() {
        if (isHovering() && MouseInput.mousePressed) {
            clickFunc.run();
            // Consumption of the input event to prevent multi-frame triggering
            MouseInput.update(); 
        }
    }

    /**
     * Renders the button graphic, applying a visual tint if a hover state is detected.
     * @param g2 The Graphics context for rendering.
     */
    public void draw(Graphics2D g2) {

        // Render base geometry
        g2.setColor(mainColor);
        g2.fillRect(pos.getIntX(), pos.getIntY(), w, h);

        // Apply visual feedback for hover interaction
        if (isHovering()) {
            g2.setColor(new Color(0, 0, 0, 100)); // Semi-transparent black overlay
            g2.fillRect(pos.getIntX(), pos.getIntY(), w, h);
        }

        // Render component borders
        g2.setColor(borderColor);
        g2.drawRect(pos.getIntX(), pos.getIntY(), w, h);

        // Configure typography and calculate center-alignment for text
        g2.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        g2.setColor(Color.WHITE);

        FontMetrics fm = g2.getFontMetrics();
        int textX = pos.getIntX() + (w - fm.stringWidth(buttonText)) / 2;
        int textY = pos.getIntY() + (h + fm.getAscent()) / 2 - 4;

        g2.drawString(buttonText, textX, textY);
    }

    /**
     * Returns a standard AWT Rectangle representing the button bounds.
     * @return Rectangle object for collision or layout logic.
     */
    public Rectangle getBounds() {
        return new Rectangle(pos.getIntX(), pos.getIntY(), w, h);
    }
}