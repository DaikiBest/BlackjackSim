// Allows split hands to share same cash (same player)
public class Cash {
    private int cash;

    public Cash(int cash) {
        this.cash = cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public int getCash() {
        return cash;
    }

}
