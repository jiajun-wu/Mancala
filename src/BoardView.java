

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author Jiajun Wu
 * May 4, 2017
 */
public class BoardView implements ChangeListener {
	private final BoardModel boardModel;
	private ArrayList<Pit> pits;
	final JLabel gameInfo;

	public BoardView(BoardModel bm) {
		boardModel = bm;
		pits = boardModel.getData();
		JFrame frame = new JFrame("Mancala");

		final JPanel jPanel = new JPanel(new GridLayout(0, BoardModel.BOARD_SIZE + 2));
		jPanel.add(boardModel.getMancala(boardModel.getPlayerB()));
		for (int i = 0; i < BoardModel.BOARD_SIZE; i++) {
			JPanel innerPanel = new JPanel(new GridLayout(2, 0));
			Pit playerBPit = pits.get(BoardModel.PLAYER_B_STORE_INDEX - 1 - i);
			Pit playerAPit = pits.get(i);
			
			// create playerB's pits on the top of the board and initialize mouse click event.
			innerPanel.add(playerBPit);
			playerBPit.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					boardModel.play(playerBPit);
				}
			});
			
			// create playerA's pits on the bottom of the board and initialize mouse click event.
			innerPanel.add(playerAPit);
			playerAPit.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					boardModel.play(playerAPit);
				}
			});
			
			jPanel.add(innerPanel);
		}

		JButton undoButton = new JButton("Undo");
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boardModel.tryUndo();
			}
		});

		jPanel.add(boardModel.getMancala(boardModel.getPlayerA()));
		frame.add(undoButton, BorderLayout.LINE_END);
		frame.add(jPanel, BorderLayout.CENTER);
		gameInfo = new JLabel(boardModel.getCurrentPlayer().getPlayerName());
		gameInfo.setHorizontalAlignment(JTextField.CENTER);
		frame.add(gameInfo, BorderLayout.SOUTH);
		frame.setSize(800, 300);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void stateChanged(ChangeEvent e) {
		pits = boardModel.getData();
		for (Pit pit : pits)
			pit.repaint();

		// display current game information from model
		gameInfo.setText(boardModel.getMessage());
	}
}