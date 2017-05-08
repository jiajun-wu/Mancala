

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * A concrete class implements Style class and paints the pits and marbles.
 * 
 * @author Jiajun Wu, Medha Korrapati, Hien Nguyen
 * May 6, 2017
 */
public class Square implements Style {

	private static final Color PIT_COLOR = Color.YELLOW;

	@Override
	public Shape getPit() {
		return new Rectangle2D.Double(0, 0, 80, 80);
	}

	@Override
	public Shape getMancala(Player p) {
		return new Rectangle2D.Double(0, 0, 50, 200);
	}

	@Override
	public Color getPitColor() {
		return PIT_COLOR;
	}
}
