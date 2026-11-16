package ax.ha.pp1.wordgame;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LlmWordsTest {

    // Ett exempel att utgå från. Skriv resten av testerna i del D.
    // Testerna ska inte anropa språkmodellen, eftersom dess svar varierar.
    @Test
    void parseFindsOneWordPerLine() {
        String answer = "KALLA\ntårta";

        assertArrayEquals(new String[] {"KALLA", "TÅRTA"}, LlmWords.parse(answer));
    }

    @Test
    void parseSkipsNumberingAndPunctuation() {
        String answer = "1. BANAN\n2. PÄRON\n3. MELON";

        assertArrayEquals(new String[] {"BANAN", "PÄRON", "MELON"}, LlmWords.parse(answer));
    }

    @Test
    void parseSkipsWordsWithTheWrongLength() {
        String answer = "KALLA KAL KALLAA PIZZA";

        assertArrayEquals(new String[] {"KALLA", "PIZZA"}, LlmWords.parse(answer));
    }

    @Test
    void parseReturnsAnEmptyArrayWhenThereAreNoWords() {
        assertArrayEquals(new String[0], LlmWords.parse("Inga ord här."));
    }

    @Test
    void parseReturnsTheWordsInUpperCase() {
        assertArrayEquals(new String[] {"PIZZA", "SOPPA"}, LlmWords.parse("pizza, soppa"));
    }

    @Test
    void parseKeepsTheSwedishLetters() {
        assertArrayEquals(new String[] {"TÅRTA", "PÄRON", "BÖNOR"}, LlmWords.parse("tårta på? pÄron, bönor"));
    }

    @Test
    void parseSkipsWordsWithCharactersOutsideTheAlphabet() {
        assertArrayEquals(new String[] {"KALLA"}, LlmWords.parse("KALÉA KALLA"));
    }
}
