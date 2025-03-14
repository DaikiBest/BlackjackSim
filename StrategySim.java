import java.util.ArrayList;
import java.util.List;

public class StrategySim extends Simulator {
    private List<Card> playerCards;

    public StrategySim(List<Card> deck) {
        super(deck);
        playerCards = new ArrayList<>();
    }

    @Override
    public void simulate() {
        currentDeck = new ArrayList<>(deck);
        String input = "";

        do {
            dealInitalPlayerCards(playerCards);
            if (playerCards.get(0).getValue() + playerCards.get(1).getValue() != 21) { // Don't test on 21
                Card card = currentDeck.remove(rand.nextInt(currentDeck.size()));
                dealerCards.add(card);
                System.out.println("You: \u001B[34m" + playerCards.get(0).getName() + " " + playerCards.get(1).getName()
                    + "\u001B[0m\t Dealer: \u001B[34m" + dealerCards.get(0).getName() + "\u001B[0m");
                System.out.println("What will you do? (h)it, (s)tand, (d)ouble, or (spl)it \t| (quit)");
                input = scanner.nextLine().trim().toLowerCase();
                if (input.equals("h") || input.equals("s") || input.equals("d") || input.equals("spl")) {
                    boolean isCorrect = evalDecision(input);
                    if (isCorrect) {
                        System.out.println("\u001B[32mCorrect!\u001B[0m\n");
                    } else {
                        System.out.println("\u001B[31mIncorrect!\u001B[0m. Correct decision was...\n");
                    }
                }
            }
            resetDeck();

        } while (!input.equals("quit"));
    }

    @Override
    protected void resetDeck() {
        playerCards.clear();
        dealerCards.clear();
        currentDeck = new ArrayList<>(deck);
    }

    private boolean evalDecision(String input) {
        Card p1 = playerCards.get(0);
        Card p2 = playerCards.get(1);
        Card d1 = dealerCards.get(0);
        boolean isCorrect = false;
        if (input.equals("spl") && p1.getValue() == p2.getValue()) {
            isCorrect = splitDecision(p1, p2, d1);
        } else if (input.equals("h")) {
            isCorrect = hitDecision(p1, p2, d1);
        } else if (input.equals("s")) {
            isCorrect = standDecision(p1, p2, d1);
        } else if (input.equals("d")) {
            isCorrect = doubleDecision(p1, p2, d1);
        }
        return isCorrect;
    }

    private boolean splitDecision(Card p1, Card p2, Card d1) {
        if (p1.getName().equals("A") || p1.getValue() == 8) { // split Ace and 8
            return true;
        } else if (p1.getValue() == 9) { // split 9; dealer 2-9 no 7
            if (d1.getValue() >= 2 && d1.getValue() <= 9 && d1.getValue() != 7)
                return true;
        } else if (p1.getValue() == 7) {
            if (d1.getValue() >= 2 && d1.getValue() <= 7)
                return true;
        } else if (p1.getValue() == 6) {
            if (d1.getValue() >= 2 && d1.getValue() <= 6)
                return true;
        } else if (p1.getValue() == 4) {
            if (d1.getValue() == 5 || d1.getValue() == 6)
                return true;
        } else if (p1.getValue() == 3 || p1.getValue() == 2) {
            if (d1.getValue() >= 2 && d1.getValue() <= 7)
                return true;
        }
        return false;
    }

    private boolean hitDecision(Card p1, Card p2, Card d1) {
        int total = p1.getValue() + p2.getValue();
        if (p1.getValue() == p2.getValue()) { // Pairs
            if (p1.getValue() == 7 || p1.getValue() == 3 || p1.getValue() == 2 && d1.getValue() >= 8) {
                return true;
            } else if (p1.getValue() == 6 && d1.getValue() >= 7) {
                return true;
            } else if (p1.getValue() == 5 && d1.getValue() >= 10) {
                return true;
            } else if (p1.getValue() == 4 && (d1.getValue() <= 4 || d1.getValue() >= 7)) {
                return true;
            }
            return false;
        }
        // Soft totals
        if (p1.getName().equals("A") || p2.getName().equals("A")) {
            if (total == 18 && d1.getValue() >= 9) {
                return true;
            } else if (total == 17 && (d1.getValue() == 2 || d1.getValue() >= 7)) {
                return true;
            } else if ((total == 16 || total == 15) && (d1.getValue() <= 3 || d1.getValue() >= 7)) {
                return true;
            } else if ((total == 14 || total == 13) && (d1.getValue() <= 4 || d1.getValue() >= 7)) {
                return true;
            }
        } else { // Hard totals
            if (total >= 13 && total <= 16 && d1.getValue() >= 7) {
                return true;
            } else if (total == 12 && (d1.getValue() >= 7 || d1.getValue() <= 3)) {
                return true;
            } else if (total == 10 && d1.getValue() >= 10) {
                return true;
            } else if (total == 9 && (d1.getValue() >= 7 || d1.getValue() == 2)) {
                return true;
            } else if (total <= 8) {
                return true;
            }
        }
        return false;
    }

    private boolean standDecision(Card p1, Card p2, Card d1) {
        int total = p1.getValue() + p2.getValue();
        if (p1.getValue() == p2.getValue() && p1.getValue() == 9 && d1.getValue() >= 10) { // Pairs
            return true;
        } else if (p1.getName().equals("A") || p2.getName().equals("A")) { // Soft totals
            if (total == 20) {
                return true;
            } else if (total == 19 && d1.getValue() != 6) {
                return true;
            } else if (total == 18 && (d1.getValue() == 7 || d1.getValue() == 8)) {
                return true;
            }
        } else { // Hard totals
            if (total >= 17) {
                return true;
            } else if (total >= 13 && d1.getValue() <= 6) {
                return true;
            } else if (total == 12 && (d1.getValue() >= 4 || d1.getValue() <= 6)) {
                return true;
            }
        }
        return false;
    }

    private boolean doubleDecision(Card p1, Card p2, Card d1) {
        int total = p1.getValue() + p2.getValue();
        if (p1.getValue() == p2.getValue() && p1.getValue() == 5 && d1.getValue() <= 9) { // Pairs
            return true;
        } else if (p1.getName().equals("A") || p2.getName().equals("A")) { // Soft totals
            if (total == 19 && d1.getValue() == 6) {
                return true;
            } else if (total == 18 && d1.getValue() <= 6) {
                return true;
            } else if (total == 17 && (d1.getValue() >= 3 || d1.getValue() <= 6)) {
                return true;
            } else if ((total == 16 || total == 15) && (d1.getValue() >= 4 || d1.getValue() <= 6)) {
                return true;
            } else if ((total == 14 || total == 13) && (d1.getValue() == 5 || d1.getValue() == 6)) {
                return true;
            }
        } else { // Hard totals
            if (total == 11) {
                return true;
            } else if (total == 10 && d1.getValue() <= 9) {
                return true;
            } else if (total == 9 && (d1.getValue() >= 3 || d1.getValue() <= 6)) {
                return true;
            }
        }
        return false;
    }
}
