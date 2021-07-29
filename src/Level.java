import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Poziomy dostępne w grze.
 */
public class Level{
    /**
     * numer identyfikacyjny poziomu
     */
    private int levelNr;
    /**
     * zmienna opisująca współrzędną x początkowego położenia bohatera w poziomie
     */
    private int xStartPosition;
    /**
     * zmienna opisująca współrzędną y początkowego położenia bohatera w poziomie
     */
    private int yStartPosition;
    /**
     * lista współrzędnych x potworów znajdujących się na poziomie
     */
    private List<Integer> monsterXPosList = new ArrayList<>();
    /**
     * lista współrzędnych y potworów znajdujących się na poziomie
     */
    private List<Integer> monsterYPosList = new ArrayList<>();
    /**
     * macierz zawierająca symbole, które reprezentują bloki
     */
    private int[][] matrix = new int [InputData.mapWidth][InputData.mapHeight];
    /**
     * macież opisująca poziom
     */
    private Object[][] map = new Object[InputData.mapWidth][InputData.mapHeight];
    Level(int nr, String path) {
        levelNr = nr;
        try {
            this.fillMatrix(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.fillMap();
    }

    /**
     * Metoda odczytująca schemat poziomu z pliku.
     * @param file ścieżka do pliku z wyglądem poziomu
     * @throws FileNotFoundException nie znaleziono pliku
     */
    private void fillMatrix(String file) throws FileNotFoundException {
        Scanner sc = new Scanner(new BufferedReader(new FileReader(file)));
        while(sc.hasNextLine()) {
            for (int i=0; i<matrix.length; i++) {
                String[] line = sc.nextLine().trim().split(" ");
                for (int j=0; j<line.length; j++) {
                    matrix[i][j] = Integer.parseInt(line[j]);
                }
            }
        }
    }


    /**
     * Metoda przekształcająca symboliczny zapis poziomu na mapę poziomu.
     */
    private void fillMap(){
        if(monsterYPosList.size()!=0){
            monsterYPosList.clear();
            monsterXPosList.clear();
        }
        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix.length; j++){
                switch (matrix[i][j]){
                    case 1:
                        map[i][j] = new Stone(i, j,false,false,false);
                        break;
                    case 2:
                        map[i][j] = new Wall(i,j);
                        break;
                    case 3:
                        map[i][j] = new Air(i,j);
                        xStartPosition = j;
                        yStartPosition = i;
                        break;
                    case 4:
                        map[i][j] = new Air(i,j);
                        monsterXPosList.add(i);
                        monsterYPosList.add(j);
                        break;
                    case 7:
                        map[i][j] = new Stone(i,j,false, true, false);
                        break;
                    case 8:
                        map[i][j] = new Stone(i,j,false, false, true);
                        break;
                    case 9:
                        map[i][j] = new Stone(i,j,true, false, false);
                        break;
                    default:
                        map[i][j] = new Air(i,j);

                }
            }
        }
    }

    /**
     * Metoda, która resetuje poziom.
     */
    public void resetMap(){fillMap();}

    /**
     * Metoda zwrcająca identyfikator poziomu.
     * @return identyfikator poziomu
     */
    public int getLevelNr(){return this.levelNr;}

    /**
     * Metoda zwracająca mapę poziomu.
     * @return mapa poziomu
     */
    public Object[][] getMap(){return map;}

    /**
     * Metoda, która zastępuje blok na podanych współrzędnych blokiem powietrza.
     * @param y współrzędna y, na której ma być zamieniony blok
     * @param x współrzędna x, na której ma być zamieniony blok
     */
    public void setAir(int y, int x){map[y][x] = new Air(y,x);}

    /**
     * Metoda, która zastępuje blok na podanych współrzędnych blokiem ognia.
     * @param x współrzędna x, na której ma być zamieniony blok
     * @param y współrzędna y, na której ma być zamieniony blok
     * @param portal czy pod blokiem ognia ukryty jest portal
     * @param coin czy pod blokiem ognia ukryty jest bonus
     * @param heart czy pod blokiem ognia ukryte jest serduszko
     */
    public void setFire(int x, int y, boolean portal,boolean coin,boolean heart){
        map[x][y] = new Fire(x,y,portal,coin,heart);
    }

    /**
     * Metoda, która zastępuje blok na podanych współrzędnych blokiem bomby.
     * @param x współrzędna x, na której ma być zamieniony blok
     * @param y współrzędna y, na której ma być zamieniony blok
     */
    public void setBomb(int x, int y){
        map[x][y] = new Bomb(x,y);
    }
    /**
     * Metoda, która zastępuje blok na podanych współrzędnych blokiem poratlu na następny poziom.
     * @param x współrzędna x, na której ma być zamieniony blok
     * @param y współrzędna y, na której ma być zamieniony blok
     */
    public void setPortal(int x, int y){
        map[x][y] = new Portal(x,y);
    }
    /**
     * Metoda, która zastępuje blok na podanych współrzędnych bonusem.
     * @param x współrzędna x, na której ma być zamieniony blok
     * @param y współrzędna y, na której ma być zamieniony blok
     */
    public void setCoin(int x, int y){
        map[x][y] = new Coin(x,y);
    }
    /**
     * Metoda, która zastępuje blok na podanych współrzędnych dodatkowym życiem.
     * @param x współrzędna x, na której ma być zamieniony blok
     * @param y współrzędna y, na której ma być zamieniony blok
     */
    public void setHeart(int x, int y){
        map[x][y] = new Heart(x,y);
    }

