# Del E: Frågor

## 1. Om WordSource skapades inne i Game

Hur hade testerna av `Game` sett ut om `Game` själv skapade sin `WordSource` med hela ordlistan? Vad hade varit svårt eller omöjligt att testa?

*Ditt svar:*

`GameTest` skapar spelet med `new Game(new WordSource(new String[] {"KALLA"}))`, så svaret är känt på förhand. Om `Game` själv hade skapat en `WordSource` med hela ordlistan hade testet inte vetat vilket ord som gällde. Då hade `isWon()`, `getAnswer()` och brädet bara gått att testa genom att spela tills ett visst läge råkade uppstå, och sex felaktiga gissningar hade inte kunnat testas alls utan att först gissa sig till svaret. Testerna hade blivit långsamma och gett olika resultat varje gång.

## 2. Om GuessResult var föränderlig

Anta att `GuessResult` hade en metod `setMark(int index, int mark)` och att `getMarks()` returnerade den interna räckan. Beskriv minst ett konkret fel som då kunde uppstå i spelet eller i testerna.

*Ditt svar:*

`Game` sparar resultatet när gissningen görs och sätter `won = result.isCorrect()`. Om någon sedan ändrade resultatet, till exempel med `getMarks()[0] = GuessResult.ABSENT`, skulle brädet visa en annan rad än den spelaren såg, och `isCorrect()` skulle kunna bli `false` efter att spelet redan förklarats vunnet. Testerna `guessResultCannotBeModifiedThroughGetMarks` och `guessResultCannotBeModifiedThroughTheArrayPassedIn` finns just för att hindra det.

## 3. Undantag

`Game.guess` kastar `IllegalArgumentException` för en ogiltig gissning och `IllegalStateException` när spelet är slut. Varför är det bättre än att returnera `null`? Var i ditt program fångas undantagen, och varför just där?

*Ditt svar:*

`null` säger ingenting om vad som gick fel och är lätt att glömma att kontrollera, och då dyker felet upp långt senare som ett `NullPointerException`. Ett undantag berättar i stället vad som hände: `IllegalArgumentException` att ordet var ogiltigt, `IllegalStateException` att spelet redan är slut. Jag fångar dem i `Main` – `IllegalArgumentException` runt `game.guess(...)`, där en ny dialog kan be om ett annat ord, och `IllegalStateException` runt `LlmWords.fetch(...)`, där spelet i stället fortsätter med `WordList`. Just därför att `Main` är den enda klassen som får visa något för användaren; `Game` måste gå att testa utan fönster.

## 4. Ord från språkmodellen

Ger språkmodellen alltid riktiga svenska ord? Ge exempel från dina körningar eller dina tester, och beskriv hur du skulle kunna kontrollera orden bättre.

*Ditt svar:*

Nej. Modellen svarar i löptext och kan ge fel längd, siffror eller påhittade ord, så `parse` behåller bara det som `Evaluator.isValidWord` godkänner: fem bokstäver ur A–Ö. Med VPN uppe svarade modellen och spelet startade på modellens ord, utan reservlistan. Utan VPN kastade `LlmClient` `IllegalStateException` ("Kunde inte nå språkmodellen (VPN?): HTTP connect timed out") och spelet föll tillbaka på `WordList` med ett meddelande. Det som är kontrollerat är alltså formen, inte betydelsen: ett påhittat ord som `TRÅLP` hade sluppit igenom. Bättre kontroll hade varit att jämföra orden mot en ordlista med riktiga ord, ungefär som `WordList` redan är.

## 5. Svårast och tid

Vad var svårast i uppgiften, och vad gjorde du för att komma vidare? Ungefär hur många timmar lade du på varje del (A, B, C, D och E)?

*Ditt svar:*

Svårast var upprepade bokstäver i `Evaluator`. Jag ritade upp `ALLAS` mot `KALLA` på papper och insåg att en bokstav bara får matchas en gång, så koden markerar rätt plats först, förbrukar de bokstäverna i svaret och letar sedan igenom resten. Testerna med `ALLAS`/`KALLA` och `EELON`/`MELON` visade när ordningen blev rätt. Näst svårast var att förstå varför `getMarks()` måste lämna ut en kopia.

| Del | Timmar |
|-----|--------|
| A   | 2      |
| B   | 4      |
| C   | 1,5    |
| D   | 1      |
| E   | 1      |
