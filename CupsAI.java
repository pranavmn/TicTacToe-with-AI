import java.util.*;

/**
 * There is an interesting question regarding how to consider draws: win or loss.
 * Furthermore, do we learn against a smart player or a dumb player?
 * 
 * 		| Smart	| Dumb
 * -------------------
 * Loss |   #1	| #2
 * -------------------
 * Win  | 	#3	| #4
 * -------------------
 * 
 * When we consider a Draw to be a WIN, then the AI will often defer an
 * immediate win in order to get the Draw.
 * 
 * When we have a Smart opponent, then the data at "dumb" locations is
 * sparse. 
 * 
 * Case #1: Best
 * 		This is the smartest AI. The opening move is heavily the center.
 * 		The AI may still deferred wins, but not in favor of draws.
 * 
 * Case #2: Not so great
 * 		The opening move is NOT the center.
 * 
 * Case #3: Not so great
 * 		We can have sparse data and the AI may gravitate toward draws
 * 		which can lead to some dumb decisions.
 * 
 * Case #4: Good
 * 		Opening move is the center.
 * 
 * @author jstride
 *
 */

// This is the Artificial Intelligence that LEARNS!!
public class CupsAI extends Player {

	private static final boolean opponentSmart = true;
	private static final boolean drawIsWin = false;
	
	private Map<String, Cup> cups = new HashMap<>();
	
	private static CupsAI learningAI = null;
	
	protected CupsAI(GameUserInterface gui, int num) {
		super(gui, num);
	}
	
	/**
	 * This encapsulate the creation of a Player AI.
	 * This method allows us to create a learning AI and a "cheater" AI.
	 * 
	 * @param gui The GUI to use when playing
	 * @param num The player number. 1 = X. 2 = O. 3 = dumb.
	 * @return a Player that has artificial intelligence
	 */
	public static Player getPlayer(GameUserInterface gui, int num) {
		
		if (CupsAI.learningAI == null) {
			// NO GUI for the learning AI
			CupsAI.learningAI = new CupsAI(null, num);
		}
		
		// create an AI that cheats off of us by reading our cups.
		return new AIReader(gui, CupsAI.learningAI.cups);
	}
	
	/**
	 * Let's create a thread and have our learningAI run on that.
	 */
	public void learn() {
		
		if (CupsAI.learningAI == null) {
			System.out.println("ERROR! There should be a learning AI");
			return;
		}
		
		// Here we have a thread to continually learn in the background!!
		Thread thread = new Thread(new Runnable() {
			public void run() {
				System.out.println("Learning!");
				int opponent = (CupsAI.opponentSmart ? 2 : 3);
				CupsAI[] aiPlayers = { CupsAI.learningAI, new CupsAI(null, opponent) };
						
				// let's just play games and learn forever
				for (;;) {
					TicTacToe.playGame(aiPlayers);
				}
			}
		});
		
		thread.start();
		
		// Sleep 1 second to allow the AI to learn for just a bit!
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return "The Artificial Intelligence";
	}
	
	public void tellPlayerResult(int winner) {
		
		// let's have a dumb AI. If playernum == 3, don't learn
		if (getPlayerNum() == 3) {
			return;
		}
		
		// we need the cups to learn. Tell each cup that we won/lost
		// get an iterator of our cups
		boolean wonGame = (winner == getPlayerNum());
		if (CupsAI.drawIsWin) {
			// this will consider a draw (no winner) to be a win.
			wonGame |= (winner == 0);
		}
		for (Map.Entry<String, Cup> entry : cups.entrySet()) {
			entry.getValue().learn(wonGame);
		}
	}
	
	public Move getMove(Board board) {
		String boardKey = board.toString();
		Cup cup = cups.get(boardKey);
		
		// if we don't already have this cup, add one.
		if (cup == null) {
			cup = new Cup(board);
			cups.put(boardKey, cup);
		}
		
		int answer = cup.getRandomMove();
		
		// never do a gui thingy. Don't display the move.
		// we let the AIReader do that.
		// we may be a dumb AI, so get the correct player number if we are dumb.
		int player = (this.getPlayerNum() == 3 ? 2 : this.getPlayerNum());
		
		return new Move(answer / 3, answer % 3, player);
	}
}
