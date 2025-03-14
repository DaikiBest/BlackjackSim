public class Card {
    private String name;
    private int value;
    private Suit suit;

    public Card(String name, int value, Suit suit) {
        this.name = name;
        this.value = value;
        this.suit = suit;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public Suit getSuit() {
        return suit;
    }
}
