

import java.awt.Color;
import java.awt.Shape;
/**
 * Interface for Mancala style
 * 
 * @author Jiajun Wu, Medha Korrapati, Hien Nguyen
 * May 6, 2017
 */
public interface Style {
	
	Shape getPit();
	
	Color getPitColor();

	Shape getMancala(Player p);
}
