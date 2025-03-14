import java.util.ArrayList;
import java.util.List;

public class BlackjackSim extends Simulator {
    protected List<Player> players;
    private int numPlayers;

    public BlackjackSim(List<Card> deck) {
        super(deck);
        players = new ArrayList<>();
    }

    @Override
    public void simulate() {
        currentDeck = new ArrayList<>(deck);
        String input;

        do {
            System.out.print("Choose number of players | else (quit). Max players 7: ");
            input = scanner.nextLine().trim().toLowerCase();

            if (input.matches("-?\\d+") && Integer.valueOf(input) <= 7 && Integer.valueOf(input) >= 1) {
                numPlayers = Integer.valueOf(input);
                startGame(numPlayers);

            } else if (input.equals("quit")) {
                return;
            } else {
                System.out.println("Invalid input");
            }

        } while (!input.equals("quit"));
    }

    private void startGame(int numPlayers) {
        for (int i = 0; i < numPlayers; i++) { // Initialize players
            Player player = new Player(i + 1, new Cash(1000));
            players.add(player);
            placeBets(player);
            dealInitalPlayerCards(player.getCards());
        }
        Card card = currentDeck.remove(rand.nextInt(currentDeck.size()));
        dealerCards.add(card);

        String input;
        do {
            for (int i = 0; i < players.size(); i++) { // Player turns
                playerTurn(i);
            }
            dealerTurn();
            gameOutcome();

            do {
                System.out.print("Play again with current players? (y)es (n)o: ");
                input = scanner.nextLine().trim().toLowerCase();
            } while (!(input.equals("y") || input.equals("n")));

            if (input.equals("y")) {
                resetGame();
                if (players.size() == 0) {
                    System.out.println("Everyone ran out of funds!!\n");
                    break;
                }
                for (Player player : players) {
                    placeBets(player);
                    dealInitalPlayerCards(player.getCards());
                }
                card = currentDeck.remove(rand.nextInt(currentDeck.size()));
                dealerCards.add(card);
            }
        } while (!input.equals("n"));
        resetDeck();
    }

    private void placeBets(Player player) {
        boolean hasDecided = false;
        do {
            System.out
                    .print("Player " + player.getNumber() + " place your bets. $" + player.getCash().getCash() + " remaining. Type (max) for max bet: ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.matches("-?\\d+") && Integer.valueOf(input) <= player.getCash().getCash() && Integer.valueOf(input) > 0) {
                player.setBet(Integer.valueOf(input));
                hasDecided = true;
            } else if (input.equalsIgnoreCase("max") || input.equalsIgnoreCase("m")) {
                player.setBet(player.getCash().getCash());
                hasDecided = true;
            } else {
                System.out.println("Invalid amount");
            }
        } while (!hasDecided);
    }

    private void displayBoard() {
        for (Player player : players) {
            System.out.print("Player " + player.getNumber() + ": ");
            for (Card card : player.getCards()) {
                System.out.print("\u001B[34m" + card.getName() + " ");

            }
            System.out.print("\u001B[0m  |  ");
        }
        System.out.print("\nDealer: ");
        for (Card card : dealerCards) {
            System.out.print("\u001B[34m" + card.getName() + " ");
        }
        System.out.println("\u001B[0m");
    }

    private void playerTurn(int index) {
        Player player = players.get(index);
        List<Card> playerCards = player.getCards();
        boolean finishedTurn = false;
        if (playerCards.size() == 2 && getTotal(playerCards) == 21) {
            System.out.println("\u001B[1mPlayer " + player.getNumber() + " got Blackjack!\u001B[0m");
            player.setFinished(true);
            return;
        }
        do {
            displayBoard();
            System.out.print("Player " + player.getNumber() + " you have ");
            for (Card card : playerCards) {
                System.out.print("\u001B[34m" + card.getName() + " \u001B[0m");
            }
            System.out.print("\nWhat will you do? (h)it, (s)tand, (d)ouble, or (spl)it: ");
            String input = scanner.nextLine().trim().toLowerCase();
            Card card;
            if (input.equals("h")) { // HIT
                card = currentDeck.remove(rand.nextInt(currentDeck.size()));
                playerCards.add(card);
                System.out.println("You are dealt: \u001B[34m" + card.getName() + "\u001B[0m");
                finishedTurn = checkOutcome(player);
            } else if (input.equals("d")) { // DOUBLE
                if (player.getBet() * 2 <= player.getCash().getCash()) {
                    // double currBet and hit
                    player.setBet(player.getBet() * 2);
                    player.setFinished(true);
                    card = currentDeck.remove(rand.nextInt(currentDeck.size()));
                    playerCards.add(card);
                    System.out.println("You are dealt: \u001B[34m" + card.getName() + "\u001B[0m");

                    finishedTurn = checkOutcome(player);
                } else {
                    System.out.println("Can't double.");
                }
            } else if (input.equals("spl")) { // SPLIT
                if (playerCards.size() == 2 && playerCards.get(0).getValue() == playerCards.get(1).getValue()) {
                    Player splitPlayer = new Player(player.getNumber(), player.getCash()); // Separate player object
                    splitPlayer.setBet(player.getBet());
                    players.add(index + 1, splitPlayer);
                    numPlayers++;
                    splitPlayer.getCards().add(playerCards.remove(1)); // Split cards
                } else {
                    System.out.println("Can't split");
                }
            } else if (input.equals("s")) { // STAND
                finishedTurn = true;
            }
            System.out.print("\n");

        } while (!finishedTurn);
    }

