
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Blackjack {
	public static void main(String[] args) {
		int seed = Integer.parseInt(args[0]);
		int playerNum = Integer.parseInt(args[1]);
		
		Deck deck = new Deck();
		deck.shuffle(seed);
		
		Hand[] players = new Hand[playerNum];
		players[0] = new Player();
		
		for (int i = 1; i< playerNum; i++) {
			players[i] = new Computer();
		}
		House house = new House();
		
		// 1. 카드 분배
		// 한장씩 두번 돌리기
		for (int turn = 0; turn < 2; turn++) {
			for (int i = 0; i < playerNum; i++) {
				players[i].addCard(deck.dealCard());
			}
			house.addCard(deck.dealCard());
		}
		
		// 2. 초기 상태 출력
		System.out.println("House: HIDDEN, " + house.cards.get(1));
		for (int i = 0; i < playerNum; i++) {
			System.out.println("Player" + (i + 1) + ": " + players[i] + " (" + players[i].getTotal() + ")");
		}
		
		// house 두 장 합 21이면 게임 끝.
		if (house.getTotal() == 21) {
			printFinalResult(players, house);
			return;
		}
		
		// 3. 플레이어 턴 돌기
		for (int i = 0; i < playerNum; i++) {
			System.out.println("\n--- Player" + (i + 1) + " turn ---");
			while (true) {
				System.out.println("Player" + (i + 1) + ": " + players[i] + " (" + players[i].getTotal() + ")");
				if (players[i].isBusted()) {
					System.out.println(" - Bust!");
					break;
				}
				if (players[i].decideAction()) {
					System.out.println("Hit"); 
					players[i].addCard(deck.dealCard());
				} else {
					System.out.println("Stand");
					System.out.println("Player" + (i + 1) + ": " + players[i] + " (" + players[i].getTotal() + ")");
					break;
				}
			}
		}
		
		// 4. house 턴
		System.out.println("\n--- House turn ---");
		while (true) {
			System.out.println("House: " + house + " (" + house.getTotal() + ")");
			if (house.isBusted()) {System.out.println(" - Bust!"); break;}
			if (house.decideAction()) {
				System.out.println("Hit");
				house.addCard(deck.dealCard());
			} else {
				System.out.println("Stand");
				System.out.println("House: " + house + " (" + house.getTotal() + ")");
				break;
			}
		}
		printFinalResult(players, house);
	}


		private static void printFinalResult(Hand[] players, House house) {
		
			System.out.println("\n--- Game Results ---");
			System.out.println("House: " + house + " (" + house.getTotal() + ")");
			int h_score = house.getTotal();
	    
			for (int i = 0; i < players.length; i++) {
				int p_score = players[i].getTotal();
				String ment;
				if (players[i].isBusted()) ment = "[Lose]";
				else if (h_score > 21) ment = "[Win]";
				else if (p_score > h_score) ment = "[Win]";
				else if (p_score < h_score) ment = "[Lose]";
				else ment = "[Draw]";
	        
				System.out.print(ment + " Player" + (i + 1) + ": " + players[i] + " (" + p_score + ")");
				if (players[i].isBusted()) System.out.print(" - Bust!");
				System.out.println();
			}
		}
	}



class Card {
	private int value; // 숫자: 1(Ace), 2~10(number), 11(J), 12(Q), 13(K)
	private int suit; // 무늬: c(0), h(1), d(2), s(3)
	
    public Card() {}
    
    public Card(int theValue, int theSuit) {
    	this.value = theValue;
    	this.suit = theSuit;
    }
    
    public int BlackjackValue() {
    	if (value >= 11) {
    		return 10;
    	}
    	if (value == 1) {
    		return 11;
    	}
    	return value;
    }
    
    // 에이스는 기본으로는 11인데 점수합산 시 21 넘으면 1이 됨. 
    public boolean isAce() {
    	return value == 1;
    }
    
    // 출력형식 (ex) Ac, 2h, Kd
    public String toString() {
    	String[] suits = {"c", "h", "d", "s"};
    	String valStr;
    	
    	if (value == 1) valStr = "A";
    	else if (value == 11) valStr = "J";
    	else if (value == 12) valStr = "Q";
    	else if (value == 13) valStr = "K";
    	else valStr = String.valueOf(value);
    	
		return valStr + suits[suit];
    }
}



class Deck {
    private Card[] deck;
    private int cardsUsed;
    
    public Deck() {
    	deck = new Card[52]; // 52장 배열 공간.
    	int deckIndex = 0;
    	
    	for (int value = 1; value <= 13; value++) {
    		for (int suit = 0; suit <= 3; suit++) {
    			deck[deckIndex] = new Card(value, suit);
    			deckIndex++;
    		}
    	}
    	cardsUsed = 0;
    }
    

    // shuffle() don't modify
    public void shuffle(int seed) {
        Random random = new Random(seed);
        for (int i = deck.length - 1; i > 0; i--) {
            int rand = (int)(random.nextInt(i + 1));
            Card temp = deck[i];
            deck[i] = deck[rand];
            deck[rand] = temp;
        }
        cardsUsed = 0;
    }

    // dealCard() don't modify
    public Card dealCard() {
        if (cardsUsed == deck.length)
            throw new IllegalStateException("No cards are left in the deck.");

        cardsUsed++;
        return deck[cardsUsed - 1];
    }
}

// Set of cards in your hand
class Hand {
	// 1. 카드 목록 저장
	protected ArrayList<Card> cards;
	
	public Hand() {
		cards = new ArrayList<>();
	}
	
	public boolean decideAction() {
		// TODO Auto-generated method stub
		return false;
	}

	// 2. 카드 추가
	public void addCard(Card card) {
		if (card != null) {
			cards.add(card);
		}
	}
	
	// 3. 합계 계산 for Ace
	public int getTotal() {
		int total = 0;
		int aceCount = 0;
		
		for (Card card : cards) {
			total += card.BlackjackValue();
			if (card.isAce()) {
				aceCount++;
			}
		}
		
		while (total > 21 && aceCount > 0) {
			total -= 10;
			aceCount--;
		}
		return total;
	}
	
	// 4. 카드 출력
	public String getCardString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cards.size(); i++) {
			if (i > 0) sb.append(", ");
			sb.append(cards.get(i));
		}
		return sb.toString();
	}
	
	public String toString() {
		return getCardString();
	}
	
	// 5. busted 여부 확인
	public boolean isBusted() {
		return getTotal() > 21;
	}
}


// Player automatically participates
// 14미만 hit, 17초과 stand, 그 사이는 1/2확률
class Computer extends Hand {
	private Random random = new Random();
	
	public boolean decideAction() {
		// hit-true, stand-false
		int total = getTotal();
		if (total < 14) {
			return true;
		} else if (total > 17) {
			return false;
		} else {
			int isHit = random.nextInt(2);
			return isHit == 1;
		}
	}
}

// Player you control
class Player extends Hand { 
	private Scanner scanner = new Scanner(System.in);
	
	public boolean decideAction() {
		while (true) {
			String input = scanner.nextLine();
			
			if (input.equalsIgnoreCase("Hit")) {
				return true;
			} else if (input.equalsIgnoreCase("Stand")) {
				return false;
			}
		}
	}
}

class House extends Hand { 
	public boolean decideAction() {
		return getTotal() <= 16;
	}
}
    


