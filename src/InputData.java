import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa odpowiadająca za odczyt danych z pliku konfiguracyjnego oraz komunikację z serwerem.
 */
public class InputData {
    public static int helpFrameWidth;
    public static int helpFrameHeight;
    public static int startFrameWidth;
    public static int startFrameHeight;
    public static int loginFrameWidth;
    public static int loginFrameHeight;
    public static int highScoresFrameWidth;
    public static int highScoresFrameHeight;
    public static int maxRowsNumber;
    public static int rulesWidth;
    public static int rulesHeight;
    public static int heroSelectFrameWidth;
    public static int heroSelectFrameHeight;
    public static int gameFrameWidth;
    public static int gameFrameHeight;
    public static int fontSize;
    public static int mapWidth;
    public static int mapHeight;
    public static String rules;
    public static String controlsPath;
    public static String spacePath;
    public static String logoPath;
    public static String escPath;
    public static Color backgroundColor;
    public static String authors;
    public static String heartFullImagePath;
    public static String heartEmptyImagePath;
    public static String starFullImagePath;
    public static String starEmptyImagePath;
    public static String bombFullImagePath;
    public static String bombEmptyImagePath;
    public static String nextButtonImagePath;
    public static String previousButtonImagePath;
    public static String heroImagePath;
    public static String heroineImagePath;
    public static String heroDescription;
    public static String heroineDescription;
    public static int heroHp;
    public static int heroineHp;
    public static int heroSpeed;
    public static int heroineSpeed;
    public static int heroFireRate;
    public static int heroineFireRate;
    public static String stoneImagePath;
    public static String wallImagePath;
    public static String airImagePath;
    public static String bombImagePath;
    public static String redBombImagePath;
    public static int bombTimeToExplode;
    public static String fire1ImagePath;
    public static String fire2ImagePath;
    public static String fire3ImagePath;
    public static String fire4ImagePath;
    public static String fire5ImagePath;
    public static String fire6ImagePath;
    public static String portalImagePath;
    public static int timeScorePerLevel;
    public static int normalSpeed;
    public static int normalFireRate;
    public static int fireTimeToDisappear;
    public static int fireFrames;
    public static int fireFramesTickRate;
    public static int defaultTickRate;
    public static int scoreLostPerSec;
    public static int pointsPerHp;
    public static String monsterImagePath;
    public static String voidImagePath;
    public static int pointsPerMonster;
    public static int pointsPerCoin;
    public static String coinImagePath;
    public static String heartImagePath;
    public static int nrOfLevels;
    public static int monsterSpeed;
    public static boolean serverStatus = false;
    public static java.util.List<String> levelPaths =new java.util.ArrayList<>();


