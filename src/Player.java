public class Player {
	private int undo_Counter;
	private boolean undo;
	private boolean currentPlayer;

	public Player(int undo_Counter, boolean undo) {
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

	public void activateCurrentPlayer(){
		undo = false;
		currentPlayer = true;
	}

	public void deactivateCurrentPlayer(){
		undo = false;
		currentPlayer = false;
	}
}
