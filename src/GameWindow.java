import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

/**
 * Klasa okna gry, w którym znajduje się mapa gry oraz statystyki gracza. Dodatkowo w tej klasie zawarta jest mechanika gry.
 */
public class GameWindow extends JPanel implements ActionListener, KeyListener {
    /**
     * Timer, który odpowiada za klatki animacji.
     */
    private Timer tm = new Timer(10, this);
    /**
     * podstawowa prędkość w kierunku poziomym
     */
    private float normalXSpeed = InputData.normalSpeed;
    /**
     * podstawowa prędkość w kierunku pionowym
     */
    private float normalYSpeed = InputData.normalSpeed;
    /**
     * prędkość bohatera w kierunku poziomym
     */
    private int xVel = 0;
    /**
     * prędkość bohatera w kierunku pionowym
     */
    private int yVel = 0;
    /**
     * zmienna pomocnicza używana do skalowania położenia gracza
     */
    private int oldX = 0;
    /**
     * zmienna pomocnicza używana do skalowania położenia gracza
     */
    private int oldY = 0;
    /**
     * zmienna pomocnicza używana do skalowania położenia potworów
     */
    private int oldX2 = 0;
    /**
     * zmienna pomocnicza używana do skalowania położenia potworów
     */
    private int oldY2 = 0;
    private JFrame frame;
    private ScoreBarPanel scoreBarPanel;
    private Player player;
    /**
     * punkty do zdobycia na konkretnym poziomie
     */
    private int score = InputData.timeScorePerLevel;
    /**
     * czy poziom został załadowany
     */
    private boolean loaded = false;
    /**
     * współrzędna x położenia bohatera
     */
    private int xHeroPos;
    /**
     * współrzędna y położenia bohatera
     */
    private int yHeroPos;
    /**
     * zbiór klas bloków w których bohater może się przemieszczać
     */
    private Set<Class> availableSpace = new HashSet<Class>();
    /**
     * aktualny poziom gry
     */
    private Level lvl = new Level(1,InputData.levelPaths.get(0));
    /**
     * lista potworów na poziomie
     */
    private java.util.List<Monster> monsters = new ArrayList<>();
    private boolean current = true;
    private boolean paused = false;


