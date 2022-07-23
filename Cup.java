import java.util.Arrays;

public class Cup {
	
	// keeps track of the count of tokens for each of 9 possible moves
	private int[] tokens = new int[9]; 
	
	// the total count of tokens
	private int count = 0;
	
	// initialize to having never made a move
	private int lastMove = -1;
	
	public Cup(Board board) {
		// add the right number of tokens
		int move = 0;
		count = 0;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				if (board.isValidMove(new Move(row, col))) {
					tokens[move] = 1;
					count++;
				}
				move++;
			}
		}
	}
	
	public String toString() {
		return Arrays.toString(tokens);
	}
	
	// as we loop through the count of tokens in this cup
	// we can't change the tokens! No learning.
	// We should be synchronized so that out count of tokens
	// doesn't change as we loop through our tokens.
	//
	// Adding the keyword synchronized will make "this" object
	// be the critical section for this entire method.
	public synchronized int getAndForgetRandomMove() {
		int move = 0;
		int rand = (int) (Math.random() * count);
		
		// loop through our token count until we find the right "bucket"
		while (rand >= tokens[move]) {
			rand -= tokens[move];
			move++;
		}
		
		return move;
	}
	
	/**
	 * picks a random move from its tokens. Remembers the move.
	 */
	public int getRandomMove() {
		// by assigning to this.lastMove, we remember the move
		this.lastMove = getAndForgetRandomMove();
		
		return this.lastMove;
	}

	/**
	 * this will put the token in or out, depending on if we won or lost.
	 * We must always have at least 1 token present.
	 * @param won true if the AI won the game and cup should add token.
	 */
	public synchronized void learn(boolean won) {
		if (lastMove == -1) {
			// this cup didn't ever make a move
			return;
		}
		
		if (won) {
			tokens[lastMove]++;
			count++;
		} else if (tokens[lastMove] > 1) {
			count--;
			tokens[lastMove]--;
		}
		
		lastMove = -1;
	}
}
