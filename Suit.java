import java.awt.Color;

public enum Suit {
    HEARTS(Color.RED), DIAMONDS(Color.RED), SPADES(Color.BLACK), CLUBS(Color.BLACK);

    private Color color;
    private Suit(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
