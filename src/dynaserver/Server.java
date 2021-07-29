package dynaserver;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Serwer gry.
 */
public class Server {
    public static void main(String[] args) throws Exception {
        InetAddress localHost = InetAddress.getLocalHost();

        System.out.println("Host address: " + localHost.getHostAddress());
        System.out.println("Hostname: " + localHost.getHostName());

        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(12345);
            System.out.println("Port: " + serverSocket.getLocalPort());
        } catch (Exception e) {
            System.err.println("Create server socket: " + e);
            return;
        }

        System.out.println("Quit with CTRL-C");
        while(true) {
            try {
                // Akceptowanie nadchodzącego połączenia
                Socket socket = serverSocket.accept();
                // Strumień danych z socketu.
                InputStream is = socket.getInputStream();
                // Stream reader
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                String clientRequest = br.readLine();
                RequestType requestType = getTypeFromRequest(clientRequest);

                String response;

                switch (requestType) {
                    case GET_MAP:
                        int lvlId = getLevelIdFromRequest(clientRequest);
                        if (lvlId!=-1) {
                            response = getLevel(lvlId);
                        }
                        else {
                            response = getLevel(1);
                            System.out.println("Invalid lvl id.");
                        }
                        break;
                    case PUSH_SCORE:
                        response = "OK";
                        Score score = getScoreFromRequest(clientRequest);
                        System.out.println("Score: { nick: '" + score.nick + "', score: " + score.score + " }");
                        break;
                    case GET_CONFIG:
                        response = getConfig();
                        break;
                    case GET_SCORES:
                        response = getScores();
                        break;
                    default:
                        response = "Invalid request.";
                }

                // Strumień wyjściowy z serwera
                OutputStream os = socket.getOutputStream();

                PrintWriter pw = new PrintWriter(os, true);
                System.out.println("Client request: '" + clientRequest + "'");
                // Odpowiedź serwera
                pw.print(response);
                pw.println();

                // Zamykanie socketa i strumieni
                socket.close();
                br.close();
                pw.close();
                is.close();
                os.close();
            } catch (Exception e) {
                System.err.println("Server exception: " + e);
            }
        }
    }

    /**
     * Rodzaje żądań obsługiwanych przez serwer
     */
    private enum RequestType {
        GET_CONFIG,
        GET_MAP,
        GET_SCORES,
        PUSH_SCORE,
        INVALID
    }

    public static class Score {
        Score(String nick, int score) {
            this.nick = nick;
            this.score = score;
        }
        public String nick;
        public int score;
    }

    /**
     * Identyfikacja typu żądania
     * @param request żądanie
     * @return typ żądania
     */
    private static RequestType getTypeFromRequest(String request) {
        if (request.matches("(?i)^GETMAP.*")) {
            return RequestType.GET_MAP;
        } else if (request.matches("(?i)^PUSH.*")) {
            return RequestType.PUSH_SCORE;
        } else if (request.matches("(?i)^GETCONFIG.*")) {
            return RequestType.GET_CONFIG;
        } else if (request.matches("(?i)^GETSCORES.*")) {
            return RequestType.GET_SCORES;
        } else {
            return RequestType.INVALID;
        }
    }

    /**
     * Odczyt pliku konfiguracyjnego.
     * @return zawartość pliku konfiguracyjnego
     */
    private static String getConfig(){
        StringBuilder sb = new StringBuilder();
        try {
            Path path = FileSystems.getDefault().getPath("src","dynaserver","resources","config.txt");
            List<String> list=Files.readAllLines(path);
            for (String s:list) {
                sb.append(s);
                sb.append("\n");
            }
        } catch (IOException e) {
            System.out.println("Folder your are trying to open does not exist.");
        }
        return sb.toString();
    }

    /**
     * Odczyt map z pliku w zależności od numeru identyfikacyjnego.
     * @param lvlId numer identyfikacyjny mapy
     * @return zawartość pliku z mapą
     */
    private static String getLevel(int lvlId){
        String lvl;
        StringBuilder sb1 = new StringBuilder("level");
        sb1.append(lvlId);
        sb1.append(".txt");
        lvl = sb1.toString();
        StringBuilder sb = new StringBuilder();
        try {
            Path path = FileSystems.getDefault().getPath("src","dynaserver","resources",lvl);
            List<String> list = Files.readAllLines(path);
            for (String s:list) {
                sb.append(s);
                sb.append("\n");
            }
        } catch (IOException e) {
            System.out.println("File your are trying to open does not exist.");
        }
        return sb.toString();
    }

    /**
     * Odczyt pliku z wynikami.
     * @return zawartść pliku z wynikami
     */
    private static String getScores(){
        StringBuilder sb = new StringBuilder();
        try {
            Path path = FileSystems.getDefault().getPath("src","dynaserver","resources","scores.txt");
            List<String> list = Files.readAllLines(path, StandardCharsets.ISO_8859_1);
            for (String s:list) {
                sb.append(s);
                sb.append("\n");
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return sb.toString();
    }

    /**
     * Odseparowanie numeru mapy z żadania.
     * @param request żądanie
     * @return numer identyfikacyjny mapy
     */
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

    /**
     * Odseparowanie wyniku gracza z żądania oraz jego zapis do pliku.
     * @param request żądanie
     * @return wynik gracza
     */
    private static Score getScoreFromRequest(String request) {
        String nick = null;
        int score = -1;
        Pattern p = Pattern.compile("^([Pp][Uu][Ss][Hh]) +\"?([A-Za-z0-9]+)\"? +([0-9]+)");
        Matcher m = p.matcher(request);
        while (m.find()) {
            nick = m.group(2);
            try {
                score = Integer.parseInt(m.group(3));
                File file = new File("src/dynaserver/resources/scores.txt");
                Writer output = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file.getAbsoluteFile(), true), "UTF-8"));
                output.append("\n"+ nick + " " + score);
                output.close();
            } catch(NumberFormatException | IOException e) {
                System.out.println("Score number cannot be parsed or cannot open the file.");
                score = -1;
            }
        }

        return new Score(nick, score);
    }
}
