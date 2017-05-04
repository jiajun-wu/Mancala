

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MancalaTester implements ActionListener {

	static JButton ellipse_3;
	static JButton ellipse_4;
	static JButton rectangel_3;
	static JButton rectangel_4;
	static JFrame gameFrame;

	public static void main(String[] args) {

		MancalaTester mt = new MancalaTester();

		gameFrame = new JFrame("Game luncher");
		gameFrame.setSize(250, 200);
		gameFrame.setLayout(new FlowLayout());
		ellipse_3 = new JButton("Ellipse with 3 stones");
		ellipse_3.setActionCommand("e3");
		ellipse_3.addActionListener(mt);

		ellipse_4 = new JButton("Ellipse with 4 stones");
		ellipse_4.setActionCommand("e4");
		ellipse_4.addActionListener(mt);

		rectangel_3 = new JButton("Rectangle with 3 stones");
		rectangel_3.setActionCommand("r3");
		rectangel_3.addActionListener(mt);

		rectangel_4 = new JButton("Rectangle with 4 stones");
		rectangel_4.setActionCommand("r4");
		rectangel_4.addActionListener(mt);

		gameFrame.add(ellipse_3);
		gameFrame.add(ellipse_4);
		gameFrame.add(rectangel_3);
		gameFrame.add(rectangel_4);

		gameFrame.setLayout(new FlowLayout());
		gameFrame.setResizable(false);
		gameFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		getCommand(e.getActionCommand());
	}

	private void getCommand(String type) {
		BoardModel bm = null;
		switch (type.charAt(0)) {
		// if it starts with an 'e' than crate ellipse shape pits
		case 'e':
			Ellipse es = new Ellipse();
			bm = new BoardModel(es);
			break;

		// if it starts with an 'r' than crate square shape pits
		case 'r':
			Square rs = new Square();
			bm = new BoardModel(rs);
			break;

		default:
			break;
		}

		BoardView bv = new BoardView(bm);
		bm.addChangeListener(bv);
		if (((Character) type.charAt(1)).equals('3'))
			// create pits with expected number of marbles
			bm.initMarblesToPits(3);
		else if (((Character) type.charAt(1)).equals('4'))
			// create pits with expected number of marbles
			bm.initMarblesToPits(4);
	}
}
