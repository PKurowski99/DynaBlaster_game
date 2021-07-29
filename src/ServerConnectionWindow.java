import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Okno łączenia z serwerem.
 */
public class ServerConnectionWindow implements ActionListener {
    private JFrame frame;
    private JPanel panel;
    private JButton playOnlineButton;
    private JButton playOfflineButton;
    private JLabel textLabel;
    public ServerConnectionWindow(){
        frame = new JFrame("DynaBlaster Server Connection Window");
        panel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        playOnlineButton = new JButton("Play Online");
        playOfflineButton = new JButton("Play Offline");
        textLabel = new JLabel("How do you want to play?");
        textLabel.setFont(new Font(textLabel.getName(), Font.PLAIN, 20));
        frame.add(panel);
        frame.setSize(300,200);
        frame.setResizable(false);
        panel.setBackground(new Color(245,207,193));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(textLabel,c);
        c.gridwidth= 1;
        c.gridy = 1;
        panel.add(playOfflineButton,c);
        c.gridx = 1;
        panel.add(playOnlineButton,c);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        playOnlineButton.addActionListener(this);
        playOfflineButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == playOfflineButton) {
            SwingUtilities.invokeLater(() ->frame.setVisible(false));
            InputData.read();
            new StartWindow();
        } else if(source == playOnlineButton){
            SwingUtilities.invokeLater(() ->frame.setVisible(false));
            InputData.client("GETCONFIG");
            InputData.read();
            if(InputData.serverStatus) {
                InputData.client("GETSCORES");
                for (int i = 1; i <= InputData.nrOfLevels; i++) {
                    StringBuilder sb = new StringBuilder("GETMAP ");
                    sb.append(i);
                    InputData.client(sb.toString());
                    sb.setLength(0);
                }
            }
            else{
                System.out.println("Cannot connect to the server, switched to offline mode.");
            }
            new StartWindow();
        }
    }
}
