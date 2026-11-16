package ax.ha.pp1.wordgame;

import java.util.ArrayList;
import java.util.List;

/**
 * En spelomgång: ett hemligt ord och högst sex gissningar.
 * Klassen vet ingenting om dialogrutor.
 */
public class Game {
    public static final int MAX_GUESSES = 6;

    private final String answer;
    private final List<GuessResult> guesses = new ArrayList<>();
    private boolean won;

    /** Startar en omgång. Svaret hämtas med source.next(). */
    public Game(WordSource source) {
        this.answer = source.next();
    }

    /**
     * Gör en gissning och returnerar resultatet. Kastar IllegalArgumentException om
     * ordet är ogiltigt (gissningen räknas då inte), och IllegalStateException om
     * spelet redan är slut.
     */
    public GuessResult guess(String word) {
        if (isOver()) {
            throw new IllegalStateException("Spelet är slut");
        }
        if (!Evaluator.isValidWord(word)) {
            throw new IllegalArgumentException("Ogiltigt ord: " + word);
        }
        GuessResult result = Evaluator.evaluate(word, answer);
        guesses.add(result);
        won = result.isCorrect();
        return result;
    }

    /** Sant om senaste gissningen var rätt. */
    public boolean isWon() {
        return won;
    }

    /** Sant om spelet är slut: vunnet eller sex gissningar gjorda. */
    public boolean isOver() {
        return won || guesses.size() >= MAX_GUESSES;
    }

    /** Antal gjorda gissningar. */
    public int getGuessCount() {
        return guesses.size();
    }

    /** Antal gissningar kvar. */
    public int getGuessesLeft() {
        return MAX_GUESSES - guesses.size();
    }

    /**
     * Svaret. Kastar IllegalStateException om spelet inte är slut, så att svaret
     * inte kan avslöjas för tidigt.
     */
    public String getAnswer() {
        if (!isOver()) {
            throw new IllegalStateException("Spelet är inte slut");
        }
        return answer;
    }

    /** Spelbrädet: en rad per gissning i formen från GuessResult, med radbrytning efter varje rad. */
    @Override
    public String toString() {
        StringBuilder board = new StringBuilder();
        for (GuessResult result : guesses) {
            board.append(result).append('\n');
        }
        return board.toString();
    }
}
