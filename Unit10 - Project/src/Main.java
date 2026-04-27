import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

public class Main {
    private static JSlider slider; 
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("DVD Zoomer");
        AppPanel app = new AppPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Setup slider
        slider = new JSlider(SwingConstants.VERTICAL, 0, 100, 50);
        slider.setMinorTickSpacing(2);
        slider.setMajorTickSpacing(20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        
        // Add components with Layout positions
        frame.setLayout(new BorderLayout());
        frame.add(app, BorderLayout.CENTER);
        frame.add(slider, BorderLayout.EAST); // Slider on the right side
        
        frame.pack();
        frame.setVisible(true);
    }

    public static int getDelay() {
        if (slider == null) {
            return 50; 
        }
        return slider.getValue();
    }
}