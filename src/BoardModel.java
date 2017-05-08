

import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Model for the Mancala game, basically all the logics goes to here
 * @author Jiajun Wu, Medha Korrapati, Hien Nguyen
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

	/**
	 * draw marbles to pits
	 * @param marbles the number of marbles need to be drawn
	 */
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

	/**
	 * move marbles to following pits
	 * @param currentPit the pit index which the player selected
	 * @param numberOfMarbles
	 */
	public void moveMarbles(int currentPit, int numberOfMarbles) {
		while (numberOfMarbles > 0) {
			if (currentPit == PLAYER_A_STORE_INDEX - 1 && compareCurrentPlayer(playerB))
				// playerB will past playerA's store, instead of putting marble
				currentPit += 2;
			else if (currentPit == PLAYER_B_STORE_INDEX - 1 && compareCurrentPlayer(playerA))
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

	/**
	 * find which pit has the last marble dropped
	 * 
	 * @param pit 
	 * @return index of the pit which last marble was dropped
	 */
	public int whereIsTheLastDroppedMarble(Pit pit) {
		int currentPitMarbles = pit.getMarbles();
		int lastPit = pit.getPitNum();
		while (currentPitMarbles > 0) {
			if (lastPit == PLAYER_A_STORE_INDEX - 1 && compareCurrentPlayer(playerA))
				// playerB will past playerA's store, instead of putting marble
				lastPit += 2;
			else if (lastPit == PLAYER_B_STORE_INDEX - 1 && compareCurrentPlayer(playerB))
				lastPit += 2;
			else
				lastPit++;

			if (lastPit == PLAYER_B_STORE_POSITION)
				lastPit = 0;

			currentPitMarbles--;
		}
		return lastPit;
	}

	/**
	 * get opponent's marbles
	 * @param lastMarbleDropped
	 */
	private void getOppoMarbles(int lastMarbleDropped) {
		int mancala = BOARD_SIZE;
		if (lastMarbleDropped == PLAYER_A_STORE_INDEX || lastMarbleDropped == PLAYER_B_STORE_INDEX)
			return;
		else if (pits.get(lastMarbleDropped).getMarbles() == 1 && compareCurrentPlayer(pits.get(lastMarbleDropped).getPlayer())) {
			
			if (pits.get(PLAYER_B_STORE_INDEX - 1 - lastMarbleDropped).getMarbles() == 0)
				return;
			if (compareCurrentPlayer(playerA))
				mancala = PLAYER_A_STORE_INDEX;
			else
				mancala = PLAYER_B_STORE_INDEX;

			pits.get(mancala)
					.setMarbles(pits.get(mancala).getMarbles()
							+ pits.get(PLAYER_B_STORE_INDEX - 1 - lastMarbleDropped).getMarbles()
							+ pits.get(lastMarbleDropped).getMarbles());
//			pits.get(mancala).setMarbles(pits.get(mancala).getMarbles() + pits.get(lastMarbleDropped).getMarbles());
			
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
		if (!compareCurrentPlayer(pitIndex.getPlayer()))
			return;

		// if current pit has zero marble, skip
		if (pitIndex.getMarbles() == 0)
			return;

		backUpCurrentStatusForUndo();

		hasFreeRun = getFreeRound(pitIndex);
		
		moveMarbles(pitIndex.getPitNum(), pitIndex.getMarbles());

		// check if current player can get opponent's marbles follows by game rule
		getOppoMarbles(whereIsTheLastDroppedMarble(pitIndex));
		
		// set zero marble to current pit after moving marbles
		pitIndex.setMarbles(0);

		if (isGameOver())
			wipeBoard();

		if (!hasFreeRun)
			switchPlayer();

		updateView();
	}

	/**
	 * back up the current status of the Mancala game, 
	 * if the current player wants to undo previous step, 
	 * the new data will be covered by backup data.
	 */
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
		
		// if the last pit was dropped on player A's store, player A gets a free round
		if ((totalMoves - PLAYER_A_STORE_INDEX) % (PLAYER_B_STORE_INDEX) == 0 && compareCurrentPlayer(playerA))
			flag = true;

		// if the last pit was dropped on player B's store, player B gets a free round
		if ((totalMoves - (PLAYER_B_STORE_INDEX)) % (PLAYER_B_STORE_INDEX) == 0 && compareCurrentPlayer(playerB))
			flag = true;

		return flag;
	}

	/**
	 * compared whether a player is in his turn
	 * 
	 * @param player the player to be compared
	 * @return true is current player, false otherwise
	 */
	private boolean compareCurrentPlayer(Player player) {
		return currentPlayer.equals(player);
	}

	/**
	 * determine whether a game is game-over
	 * @return true if the game is game-over, false otherwise
	 */
	public boolean isGameOver() {
		boolean noMarblesInPitsA = true;
		for (int i = 0; i < PLAYER_A_STORE_INDEX; i++)
			if (pits.get(i).getMarbles() != 0)
				noMarblesInPitsA = false;

		boolean noMarblesInPitsB = true;
		for (int i = PLAYER_A_STORE_INDEX + 1; i < PLAYER_B_STORE_INDEX; i++)
			if (pits.get(i).getMarbles() != 0)
				noMarblesInPitsB = false;

		isGameOver = noMarblesInPitsA || noMarblesInPitsB;
		
		if (isGameOver)
			getResult();

		return isGameOver;
	}

	/**
	 * when a player clicks undo button, check whether he can do undo
	 */
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

	/**
	 * undo previous step
	 * 
	 */
	private void undo() {
		currentPlayer.setUndo(true);
		int currentPlayerUndoCounter = currentPlayer.getUndo_Counter();
		currentPlayer.setUndo_Counter(++currentPlayerUndoCounter);

		for (Pit pit : pits)
			pit.setMarbles(lastStep[pit.getPitNum()]);

		if (!hasFreeRun)
			switchPlayer();
	}

	/**
	 * get Mancala information of a player 
	 * @param player
	 * @return the player's Mancala, null if no info
	 */
	public Mancala getMancala(Player player) {
		for (Pit pit : pits)
			if (pit.getPlayer().equals(player) && pit instanceof Mancala)
				return (Mancala) pit;

		return null;
	}

	/**
	 * after a player finished his round, switch 
	 */
	private void switchPlayer() {
		if (!isGameOver) {
			if (compareCurrentPlayer(playerA)) {
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

	/**
	 * determine whether the player has previous step to undo
	 * @return
	 */
	private boolean hasLastStep() {
		for (Pit pit : pits)
			if (pit.getMarbles() != lastStep[pit.getPitNum()])
				return true;
		return false;
	}

	/**
	 * game over, Mancala gets all the marbles and clear the pits.
	 */
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

	/**
	 * get the final game result and to be shown to the board
	 */
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
	
	/**
	 * calculate the player's marbles number
	 * @param player
	 * @return the total marbles number
	 */
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