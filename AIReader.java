import java.util.Map;

// This Artificial Intelligence cheats off another AI's cups
// that are learning. We don't learn. 
public class AIReader extends CupsAI {

	// this will be a reference to a set of cups that are learning
	private Map<String, Cup> cups;
	
	public AIReader(GameUserInterface gui, Map<String, Cup> cups) {
		super(gui, 1);
		
		// use the cups given to us
		this.cups = cups;
	}
	
	// this AI will not teach the cups anything
	public void tellPlayerResult(boolean wonGame) {
	}
	
	// get a random move from the cups, but do NOT learn
	// Do a GUI if we have it.
	public Move getMove(Board board) {
		String boardKey = board.toString();
		Cup cup = cups.get(boardKey);
		
		// if the cup doesn't exist, make up a move and do
		// not add to our list of cups.
		if (cup == null) {
			System.out.println("complete GUESS! No learning!");
			cup = new Cup(board);
		}
		
		int answer = cup.getRandomMove();
		
		if (getGui() != null) {
			// show the state and tokens before we move
			System.out.print(board);
			System.out.print(" Tokens = ");
			System.out.print(cup);
			System.out.println(" Move = " + answer);
			
			getGui().showAIMove(new Move(answer/3, answer%3));			
		}
		
		return new Move(answer / 3, answer % 3, this.getPlayerNum());
	}
}
