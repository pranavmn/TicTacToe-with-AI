import java.util.*;

public class GameTreeAI extends Player {
	
	private Map<String, Node> treeNodes = new HashMap<String, Node>();
	private Node root;
	private GameTreeAI(GameUserInterface gui, int num) {
		super(gui, num);
	}

	public static Player getPlayer(GameUserInterface gui, int num) {
		return new GameTreeAI(gui, num);
	}
	
	public String toString() {
		return "Game Tree AI";
	}
	
	private void createGameTree(Node node, Board board) {
		Move[] moves = board.getAllMoves();
		for(int i = 0; i < moves.length; i++) {
			Node childNode = new Node(board.toString());
			treeNodes.put(board.toString(), node);
			
			node.children.add(childNode);
			
			childNode.move = moves[i];
			
			
			board.doMove(moves[i]);
			
			
			int winner = board.findWinner();
			if(board.findWinner() != 0 || board.isBoardFull()) {
				childNode.updateStatistics(winner);
			} else {
				createGameTree(childNode, board);
			}
			board.undoMove(moves[i]);
		}
		
		node.finalizeStatistics();
	}
	
	public void learn() {
		Board board = new Board();
		root = new Node(board.toString());
		//board = board.copy
		createGameTree(root, board);
		
		System.out.println(root.toString());
	}
	
	public Move getMove(Board board) {
		// TODO: Student must fill in this code
		
		// once we get our move from our Game Tree
		// we must show the move and return it
		// find the node 
		//
		//Node node = findNode(root, board.toString());
		Node node = treeNodes.get(board.toString());

		for(int i = 0; i < node.children.size(); i++) {
			System.out.println(node.children.get(i).getBoard());
		}
		Move bestMove = node.getBestMove();
		//node.pickBestMove(board);
	
		
		getGui().showAIMove(bestMove);
		
		System.out.println(bestMove.toString());
		// TODO: Student should replace this return statement
		return bestMove;

	}
	
	
}