    public GameWindow(Player p) {
        fillMonsterList();
        availableSpace.add(Air.class);
        availableSpace.add(Fire.class);
        availableSpace.add(Portal.class);
        availableSpace.add(Coin.class);
        availableSpace.add(Heart.class);
        availableSpace.add(Bomb.class);
        player = p;
        normalXSpeed = normalXSpeed * p.getChosenHero().getSpeed();
        normalYSpeed = normalYSpeed * p.getChosenHero().getSpeed();
        frame = new JFrame("DynaBlaster game window");
        frame.setLayout(new BorderLayout());
        frame.setSize(InputData.gameFrameWidth, InputData.gameFrameHeight);
        frame.setLocationRelativeTo(null);
        this.setLayout(null);

        scoreBarPanel = new ScoreBarPanel();

        frame.add(scoreBarPanel, BorderLayout.EAST);
        frame.add(this, BorderLayout.CENTER);
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwingUtilities.invokeLater(() -> frame.setVisible(false));
                current = false;
                new StartWindow();
            }
        });
        tm.start();
        setFocusable(true);
        addKeyListener(this);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                lvl.resetMap();
            }
        });
    }

    /**
     * Metoda odpowiada za uzupełnienie listy potworów, odpowiednio dla każdego poziomu
     */
    private void fillMonsterList() {
        int nrOfMonsters = lvl.getMonsterXPosList().size();
        if (nrOfMonsters > 0) {
            monsters.clear();
            for (int i = 0; i < nrOfMonsters; i++) {
                monsters.add(new Monster(lvl.getMonsterXPosList().get(i), lvl.getMonsterYPosList().get(i), i));
            }
        }
    }

    /**
     * Metoda realizuje zebranie bonusu przez bohatera. Obiekt klasy Coin jest zastępowany przez obiekt klasy Air.
     * Punkty bonusowe są dodawane do sumy punktów gracza.
     * @param x współrzędna x zebranego bonusu
     * @param y współrzędna y zebranego bonusu
     */
    private void pickCoin(int x, int y) {
        lvl.setAir(y, x);
        player.setScores(player.getScores() + Coin.getValue());
        scoreBarPanel.addPoints();
    }

    /**
     * Metoda realizuje zebranie serduszka przez bohatera. Obiekt klasy Heart jest zastępowany przez obiekt klasy Air.
     * Dodatkowe życie jest dodawane do ogólnej liczby żyć gracza. Jeżeli jest ona maksymalna doliczane są dodatkowe punkty.
     * @param x współrzędna x zebranego serduszka
     * @param y współrzędna y zebranego serduszka
     */
    private void pickHeart(int x, int y) {
        lvl.setAir(y, x);
        if (player.getHeroHp() < 5) {
            player.addHp();
            scoreBarPanel.changeHp();
        } else {
            player.setScores(player.getScores() + Heart.getValue());
            scoreBarPanel.addPoints();
        }
    }

    /**
     * Metoda realizuje śmierć bohatera.
     */
    private void killPlayer() {
        Dimension dim = this.getSize();
        if (current) {
            player.reduceHp();
            if (player.getHeroHp() == 0) {
                SwingUtilities.invokeLater(() -> frame.setVisible(false));
                lvl.resetMap();
                current = false;
                new MessageWindow("You Lost", player);
            } else {
                lvl.setAir(lvl.getXStartPosition(), lvl.getYStartPosition());
                scoreBarPanel.changeHp();
                xHeroPos = lvl.getXStartPosition() * dim.width / InputData.mapWidth;
                yHeroPos = lvl.getYStartPosition() * dim.height / InputData.mapHeight;
            }
        }
    }

    /**
     * Metoda odpowiada za przejścia na kolejne poziomy.
     */
    private void changeLvl() {
        int finalScore = player.getScores() + score;
        player.setScores(finalScore);
        if (lvl.getLevelNr() == InputData.nrOfLevels) {
            finalScore = finalScore + player.getHeroHp() * Heart.getValue();
            player.setScores(finalScore);
            SwingUtilities.invokeLater(() -> frame.setVisible(false));
            lvl.resetMap();
            current = false;
            new MessageWindow("You Won!", player);
        } else {
            int lvlIndex = lvl.getLevelNr();
            lvl.resetMap();
            lvl = new Level(lvlIndex + 1,InputData.levelPaths.get(lvlIndex));
            score = InputData.timeScorePerLevel;
            fillMonsterList();
            lvl.resetMap();
            scoreBarPanel.changeLvlLabel();
            scoreBarPanel.addPoints();
            loaded = false;
        }
    }

    /**
     * Metoda odpowiedzialna za rysowanie.
     * @param g element klasy graficznej
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension dim = this.getSize();
        if (dim.width > InputData.mapWidth && dim.height > InputData.mapHeight) {

            g.setColor(InputData.backgroundColor);
            g.fillRect(0, 0, dim.width, dim.height);
            int x = dim.width / InputData.mapWidth;
            int y = dim.height / InputData.mapHeight;
            if (oldX != 0 && oldY != 0) {
                xHeroPos = xHeroPos * x / oldX;
                yHeroPos = yHeroPos * y / oldY;
                normalXSpeed = normalXSpeed * x / oldX;
                normalYSpeed = normalYSpeed * y / oldY;
                Monster.changeVelocity(Monster.getVelocity() * y / oldY);
            }
            oldX = x;
            oldY = y;
            int currentX = 0;
            int currentY = 0;

            for (Object[] i : lvl.getMap()) {
                for (Object j : i) {
                    if (j.getClass() == Stone.class) {
                        Stone stone = (Stone) j;
                        g.drawImage(stone.getResizedImage(x, y), currentX, currentY, null);
                    }
                    if (j.getClass() == Wall.class) {
                        Wall wall = (Wall) j;
                        g.drawImage(wall.getResizedImage(x, y), currentX, currentY, null);
                    }
                    if (j.getClass() == Portal.class) {
                        Portal portal = (Portal) j;
                        g.drawImage(portal.getResizedImage(x, y), currentX, currentY, null);
                    }
                    if (j.getClass() == Coin.class) {
                        Coin coin = (Coin) j;
                        g.drawImage(coin.getResizedImage(x, y), currentX, currentY, null);
                    }
                    if (j.getClass() == Heart.class) {
                        Heart heart = (Heart) j;
                        g.drawImage(heart.getResizedImage(x, y), currentX, currentY, null);
                    }
                    if (j.getClass() == Air.class) {
                        Air air = (Air) j;
                        g.drawImage(air.getResizedImage(x, y), currentX, currentY, null);
                    }
                    if (j.getClass() == Bomb.class) {
                        Bomb bomb = (Bomb) j;
                        if (bomb.getTimeToExplode() > 0)
                            g.drawImage(bomb.getResizedImage(x, y), currentX, currentY, null);
                        else {
                            lvl.bombExplode(currentY / y, currentX / x);
                        }
                    }
                    if (j.getClass() == Fire.class) {
                        Fire fire = (Fire) j;
                        if (fire.getTimeToDisappear() > 0) {
                            g.drawImage(fire.getResizedImage(x, y), currentX, currentY, null);
                        } else {
                            if (fire.getPortalInformation()) {
                                lvl.setPortal(currentY / y, currentX / x);
                            } else if (fire.getCoinInformation()) {
                                lvl.setCoin(currentY / y, currentX / x);
                            } else if (fire.getHeartInformation()) {
                                lvl.setHeart(currentY / y, currentX / x);
                            } else {
                                lvl.setAir(currentY / y, currentX / x);
                            }
                        }
                    }
                    currentX += x;
                }
                currentX = 0;
                currentY += y;
            }
            if (!loaded) {
                int scalingWidthFactor = dim.width / InputData.mapWidth;
                int scalingHeightFactor = dim.height / InputData.mapHeight;
                xHeroPos = lvl.getXStartPosition() * scalingWidthFactor;
                yHeroPos = lvl.getYStartPosition() * scalingHeightFactor;
                loaded = true;
                if (lvl.getMonsterXPosList().size() > 0) {
                    for (Monster m : monsters) {
                        m.changePosition(m.getXMapPos() * scalingWidthFactor, m.getYMapPos() * scalingHeightFactor);
                    }
                }
            }
            if (lvl.getMonsterXPosList().size() > 0) {
                for (Monster m : monsters) {
                    g.drawImage(m.getResizedImage(x, y), m.getXPos(), m.getYPos(), null);
                }
            }
            g.drawImage(player.getChosenHero().getResizedImage(x, y), xHeroPos, yHeroPos, null);
        }

    }

    /**
     * Metoda odpowiadająca za obsługę zdarzeń.
     * @param e zdarzenie
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (current) {
            Dimension dim = this.getSize();
            if (dim.width > InputData.mapWidth && dim.height > InputData.mapHeight) {
                int x = dim.width / InputData.mapWidth;
                int y = dim.height / InputData.mapHeight;
                if (!paused) {
                    if (xVel < 0 && yVel == 0) {
                        if (availableSpace.contains(lvl.getMap()
                                [yHeroPos / (this.getSize().height / InputData.mapHeight)]
                                [(xHeroPos + xVel) / (this.getSize().width / InputData.mapWidth)]
                                .getClass())) {
                            xHeroPos = xHeroPos + xVel;
                        } else {
                            int temp = (xHeroPos / (this.getSize().width / InputData.mapWidth));
                            xHeroPos = temp * (this.getSize().width / InputData.mapWidth);
                        }
                    } else if (xVel > 0 && yVel == 0) {
                        if (availableSpace.contains(lvl.getMap()
                                [yHeroPos / (this.getSize().height / InputData.mapHeight)]
                                [(int) Math.ceil((float) (xHeroPos + xVel) / (float) (this.getSize().width / InputData.mapWidth))]
                                .getClass())) {
                            xHeroPos = xHeroPos + xVel;
                        } else {
                            int temp = (int) Math.ceil((float) xHeroPos / (float) (this.getSize().width / InputData.mapWidth));
                            xHeroPos = temp * (this.getSize().width / InputData.mapWidth);
                        }
                    }

                    if (xVel == 0 && yVel < 0) {
                        if (availableSpace.contains(lvl.getMap()
                                [(yHeroPos + yVel) / (this.getSize().height / InputData.mapHeight)]
                                [xHeroPos / (this.getSize().width / InputData.mapWidth)]
                                .getClass())) {
                            yHeroPos = yHeroPos + yVel;
                        } else {
                            int temp = (yHeroPos / (this.getSize().height / InputData.mapHeight));
                            yHeroPos = temp * (this.getSize().height / InputData.mapHeight);

                        }
                    } else if (xVel == 0 && yVel > 0) {
                        if (availableSpace.contains(lvl.getMap()
                                [(int) Math.ceil((float) (yHeroPos + yVel) / (float) (this.getSize().height / InputData.mapHeight))]
                                [xHeroPos / (this.getSize().width / InputData.mapWidth)]
                                .getClass())) {
                            yHeroPos = yHeroPos + yVel;
                        } else {
                            int temp = (int) Math.ceil((float) yHeroPos / (float) (this.getSize().height / InputData.mapHeight));
                            yHeroPos = temp * (this.getSize().height / InputData.mapHeight);
                        }
                    }
                    if (lvl.getMap()
                            [(int) Math.ceil((float) (yHeroPos) / (float) (this.getSize().height / InputData.mapHeight))]
                            [xHeroPos / (this.getSize().width / InputData.mapWidth)]
                            .getClass() == Portal.class ||
                            lvl.getMap()
                                    [yHeroPos / (this.getSize().height / InputData.mapHeight)]
                                    [(int) Math.ceil((float) (xHeroPos) / (float) (this.getSize().width / InputData.mapWidth))]
                                    .getClass() == Portal.class ||
                            lvl.getMap()
                                    [(int) Math.ceil((float) (yHeroPos) / (float) (this.getSize().height / InputData.mapHeight))]
                                    [(int) Math.ceil((float) (xHeroPos) / (float) (this.getSize().width / InputData.mapWidth))]
                                    .getClass() == Portal.class ||
                            lvl.getMap()
                                    [yHeroPos / (this.getSize().height / InputData.mapHeight)]
                                    [xHeroPos / (this.getSize().width / InputData.mapWidth)]
                                    .getClass() == Portal.class) {
                        changeLvl();
                    }


                    if (lvl.getMap()
                            [(int) Math.ceil((float) (yHeroPos) / (float) (this.getSize().height / InputData.mapHeight))]
                            [xHeroPos / (this.getSize().width / InputData.mapWidth)]
                            .getClass() == Coin.class) {
                        pickCoin(xHeroPos / (this.getSize().width / InputData.mapWidth),
                                (int) Math.ceil((float) (yHeroPos) / (float) (this.getSize().height / InputData.mapHeight)));
                    }
                    if (lvl.getMap()
                            [yHeroPos / (this.getSize().height / InputData.mapHeight)]
                            [(int) Math.ceil((float) (xHeroPos) / (float) (this.getSize().width / InputData.mapWidth))]
                            .getClass() == Coin.class) {
                        pickCoin((int) Math.ceil((float) (xHeroPos) / (float) (this.getSize().width / InputData.mapWidth)),
                                yHeroPos / (this.getSize().height / InputData.mapHeight));
                    }
                    if (lvl.getMap()
                            [(int) Math.ceil((float) (yHeroPos) / (float) (this.getSize().height / InputData.mapHeight))]
                            [(int) Math.ceil((float) (xHeroPos) / (float) (this.getSize().width / InputData.mapWidth))]
                            .getClass() == Coin.class) {
                        pickCoin((int) Math.ceil((float) (xHeroPos) / (float) (this.getSize().width / InputData.mapWidth)),
                                (int) Math.ceil((float) (yHeroPos) / (float) (this.getSize().height / InputData.mapHeight)));
                    }
                    if (lvl.getMap()
                            [yHeroPos / (this.getSize().height / InputData.mapHeight)]
                            [xHeroPos / (this.getSize().width / InputData.mapWidth)]
                            .getClass() == Coin.class) {
                        pickCoin(xHeroPos / (this.getSize().width / InputData.mapWidth),
                                yHeroPos / (this.getSize().height / InputData.mapHeight));
                    }


                    if (lvl.getMap()
                            [(int) Math.ceil((float) (yHeroPos) / (float) (this.getSize().height / InputData.mapHeight))]
                            [xHeroPos / (this.getSize().width / InputData.mapWidth)]
                            .getClass() == Heart.class) {
                        pickHeart(xHeroPos / (this.getSize().width / InputData.mapWidth),
                                (int) Math.ceil((float) (yHeroPos) / (float) (this.getSize().height / InputData.mapHeight)));
                    }
                    if (lvl.getMap()
                            [yHeroPos / (this.getSize().height / InputData.mapHeight)]
                            [(int) Math.ceil((float) (xHeroPos) / (float) (this.getSize().width / InputData.mapWidth))]
                            .getClass() == Heart.class) {
                        pickHeart((int) Math.ceil((float) (xHeroPos) / (float) (this.getSize().width / InputData.mapWidth)),
                                yHeroPos / (this.getSize().height / InputData.mapHeight));
                    }
                    if (lvl.getMap()
                            [(int) Math.ceil((float) (yHeroPos) / (float) (this.getSize().height / InputData.mapHeight))]
                            [(int) Math.ceil((float) (xHeroPos) / (float) (this.getSize().width / InputData.mapWidth))]
                            .getClass() == Heart.class) {
                        pickHeart((int) Math.ceil((float) (xHeroPos) / (float) (this.getSize().width / InputData.mapWidth)),
                                (int) Math.ceil((float) (yHeroPos) / (float) (this.getSize().height / InputData.mapHeight)));
                    }
                    if (lvl.getMap()
                            [yHeroPos / (this.getSize().height / InputData.mapHeight)]
                            [xHeroPos / (this.getSize().width / InputData.mapWidth)]
                            .getClass() == Heart.class) {
                        pickHeart(xHeroPos / (this.getSize().width / InputData.mapWidth),
                                yHeroPos / (this.getSize().height / InputData.mapHeight));
                    }


                    if (lvl.getMap()
                            [(int) Math.ceil((float) (yHeroPos) / (float) (this.getSize().height / InputData.mapHeight))]
                            [xHeroPos / (this.getSize().width / InputData.mapWidth)]
                            .getClass() == Fire.class ||
                            lvl.getMap()
                                    [yHeroPos / (this.getSize().height / InputData.mapHeight)]
                                    [(int) Math.ceil((float) (xHeroPos) / (float) (this.getSize().width / InputData.mapWidth))]
                                    .getClass() == Fire.class ||
                            lvl.getMap()
                                    [(int) Math.ceil((float) (yHeroPos) / (float) (this.getSize().height / InputData.mapHeight))]
                                    [(int) Math.ceil((float) (xHeroPos) / (float) (this.getSize().width / InputData.mapWidth))]
                                    .getClass() == Fire.class ||
                            lvl.getMap()
                                    [yHeroPos / (this.getSize().height / InputData.mapHeight)]
                                    [xHeroPos / (this.getSize().width / InputData.mapWidth)]
                                    .getClass() == Fire.class) {
                        killPlayer();
                    }
                }
                if (oldX2 != 0 && oldY2 != 0) {
                    if (lvl.getMonsterXPosList().size() > 0) {
                        for (Monster m : monsters) {
                            m.changePosition(m.getXPos() * x / oldX2, m.getYPos() * y / oldY2);
                        }
                    }
                }
                if (!paused) {
                    if (lvl.getMonsterXPosList().size() > 0) {
                        for (Monster m : monsters) {
                            if (m.isAlive()) {
                                if (m.getDirection() > 0) {
                                    if (availableSpace.contains(lvl.getMap()
                                            [(int) Math.ceil((m.getYPos() + Monster.getVelocity()) / (float) (this.getSize().height / InputData.mapHeight))]
                                            [m.getXPos() / (this.getSize().width / InputData.mapWidth)]
                                            .getClass())) {
                                        m.changeYPos(m.getYPos() + (int) Monster.getVelocity());
                                    } else {
                                        int temp = (int) Math.ceil((float) m.getYPos() / (float) (this.getSize().height / InputData.mapHeight));
                                        m.changeYPos(temp * (this.getSize().height / InputData.mapHeight));
                                        m.changeDirection();
                                    }

                                }
                                if (m.getDirection() < 0) {
                                    if (availableSpace.contains(lvl.getMap()
                                            [(int) (m.getYPos() - Monster.getVelocity()) / (this.getSize().height / InputData.mapHeight)]
                                            [m.getXPos() / (this.getSize().width / InputData.mapWidth)]
                                            .getClass())) {
                                        m.changeYPos(m.getYPos() - (int) Monster.getVelocity());
                                    } else {
                                        int temp = (m.getYPos() / (this.getSize().height / InputData.mapHeight));
                                        m.changeYPos((temp) * (this.getSize().height / InputData.mapHeight));
                                        m.changeDirection();
                                    }
                                }
                                if (lvl.getMap()
                                        [(int) Math.ceil((float) (m.getYPos()) / (float) (this.getSize().height / InputData.mapHeight))]
                                        [m.getXPos() / (this.getSize().width / InputData.mapWidth)]
                                        .getClass() == Fire.class ||
                                        lvl.getMap()
                                                [m.getYPos() / (this.getSize().height / InputData.mapHeight)]
                                                [(int) Math.ceil((float) (m.getXPos()) / (float) (this.getSize().width / InputData.mapWidth))]
                                                .getClass() == Fire.class ||
                                        lvl.getMap()
                                                [(int) Math.ceil((float) (m.getYPos()) / (float) (this.getSize().height / InputData.mapHeight))]
                                                [(int) Math.ceil((float) (m.getXPos()) / (float) (this.getSize().width / InputData.mapWidth))]
                                                .getClass() == Fire.class ||
                                        lvl.getMap()
                                                [m.getYPos() / (this.getSize().height / InputData.mapHeight)]
                                                [m.getXPos() / (this.getSize().width / InputData.mapWidth)]
                                                .getClass() == Fire.class) {
                                    m.killMonster();
                                    player.setScores(player.getScores() + InputData.pointsPerMonster);
                                    scoreBarPanel.addPoints();
                                }
                            }
                        }
                    }
                }
                int endOfXHeroPos = xHeroPos + x;
                int endOfYHeroPos = yHeroPos + y;
                for (Monster m : monsters) {
                    if (m.isAlive()) {
                        int endOfXMonsterPos = m.getXPos() + x - 1;
                        int endOfYMonsterPos = m.getYPos() + y - 1;
                        if ((xHeroPos <= (m.getXPos() + 1) && endOfXHeroPos >= (m.getXPos()+1) &&
                                yHeroPos <= (m.getYPos() + 1) && endOfYHeroPos >= (m.getYPos()+1)) ||
                                (xHeroPos <= endOfXMonsterPos && endOfXHeroPos >= endOfXMonsterPos &&
                                        yHeroPos <= (m.getYPos()+1) && endOfYHeroPos >= (m.getYPos()+1)) ||
                                (xHeroPos <= (m.getXPos()+1) && endOfXHeroPos >= (m.getXPos()+1) &&
                                        yHeroPos <= endOfYMonsterPos && endOfYHeroPos >= endOfYMonsterPos) ||
                                (xHeroPos <= endOfXMonsterPos && endOfXHeroPos >= endOfXMonsterPos &&
                                        yHeroPos <= endOfYMonsterPos && endOfYHeroPos >= endOfYMonsterPos)) {
                            killPlayer();
                        }
                    }
                }
                oldX2 = x;
                oldY2 = y;
                repaint();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Metoda odpowiada za poruszanie się bohatera.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        double xDownPos = Math.floor((float) xHeroPos / (float) (this.getSize().width / InputData.mapWidth));
        double xRealPos = (double) xHeroPos / (float) (this.getSize().width / InputData.mapWidth);
        double yDownPos = Math.floor((float) yHeroPos / (float) (this.getSize().height / InputData.mapHeight));
        double yRealPos = (double) yHeroPos / (float) (this.getSize().height / InputData.mapHeight);
        int c = e.getKeyCode();
        if (!paused) {
            if (c == KeyEvent.VK_RIGHT) {
                if (availableSpace.contains(lvl.getMap()
                        [yHeroPos / (this.getSize().height / InputData.mapHeight)]
                        [xHeroPos / (this.getSize().width / InputData.mapWidth) + 1]
                        .getClass())) {
                    if (availableSpace.contains(lvl.getMap()
                            [(int) Math.ceil((float) yHeroPos / (float) (this.getSize().height / InputData.mapHeight))]
                            [xHeroPos / (this.getSize().width / InputData.mapWidth) + 1]
                            .getClass())) {
                        xVel = (int) normalXSpeed;
                        yVel = 0;
                    } else {

                        if (yRealPos - yDownPos <= 0.2) {
                            yHeroPos = (int) yDownPos * (this.getSize().height / InputData.mapHeight);
                            xVel = (int) normalXSpeed;
                            yVel = 0;
                        }
                    }
                } else {
                    if (availableSpace.contains(lvl.getMap()
                            [(int) Math.ceil((float) yHeroPos / (float) (this.getSize().height / InputData.mapHeight))]
                            [xHeroPos / (this.getSize().width / InputData.mapWidth) + 1]
                            .getClass()) && yRealPos - yDownPos >= 0.8) {
                        yHeroPos = (int) (yDownPos + 1) * (this.getSize().height / InputData.mapHeight);
                        xVel = (int) normalXSpeed;
                        yVel = 0;
                    } else {
                        int temp = (xHeroPos / (this.getSize().width / InputData.mapWidth));
                        xHeroPos = temp * (this.getSize().width / InputData.mapWidth);
                        xVel = 0;
                    }
                }
            }
            if (c == KeyEvent.VK_LEFT) {
                if (availableSpace.contains(lvl.getMap()
                        [yHeroPos / (this.getSize().height / InputData.mapHeight)]
                        [(int) Math.ceil((float) xHeroPos / (float) (this.getSize().width / InputData.mapWidth)) - 1]
                        .getClass())) {
                    if (availableSpace.contains(lvl.getMap()
                            [(int) Math.ceil((float) yHeroPos / (float) (this.getSize().height / InputData.mapHeight))]
                            [(int) Math.ceil((float) xHeroPos / (float) (this.getSize().width / InputData.mapWidth)) + 1]
                            .getClass())) {
                        xVel = (int) -normalXSpeed;
                        yVel = 0;
                    } else {

                        if (yRealPos - yDownPos <= 0.2) {
                            yHeroPos = (int) yDownPos * (this.getSize().height / InputData.mapHeight);
                            xVel = (int) -normalXSpeed;
                            yVel = 0;
                        }
                    }
                } else {
                    if (availableSpace.contains(lvl.getMap()
                            [(int) Math.ceil((float) yHeroPos / (float) (this.getSize().height / InputData.mapHeight))]
                            [(int) Math.ceil((float) xHeroPos / (float) (this.getSize().width / InputData.mapWidth)) - 1]
                            .getClass()) && yRealPos - yDownPos >= 0.8) {
                        yHeroPos = (int) (yDownPos + 1) * (this.getSize().height / InputData.mapHeight);
                        xVel = (int) -normalXSpeed;
                        yVel = 0;
                    } else {
                        int temp = (int) Math.ceil((float) xHeroPos / (float) (this.getSize().width / InputData.mapWidth));
                        xHeroPos = temp * (this.getSize().width / InputData.mapWidth);
                        xVel = 0;
                    }
                }
            }

            if (c == KeyEvent.VK_UP) {
                if (availableSpace.contains(lvl.getMap()
                        [(int) Math.ceil((float) yHeroPos / (float) (this.getSize().height / InputData.mapHeight)) - 1]
                        [xHeroPos / (this.getSize().width / InputData.mapWidth)]
                        .getClass())) {
                    if (availableSpace.contains(lvl.getMap()
                            [(int) Math.ceil((float) yHeroPos / (float) (this.getSize().height / InputData.mapHeight)) - 1]
                            [(int) Math.ceil((float) xHeroPos / (float) (this.getSize().width / InputData.mapWidth))]
                            .getClass())) {
                        xVel = 0;
                        yVel = (int) -normalYSpeed;
                    } else {
                        if (xRealPos - xDownPos <= 0.2) {
                            xHeroPos = (int) xDownPos * (this.getSize().width / InputData.mapWidth);
                            xVel = 0;
                            yVel = (int) -normalYSpeed;
                        }
                    }
                } else {
                    if (availableSpace.contains(lvl.getMap()
                            [(int) Math.ceil((float) yHeroPos / (float) (this.getSize().height / InputData.mapHeight))]
                            [(int) Math.floor((float) xHeroPos / (float) (this.getSize().width / InputData.mapWidth))]
                            .getClass()) && xRealPos - xDownPos >= 0.8) {
                        xHeroPos = (int) (xDownPos + 1) * (this.getSize().width / InputData.mapWidth);
                        xVel = 0;
                        yVel = (int) -normalYSpeed;
                    } else {
                        int temp = (int) Math.ceil((float) yHeroPos / (float) (this.getSize().height / InputData.mapHeight));
                        yHeroPos = temp * (this.getSize().height / InputData.mapHeight);
                        yVel = 0;
                    }
                }
            }
            if (c == KeyEvent.VK_DOWN) {
                if (availableSpace.contains(lvl.getMap()
                        [yHeroPos / (this.getSize().height / InputData.mapHeight) + 1]
                        [xHeroPos / (this.getSize().width / InputData.mapWidth)]
                        .getClass())) {
                    if (availableSpace.contains(lvl.getMap()
                            [yHeroPos / (this.getSize().height / InputData.mapHeight) + 1]
                            [(int) Math.ceil((float) xHeroPos / (float) (this.getSize().width / InputData.mapWidth))]
                            .getClass())) {
                        xVel = 0;
                        yVel = (int) normalYSpeed;
                    } else {
                        if (xRealPos - xDownPos <= 0.2) {
                            xHeroPos = (int) xDownPos * (this.getSize().width / InputData.mapWidth);
                            xVel = 0;
                            yVel = (int) normalYSpeed;
                        }
                    }
                } else {
                    if (availableSpace.contains(lvl.getMap()
                            [yHeroPos / (this.getSize().height / InputData.mapHeight)]
                            [(int) Math.ceil((float) xHeroPos / (float) (this.getSize().width / InputData.mapWidth))]
                            .getClass()) && xRealPos - xDownPos >= 0.8) {
                        xHeroPos = (int) (xDownPos + 1) * (this.getSize().width / InputData.mapWidth);
                        xVel = 0;
                        yVel = (int) normalYSpeed;
                    } else {
                        int temp = (yHeroPos / (this.getSize().height / InputData.mapHeight));
                        yHeroPos = temp * (this.getSize().height / InputData.mapHeight);
                        yVel = 0;
                    }
                }
            }
            if (c == KeyEvent.VK_SPACE) {
                putBomb();
            }
        }
        if (c == KeyEvent.VK_ESCAPE) {
            paused = !paused;
            Fire.pause();
            Bomb.pause();
            scoreBarPanel.pause();
            System.out.println("Paused: " + paused);
        }
    }

    /**
     * Metoda odpowiada za obsługę zwolnienia przycisku.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int c = e.getKeyCode();
        if (c == KeyEvent.VK_LEFT || c == KeyEvent.VK_RIGHT) xVel = 0;
        if (c == KeyEvent.VK_UP || c == KeyEvent.VK_DOWN) yVel = 0;
    }

    /**
     * Metoda odpowiada za postawienie bomby przez bohatera.
     */
    private void putBomb() {
        double xDownPos = Math.floor((float) xHeroPos / (float) (this.getSize().width / InputData.mapWidth));
        double xRealPos = (double) xHeroPos / (float) (this.getSize().width / InputData.mapWidth);
        double yDownPos = Math.floor((float) yHeroPos / (float) (this.getSize().height / InputData.mapHeight));
        double yRealPos = (double) yHeroPos / (float) (this.getSize().height / InputData.mapHeight);
        if (player.getTimeToPutBomb() == 0) {
            if (xRealPos - xDownPos <= 0.5 && yRealPos - yDownPos <= 0.5) {
                lvl.setBomb((int) yDownPos, (int) xDownPos);
            } else if (xRealPos - xDownPos > 0.5 && yRealPos - yDownPos <= 0.5) {
                lvl.setBomb((int) yDownPos, (int) Math.ceil(xRealPos));
            } else if (xRealPos - xDownPos <= 0.5 && yRealPos - yDownPos > 0.5) {
                lvl.setBomb((int) Math.ceil(yRealPos), (int) xDownPos);
            } else {
                lvl.setBomb((int) Math.ceil(yRealPos), (int) Math.ceil(xRealPos));
            }
            player.putBomb();
        }
    }

    /**
     * Klasa zawierająca statystyki gracza i stanu gry. Odpowiada również za redukcję punktów wraz z upływem czasu.
     */
    class ScoreBarPanel extends StatsPanel implements ActionListener {
        private JButton cancelButton;
        private Image heartEmptyResized = heartEmptyImage.getImage().getScaledInstance(40, 40, Image.SCALE_AREA_AVERAGING);
        private Image heartFullResized = heartFullImage.getImage().getScaledInstance(40, 40, Image.SCALE_AREA_AVERAGING);
        private ImageIcon heartEmptyResizedIcon = new ImageIcon(heartEmptyResized);
        private ImageIcon heartFullResizedIcon = new ImageIcon(heartFullResized);

        private Font specialFont = new Font("specialFont", Font.BOLD, 20);

        private JLabel lvlLabel = new JLabel("LEVEL:");
        private JLabel lvlNrLabel = new JLabel(String.valueOf(lvl.getLevelNr()));

        private JLabel scoreLabel = new JLabel("SCORE:");
        private JLabel scorePointsLabel = new JLabel(String.valueOf(score));

        private JLabel totalScoreLabel = new JLabel("YOUR SCORE:");
        private JLabel totalScorePointsLabel = new JLabel("0");

        private JLabel pausedLabel = new JLabel("Paused:");
        private JLabel isPausedLabel = new JLabel(String.valueOf(paused));
        private JLabel hpLabel = new JLabel("HP:");
        private JLabel heart1Label = new JLabel(heartEmptyResizedIcon);
        private JLabel heart2Label = new JLabel(heartEmptyResizedIcon);
        private JLabel heart3Label = new JLabel(heartEmptyResizedIcon);
        private JLabel heart4Label = new JLabel(heartEmptyResizedIcon);
        private JLabel heart5Label = new JLabel(heartEmptyResizedIcon);


        public ScoreBarPanel() {
            java.util.Timer timer = new java.util.Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (score > 0) {
                        if (!paused) {
                            score -= InputData.scoreLostPerSec;
                            scorePointsLabel.setText(String.valueOf(score));
                        }
                    }
                }
            };
            timer.scheduleAtFixedRate(task, InputData.defaultTickRate, InputData.defaultTickRate);
            this.changeStats(player.getChosenHero().getHp(), heart1Label, heart2Label, heart3Label, heart4Label, heart5Label, heartFullResizedIcon, heartEmptyResizedIcon);
            cancelButton = new JButton("Cancel");
            cancelButton.setFont(specialFont);
            lvlLabel.setFont(specialFont);
            lvlNrLabel.setFont(specialFont);
            scoreLabel.setFont(specialFont);
            scorePointsLabel.setFont(specialFont);
            totalScoreLabel.setFont(specialFont);
            totalScorePointsLabel.setFont(specialFont);
            hpLabel.setFont(specialFont);
            pausedLabel.setFont(specialFont);
            isPausedLabel.setFont(specialFont);
            Dimension dim = new Dimension();
            dim.width = 300;
            this.setPreferredSize(dim);
            this.setBackground(Color.lightGray);
            this.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 0, 20, 0);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weighty = 1;
            gbc.weightx = 2;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            this.add(hpLabel, gbc);
            gbc.weightx = 1;
            gbc.anchor = GridBagConstraints.NORTHEAST;
            gbc.gridx = 1;
            this.add(heart1Label, gbc);
            gbc.gridx = 2;
            this.add(heart2Label, gbc);
            gbc.gridx = 3;
            this.add(heart3Label, gbc);
            gbc.gridx = 4;
            this.add(heart4Label, gbc);
            gbc.gridx = 5;
            this.add(heart5Label, gbc);

            gbc.gridwidth = 3;
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            this.add(scoreLabel, gbc);
            gbc.anchor = GridBagConstraints.NORTHEAST;
            gbc.gridx = 5;
            this.add(scorePointsLabel, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            this.add(totalScoreLabel, gbc);
            gbc.anchor = GridBagConstraints.NORTHEAST;
            gbc.gridx = 5;
            this.add(totalScorePointsLabel, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            this.add(lvlLabel, gbc);
            gbc.anchor = GridBagConstraints.NORTHEAST;
            gbc.gridx = 5;
            this.add(lvlNrLabel, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            this.add(pausedLabel, gbc);
            gbc.gridx = 5;
            gbc.anchor = GridBagConstraints.NORTHEAST;
            this.add(isPausedLabel, gbc);

            gbc.anchor = GridBagConstraints.SOUTHEAST;
            gbc.gridx = 1;
            gbc.gridy = 5;
            this.add(cancelButton, gbc);
            cancelButton.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> frame.setVisible(false));
            current = false;
            lvl.resetMap();
            new StartWindow();
        }

        /**
         * Metoda odpowiada za zmianę wyświetlanych punktów życia.
         */
        public void changeHp() {
            this.changeStats(player.getHeroHp(), heart1Label, heart2Label, heart3Label, heart4Label, heart5Label, heartFullResizedIcon, heartEmptyResizedIcon);
        }

        /**
         * Metoda odpowiada za zmianę wyświetlanych punktów.
         */
        public void addPoints() {
            totalScorePointsLabel.setText(String.valueOf(player.getScores()));
        }

        /**
         * Metoda odpowiada za zmianę wyświetlania numeru poziomu.
         */
        public void changeLvlLabel() {
            lvlNrLabel.setText(String.valueOf(lvl.getLevelNr()));
        }

        /**
         * Metoda odpowiada za zmianę wyświetlania stanu gry.
         */
        public void pause() {
            isPausedLabel.setText(String.valueOf(paused));
        }

    }

}

