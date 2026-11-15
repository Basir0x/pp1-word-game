package ax.ha.pp1.wordgame;

import java.util.Locale;

/**
 * Spelreglerna: kontrollerar ord och jämför en gissning med svaret.
 */
public class Evaluator {
    public static final int WORD_LENGTH = 5;

    /** Det svenska alfabetet A–Ö: A–Z plus Å, Ä och Ö. Inga andra tecken är giltiga. */
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ";

    /** Sant om ordet har fem bokstäver ur alfabetet A–Ö, versaler eller gemener. */
    public static boolean isValidWord(String word) {
        if (word == null) {
            return false;
        }
        String upper = word.toUpperCase(Locale.ROOT);
        if (upper.length() != WORD_LENGTH) {
            return false;
        }
        for (int i = 0; i < upper.length(); i++) {
            if (ALPHABET.indexOf(upper.charAt(i)) < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Jämför gissningen med svaret. Versaler och gemener räknas lika.
     * Kastar IllegalArgumentException om gissningen eller svaret inte är ett giltigt ord.
     */
    public static GuessResult evaluate(String guess, String answer) {
        if (!isValidWord(guess)) {
            throw new IllegalArgumentException("Ogiltig gissning: " + guess);
        }
        if (!isValidWord(answer)) {
            throw new IllegalArgumentException("Ogiltigt svar: " + answer);
        }
        String gissning = guess.toUpperCase(Locale.ROOT);
        String svar = answer.toUpperCase(Locale.ROOT);
        int[] marks = new int[WORD_LENGTH];

        // Steg 1: rätt bokstav på rätt plats. De bokstäverna i svaret förbrukas,
        // så att de inte kan användas igen i steg 2.
        boolean[] forbrukad = new boolean[WORD_LENGTH];
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (gissning.charAt(i) == svar.charAt(i)) {
                marks[i] = GuessResult.CORRECT;
                forbrukad[i] = true;
            }
        }

        // Steg 2: de övriga bokstäverna, vänster till höger. Varje bokstav i gissningen
        // tar en ledig bokstav i svaret, så upprepade bokstäver räknas bara så många
        // gånger som de finns i svaret.
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (marks[i] == GuessResult.CORRECT) {
                continue;
            }
            for (int j = 0; j < WORD_LENGTH; j++) {
                if (!forbrukad[j] && svar.charAt(j) == gissning.charAt(i)) {
                    marks[i] = GuessResult.MISPLACED;
                    forbrukad[j] = true;
                    break;
                }
            }
        }

        return new GuessResult(gissning, marks);
    }
}
