import java.awt.*;

class Pacman {
	private Foo f;
	private static final int rad = 30;
	private static final int speed = 1;
	//private boolean fdir;

	public int n;
	public Point p;
	public int dir;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int UP = 3;
	public static final int DOWN = 4;

	public Pacman(Board b, int n) {
		this.f = b.f;
		this.n = n;
		Point p = f.coordinateAt(n);
		/*this.p = new Point(p.x * Board.grid + Board.grid / 2, 
			p.y * Board.grid + 3 * Board.grid / 2);*/
		this.p = Board.getCoorOnBoard(p);
	}

	public void paint(Graphics2D g) {
		g.setPaint(Color.yellow);
		//g.drawLine(p.x, p.y, p.x+rad/2, p.y-rad/2);
		if (dir == RIGHT)
			g.fillArc(p.x-rad/2, p.y-rad/2, rad, rad, 45, 270);
		else if (dir == LEFT)
			g.fillArc(p.x-rad/2, p.y-rad/2, rad, rad, 45+180, 270);
		else if (dir == UP)
			g.fillArc(p.x-rad/2, p.y-rad/2, rad, rad, 45+90, 270);
		else if (dir == DOWN)
			g.fillArc(p.x-rad/2, p.y-rad/2, rad, rad, 45+270, 270);
		else 
			g.fillArc(p.x-rad/2, p.y-rad/2, rad, rad, 45, 270);
		//g.fillOval(p.x-rad/2, p.y-rad/2, rad, rad);
	}

	//int len = 0;

	public void moveLeft() {
		//if (dir != temp)
		//	dir = temp;
		int l = f.getLeft(n);
		Point t = f.coordinateAt(l);
		if (t != null) {
			n = l;
			p = Board.getCoorOnBoard(t);
			dir = LEFT;
			//fdir = false;
		}
	}
	
	public void moveRight() {
		//if (dir != temp)
		//	dir = temp;
		int l = f.getRight(n);
		Point t = f.coordinateAt(l);
		if (t != null) {
			n = l;
			p = Board.getCoorOnBoard(t);
			dir = RIGHT;
			//fdir = true;
		}
	}

	public void moveUp() {
		//if (dir != temp)
		//	dir = temp;
		int l = f.getUp(n);
		Point t = f.coordinateAt(l);
		if (t != null) {
			n = l;
			p = Board.getCoorOnBoard(t);
			dir = UP;
		}
	}

	public void moveDown() {
		//if (dir != temp)
		//	dir = temp;
		int l = f.getDown(n);
		Point t = f.coordinateAt(l);
		if (t != null) {
			n = l;
			p = Board.getCoorOnBoard(t);
			dir = DOWN;
		}
	}

	private int temp = 0;
	
	public void changeDir(int dir) {
		this.dir = dir;
	}
}
