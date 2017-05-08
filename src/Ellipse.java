

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * A concrete class implements Style class and paints the pits and marbles.
 * 
 * @author Jiajun Wu, Medha Korrapati, Hien Nguyen
 * May 6, 2017
 */
public class Ellipse implements Style {

	private static final Color PIT_COLOR = Color.GREEN;
	
	@Override
	public Shape getPit() {
		return new Ellipse2D.Double(0, 0, 60, 80);
	}

	@Override
	public Shape getMancala(Player p) {
		return new RoundRectangle2D.Double(0, 0, 50, 200, 70, 70);
	}

	@Override
	public Color getPitColor() {
		return PIT_COLOR;
	}

}
