import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

class Food {
	private int node;
	private Board b;
	private boolean eaten = false;
	public Food(Board b, int n) {
		this.b = b;
		node = n;
		eaten = false;
	}
	public void eat() {
		eaten = true;
	}
	public boolean isEaten() {
		return eaten;
	}
	public void paint(Graphics2D g) {
		if (eaten) return;
		Point p = b.f.coordinateAt(node);
		if (p != null) {
			p = Board.getCoorOnBoard(p);
			Ellipse2D.Double circle = new Ellipse2D.Double(p.x-3, p.y-3, 6, 6);
			g.fill(circle);
		}
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + node;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj )
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Food other = (Food) obj;
		if (node != other.node)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Food=" + node;
	}
}