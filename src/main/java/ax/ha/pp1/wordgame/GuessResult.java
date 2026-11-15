package ax.ha.pp1.wordgame;

import java.util.Locale;

/**
 * Resultatet av en gissning: gissningens bokstäver och en markering per bokstav.
 * Klassen är oföränderlig: fälten är final, och räckan av markeringar kopieras både
 * när den tas emot och när den lämnas ut.
 */
public final class GuessResult {
    public static final int ABSENT = 0;     // finns inte (fler gånger) i ordet
    public static final int MISPLACED = 1;  // finns, men på en annan plats
    public static final int CORRECT = 2;    // rätt bokstav på rätt plats

    private final String guess;
    private final int[] marks;

    /**
     * Skapar ett resultat. Kastar IllegalArgumentException om längderna skiljer sig
     * eller om en markering är ogiltig.
     */
    public GuessResult(String guess, int[] marks) {
        if (guess == null || marks == null) {
            throw new IllegalArgumentException("Gissning och markeringar måste finnas");
        }
        if (guess.length() != marks.length) {
            throw new IllegalArgumentException(
                "Gissningen har " + guess.length() + " bokstäver men det finns " + marks.length + " markeringar");
        }
        // Kopiera och kontrollera i samma svep, så att fältet aldrig får en räcka
        // som någon utanför klassen kan ändra.
        int[] copy = new int[marks.length];
        for (int i = 0; i < marks.length; i++) {
            if (marks[i] < ABSENT || marks[i] > CORRECT) {
                throw new IllegalArgumentException("Ogiltig markering: " + marks[i]);
            }
            copy[i] = marks[i];
        }
        this.guess = guess.toUpperCase(Locale.ROOT);
        this.marks = copy;
    }

    /** Gissningen med versaler, t.ex. "ALLAS". */
    public String getGuess() {
        return guess;
    }

    /** Antal bokstäver. */
    public int length() {
        return guess.length();
    }

    /** Bokstaven på plats index (0–4). */
    public char letterAt(int index) {
        return guess.charAt(index);
    }

    /** Markeringen på plats index: ABSENT, MISPLACED eller CORRECT. */
    public int markAt(int index) {
        return marks[index];
    }

    /** Alla markeringar, t.ex. {1, 1, 2, 1, 0}. En kopia, så att resultatet inte går att ändra. */
    public int[] getMarks() {
        return marks.clone();
    }

    /** Sant om alla bokstäver är CORRECT. */
    public boolean isCorrect() {
        for (int mark : marks) {
            if (mark != CORRECT) {
                return false;
            }
        }
        return true;
    }

    /** Till exempel (A)(L)[L](A)-S- för gissningen ALLAS mot svaret KALLA. */
    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < marks.length; i++) {
            char letter = guess.charAt(i);
            switch (marks[i]) {
                case CORRECT -> text.append('[').append(letter).append(']');
                case MISPLACED -> text.append('(').append(letter).append(')');
                default -> text.append('-').append(letter).append('-');
            }
        }
        return text.toString();
    }
}
