

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Random;

import javax.swing.JComponent;

/**
 * To create a Shape type pit
 * @author Jiajun Wu, Medha Korrapati, Hien Nguyen
 *
 */
public class Pit extends JComponent {
	private static final int STONE_HEIGHT = 10;

	private static final Color STONE_COLOR = Color.BLUE;

	private int marbles;
	private int pitNum;
	private Player player;
	private Style style;

	public Pit(int n, int pitNum, Player player, Style style) {
		this.marbles = n;
		this.pitNum = pitNum;
		this.player = player;
		this.style = style;
	}

	public Player getPlayer() {
		return player;
	}

	public void setMarbles(int n) {
		marbles = n;
	}

	public int getMarbles() {
		return marbles;
	}

	public int getPitNum() {
		return pitNum;
	}

	public boolean isEmpty() {
		if (marbles == 0)
			return true;
		else
			return false;
	}

	public Style getBoard() {
		return style;
	}

	public Shape drawAPit(Style style) {
		return style.getPit();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(style.getPitColor());
		g2.fill(drawAPit(style));

		// draw marbles within the pit
		for (int i = 0; i < getMarbles(); i++) {
			Ellipse2D.Double stone = new Ellipse2D.Double(
					0 + new Random().nextDouble() * (drawAPit(style).getBounds().width - 2 * STONE_HEIGHT),
					0 + new Random().nextDouble() * (drawAPit(style).getBounds().height - 2 * STONE_HEIGHT),
					STONE_HEIGHT, STONE_HEIGHT);
			g2.setColor(STONE_COLOR);
			g2.fill(stone);
			g2.draw(stone);
		}
	}
}