import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.List;
import java.util.LinkedList;

import javax.swing.*;

public class Board extends Canvas implements KeyListener {
	public static final int grid = 30;
	public Foo f;

	private int boardWidth;
	private int boardHeight;
	private final int[] wall = {39, 40, 41, 62, 83, 104, 125, 146, 145, 144, 123, 102, 
			81, 60, 188, 209, 230, 251, 272, 293, 292, 291, 270, 249, 228, 207, 186, 187, 
			23, 24, 25, 46, 67, 88, 109, 130, 129, 128, 107, 86, 65, 44, 170, 171, 172, 
			193, 214, 235, 256, 277, 276, 275, 254, 233, 212, 191, 278, 279, 280, 282, 
			283, 284, 285, 286, 288, 289, 290, 216, 237, 238, 239, 240, 241, 243, 244, 
			245, 246, 247, 226, 27, 28, 29, 30, 31, 33, 34, 35, 36, 37, 48, 90, 111, 132, 
			153, 174, 58, 100, 121, 142, 163, 184, 155, 176, 197, 198, 199, 200, 201, 202, 
			203, 182, 161, 140, 139, 134, 135, 92, 71, 72, 73, 74, 75, 76, 77, 98};
	private Pacman p;
	
	private List<Food> food;
	private final int[] noFood = {136, 137, 138, 156, 157, 158, 159, 160, 177, 178, 179, 180, 181};
	
	private Ghost[] ghosts;
	private List<Integer>[] paths;
	private int[] ghostPathIndex;
	private static final int nOG = 2;
	
	private Timer t;
	
	boolean repaintInProgress = false;

	Board(PacmanGame g) {
		// so ignore System's paint request I will handle them
		setIgnoreRepaint(true);

		this.boardWidth = g.boardWidth;
		this.boardHeight = g.boardHeight;
		f = new Foo(g.boardWidth / grid - 1, g.boardHeight / grid - 2); // -1 is needed.
		for (int i : wall) {	// Remove node at the gn wall in the graph
			f.remove(i);
		}
		f.dfs(1);	// Create array of nodes.
		// creates Pacman and Ghosts at the given coordinates
		p = new Pacman(this, 21);
		ghosts = new Ghost[nOG];
		ghosts[0] = new Ghost(this, 158, "ghosts_red_new.jpg");
		ghosts[1] = new Ghost(this, 137, "ghosts_blue_new.jpg");
		paths = (List<Integer>[]) new List[nOG];
		ghostPathIndex = new int[nOG];
		
		for (int i = 0; i < nOG; i++) {
			ghosts[i].dijkstra(20);//ghosts are directed to node 20, first.
			paths[i] = ghosts[i].pathTo(20);
		}
		food = new LinkedList<>();
		for (int i: f.dfsOrder) {
			if (i != 0)
				food.add(new Food(this, i));
		}
		for (int i : noFood) {
			food.remove(new Food(this, i));
		}
		/*for (int i1: paths[0]) {
			System.out.print("v = " + i1 + ", ");
		}
		System.out.println();*/
		g.addKeyListener(this);
				
		t = new Timer(75, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Board.this.myRepaint();
			}
		});
		t.start();
	}
	
	private boolean[] b = new boolean[]{true, true};
	
	public void myRepaint() {
		if(repaintInProgress)
			return;
		repaintInProgress = true;

		BufferStrategy strategy = getBufferStrategy();
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		
		if (food.size() == 0) {
			gameOver(g, true);
		} else if (ghosts[0].node == p.n || ghosts[1].node == p.n) {
			gameOver(g, false);
		} else {
			Food cmp = new Food(this, p.n);
			if (food.contains(cmp)) {
				food.remove(cmp);
			}
			switch (p.dir) {
				case Pacman.LEFT:
					p.moveLeft();
					//g1.moveLeft();
					break;
				case Pacman.RIGHT:
					p.moveRight();
					//g1.moveRight();
					break;
				case Pacman.UP:
					p.moveUp();
					//g1.moveUp();
					break;
				case Pacman.DOWN:
					p.moveDown();
					//g1.moveDown();
					break;
			}
			for (int i = 0; i < nOG; i++) {
				if(paths[i].size() - ghostPathIndex[i] <= 3) {
					ghosts[i].dijkstra(ghosts[i].node);
					paths[i] = ghosts[i].pathTo(p.n);
					ghostPathIndex[i] = 1;
				}
				if (b[i]) 
					ghosts[i].update(paths[i], ghostPathIndex[i]++);
				b[i] = !b[i];
			}
			paint(g);
		}

		if(g != null)
			g.dispose();
		strategy.show();
		Toolkit.getDefaultToolkit().sync();

		repaintInProgress = false;
	}

	private void paint(Graphics2D g) {
		g.setPaint(Color.black);
		g.fillRect(0, 0, boardWidth, boardHeight);
		/*g.setPaint(Color.red);
		for (int i: f.dfsOrder) {
			Point p = f.coordinateAt(i);
			if (p != null) {
				p = getCoorOnBoard(p);
				Ellipse2D.Double circle = 
					new Ellipse2D.Double(p.x-3, p.y-3, 6, 6);
				g.fill(circle);
				g.drawString(i + "", p.x-10, p.y);
			}
		}*/
		g.setPaint(Color.red);
		for (Food f: food) {
			f.paint(g);
		}
		g.setPaint(new Color(10, 10, 240));
		for (int i: wall) {
			Point p = f.coordinateAt(i);
			if (p != null) {
				p = getCoorOnBoard(p);
				g.fillRect(p.x-grid/2, p.y-grid/2, grid, grid);
			}
		}
		p.paint(g);
		for (int i = 0; i < nOG; i++)
			ghosts[i].paint(g);
	}

	private void gameOver(Graphics2D g, boolean won) {
		g.setPaint(Color.black);
		g.fillRect(0, 0, boardWidth, boardHeight);
		
		g.setPaint(Color.red);
		g.setFont(new Font("Arial", Font.BOLD, 35));
		g.drawString("Game Over!!!", this.boardWidth/2-100, this.boardHeight/2-25);
		if (won) {
			g.drawString("You Won!!!", this.boardWidth/2-80, this.boardHeight/2+25);
		} else {
			g.drawString("You Lost!!!", this.boardWidth/2-80, this.boardHeight/2+25);
		}
		t.stop();
	}

	public void keyReleased(KeyEvent e) {}

	public void keyPressed(KeyEvent e) {
		//System.out.println("keyPressed : " + e);
		switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				//p.moveLeft();
				p.changeDir(Pacman.LEFT);
				break;
			case KeyEvent.VK_RIGHT:
				//p.moveRight();
				p.changeDir(Pacman.RIGHT);
				break;
			case KeyEvent.VK_UP:
				//p.moveUp();
				p.changeDir(Pacman.UP);
				break;
			case KeyEvent.VK_DOWN:
				//p.moveDown();
				p.changeDir(Pacman.DOWN);
				break;
		}
	}

	public void keyTyped(KeyEvent e) {}

	public static Point getCoorOnBoard(Point p) {
		return new Point(p.x * grid + 2 * grid / 2, 
			p.y * grid + 2 * grid / 2);
	}
}
