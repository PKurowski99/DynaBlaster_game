import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Okno pomocy zawierające informację o grze, sterowaniu i autorach.
 */
public class HelpWindow implements ActionListener {
    private JFrame frame;
    private JPanel panel;
    private JButton okButton;
    private JTextArea label;
    private JLabel move;
    private JLabel space;
    private JLabel esc;
    private JLabel authors;
    private JLabel controlsImage;
    private JLabel spaceImage;
    private JLabel escImage;
    static boolean opened = false;

    public HelpWindow() {
        if (!opened) {
            opened = true;
            frame = new JFrame("DynaBlaster Help Window");
            panel = new JPanel();
            okButton = new JButton("OK");
            move = new JLabel("MOVE:");
            space = new JLabel("BOMB:");
            esc = new JLabel("PAUSE:");
            authors = new JLabel();

            frame.setSize(InputData.helpFrameWidth, InputData.helpFrameHeight);
            frame.setLocationRelativeTo(null);
            label = new JTextArea(InputData.rules, 5, 10);
            label.setPreferredSize(new Dimension(InputData.rulesWidth, InputData.rulesHeight));
            ImageIcon card = new ImageIcon(InputData.controlsPath);
            controlsImage = new JLabel(card);
            ImageIcon card2 = new ImageIcon(InputData.spacePath);
            spaceImage = new JLabel(card2);
            ImageIcon card3 = new ImageIcon(InputData.escPath);
            escImage = new JLabel(card3);
            panel.setBackground(InputData.backgroundColor);
            authors.setText(InputData.authors);

            okButton.setFont(new Font(label.getName(), Font.PLAIN, InputData.fontSize - 2));

            frame.add(panel);

            label.setEditable(false);
            label.setLineWrap(true);
            label.setWrapStyleWord(true);
            label.setFont(new Font(label.getName(), Font.PLAIN, InputData.fontSize));
            label.setBackground(null);

            move.setFont(new Font(label.getName(), Font.PLAIN, InputData.fontSize));
            space.setFont(new Font(label.getName(), Font.PLAIN, InputData.fontSize));
            esc.setFont(new Font(label.getName(), Font.PLAIN, InputData.fontSize));
            authors.setFont(new Font(label.getName(), Font.PLAIN, InputData.fontSize));

            panel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.gridx = 1;
            c.gridy = 0;
            panel.add(label, c);
            c.gridy = 1;
            c.insets = new Insets(0, 0, 10, 0);
            panel.add(move, c);
            c.gridy = 2;
            panel.add(controlsImage, c);
            c.gridy = 3;
            panel.add(space, c);
            c.gridy = 4;
            c.insets = new Insets(0, 0, 30, 0);
            panel.add(spaceImage, c);

            c.gridy = 5;
            panel.add(esc,c);
            c.gridy = 6;
            panel.add(escImage,c);
            c.gridy = 7;
            c.ipady = 10;
            c.ipadx = 13;
            panel.add(okButton, c);
            c.gridy = 8;
            panel.add(authors, c);

            okButton.addActionListener(this);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    SwingUtilities.invokeLater(() ->frame.setVisible(false));
                    opened = false;
                }
            });
            SwingUtilities.invokeLater(() ->frame.setVisible(true));
        }
    }
    /**
     * Metoda odpowiadająca za obsługę zdarzeń.
     * @param e zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() ->frame.setVisible(false));
        opened = false;
    }
}