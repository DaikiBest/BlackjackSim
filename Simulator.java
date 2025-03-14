import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

abstract class Simulator {
    protected Random rand;
    protected Scanner scanner;
    protected List<Card> deck;
    protected List<Card> currentDeck;
    
    protected List<Card> dealerCards;

    protected Simulator(List<Card> deck) {
        rand = new Random();
        this.deck = deck;
        scanner = new Scanner(System.in);
        dealerCards = new ArrayList<>();
    }
    
    protected void dealInitalPlayerCards(List<Card> playerCards) {
        Card card = currentDeck.remove(rand.nextInt(currentDeck.size()));
        playerCards.add(card);
        card = currentDeck.remove(rand.nextInt(currentDeck.size()));
        playerCards.add(card);
    }

    abstract public void simulate();
    abstract protected void resetDeck();
}
