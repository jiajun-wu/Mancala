

import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author Jiajun Wu
 * May 4, 2017
 */
public class BoardModel {
	private static final int MAX_UNDO = 3;
	public static final int BOARD_SIZE = 6;
	public static final int PLAYER_A_STORE_POSITION = BOARD_SIZE + 1;
	public static final int PLAYER_A_STORE_INDEX = PLAYER_A_STORE_POSITION - 1;
	public static final int PLAYER_B_STORE_POSITION = 2 * (BOARD_SIZE + 1);
	public static final int PLAYER_B_STORE_INDEX = PLAYER_B_STORE_POSITION - 1;

	private boolean hasFreeRun;
	private ArrayList<ChangeListener> listeners;
	private ArrayList<Pit> pits;
	private int[] lastStep;
	private boolean isGameOver;

	private Player playerA;
	private Player playerB;
	private Player currentPlayer;

	private String message;

	public BoardModel(Style style) {

		playerA = new Player("Player A", 0, false);
		playerB = new Player("Player B", 0, false);

		currentPlayer = playerA;
		setMessage(currentPlayer.getPlayerName());

		hasFreeRun = false;
		pits = new ArrayList<Pit>();

		// adding pits for player A
		for (int i = 0; i < PLAYER_A_STORE_INDEX; i++)
			pits.add(new Pit(0, i, playerA, style));
		pits.add(new Mancala(0, PLAYER_A_STORE_INDEX, playerA, style));

		// adding pits for player B
		for (int i = PLAYER_A_STORE_INDEX + 1; i < PLAYER_B_STORE_INDEX; i++)
			pits.add(new Pit(0, i, playerB, style));
		pits.add(new Mancala(0, PLAYER_B_STORE_INDEX, playerB, style));

		lastStep = new int[pits.size()];

		listeners = new ArrayList<ChangeListener>();

	}

	public void initMarblesToPits(int marbles) {
		int counter = 0;
		for (Pit pit : pits)
			if (!(pit instanceof Mancala)) {
				pit.setMarbles(marbles);
				lastStep[counter] = marbles;
			} else
				counter++;

		backUpCurrentStatusForUndo();
		
		ChangeEvent event = new ChangeEvent(this);
		for (ChangeListener listener : listeners)
			listener.stateChanged(event);
	}

	public void addChangeListener(ChangeListener listener) {
		listeners.add(listener);
	}

	public void moveMarbles(int currentPit, int numberOfMarbles) {
		while (numberOfMarbles > 0) {
			if (currentPit == PLAYER_A_STORE_INDEX - 1 && matchCurrentPlayer(playerB))
				// playerB will past playerA's store, instead of putting marble
				currentPit += 2;
			else if (currentPit == PLAYER_B_STORE_INDEX - 1 && matchCurrentPlayer(playerA))
				currentPit = 0;
			else
				currentPit++;

			// last marble is dropped on mancala B, set to player A 1st pit
			if (currentPit == PLAYER_B_STORE_POSITION)
				currentPit = 0;

			pits.get(currentPit).setMarbles(pits.get(currentPit).getMarbles() + 1);
			numberOfMarbles--;
		}
	}

	public int whereIsTheLastDroppedMarble(Pit pit) {
		int currentPitMarbles = pit.getMarbles();
		int currentPit = pit.getPitNum();
		while (currentPitMarbles > 0) {
			if (currentPit == PLAYER_A_STORE_INDEX - 1 && matchCurrentPlayer(playerA))
				// playerB will past playerA's store, instead of putting marble
				currentPit += 2;
			else if (currentPit == PLAYER_B_STORE_INDEX - 1 && matchCurrentPlayer(playerB))
				currentPit += 2;
			else
				currentPit++;

			if (currentPit == PLAYER_B_STORE_POSITION)
				currentPit = 0;

			currentPitMarbles--;
		}
		return currentPit;
	}

	private void getOppoMarbles(int lastMarbleDropped) {
		// TODO need to fix final numbers!!!!!!!!
		int mancala = BOARD_SIZE;
		if (lastMarbleDropped == PLAYER_A_STORE_INDEX || lastMarbleDropped == PLAYER_B_STORE_INDEX)
			return;
		else if (pits.get(lastMarbleDropped).getMarbles() == 1
				&& matchCurrentPlayer(pits.get(lastMarbleDropped).getPlayer())) {
			if (pits.get(PLAYER_B_STORE_INDEX - 1 - lastMarbleDropped).getMarbles() == 0)
				return;
			if (matchCurrentPlayer(playerA))
				mancala = PLAYER_A_STORE_INDEX;
			else
				mancala = PLAYER_B_STORE_INDEX;

			pits.get(mancala).setMarbles(pits.get(mancala).getMarbles()
					+ pits.get(PLAYER_B_STORE_INDEX - 1 - lastMarbleDropped).getMarbles());
			pits.get(mancala).setMarbles(pits.get(mancala).getMarbles() + pits.get(lastMarbleDropped).getMarbles());
			pits.get(PLAYER_B_STORE_INDEX - 1 - lastMarbleDropped).setMarbles(0);
			pits.get(lastMarbleDropped).setMarbles(0);
		}
	}

	/**
	 * when click inside the pit, algorithm will go from here 
	 * @param pitIndex
	 * 				current pit index
	 */
	public void play(Pit pitIndex) {
		
		// check if current player is trying to move his own pits
		if (!matchCurrentPlayer(pitIndex.getPlayer()))
			return;

		// if current pit has zero marble, skip
		if (pitIndex.getMarbles() == 0)
			return;

		backUpCurrentStatusForUndo();

		hasFreeRun = getFreeRound(pitIndex);
		
		moveMarbles(pitIndex.getPitNum(), pitIndex.getMarbles());

		// set zero marble to current pit after moving marbles
		pitIndex.setMarbles(0);

		// check if current player can get opponent's marbles follows by game rule
		getOppoMarbles(whereIsTheLastDroppedMarble(pitIndex));

		if (isGameOver())
			wipeBoard();

		if (!getFreeRound(pitIndex))
			switchPlayer();

		updateView();
	}

