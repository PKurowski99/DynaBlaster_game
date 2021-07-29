import java.util.Comparator;

/**
 * Komparator służacy do porównywania wyników graczy w celu ich późniejszego posortowania.
 */
public class ScoresComparator implements Comparator <Player> {

    @Override
    public int compare(Player o1, Player o2) {
        return -Integer.compare(o1.getScores(), o2.getScores());
    }
}
