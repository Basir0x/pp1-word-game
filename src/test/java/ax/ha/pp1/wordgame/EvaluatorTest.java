package ax.ha.pp1.wordgame;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvaluatorTest {

    // Ett exempel att utgå från. Skriv resten av testerna i del A.
    @Test
    void allLettersCorrectGivesACorrectResult() {
        GuessResult result = Evaluator.evaluate("KALLA", "KALLA");

        assertTrue(result.isCorrect());
        assertEquals("[K][A][L][L][A]", result.toString());
    }

    // Punkt 1 och 2: rätt bokstav på rätt plats respektive fel plats.
    @Test
    void correctLetterInWrongPlaceIsMisplaced() {
        // Arrange: alla bokstäver finns i svaret, men en plats för tidigt.
        // Act
        GuessResult result = Evaluator.evaluate("ABCDE", "EABCD");

        // Assert
        assertEquals("(A)(B)(C)(D)(E)", result.toString());
        assertEquals(GuessResult.MISPLACED, result.markAt(0));
        assertFalse(result.isCorrect());
    }

    // Punkt 3: bokstäver som inte finns i svaret.
    @Test
    void lettersThatAreNotInTheAnswerAreAbsent() {
        GuessResult result = Evaluator.evaluate("ABCDE", "FGHIJ");

        assertEquals("-A--B--C--D--E-", result.toString());
        assertEquals(GuessResult.ABSENT, result.markAt(0));
        assertFalse(result.isCorrect());
    }

    // Punkt 4a: exemplet från uppgiften, ALLAS mot KALLA.
    @Test
    void duplicateInGuessCountsOnlyAsManyTimesAsInAnswer() {
        GuessResult result = Evaluator.evaluate("ALLAS", "KALLA");

        assertEquals("(A)(L)[L](A)-S-", result.toString());
        assertEquals(GuessResult.ABSENT, result.markAt(4));
    }

    // Punkt 4b: en rätt placerad bokstav går före en felplacerad.
    // E finns bara på plats 1 i MELON. Den första E:en i gissningen blir
    // därför ABSENT, trots att den står före den rätt placerade.
    @Test
    void correctPlacementWinsOverAnEarlierDuplicate() {
        GuessResult result = Evaluator.evaluate("EELON", "MELON");

        assertEquals("-E-[E][L][O][N]", result.toString());
        assertEquals(GuessResult.ABSENT, result.markAt(0));
        assertEquals(GuessResult.CORRECT, result.markAt(1));
    }

    // Punkt 5: gemener i gissningen, och resultatet visas med versaler.
    @Test
    void lowerCaseGuessIsAcceptedAndShownInUpperCase() {
        GuessResult result = Evaluator.evaluate("allas", "kalla");

        assertEquals("ALLAS", result.getGuess());
        assertEquals("(A)(L)[L](A)-S-", result.toString());
    }

    @Test
    void answerCaseDoesNotMatter() {
        assertEquals("(A)(L)[L](A)-S-", Evaluator.evaluate("ALLAS", "kalla").toString());
    }

    // Punkt 6: å, ä och ö, och att A och Å räknas som olika bokstäver.
    @Test
    void swedishLettersAreValidWords() {
        assertTrue(Evaluator.isValidWord("ÅÄÖAB"));
        assertTrue(Evaluator.isValidWord("åäöab"));
    }

    @Test
    void ringAIsNotTheSameLetterAsA() {
        // TÅRTA mot TARTA: Å står där A står, men är en annan bokstav.
        GuessResult result = Evaluator.evaluate("TÅRTA", "TARTA");

        assertEquals("[T]-Å-[R][T][A]", result.toString());
        assertEquals(GuessResult.ABSENT, result.markAt(1));
    }

    // Punkt 7: ogiltiga gissningar ska ge IllegalArgumentException.
    @Test
    void guessWithWrongLengthIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("KAL", "KALLA"));
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("KALLAA", "KALLA"));
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("", "KALLA"));
    }

    @Test
    void guessWithDigitIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("KA1LA", "KALLA"));
    }

    @Test
    void guessWithSpaceIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("KA LA", "KALLA"));
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("KALLA ", "KALLA"));
    }

    @Test
    void guessWithSpecialCharacterIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("KAL!A", "KALLA"));
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("KALLA!", "KALLA"));
    }

    @Test
    void guessWithLetterOutsideTheSwedishAlphabetIsRejected() {
        // É hör inte till A–Z, Å, Ä, Ö.
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("KALÉA", "KALLA"));
    }

    @Test
    void invalidAnswerIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("KALLA", "KAL"));
        assertThrows(IllegalArgumentException.class, () -> Evaluator.evaluate("KALLA", "KA1LA"));
    }

    // Punkt 8: isValidWord för giltiga och ogiltiga ord.
    @Test
    void isValidWordAcceptsFiveLettersFromTheSwedishAlphabet() {
        assertTrue(Evaluator.isValidWord("KALLA"));
        assertTrue(Evaluator.isValidWord("panna"));
        assertTrue(Evaluator.isValidWord("TÅRTA"));
    }

    @Test
    void isValidWordRejectsEverythingElse() {
        assertFalse(Evaluator.isValidWord(null));
        assertFalse(Evaluator.isValidWord(""));
        assertFalse(Evaluator.isValidWord("KALL"));
        assertFalse(Evaluator.isValidWord("KALLAA"));
        assertFalse(Evaluator.isValidWord("KA1LA"));
        assertFalse(Evaluator.isValidWord("KA LA"));
        assertFalse(Evaluator.isValidWord("KAL-A"));
        assertFalse(Evaluator.isValidWord("KALLA "));
        assertFalse(Evaluator.isValidWord("KALÉA"));
    }
}