	private void backUpCurrentStatusForUndo() {
		for (Pit pit : pits)
			lastStep[pit.getPitNum()] = pit.getMarbles();
	}

	/**
	 * To evaluated whether last step is able to gain a free round
	 * @param pit
	 * @return
	 */
	private boolean getFreeRound(Pit pit) {
		boolean flag = false;
		int totalMoves = pit.getPitNum() + pit.getMarbles();
		if ((totalMoves - BOARD_SIZE) % (PLAYER_B_STORE_INDEX) == 0 && matchCurrentPlayer(playerA))
			flag = true;

		if ((totalMoves - (PLAYER_B_STORE_INDEX)) % (PLAYER_B_STORE_INDEX) == 0 && matchCurrentPlayer(playerB))
			flag = true;

		return flag;
	}

	private boolean matchCurrentPlayer(Player player) {
		return currentPlayer.equals(player);
	}

	public boolean isGameOver() {
		// TODO debug here
		boolean noMarblesInPitsA = true;
		for (int i = 0; i < BOARD_SIZE; i++)
			if (pits.get(i).getMarbles() != 0)
				noMarblesInPitsA = false;

		boolean noMarblesInPitsB = true;
		for (int i = PLAYER_A_STORE_POSITION; i < PLAYER_B_STORE_INDEX; i++)
			if (pits.get(i).getMarbles() != 0)
				noMarblesInPitsB = false;

		isGameOver = noMarblesInPitsA || noMarblesInPitsB;
		if (isGameOver)
			getResult();

		return isGameOver;
	}

	public void tryUndo() {
		if (isGameOver)
			return;

		if (!hasLastStep()) {
			setMessage("You do not have previous step status!");
			updateView();
			return;
		}

		if (currentPlayer.getUndo_Counter() < MAX_UNDO && currentPlayer.isUndo() == false) {
			undo();
		} else if(currentPlayer.getUndo_Counter() == MAX_UNDO)
			setMessage("You had used up maximum (" + MAX_UNDO + ") undo times. You can't undo!");
		else setMessage("You can't undo multiple times in a row!");

		updateView();
	}

	private void undo() {
		currentPlayer.setUndo(true);
		int currentPlayerUndoCounter = currentPlayer.getUndo_Counter();
		currentPlayer.setUndo_Counter(++currentPlayerUndoCounter);

		for (Pit pit : pits)
			pit.setMarbles(lastStep[pit.getPitNum()]);

		if (!hasFreeRun)
			switchPlayer();
	}

	public Mancala getMancala(Player player) {
		for (Pit pit : pits)
			if (pit.getPlayer().equals(player) && pit instanceof Mancala)
				return (Mancala) pit;

		return null;
	}

	private void switchPlayer() {
		if (!isGameOver) {
			if (matchCurrentPlayer(playerA)) {
				playerA.deactivateCurrentPlayer();
				playerB.activateCurrentPlayer();
				currentPlayer = playerB;
			} else {
				playerB.deactivateCurrentPlayer();
				playerA.activateCurrentPlayer();
				currentPlayer = playerA;
			}
			
			backUpCurrentStatusForUndo();
			setMessage(currentPlayer.getPlayerName());
		}
		updateView();
	}

	private boolean hasLastStep() {
		for (Pit pit : pits)
			if (pit.getMarbles() != lastStep[pit.getPitNum()])
				return true;
		return false;
	}

	private void wipeBoard() {
		for (int i = 0; i < PLAYER_A_STORE_INDEX; i++) {
			pits.get(PLAYER_A_STORE_INDEX).setMarbles(pits.get(PLAYER_A_STORE_INDEX).getMarbles() + pits.get(i).getMarbles());
			pits.get(i).setMarbles(0);
		}
		
		for (int i = PLAYER_A_STORE_INDEX + 1; i < PLAYER_B_STORE_INDEX; i++) {
			pits.get(PLAYER_B_STORE_INDEX).setMarbles(pits.get(PLAYER_B_STORE_INDEX).getMarbles() + pits.get(i).getMarbles());
			pits.get(i).setMarbles(0);
		}
	}

	private void updateView() {
		ChangeEvent event = new ChangeEvent(this);
		for (ChangeListener listener : listeners)
			listener.stateChanged(event);
	}

	private void getResult() {
		int pA = getPlayerMarbles(playerA);
		int pB = getPlayerMarbles(playerB);
		String result = "Game Over! " + getPlayerA().getPlayerName() + " has " + pA;
		result += " marbles, " + getPlayerB().getPlayerName() + " has " + pB + " marbles. ";
		
		if (pA == pB)
			setMessage(result + "Tier Game!");
		
		else if (pA > pB)
			setMessage(result + getPlayerA().getPlayerName() + " Wins!");
		
		else
			setMessage(result + getPlayerB().getPlayerName() + " Wins!");
	
		updateView();
	}
	
	private int getPlayerMarbles(Player player) {
		int count = 0;
		for (Pit pit : pits)
			if (pit.getPlayer().equals(player))
				count += pit.getMarbles();
		return count;
	}

	public ArrayList<Pit> getData() {
		return pits;
	}

	public Player getPlayerA() {
		return playerA;
	}

	public void setPlayerA(Player playerA) {
		this.playerA = playerA;
	}

	public Player getPlayerB() {
		return playerB;
	}

	public void setPlayerB(Player playerB) {
		this.playerB = playerB;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		this.message = message;
	}
}