    /**
     * Funkcja odpowiada za odczyt danych z pliku konfiguracyjnego i ich parsowanie.
     */
    public static void read(){
        try (FileReader reader = new FileReader("config.txt")) {
            Properties properties = new Properties();
            properties.load(reader);

            helpFrameWidth = Integer.parseInt(properties.getProperty("helpFrameWidth"));
            helpFrameHeight = Integer.parseInt(properties.getProperty("helpFrameHeight"));

            loginFrameWidth = Integer.parseInt(properties.getProperty("loginFrameWidth"));
            loginFrameHeight = Integer.parseInt(properties.getProperty("loginFrameHeight"));

            highScoresFrameWidth = Integer.parseInt(properties.getProperty("highScoresFrameWidth"));
            highScoresFrameHeight = Integer.parseInt(properties.getProperty("highScoresFrameHeight"));

            maxRowsNumber = Integer.parseInt(properties.getProperty("maxRowsNumber"));

            startFrameWidth = Integer.parseInt(properties.getProperty("startFrameWidth"));
            startFrameHeight = Integer.parseInt(properties.getProperty("startFrameHeight"));

            heroSelectFrameWidth = Integer.parseInt(properties.getProperty("heroSelectFrameWidth"));
            heroSelectFrameHeight = Integer.parseInt(properties.getProperty("heroSelectFrameHeight"));

            gameFrameWidth = Integer.parseInt(properties.getProperty("gameFrameWidth"));
            gameFrameHeight = Integer.parseInt(properties.getProperty("gameFrameHeight"));

            rules = properties.getProperty("rules");

            rulesWidth = Integer.parseInt(properties.getProperty("rulesWidth"));
            rulesHeight = Integer.parseInt(properties.getProperty("rulesHeight"));

            fontSize = Integer.parseInt(properties.getProperty("fontSize"));

            mapWidth = Integer.parseInt(properties.getProperty("mapWidth"));
            mapHeight = Integer.parseInt(properties.getProperty("mapHeight"));

            controlsPath = properties.getProperty("controlsPath");

            spacePath = properties.getProperty("spacePath");
            logoPath = properties.getProperty("logo");
            escPath = properties.getProperty("escPath");

            int r = Integer.parseInt(properties.getProperty("backColorR"));
            int g = Integer.parseInt(properties.getProperty("backColorG"));
            int b = Integer.parseInt(properties.getProperty("backColorB"));
            backgroundColor = new Color(r,g,b);

            authors = properties.getProperty("authors");

            heartFullImagePath = properties.getProperty("heartFullImagePath");
            heartEmptyImagePath = properties.getProperty("heartEmptyImagePath");
            starFullImagePath = properties.getProperty("starFullImagePath");
            starEmptyImagePath = properties.getProperty("starEmptyImagePath");
            bombFullImagePath = properties.getProperty("bombFullImagePath");
            bombEmptyImagePath = properties.getProperty("bombEmptyImagePath");

            nextButtonImagePath = properties.getProperty("nextButtonImagePath");
            previousButtonImagePath = properties.getProperty("previousButtonImagePath");

            heroImagePath = properties.getProperty("heroImagePath");
            heroineImagePath = properties.getProperty("heroineImagePath");

            heroDescription = properties.getProperty("heroDescription");
            heroineDescription = properties.getProperty("heroineDescription");

            monsterImagePath = properties.getProperty("monsterImagePath");
            voidImagePath = properties.getProperty("voidImagePath");

            heroHp = Integer.parseInt(properties.getProperty("heroHp"));
            heroineHp = Integer.parseInt(properties.getProperty("heroineHp"));
            heroSpeed = Integer.parseInt(properties.getProperty("heroSpeed"));
            heroineSpeed = Integer.parseInt(properties.getProperty("heroineSpeed"));
            heroFireRate = Integer.parseInt(properties.getProperty("heroFireRate"));
            heroineFireRate = Integer.parseInt(properties.getProperty("heroineFireRate"));
            monsterSpeed = Integer.parseInt(properties.getProperty("monsterSpeed"));

            pointsPerMonster = Integer.parseInt(properties.getProperty("pointsPerMonster"));
            pointsPerCoin = Integer.parseInt(properties.getProperty("pointsPerCoin"));

            stoneImagePath = properties.getProperty("stoneImagePath");
            wallImagePath = properties.getProperty("wallImagePath");
            airImagePath = properties.getProperty("airImagePath");
            bombImagePath = properties.getProperty("bombImagePath");
            redBombImagePath = properties.getProperty("redBombImagePath");
            fire1ImagePath = properties.getProperty("fire1ImagePath");
            fire2ImagePath = properties.getProperty("fire2ImagePath");
            fire3ImagePath = properties.getProperty("fire3ImagePath");
            fire4ImagePath = properties.getProperty("fire4ImagePath");
            fire5ImagePath = properties.getProperty("fire5ImagePath");
            fire6ImagePath = properties.getProperty("fire6ImagePath");
            bombTimeToExplode = Integer.parseInt(properties.getProperty("bombTimeToExplode"));
            portalImagePath = properties.getProperty("portalImagePath");
            timeScorePerLevel = Integer.parseInt(properties.getProperty("timeScorePerLevel"));
            normalSpeed = Integer.parseInt(properties.getProperty("normalSpeed"));
            normalFireRate = Integer.parseInt(properties.getProperty("normalFireRate"));
            fireFrames = Integer.parseInt(properties.getProperty("fireFrames"));
            fireTimeToDisappear = Integer.parseInt(properties.getProperty("fireTimeToDisappear"));
            fireFramesTickRate = Integer.parseInt(properties.getProperty("fireFramesTickRate"));
            scoreLostPerSec= Integer.parseInt(properties.getProperty("scoreLostPerSec"));
            defaultTickRate= Integer.parseInt(properties.getProperty("defaultTickRate"));
            pointsPerHp= Integer.parseInt(properties.getProperty("pointsPerHp"));
            coinImagePath = properties.getProperty("coinImagePath");
            heartImagePath = properties.getProperty("heartImagePath");
            nrOfLevels = Integer.parseInt(properties.getProperty("nrOfLevels"));
            fillPaths();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Funkcja odpowiedzialna za wysyłanie żadań do serwera oraz ich odbiór i przetwarzanie.
     * @param request żądanie do serwera
     */
    public static void client(String request) {
        try {
            // Otwarcie nowego socketu
            Socket socket = new Socket("localhost", 12345);
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            System.out.print("Sending request\n");
            pw.println(request);
            // Odpowiedz serwera
            InputStream is = socket.getInputStream();
            // Reader ze strumienia
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            // Zmienna pomocnicza do przetrzymywania linii tekstu
            String _line = "";
            // Czyta wszystkie linie do konca
            if(!request.equals("GETSCORES")) {
                while ((_line = br.readLine()) != null) {
                    // Dodaj linie do StringBuffera
                        sb.append(_line);
                        sb.append('\n');
                }
                if (request.equals("GETCONFIG")) {
                    saveFile("config.txt", sb);
                    System.out.print("Imported configuration file from the server successfully.\n");
                } else if (request.matches("(?i)^GETMAP.*")) {
                    sb.setLength(sb.length()-2);
                    StringBuilder sbl = new StringBuilder("level");
                    sbl.append(getLevelIdFromRequest(request));
                    sbl.append(".txt");
                    saveFile(sbl.toString(), sb);
                    System.out.print("Imported level "+getLevelIdFromRequest(request) +" from the server successfully.\n");
                } else if (request.matches("(?i)^PUSH.*")) {
                    System.out.print("Score send.");
                }
            }
            else {
                File f = new File("highScores.txt");
                IOPlayersData.clearAll();
                if(f.exists()){
                    f.delete();
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Scores:");
                String nick = null;
                int score = - 1;
                Pattern p = Pattern.compile("^\"?([A-Za-z0-9]+)\"? +([0-9]+)");
                while ((_line = br.readLine()) != null) {
                    Matcher m = p.matcher(_line);
                    while (m.find()) {
                        nick = m.group(1);
                        score = Integer.parseInt(m.group(2));
                        System.out.println(nick +" "+ score);
                        IOPlayersData.players.add(new Player(nick,score));
                    }
                }
                System.out.println("End of scores.");
                IOPlayersData.save();
            }
            serverStatus = true;
            socket.close();
            br.close();
        } catch (Exception e) {
            System.err.println("Client exception: " + e + ". Can't connect to the server.");
        }
    }

    /**
     * Funkcja nadpisuje plik.
     * @param path ścieżka do pliku
     * @param serverResponse zawartość pliku
     */
    private static void saveFile(String path,StringBuffer serverResponse){
        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(serverResponse.toString());
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    private static void fillPaths(){
        if(!levelPaths.isEmpty()){
            levelPaths.clear();
        }
        for(int i = 0; i<nrOfLevels;i++){
            StringBuilder sb = new StringBuilder("level");
            sb.append((i+1));
            sb.append(".txt");
            levelPaths.add(sb.toString());
            sb.setLength(0);
        }
    }
    private static int getLevelIdFromRequest(String request){
        int lvlId = -1;
        Pattern p = Pattern.compile("^([Gg][Ee][Tt][Mm][Aa][Pp]) +([0-9]+)");
        Matcher m = p.matcher(request);
        while (m.find()) {
            try {
                lvlId = Integer.parseInt(m.group(2));
            } catch(NumberFormatException e) {
                System.out.println("Score number cannot be parsed");
                lvlId = -1;
            }
        }
        return lvlId;
    }
}
