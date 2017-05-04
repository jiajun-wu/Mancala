

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * Mancala is a pit; however, shape and size will be a little different
 * @author Jiajun Wu
 *
 */
public class Mancala extends Pit {

	public Mancala(int n, int getPitNum, Player player, Style style) {
		super(n, getPitNum, player, style);
	}

	public Player getPlayer() {
		return super.getPlayer();
	}

	public void setMarbles(int n) {
		super.setMarbles(n);
	}

	public int getMarbles() {
		return super.getMarbles();
	}

	public int getPitNum() {
		return super.getPitNum();
	}

	public boolean isEmpty() {
		return super.isEmpty();
	}

    public Shape drawAPit(Style style) {
        return style.getMancala(getPlayer());
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.draw(drawAPit(getBoard()));
    }
}