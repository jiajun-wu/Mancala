import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * This class implements the Styles interface. It draws 12 pits in square
 * shapes, each pit with 4 stones, and 2 mancala.
 * 
 * @author Hien Nguyen
 */
public class Square implements Styles {

	/**
	 * Draw 12 pits, each pit with 4 stones, and 2 mancala
	 * 
	 * @param g2
	 *            Graphics2D object
	 */

	@Override
	public void draw(Graphics2D g2) {
		// draw labels
		for (int i = 6; i > 0; i--) {
			String labelB = "B" + (7 - i);
			g2.setFont(new Font("Arial", Font.BOLD, 33));
			g2.setColor(Color.BLUE);
			g2.drawString(labelB, 110 + (100 * i), 150);

			String labelA = "A" + i;
			g2.setFont(new Font("Arial", Font.BOLD, 33));
			g2.drawString(labelA, 110 + (100 * i), 550);

		}
		// draw mancalaA
		RoundRectangle2D mancalaA = new RoundRectangle2D.Float(800, 215, 70, 250, 70, 70);
		g2.setColor(Color.CYAN);
		g2.fill(mancalaA);
		g2.draw(mancalaA);

		/* draw pits for player A */
		for (int i = 1; i <= 6; i++) {
			RoundRectangle2D pitA = new RoundRectangle2D.Float(100 + (100 * i), 440, 70, 70, 10, 10);
			g2.setColor(Color.CYAN);
			g2.fill(pitA);
			g2.draw(pitA);
			for (int j = 1; j <= 2; j++) {
				// draw 2 stones for each pit
				Ellipse2D.Double stoneA11 = new Ellipse2D.Double(200 + (20 * j) + (100 * (i - 1)), 460, 10, 10);
				g2.setColor(Color.GRAY);
				g2.fill(stoneA11);
				g2.draw(stoneA11);
			}
			for (int j = 1; j <= 2; j++) {
				// draw 2 stones for each pitch
				Ellipse2D.Double stoneA11 = new Ellipse2D.Double(200 + (20 * j) + (100 * (i - 1)), 480, 10, 10);
				g2.setColor(Color.GRAY);
				g2.fill(stoneA11);
				g2.draw(stoneA11);
			}
		}

		/* draw 6 pits for player B */
		for (int i = 1; i <= 6; i++) {
			RoundRectangle2D pitB6 = new RoundRectangle2D.Float(100 + (100 * i), 160, 70, 70, 10, 10);
			g2.setColor(Color.CYAN);
			g2.fill(pitB6);
			g2.draw(pitB6);
			for (int j = 1; j <= 2; j++) {
				// draw 2 stones for each pit
				Ellipse2D.Double stoneA11 = new Ellipse2D.Double(200 + (20 * j) + (100 * (i - 1)), 170, 10, 10);
				g2.setColor(Color.GRAY);
				g2.fill(stoneA11);
				g2.draw(stoneA11);
			}
			for (int j = 1; j <= 2; j++) {
				// draw 2 stones for each pit
				Ellipse2D.Double stoneA11 = new Ellipse2D.Double(200 + (20 * j) + (100 * (i - 1)), 190, 10, 10);
				g2.setColor(Color.GRAY);
				g2.fill(stoneA11);
				g2.draw(stoneA11);
			}
		}

		// draw mancalaB
		RoundRectangle2D mancalaB = new RoundRectangle2D.Float(83, 215, 70, 250, 70, 70);
		g2.setColor(Color.CYAN);
		g2.fill(mancalaB);
		g2.draw(mancalaB);

	}
}
