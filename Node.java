import java.util.*;

public class Node {

	// TODO: Student must implement this class
	public int Xwins;
	public int Owins;
	public int draws;
	
	private String board;
	
	public ArrayList<Node> children;
	public Move move;
	
	public Node(String board) {
		this.setBoard(board);
		children = new ArrayList<Node>();
	}
	
	public void updateStatistics(int winner) {
		if(winner == 1) {
			Xwins++;
		}
		else if(winner == 2) {
			Owins++;
		}
		else {
			draws++;
		}
	}
	
	public void finalizeStatistics() {
		int[] data = new int[3];
		for(int i = 0; i < children.size(); i++) {
			data[0] += children.get(i).Xwins;
			data[1] += children.get(i).Owins;
			data[2] += children.get(i).draws;
		}
		
		Xwins = data[0];
		Owins = data[1];
		draws = data[2];
	}
	
	public String toString() {
		String output = "";
		
		output = this.Xwins + ", " + this.Owins + ", " + this.draws + ", ";
		
		return output;
	}

	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	public Move getBestMove() {
		System.out.println(this.board);
		Node bestChild = null;
		int i = 0;
		int max = -1;
		while(i < children.size()) {
			//int score = 10 * children.get(i).Xwins - 10 * children.get(i).Owins + children.get(i).draws;
			int score = children.get(i).Xwins;
			if(max < score) {
				bestChild = this.children.get(i);
				max = score;
			}
			i++;
		}
		
		Move bestMove = bestChild.move;
		
		return bestMove;
	}
}
