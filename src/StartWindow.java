import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Okno startowe programu.
 */
public class StartWindow implements ActionListener{
    private JPanel panel;
    private JButton playButton;
    private JButton scoresButton;
    private JButton exitButton;
    private JFrame frame;
    private JMenuItem helpMenu;
    private JLabel dynaImageLabel;

    public StartWindow(){

        frame = new JFrame("DynaBlaster main window");
        panel = new JPanel();
        playButton = new JButton("PLAY");
        scoresButton = new JButton("SCORES");
        exitButton = new JButton("EXIT");

        JMenuBar jmb = new JMenuBar();
        frame.setJMenuBar(jmb);
        JMenu menu = new JMenu("MORE");
        helpMenu = new JMenuItem("Help");
        menu.add(helpMenu);
        jmb.add(menu);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        frame.setSize(InputData.startFrameWidth, InputData.startFrameHeight);
        ImageIcon card = new ImageIcon(InputData.logoPath);
        dynaImageLabel = new JLabel(card);
        panel.setBackground(InputData.backgroundColor);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,0,70,0);
        c.gridy = 1;
        panel.add(dynaImageLabel,c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;
        c.ipadx = 800;
        c.insets = new Insets(0,0,20,0);
        c.gridy = 2;
        panel.add(playButton,c);
        c.gridy = 3;
        panel.add(scoresButton,c);
        c.gridy = 4;
        panel.add(exitButton,c);
        playButton.addActionListener(this);
        scoresButton.addActionListener(this);
        exitButton.addActionListener(this);
        helpMenu.addActionListener(this);

        frame.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(() ->frame.setVisible(true));
    }

    /**
     * Metoda odpowiadająca za obsługę zdarzeń.
     * @param e zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if(source == playButton) {
            SwingUtilities.invokeLater(() ->frame.setVisible(false));
            new LoginWindow();
        }
        else if(source == scoresButton) {
            SwingUtilities.invokeLater(() ->frame.setVisible(false));
            new HighScoresWindow();
        }
        else if(source == exitButton)
            System.exit(0);

        else if(source == helpMenu){
            new HelpWindow();
        }
    }
}
