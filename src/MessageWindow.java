import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

/**
 * Okno wyswietlające informację o zakończeniu gry oraz wynik gracza.
 */

public class MessageWindow implements ActionListener {
    private JFrame frame;
    private JPanel panel;
    private JLabel messageLabel;
    private JLabel messageLabel2;
    private JLabel scoreLabel;
    private JButton okButton;
    private JButton sendScoreButton;
    private Player p;

    public MessageWindow(String message,Player player) {
        p = player;
        frame = new JFrame("DynaBlaster message window");
        panel = new JPanel();
        messageLabel = new JLabel(message);
        messageLabel.setFont(new Font(messageLabel.getName(), Font.PLAIN, 80));
        messageLabel2 = new JLabel("Your score:");
        messageLabel2.setFont(new Font(messageLabel2.getName(), Font.PLAIN, 30));
        scoreLabel = new JLabel(String.valueOf(p.getScores()));
        scoreLabel.setFont(new Font(scoreLabel.getName(), Font.PLAIN, 30));
        okButton = new JButton("Ok");
        okButton.setSize(100,50);
        sendScoreButton = new JButton("Send Score");
        okButton.setSize(100,50);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setSize(InputData.loginFrameWidth, InputData.loginFrameHeight);
        panel.setLayout(new GridBagLayout());
        panel.setBackground(InputData.backgroundColor);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 10, 10);
        panel.add(messageLabel, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        panel.add(messageLabel2, c);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        panel.add(scoreLabel, c);
        c.gridx = 0;
        c.gridy = 2;
        if(!InputData.serverStatus){c.gridwidth = 2;}
        panel.add(okButton, c);
        if(InputData.serverStatus) {
            c.gridx = 1;
            panel.add(sendScoreButton, c);
        }
        okButton.addActionListener(this);
        sendScoreButton.addActionListener(this);

        SwingUtilities.invokeLater(() -> frame.setVisible(true));
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwingUtilities.invokeLater(() -> frame.setVisible(false));
                IOPlayersData.save();
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

        if (source == okButton) {
            SwingUtilities.invokeLater(() -> frame.setVisible(false));
            IOPlayersData.save();
            new StartWindow();
        }
        if (source == sendScoreButton){
            SwingUtilities.invokeLater(() -> frame.setVisible(false));
            IOPlayersData.save();
            InputData.client("PUSH " + p.getNickname() + " " + p.getScores());
            new StartWindow();
        }
    }
}
