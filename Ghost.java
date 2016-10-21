import java.awt.*;
import java.util.*;
import java.util.List;
import java.awt.image.*;
import java.io.File;

import javax.imageio.ImageIO;

public class Ghost {
	public static final int rad = 30;
	public static final int[] path = {};
	public int node;

	private Board b;
	private Point p;
	private BufferedImage img;

	public Ghost(Board b, int n, String filename) {
		this.b = b;
		node = n;
		p = Board.getCoorOnBoard(b.f.coordinateAt(n));
		try {
			img = ImageIO.read(new File(filename));
		} catch (Exception e) {
			System.out.println("Image doesn't exists");
			e.printStackTrace();
		}
	}

	private int[] dist;
	private int[] prev;

	public void dijkstra(int source) {
		dist = new int[b.f.dfsOrder.length+1];
		prev = new int[b.f.dfsOrder.length+1];

		PriorityQueue<DistTuple> q =
			new PriorityQueue<>(b.f.dfsOrder.length, new Comparator<DistTuple>() {
				public int compare(DistTuple d1, DistTuple d2) {
					return d1.dis - d2.dis;
				}
			});

		for (int v: b.f.dfsOrder) {
			if (v != source) {
				dist[v] = Integer.MAX_VALUE;
				prev[v] = -1;
			}
			q.add(new DistTuple(v, dist[v]));
		}

		//q.add(new DistTuple(source, 0));
		while (q.size() != 0) {
			DistTuple u = q.poll();
			//for (neighbor v of u) {
			for (int v: b.f.getAdj(u.node)) {
				if (v != -1 && q.contains(new DistTuple(v, 0))) {
					int alt = dist[u.node] + 50;
					if (alt < dist[v]) {
						dist[v] = alt;
						prev[v] = u.node;
						DistTuple d = new DistTuple(v, alt);
						q.remove(d);
						q.add(d);
					}
				}
			}
			// System.out.print(u.node + " ");
		}
		// System.out.println();
	}

	public List<Integer> pathTo(int des) {
		List<Integer> l = new LinkedList<>();
		int u = des;

		//l.add(des);
		while (prev[u] != -1) {
			l.add(0, u);
			u = prev[u];
			//System.out.println(u + " " + prev[u]);
		}
		return l;
	}

	public void update(List<Integer> l, int i) {
		if (i >= l.size())
			return;
		 node = l.get(i);
		 p = Board.getCoorOnBoard(b.f.coordinateAt(node));
	}

	public void moveLeft() {
		int l = b.f.getLeft(node);
		Point t = b.f.coordinateAt(l);
		if (t != null) {
			node = l;
			p = Board.getCoorOnBoard(t);
		}
	}

	public void moveRight() {
		int l = b.f.getRight(node);
		Point t = b.f.coordinateAt(l);
		if (t != null) {
			node = l;
			p = Board.getCoorOnBoard(t);
		}
	}

	public void moveUp() {
		int l = b.f.getUp(node);
		Point t = b.f.coordinateAt(l);
		if (t != null) {
			node = l;
			p = Board.getCoorOnBoard(t);
		}
	}

	public void moveDown() {
		int l = b.f.getLeft(node);
		Point t = b.f.coordinateAt(l);
		if (t != null) {
			node = l;
			p = Board.getCoorOnBoard(t);
		}
	}

	public void paint(Graphics2D g) {
		g.setPaint(Color.green);
		g.drawImage(img, p.x-rad/2, p.y-rad/2, p.x+rad/2, p.y+rad/2,
				0, 0, img.getWidth(), img.getHeight(), null);
	}

	static class DistTuple {
		public int node;
		public int dis;
		public DistTuple(int n, int d) {
			node = n;
			dis = d;
		}
		@Override
		public String toString() {
			return "(" + node + ", " + dis + ")";
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
			if (this == obj)
				return true;
			if (obj == null || getClass() != obj.getClass())
				return false;
			DistTuple other = (DistTuple) obj;
			if (node != other.node)
				return false;
			return true;
		}
	}
}