    private void dealerTurn() {
        boolean finishedTurn = false;
        do {
            int total = getTotal(dealerCards);
            if (total < 17) { // Hit under 17
                Card card = currentDeck.remove(rand.nextInt(currentDeck.size()));
                dealerCards.add(card);
                System.out.println("Dealer dealt: \u001B[34m" + card.getName() + "\u001B[0m");
            } else {
                finishedTurn = true;
            }
        } while (!finishedTurn);
        System.out.println("Dealer total: \u001B[36m" + getTotal(dealerCards) + "\u001B[0m");
    }

    // Check outcome after playing turn. Returns true if move is player is finished
    private boolean checkOutcome(Player player) {
        if (getTotal(player.getCards()) > 21) {
            player.setFinished(true);
            System.out.println(
                    "\u001B[1mOh no! Player " + player.getNumber() + " busted!\u001B[0m");
        } else if (getTotal(player.getCards()) == 21) { // 21
            System.out.println("\u001B[1mPlayer " + player.getNumber() + " got 21!\u001B[0m");
            player.setFinished(true);
        }
        return player.getFinished();
    }

    private void gameOutcome() {
        System.out.println("\n\u001B[1mGAME OUTCOME\u001B[0m");
        displayBoard();
        int playerTotal;
        int dealerTotal = getTotal(dealerCards);
        for (Player player : players) {
            int pNum = player.getNumber();
            playerTotal = getTotal(player.getCards());
            System.out.print("Player " + pNum + ": ");
            if (playerTotal == 21 && player.getCards().size() == 2
                    && !(dealerTotal == 21 && dealerCards.size() == 2)) { // Only player has Blackjack
                System.out.print("\u001B[35m+$" + player.getBet() * 2 + "\u001B[0m");
                player.addCash(player.getBet() * 2);
            } else if ((playerTotal > dealerTotal || dealerTotal > 21) && playerTotal <= 21) { // Win
                System.out.print("\u001B[32m+$" + player.getBet() + "\u001B[0m");
                player.addCash(player.getBet());
            } else if (playerTotal == dealerTotal) { // Bounce
                System.out.print("bounced");
            } else {
                System.out.print("\u001B[31m-$" + player.getBet() + "\u001B[0m");
                player.deductCash(player.getBet());
            }
            System.out.print("\n");
        }
    }

    @Override
    protected void resetDeck() {
        players.clear();
        dealerCards.clear();
        currentDeck = new ArrayList<>(deck);
    }

    private void resetGame() {
        List<Player> tempPlayers = new ArrayList<>();
        List<Integer> playerNums = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            Player player = players.get(i);
            if (!playerNums.contains(player.getNumber())) { // remove all duplicate players (from splitting)
                if (player.getCash().getCash() > 0) {
                    playerNums.add(player.getNumber());
                    player.resetPlayer();
                    tempPlayers.add(player);
                } else {
                    System.out.println("Player " + players.get(i).getNumber() + " ran out of funds!");
                }
            }
        }
        players = tempPlayers;
        numPlayers = players.size();
        dealerCards.clear();
        currentDeck = new ArrayList<>(deck);
    }

    private int getTotal(List<Card> cards) {
        int numAces = 0;
        int total = 0;
        for (Card card : cards) { // count non-aces
            if (card.getName().equals("A")) {
                numAces++;
            }
            total += card.getValue();
            if (total > 21 && numAces > 0) {
                total -= 10;
                numAces--;
            }
        }
        return total;
    }

}
