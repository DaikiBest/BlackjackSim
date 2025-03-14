import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Card> playerCards;
    private int playerNumber;
    private Cash cash;
    private int currBet;
    private boolean isFinished;

    public Player(int playerNumber, Cash cash) {
        playerCards = new ArrayList<>();
        this.playerNumber = playerNumber;
        this.cash = cash;
        isFinished = false;
        currBet = 0;
    }

    public void addCash(int amount) {
        cash.setCash(cash.getCash() + amount);
    }

    public void deductCash(int amount) {
        cash.setCash(cash.getCash() - amount);
    }

    public void resetPlayer() {
        playerCards = new ArrayList<>();
        isFinished = false;
        currBet = 0;
    }
    
    public void setBet(int bet) {
        currBet = bet;
    }

    public void setFinished(boolean finish) {
        isFinished = finish;
    }

    public int getNumber() {
        return playerNumber;
    }

    public Cash getCash() {
        return cash;
    }

    public int getBet() {
        return currBet;
    }

    public boolean getFinished() {
        return isFinished;
    }

    public List<Card> getCards() {
        return playerCards;
    }
}
