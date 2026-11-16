package ax.ha.pp1.wordgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Hämtar ord till spelet från skolans språkmodell (del D).
 */
public class LlmWords {

    /**
     * Frågar modellen efter ungefär count ord och returnerar de giltiga orden i svaret.
     * Kastar IllegalStateException, via LlmClient, om modellen inte går att nå.
     */
    public static String[] fetch(LlmClient client, int count) {
        throw new UnsupportedOperationException("Inte implementerad än");
    }

    /** Alla giltiga ord i texten, med versaler, i den ordning de står. Ogiltiga hoppas över. Kan vara tom. */
    public static String[] parse(String text) {
        List<String> ord = new ArrayList<>();
        if (text == null) {
            return new String[0];
        }

        // Modellen svarar i löptext, så vi samlar bokstäver till ett ord i taget och
        // provar varje ord vid varje tecken som inte är en bokstav.
        StringBuilder kandidat = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char tecken = text.charAt(i);
            if (Character.isLetter(tecken)) {
                kandidat.append(tecken);
            } else {
                addIfValid(ord, kandidat);
                kandidat.setLength(0);
            }
        }
        addIfValid(ord, kandidat);

        return ord.toArray(new String[0]);
    }

    /** Lägger till ordet om det är ett giltigt spelord, med versaler. */
    private static void addIfValid(List<String> ord, StringBuilder kandidat) {
        String word = kandidat.toString();
        if (Evaluator.isValidWord(word)) {
            ord.add(word.toUpperCase(Locale.ROOT));
        }
    }
}

