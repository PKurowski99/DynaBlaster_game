import java.io.*;
import java.util.Collections;
import java.util.Vector;

/**
 * Klasa odpowiadająca za operacje odczytu z pliku i zapisu danych graczy (nazwa, uzyskany wynik) do pliku.
 */
public class IOPlayersData {
    /**
     * vector graczy.
     */
    public static Vector<Player> players;
    /**
     * pomocniczy vector graczy wczytanych z pliku
     */
    public static Vector<Player> fromFile;

    public IOPlayersData(){
        players = new Vector<Player>();
        fromFile = new Vector<Player>();

        IOPlayersData.readPlayers();
        players = fromFile;
    }

    /**
     * Funkcja odpowiadająca za zapis danych graczy do pliku.
     */
    public static void save(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("highScores.txt"));
            outputStream.writeObject(players);
            outputStream.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Funkcja odpowiadająca za odczyt danych graczy z pliku.
     */
    public static void readPlayers(){
        File f = new File("highScores.txt");
        if (f.isFile()) {
            long size = f.length();
            if (size != 0) {
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("highScores.txt"));
                    if(inputStream.available()== 0){
                        fromFile = (Vector<Player>) inputStream.readObject();
                    }
                    inputStream.close();
                }
                catch (IOException | ClassNotFoundException e){
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * Funkcja sortująca graczy w vectorze w zależności od liczby punktów.
     */
    public static void vectorSort(){
        ScoresComparator scoresComparator = new ScoresComparator();
        Collections.sort(players,scoresComparator);
    }
    public  static  void clearAll(){
        players.clear();
        fromFile.clear();
    }
}