    /**
     * Metoda zwracająca startową współrzędną x bohatera.
     * @return startowa współrzędna x bohatera
     */
    public int getXStartPosition(){return xStartPosition;}
    /**
     * Metoda zwracająca startową współrzędną y bohatera.
     * @return startowa współrzędna y bohatera
     */
    public int getYStartPosition(){return yStartPosition;}
    /**
     * Metoda zwracająca liste startowych współrzędnych x potworów.
     * @return lista strtowych współrzędnych x potworów
     */
    public List<Integer> getMonsterXPosList(){return monsterXPosList;}
    /**
     * Metoda zwracająca liste startowych współrzędnych y potworów.
     * @return lista strtowych współrzędnych y potworów
     */
    public List<Integer> getMonsterYPosList(){return monsterYPosList;}

    /**
     * Metoda odpowiadająca za wybuch bomby i rozprzestrzenianie się ognia
     * @param x współrzędna x wybuchu bomby
     * @param y współrzędna y wybuchu bomby
     */
    public void bombExplode(int x,int y){
        if (x>=0 && x<= InputData.mapWidth && y>=0 && y<= InputData.mapHeight){
            setFire(x,y,false, false, false);
            if(x+1<= InputData.mapWidth){
                if(map[x+1][y].getClass() == Air.class || map[x+1][y].getClass() == Fire.class){
                    setFire(x+1,y,false,false,false);
                    if(x+2<= InputData.mapWidth){
                        if(map[x+2][y].getClass() == Air.class || map[x+2][y].getClass() == Fire.class){
                            setFire(x+2,y,false,false,false);
                        }
                        else if(map[x+2][y].getClass() == Stone.class){
                            Stone stone = (Stone)map[x+2][y];
                            setFire(x+2,y,stone.getPortalInformation(),stone.getCoinInformation(),stone.getHeartInformation());
                        }
                    }
                }
                else if(map[x+1][y].getClass() == Stone.class){
                    Stone stone = (Stone)map[x+1][y];
                    setFire(x+1,y,stone.getPortalInformation(),stone.getCoinInformation(),stone.getHeartInformation());
                }
            }
            if(y+1<= InputData.mapHeight){
                if(map[x][y+1].getClass() == Air.class || map[x][y+1].getClass() == Fire.class){
                    setFire(x,y+1,false,false,false);
                    if(y+2<= InputData.mapHeight){
                        if(map[x][y+2].getClass() == Air.class || map[x][y+2].getClass() == Fire.class){
                            setFire(x,y+2,false,false,false);
                        }
                        else if(map[x][y+2].getClass() == Stone.class){
                            Stone stone = (Stone)map[x][y+2];
                            setFire(x,y+2,stone.getPortalInformation(),stone.getCoinInformation(),stone.getHeartInformation());
                        }
                    }
                }
                else if(map[x][y+1].getClass() == Stone.class){
                    Stone stone = (Stone)map[x][y+1];
                    setFire(x,y+1,stone.getPortalInformation(),stone.getCoinInformation(),stone.getHeartInformation());
                }
            }
            if(x-1>= 0){
                if(map[x-1][y].getClass() == Air.class || map[x-1][y].getClass() == Fire.class){
                    setFire(x-1,y,false,false,false);
                    if(x-2>= 0){
                        if(map[x-2][y].getClass() == Air.class||map[x-2][y].getClass() == Fire.class){
                            setFire(x-2,y,false,false,false);
                        }
                        else if(map[x-2][y].getClass() == Stone.class){
                            Stone stone = (Stone)map[x-2][y];
                            setFire(x-2,y,stone.getPortalInformation(),stone.getCoinInformation(),stone.getHeartInformation());
                        }
                    }
                }
                else if(map[x-1][y].getClass() == Stone.class){
                    Stone stone = (Stone)map[x-1][y];
                    setFire(x-1,y,stone.getPortalInformation(),stone.getCoinInformation(),stone.getHeartInformation());
                }
            }
            if(y-1>= 0){
                if(map[x][y-1].getClass() == Air.class||map[x][y-1].getClass() == Fire.class){
                    setFire(x,y-1,false,false,false);
                    if(y-2>= 0){
                        if(map[x][y-2].getClass() == Air.class||map[x][y-2].getClass() == Fire.class){
                            setFire(x,y-2,false,false,false);
                        }
                        else if(map[x][y-2].getClass() == Stone.class){
                            Stone stone = (Stone)map[x][y-2];
                            setFire(x,y-2,stone.getPortalInformation(),stone.getCoinInformation(),stone.getHeartInformation());
                        }
                    }
                }
                else if(map[x][y-1].getClass() == Stone.class){
                    Stone stone = (Stone)map[x][y - 1];
                    setFire(x,y-1,stone.getPortalInformation(),stone.getCoinInformation(),stone.getHeartInformation());
                }
            }

        }

    }
}
