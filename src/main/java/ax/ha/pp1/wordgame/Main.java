package ax.ha.pp1.wordgame;

import javax.swing.JOptionPane;

/**
 * Programmet som spelar spelet med dialogrutor.
 * JOptionPane finns bara här, så att spelreglerna går att testa utan fönster.
 */
public class Main {

    void main() {
        Game game = new Game(new WordSource(WordList.all()));
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
}
