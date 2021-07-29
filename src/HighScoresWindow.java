import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

/**
 * Okienko wyświetlające najlepsze wyniki.
 */
public class HighScoresWindow implements ActionListener {
    private JFrame frame;
    private JPanel panel;
    private JTable table;
    private JButton okButton;
    private int rows;
    private Vector<Vector> data;
    private Vector<String> columnNames;

    static boolean opened = false;
    public HighScoresWindow() {
        if (!opened) {
            opened = true;
            frame = new JFrame("High Scores");
            panel = new JPanel();
            okButton = new JButton("OK");
            data = new Vector<Vector>();
            rows = Integer.min(InputData.maxRowsNumber,IOPlayersData.players.size());

            IOPlayersData.readPlayers();
            IOPlayersData.vectorSort();

            settings();
        }
    }
    public void settings(){

        frame.setLocationRelativeTo(null);
        frame. setSize(new Dimension(InputData.highScoresFrameWidth, InputData.highScoresFrameHeight));
        columnNames = new Vector<String>();
        columnNames.add("RANK");
        columnNames.add("NICKNAME");
        columnNames.add("SCORE");

        for (int i = 0; i < rows; i++) {
            Vector<String> row = new Vector<String>();
            row.addElement(Integer.toString(i+1)+".");
            row.addElement((IOPlayersData.players.elementAt(i).getNickname()));
            row.addElement(Integer.toString(IOPlayersData.players.elementAt(i).getScores()));
            data.addElement(row);
        }
        table = new JTable(data, columnNames);
        JScrollPane sp = new JScrollPane(table);
        sp.setBackground(InputData.backgroundColor);
        table.setBackground(Color.WHITE);
        table.setFont(new Font(table.getName(), Font.PLAIN, InputData.fontSize - 2));
        table.setRowHeight(40);
        table.setSize(new Dimension(300, 220));

        panel.setBackground(InputData.backgroundColor);

        okButton.setFont(new Font(okButton.getName(), Font.PLAIN, InputData.fontSize - 2));
        okButton.setSize(new Dimension(40,30));

        frame.add(panel);
        panel.add(sp);
        panel.add(okButton);

        okButton.addActionListener(this);

        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwingUtilities.invokeLater(() ->frame.setVisible(false));
                new StartWindow();
                opened =false;
            }
        });
    }

    /**
     * Metoda obsługi zdarzeń.
     * @param e zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() ->frame.setVisible(false));
        opened = false;
        new StartWindow();
    }

}
