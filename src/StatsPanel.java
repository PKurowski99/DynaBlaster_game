import javax.swing.*;

/**
 * Panel statystyk.
 */
public class StatsPanel extends JPanel {

    protected ImageIcon heartFullImage = new ImageIcon(InputData.heartFullImagePath);
    protected ImageIcon heartEmptyImage = new ImageIcon(InputData.heartEmptyImagePath);
    protected ImageIcon starFullImage = new ImageIcon(InputData.starFullImagePath);
    protected ImageIcon starEmptyImage = new ImageIcon(InputData.starEmptyImagePath);
    protected ImageIcon bombFullImage = new ImageIcon(InputData.bombFullImagePath);
    protected ImageIcon bombEmptyImage = new ImageIcon(InputData.bombEmptyImagePath);

    public StatsPanel(){

    }

    protected void changeStats(int points, JLabel x1, JLabel x2, JLabel x3, JLabel x4, JLabel x5, ImageIcon full, ImageIcon empty) {
        switch (points) {
            case 2:
                x1.setIcon(full);
                x2.setIcon(full);
                x3.setIcon(empty);
                x4.setIcon(empty);
                x5.setIcon(empty);
                break;
            case 3:
                x1.setIcon(full);
                x2.setIcon(full);
                x3.setIcon(full);
                x4.setIcon(empty);
                x5.setIcon(empty);
                break;
            case 4:
                x1.setIcon(full);
                x2.setIcon(full);
                x3.setIcon(full);
                x4.setIcon(full);
                x5.setIcon(empty);
                break;
            case 5:
                x1.setIcon(full);
                x2.setIcon(full);
                x3.setIcon(full);
                x4.setIcon(full);
                x5.setIcon(full);
                break;
            default:
                x1.setIcon(full);
                x2.setIcon(empty);
                x3.setIcon(empty);
                x4.setIcon(empty);
                x5.setIcon(empty);

        }
    }
}
