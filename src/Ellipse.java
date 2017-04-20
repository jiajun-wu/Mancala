import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * This class implements the Styles interface. It draws 12 pits in ellipse
 * shapes, each pit with 4 stones, and 2 mancala.
 * 
 * @author Hien Nguyen
 */

public class Ellipse implements Styles {

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
			g2.setColor(Color.GREEN);
			g2.drawString(labelB, 110 + (100 * i), 150);

			String labelA = "A" + i;
			g2.setFont(new Font("Arial", Font.BOLD, 33));
			g2.drawString(labelA, 110 + (100 * i), 550);

		}

		// draw mancalaA
		RoundRectangle2D mancalaA = new RoundRectangle2D.Float(800, 215, 70, 250, 70, 70);
		g2.setColor(Color.GREEN);
		g2.fill(mancalaA);
		g2.draw(mancalaA);

		/* draw pits for player A */
		for (int i = 1; i <= 6; i++) {
			Ellipse2D.Double pitA = new Ellipse2D.Double(100 + (100 * i), 440, 60, 80);
			g2.setColor(Color.GREEN);
			g2.fill(pitA);
			g2.draw(pitA);

			// draw 4 stones for each pit
			for (int j = 1; j <= 2; j++) {
				Ellipse2D.Double stoneA_1stRow = new Ellipse2D.Double(195 + (20 * j) + (100 * (i - 1)), 460, 10, 10);
				g2.setColor(Color.GRAY);
				g2.fill(stoneA_1stRow);
				g2.draw(stoneA_1stRow);
				Ellipse2D.Double stoneA_2ndRow = new Ellipse2D.Double(195 + (20 * j) + (100 * (i - 1)), 480, 10, 10);
				g2.setColor(Color.GRAY);
				g2.fill(stoneA_2ndRow);
				g2.draw(stoneA_2ndRow);
			}
		}

		/* draw 6 pits for player B */
		for (int i = 1; i <= 6; i++) {
			Ellipse2D.Double pitB6 = new Ellipse2D.Double(100 + (100 * i), 155, 60, 80);
			g2.setColor(Color.GREEN);
			g2.fill(pitB6);
			g2.draw(pitB6);
			// draw 4 stones for each pit
			for (int j = 1; j <= 2; j++) {
				Ellipse2D.Double stoneB_1stRow = new Ellipse2D.Double(195 + (20 * j) + (100 * (i - 1)), 175, 10, 10);
				g2.setColor(Color.GRAY);
				g2.fill(stoneB_1stRow);
				g2.draw(stoneB_1stRow);
				Ellipse2D.Double stoneB_2ndRow = new Ellipse2D.Double(195 + (20 * j) + (100 * (i - 1)), 195, 10, 10);
				g2.setColor(Color.GRAY);
				g2.fill(stoneB_2ndRow);
				g2.draw(stoneB_2ndRow);
			}
		}

		// draw mancalaB
		RoundRectangle2D mancalaB = new RoundRectangle2D.Float(83, 215, 70, 250, 70, 70);
		g2.setColor(Color.GREEN);
		g2.fill(mancalaB);
		g2.draw(mancalaB);

	}
}
