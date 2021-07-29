import java.awt.EventQueue;
import java.util.List;

/**
 * Klasa główna, w której znajduje się funkcja main.
 */
class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                InputData.read();

                new IOPlayersData();

                new ServerConnectionWindow();
            }
        });
    }
}