import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Blackjack {
    private List<Card> deck;
    private Scanner scanner;

    public Blackjack() {
        instantiateDeck();
        scanner = new Scanner(System.in);
    }

    public void run() {
        String input;
        Simulator sim;
        do {
            System.out.println("Choose a simulator: (s)trategy, (b)lackjack | (quit)");
            input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("s")) {
                sim = new StrategySim(deck);
                sim.simulate();
            } else if (input.equalsIgnoreCase("b")) {
                sim = new BlackjackSim(deck);
                sim.simulate();
            }

        } while (!input.equalsIgnoreCase("quit"));
    }

    private void instantiateDeck() {
        deck = new ArrayList<>();
        for (Suit s : Suit.values()) {
            deck.add(new Card("A", 11, s));
            for (int i = 2; i <= 10; i++) {
                deck.add(new Card(String.valueOf(i), i, s));
            }
            deck.add(new Card("J", 10, s));
            deck.add(new Card("Q", 10, s));
            deck.add(new Card("K", 10, s));
        }
    }
}
