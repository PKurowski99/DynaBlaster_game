import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Okno odpowiadające za logowanie gracza do gry.
 */
public class LoginWindow implements ActionListener {
    private JFrame frame;
    private JPanel panel;
    private JLabel nicknameLabel;
    private JTextField nicknameText;
    private JButton okButton;
    private JButton cancelButton;
    public LoginWindow(){
        frame = new JFrame("DynaBlaster login window");
        panel = new JPanel();
        nicknameLabel = new JLabel("Nickname: ");
        nicknameText = new JTextField(20);
        okButton = new JButton("Login");
        cancelButton = new JButton("Cancel");

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setSize(InputData.loginFrameWidth,InputData.loginFrameHeight);
        panel.setLayout(new GridBagLayout());
        panel.setBackground(InputData.backgroundColor);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10,10,10,10);
        panel.add(nicknameLabel,c);
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 3;
        panel.add(nicknameText,c);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        panel.add(okButton,c);
        c.insets = new Insets(10,160,10,10);
        c.gridx = 2;
        c.gridy = 1;
        panel.add(cancelButton,c);

        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        SwingUtilities.invokeLater(() ->frame.setVisible(true));
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwingUtilities.invokeLater(() ->frame.setVisible(false));
                new StartWindow();
            }
        });
    }

    /**
     * Metoda odpowiadająca za obsługę zdarzeń.
     * @param e zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if(source == okButton) {
            Hero.reload();
            Player p;
            if(!nicknameText.getText().isEmpty()) {
                p = new Player(nicknameText.getText());
            } else{
                p = new Player("NoName");
            }
            IOPlayersData.players.add(p);
            SwingUtilities.invokeLater(() ->frame.setVisible(false));
            new HeroSelectWindow(p);
        }
        else if(source == cancelButton) {
            SwingUtilities.invokeLater(() ->frame.setVisible(false));
            new StartWindow();
        }
    }
}
