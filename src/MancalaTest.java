
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * CS151
 * 
 * @author Hien Nguyen Date: 4/14/17
 */
public class MancalaTest {

	public static void main(String[] args) {
		final int SIZE = 1000;

		// Create a frame
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());

		// Create buttons for selection of styles
		JButton ellipseButton = new JButton("Ellipse");
		JButton squareButton = new JButton("Square");

		frame.add(ellipseButton);
		frame.add(squareButton);

		// to display the frame and its components to the screen
		frame.setVisible(true);
		// to close all threads when click close
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// to make the screen just fit its components
		frame.pack();

		// display game board with ellipse pit shape after user selection
		ellipseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ellipseButton == e.getSource()) {
					ellipseButton.setVisible(false);
					squareButton.setVisible(false);
				}
				Context context = new Context(new Ellipse(), SIZE, SIZE);
				// Convert context to type JLabel in order for it to work with
				// type Container
				final JLabel label = new JLabel(context);
				// Get content pane of frame
				Container contentPane = frame.getContentPane();
				// Set the layout of contentPane
				contentPane.setLayout(new FlowLayout());
				// add the clockPanel to contentPane
				contentPane.add(label);
				// to display the frame and its components to the screen
				frame.setVisible(true);
				// to close all threads when click close
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				// to make the screen just fit its components
				frame.pack();
			}
		});

		// display game board with circle pit shape after user selection
		squareButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (squareButton == e.getSource()) {
					ellipseButton.setVisible(false);
					squareButton.setVisible(false);
				}
				Context context = new Context(new Square(), SIZE, SIZE);
				// Convert context to type JLabel in order for it to work with
				// type Container
				final JLabel label = new JLabel(context);
				// Get content pane of frame
				Container contentPane = frame.getContentPane();
				// Set the layout of contentPane
				contentPane.setLayout(new FlowLayout());
				// add the clockPanel to contentPane
				contentPane.add(label);
				// to display the frame and its components to the screen
				frame.setVisible(true);
				// to close all threads when click close
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				// to make the screen just fit its components
				frame.pack();
			}
		});

	}
}
