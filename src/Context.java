
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;

/**
 * Create the context for selected game board style to be drawn.
 * 
 * @author Hien Nguyen
 *
 */
public class Context implements Icon {
	private Styles style;
	private int width;
	private int height;

	public Context(Styles style, int width, int height) {

		this.style = style;
		this.width = width;
		this.height = height;
	}

	/**
	 * Get height of the icon
	 * 
	 * @return height the height of the icon
	 */
	@Override
	public int getIconHeight() {
		return height;
	}

	/**
	 * Get width of the icon
	 * 
	 * @return width the width of the icon
	 */
	@Override
	public int getIconWidth() {
		return width;
	}

	/**
	 * Paint the game board with 12 pits, labels, stones, and 2 mancala
	 * 
	 * @param c
	 *            Component object
	 * @param g
	 *            Graphics object
	 * @param x
	 *            location of the icon on x axis
	 * @param y
	 *            location of the icon on y axis
	 */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D) g;

		// Draw string MANCALA B
		g2.setFont(new Font("Arial", Font.BOLD, 33));
		g2.drawString("M", 5, 160);
		g2.drawString("A", 5, 210);
		g2.drawString("N", 5, 260);
		g2.drawString("C", 5, 310);
		g2.drawString("A", 5, 360);
		g2.drawString("L", 5, 410);
		g2.drawString("A", 5, 460);
		g2.drawString("B", 5, 550);

		// Draw string MANCALA A
		g2.drawString("M", 918, 160);
		g2.drawString("A", 918, 210);
		g2.drawString("N", 918, 260);
		g2.drawString("C", 918, 310);
		g2.drawString("A", 918, 360);
		g2.drawString("L", 918, 410);
		g2.drawString("A", 918, 460);
		g2.drawString("A", 918, 550);

		// draw an arrow for player B
		g2.drawString("<", 310, 80);
		g2.setStroke(new BasicStroke(5));
		g2.drawLine(310, 67, 410, 67);

		// draw string Player B
		g2.drawString("PLAYER B", 430, 80);

		// draw string Player A
		g2.drawString("PLAYER A", 320, 633);

		// draw an arrow for player A
		g2.setStroke(new BasicStroke(5));
		g2.drawLine(500, 620, 600, 620);
		g2.drawString(">", 590, 633);

		// draw the game board surface
		RoundRectangle2D gameBoard = new RoundRectangle2D.Float(50, 100, 849, 480, 50, 50);
		g2.draw(gameBoard);

		// draw 12 pits, labels, and 2 mancala
		style.draw(g2);
	}

}
