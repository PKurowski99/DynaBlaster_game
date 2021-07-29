import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Okienko wyboru postaci. Zawiera wszystkie informacje o bohaterach.
 */
public class HeroSelectWindow extends StatsPanel implements ActionListener {
    private JFrame frame;
    private JTextArea textArea;
    private JLabel heroLabel;

    private JLabel hpLabel = new JLabel("HP:");
    private JLabel heart1 = new JLabel(heartFullImage);
    private JLabel heart2 = new JLabel(heartFullImage);
    private JLabel heart3 = new JLabel(heartEmptyImage);
    private JLabel heart4 = new JLabel(heartEmptyImage);
    private JLabel heart5 = new JLabel(heartEmptyImage);

    private JLabel speedLabel = new JLabel("SPEED:");
    private JLabel star1 = new JLabel(starFullImage);
    private JLabel star2 = new JLabel(starFullImage);
    private JLabel star3 = new JLabel(starFullImage);
    private JLabel star4 = new JLabel(starEmptyImage);
    private JLabel star5 = new JLabel(starEmptyImage);

    private JLabel fireRateLabel = new JLabel("FIRE RATE:");
    private JLabel bomb1 = new JLabel(bombFullImage);
    private JLabel bomb2 = new JLabel(bombFullImage);
    private JLabel bomb3 = new JLabel(bombFullImage);
    private JLabel bomb4 = new JLabel(bombFullImage);
    private JLabel bomb5 = new JLabel(bombEmptyImage);

    private JButton nextButton = new JButton(new ImageIcon(InputData.nextButtonImagePath));
    private JButton previousButton = new JButton(new ImageIcon(InputData.previousButtonImagePath));
    private JButton playButton = new JButton("PLAY");
    private JButton cancelButton = new JButton("CANCEL");

    private Font specialFont = new Font("specialFont", Font.BOLD, 20);
    private byte currentHero = 1;
    private Player player ;


    public HeroSelectWindow(Player p){
        super();
        player = p;
        textArea = new JTextArea("ABOUT HERO:\n"+player.getChosenHero().getHeroDescription(),15,24);
        heroLabel = new JLabel(new ImageIcon(player.getChosenHero().getResizedImage(500,500)));
        frame = new JFrame("DynaBlaster hero select window");
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setFont(specialFont);
        textArea.setWrapStyleWord(true);

        this.setLayout(null);

        heroLabel.setBounds(620,100,500,500);
        this.add(heroLabel);

        statsBoundsSetting();
        
        frame.setSize(InputData.heroSelectFrameWidth,InputData.heroSelectFrameHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(this);

        this.setBackground(InputData.backgroundColor);
        textArea.setBackground(new Color(169, 133, 127));

        this.add(hpLabel);
        this.add(heart1);
        this.add(heart2);
        this.add(heart3);
        this.add(heart4);
        this.add(heart5);

        this.add(speedLabel);
        this.add(star1);
        this.add(star2);
        this.add(star3);
        this.add(star4);
        this.add(star5);

        this.add(fireRateLabel);
        this.add(bomb1);
        this.add(bomb2);
        this.add(bomb3);
        this.add(bomb4);
        this.add(bomb5);

        textArea.setBounds(0,250,410,435);
        this.add(textArea);
        Border border = BorderFactory.createLineBorder(new Color(92, 43, 0),5);
        textArea.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        nextButton.setBounds(1100,280,120,60);
        previousButton.setBounds(500,280,120,60);
        cancelButton.setBounds(1040,640,100,40);
        playButton.setBounds(1160,640,100,40);

        cancelButton.setFont(new Font("specialFont", Font.BOLD, 15));
        playButton.setFont(new Font("specialFont", Font.BOLD, 15));

        this.add(nextButton);
        this.add(previousButton);
        this.add(cancelButton);
        this.add(playButton);

        nextButton.addActionListener(this);
        previousButton.addActionListener(this);
        cancelButton.addActionListener(this);
        playButton.addActionListener(this);

        SwingUtilities.invokeLater(() ->frame.setVisible(true));
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwingUtilities.invokeLater(() ->frame.setVisible(false));
                new StartWindow();
            }
        });
    }
    private void statsBoundsSetting(){

        hpLabel.setBounds(10,0,58,58);
        hpLabel.setFont(specialFont);
        heart1.setBounds(128,0,58,58);
        heart2.setBounds(186,0,58,58);
        heart3.setBounds(244,0,58,58);
        heart4.setBounds(302,0,58,58);
        heart5.setBounds(360,0,58,58);

        speedLabel.setBounds(10,60,100,58);
        speedLabel.setFont(specialFont);
        star1.setBounds(128,60,58,58);
        star2.setBounds(186,60,58,58);
        star3.setBounds(244,60,58,58);
        star4.setBounds(302,60,58,58);
        star5.setBounds(360,60,58,58);

        fireRateLabel.setBounds(10,120,140,58);
        fireRateLabel.setFont(specialFont);
        bomb1.setBounds(128,120,58,58);
        bomb2.setBounds(186,120,58,58);
        bomb3.setBounds(244,120,58,58);
        bomb4.setBounds(302,120,58,58);
        bomb5.setBounds(360,120,58,58);
    }

    /**
     * Metoda obsługi zdarzeń.
     * @param e zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == playButton) {
            new GameWindow(player);
            SwingUtilities.invokeLater(() ->frame.setVisible(false));
        }
        else if(source == cancelButton) {
            new StartWindow();
            SwingUtilities.invokeLater(() ->frame.setVisible(false));
        }
        else if(source == nextButton) {
            if (currentHero == 2){
                currentHero = 1;
                player.setChosenHero(Hero.hero);
            }
            else{
                currentHero += 1;
                player.setChosenHero(Hero.heroine);
            }
            this.changeHero();
        }
        else if(source == previousButton) {
            if (currentHero == 1){
                ;player.setChosenHero(Hero.heroine);
                currentHero = 2;
            }
            else{
                currentHero -= 1;
                player.setChosenHero(Hero.hero);
            }
            this.changeHero();
        }
    }

    /**
     * Metoda odpowiada za zmianę wyświetlanego bohatera.
     */
    private void changeHero(){
            this.changeStats(player.getChosenHero().getHp(), heart1, heart2, heart3, heart4, heart5, heartFullImage, heartEmptyImage);
            this.changeStats(player.getChosenHero().getSpeed(), star1, star2, star3, star4, star5, starFullImage, starEmptyImage);
            this.changeStats(player.getChosenHero().getFireRate(), bomb1, bomb2, bomb3, bomb4, bomb5, bombFullImage, bombEmptyImage);
            heroLabel.setIcon(new ImageIcon(player.getChosenHero().getResizedImage(500,500)));
            textArea.setText("ABOUT HERO:\n"+player.getChosenHero().getHeroDescription());

    }

}
