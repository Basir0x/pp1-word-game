package ax.ha.pp1.wordgame;

import javax.swing.JOptionPane;

/**
 * Programmet som spelar spelet med dialogrutor.
 * JOptionPane finns bara här, så att spelreglerna går att testa utan fönster.
 */
public class Main {

    /** Färre ord än så här från modellen är för tunt att spela med. */
    private static final int MIN_WORDS = 5;

    void main() {
        Game game = new Game(wordSource());
        String info = "Gissa ett svenskt ord med fem bokstäver.";

        while (!game.isOver()) {
            int nummer = game.getGuessCount() + 1;
            String gissning = JOptionPane.showInputDialog(
                    game + info + "\n\nGissning " + nummer + " av " + Game.MAX_GUESSES);

            // Avbryt ger null, och då avslutas programmet direkt.
            if (gissning == null) {
                return;
            }

            try {
                game.guess(gissning);
            } catch (IllegalArgumentException e) {
                // Ogiltigt ord räknas inte som en gissning, så det är bara att fråga igen.
                JOptionPane.showMessageDialog(null,
                        "\"" + gissning + "\" är inte ett giltigt ord.\n"
                                + "Använd fem bokstäver ur A–Ö.");
            }
        }

        if (game.isWon()) {
            JOptionPane.showMessageDialog(null,
                    game + "\nRätt! Du klarade det på " + game.getGuessCount()
                            + " försök av " + Game.MAX_GUESSES + ".");
        } else {
            JOptionPane.showMessageDialog(null,
                    game + "\nInga försök kvar. Rätt ord var " + game.getAnswer() + ".");
        }
    }

    /**
     * Ord från språkmodellen om den är konfigurerad och svarar, annars ordlistan.
     * Spelet fungerar alltså även utan nät och utan VPN.
     */
    private static WordSource wordSource() {
        if (modelIsConfigured()) {
            try {
                String[] words = LlmWords.fetch(new LlmClient(), 20);
                if (words.length >= MIN_WORDS) {
                    return new WordSource(words);
                }
                message("Språkmodellen gav för få ord. Spelet använder ordlistan i stället.");
            } catch (IllegalStateException e) {
                message("Kunde inte nå språkmodellen. Spelet använder ordlistan i stället.");
            }
        }
        return new WordSource(WordList.all());
    }

    /** Sant om både adressen och modellnamnet finns i miljövariablerna. */
    private static boolean modelIsConfigured() {
        String url = System.getenv("PP1_LLM_URL");
        String model = System.getenv("PP1_LLM_MODEL");
        return url != null && !url.isBlank() && model != null && !model.isBlank();
    }

    private static void message(String text) {
        JOptionPane.showMessageDialog(null, text);
    }
}
