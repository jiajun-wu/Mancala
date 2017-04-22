import java.util.ArrayList;
import java.util.Hashtable;

public class BoardModel {

	private static final int MAX_UNDO = 3;
	public static final int BOARD_SIZE = 6;

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

		h = new Hashtable<>();
		ArrayList<Integer> stonesListA = new ArrayList<>(BOARD_SIZE + 1);
		ArrayList<Integer> stonesListB = new ArrayList<>(BOARD_SIZE + 1);

		for (int i = 0; i < BOARD_SIZE; i++) {
			stonesListA.set(i, stones);
			stonesListB.set(i, stones);
		}

		h.put(playerA, stonesListA);
		h.put(playerB, stonesListB);
	}

	private int stones;
	private ArrayList<Player> players;
	private Player currentPlayer;

	Player playerA;
	Player playerB;
	Hashtable<Player, ArrayList<Integer>> h;
	Hashtable<Player, ArrayList<Integer>> lastStep;

	private ArrayList<Integer> mancalas = new ArrayList<>((BOARD_SIZE + 1) * 2);

	private boolean checkEmptyPits() {
		boolean flag = false;
		int counter = 0;

		ArrayList<Integer> mypits = h.get(currentPlayer);
		for (int i = 0; i < BOARD_SIZE; i++)
			if (mypits.get(i) == 0)
				counter++;

		if (counter == BOARD_SIZE)
			flag = true;

		return flag;
	}

	/**
	 * determine whether current player can undo previous step
	 */
	public void tryUndo() {
		if (currentPlayer.getUndo_Counter() < MAX_UNDO && currentPlayer.isUndo() == false) {
			undo();
		}
	}

	private void undo() {
		currentPlayer.setUndo(true);
		int currentUndoCounter = currentPlayer.getUndo_Counter();
		currentPlayer.setUndo_Counter(++currentUndoCounter);
		
		h = (Hashtable<Player, ArrayList<Integer>>) lastStep.clone();
	}

	public void play() {
		if (currentPlayer == null)
			currentPlayer = playerA;

		// back up last step in case of undo
		Hashtable<Player, ArrayList<Integer>> lastStep = (Hashtable<Player, ArrayList<Integer>>) h.clone();

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

		checkEmptyPits();
		switchPlayer();
	}
}
