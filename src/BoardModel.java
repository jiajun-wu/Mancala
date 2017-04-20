import java.util.ArrayList;

public class BoardModel {

	private static final int MAX_UNDO = 3;
	private static final int BOARD_SIZE = 6;

	public BoardModel(int stones) {
		this.stones = stones;
		for (int i = 0; i < mancalas.size(); i++)
			if (i != BOARD_SIZE || i != BOARD_SIZE * 2 + 1)
				mancalas.set(i, stones);

		players = new ArrayList<>();
		players.add(new Player(0, false));
		players.add(new Player(0, false));

		this.playerA = new Player(0, false);
		this.playerB = new Player(0, false);
	}

	private int stones;
	private ArrayList<Player> players;
	private Player currentPlayer;
	private Player playerA;
	private Player playerB;

	private ArrayList<Integer> mancalas = new ArrayList<>((BOARD_SIZE + 1) * 2);

	private boolean checkEmptyPits() {
		boolean flag = false;

		return flag;
	}

	public void tryUndo() {
		if (currentPlayer.getUndo_Counter() < MAX_UNDO && currentPlayer.isUndo() == false) {
			undo();
		}
	}

	private void undo() {
		currentPlayer.setUndo(true);
		int currentUndoCounter = currentPlayer.getUndo_Counter();
		currentPlayer.setUndo_Counter(++currentUndoCounter);

	}

	public void play() {
		
	}

	private void switchPlayer() {
		if (currentPlayer.equals(playerA))
			playerA.deactivateCurrentPlayer();
		else {
			playerB.activateCurrentPlayer();
			currentPlayer = playerB;
		}
	}

	public void endOnePlay() {
		
		switchPlayer();
	}
}
