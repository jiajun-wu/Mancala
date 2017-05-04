

/**
 * This class is to create a player with his name, and store important game
 * information such as: the times the player did the undo, and determine whether
 * he is the current player is player the game.
 * 
 * @author Jiajun Wu
 *
 */
public class Player {

	private String playerName;
	private int undo_Counter;
	private boolean undo;
	private boolean currentPlayer;

	/**
	 * Construct a player
	 * @param playerName
	 * @param undo_Counter
	 * @param undo
	 */
	public Player(String playerName, int undo_Counter, boolean undo) {
		this.playerName = playerName;
		this.undo_Counter = undo_Counter;
		this.undo = undo;
		this.currentPlayer = false;
	}

	public int getUndo_Counter() {
		return undo_Counter;
	}

	public void setUndo_Counter(int undo_Counter) {
		this.undo_Counter = undo_Counter;
	}

	public boolean isUndo() {
		return undo;
	}

	public void setUndo(boolean undo) {
		this.undo = undo;
	}

	public boolean isCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(boolean currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public void activateCurrentPlayer() {
		undo = false;
		currentPlayer = true;
	}

	public void deactivateCurrentPlayer() {
		undo = false;
		currentPlayer = false;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
}
