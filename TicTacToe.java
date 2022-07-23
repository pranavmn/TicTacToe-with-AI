import java.util.*;

public class TicTacToe {
	
	private GameUserInterface gui;
	public static boolean useGameTree = true;
	
	public static void main(String[] args) {
		TicTacToe game = new TicTacToe();
		Scanner console = new Scanner(System.in);
		
		System.out.print("GUI or console? (G/C): ");
		boolean useGUI = console.nextLine().toLowerCase().startsWith("g");
		
		System.out.print("GameTree or Cups? (G/C): ");
		useGameTree = console.nextLine().toLowerCase().startsWith("g");
		
		// let's use our GameManager object as the semaphore
		SwingUI gui = new SwingUI(game);
		
		if (useGUI) {
			game.setGameInterface(gui);
			
			// start up the UI on the EVT
			SwingUI.startUIOnEventDispatchThread(gui);
		} else {
			game.setGameInterface(new ConsoleUI(game));
		}
		
		// this could go too quickly and cause us to interact with the GUI
		// before the GUI has been fully created. So, we need to sleep a bit
		// here before we start playing the game.
		// Ideally, we'd have some other wait/notification or active wait.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		game.play();

		console.close();
	}
	
	public void setGameInterface(GameUserInterface gui) {
		this.gui = gui;
	}
	
	public void play() {
		
		// get player count
		int playerCount = gui.getPlayerCount();
		
		// start with two human Players
		Player players[] = { new Player(gui, 1), new Player(gui, 2) } ;
		
		// TODO ask for count of games to train
		// TODO show a progress bar in the UI
		// TODO Do UI to get who goes first
		// TODO Check to see which AI to create
		if (playerCount == 1) {
			Player ai = (useGameTree ? GameTreeAI.getPlayer(gui, 1) : CupsAI.getPlayer(gui, 1));
			ai.learn();
			players[0] = ai;
		}
		
		boolean keepPlaying = true;
		
		while (keepPlaying) {
			String winner = playGame(players);
			keepPlaying = gui.askPlayAgain(winner);
		}
		
		// TODO: move to GUI
		System.out.println("End of Session. Thanks for playing.");
		System.exit(0);
	}
	
	/**
	 * Plays a game with the given players and stick count
	 * @param players The players involved in the game with their own GUI
	 * @return The player that one.
	 */
	public static String playGame(Player[] players) {
		boolean gameOver = false;
		int playerNum = 0;
		Board board = new Board();
		int count = 0;
		int winnerNum = 0;
		while (!gameOver) {
			Move move = players[playerNum].getMove(board);
			board.doMove(move);
			
			// keep track of the count of moves made
			count++;
			
			// go to the next player
			playerNum = (playerNum + 1) % 2;
			
			winnerNum = board.findWinner();
			gameOver = (count == 9 || winnerNum != 0);
		}
		
		String winner;
		
		if (winnerNum == 0) {
			winner = "STALEMATE: Nobody";
		} else {
			// give winning message
			winner = players[winnerNum-1].toString();
		}
		
		// inform players of who won
		players[0].tellPlayerResult(winnerNum);
		players[1].tellPlayerResult(winnerNum);
		
		return winner;
	}

}
