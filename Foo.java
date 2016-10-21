import java.util.Arrays;
class Node {
	public int val;
	public Node next;
	public Node(int v, Node n) {
		val = v;
		next = n;
	}
}
class List {
	Node head;
	public void insert(int v) {
		if (head == null) {
			head = new Node(v, null);
		} else {
			Node temp = head, prev = null;
			while (temp != null && temp.val < v) {
				prev = temp;
				temp = temp.next;
			}
			if (prev == null) {
				head = new Node(v, head);
			} else {
				prev.next = new Node(v, prev.next);
			}
		}
	}
	public void remove(int node) {
		Node temp = head, prev = null;
		while (temp != null && temp.val != node) {
			prev = temp;
			temp = temp.next;
		}
		if (temp != null) {
			if (prev != null)
				prev.next = temp.next;
			else
				head = head.next;
		}
	}
	public boolean isEmpty() {
		return head == null;
	}
	public void display() {
		Node temp = head;
		while (temp != null) {
			System.out.print(temp.val + " ");
			temp = temp.next;
		}
	}
}
class Point {
	public int x;
	public int y;
	public Point() {}
	public Point(int x, int y) {
		this.x = x; this.y = y;
	}
	public Point avg(Point p) {
		return new Point((x+p.x)/2, (y+p.y)/2);
	}
	@Override
	public String toString() {
		return x + ", " + y;
	}
}

public class Foo {
	private List[] graph;
	private int s;
	private int w;
	private int h;
	
	public int[] dfsOrder;
	public int dfsCount;

	public Foo(int w, int h) {
		graph = new List[w*h+1];
		dfsOrder = new int[w*h];
		dfsCount = 0;
		this.s = w*h;
		this.w = w;
		this.h = h;

		for (int i = 1; i <= s; i++)
			graph[i] = new List();

		for (int i = 0; i < h; i++) {
			for (int j = i*w+1; j <= (i+1)*w-1; j++) {
				graph[j].insert(j+1);
			}
			for (int j = (i+1)*w-1; j > i*w+1; j--) {
				graph[j].insert(j-1);
			} 
			graph[(i+1)*w].insert((i+1)*w-1);
		}
		for (int j = 1; j <= w; j++) {
			for (int i = 0; i < h-1; i++) {
				graph[i*w+j].insert((i+1)*w+j);
			}
			for (int i = 1; i < h; i++) {
				graph[i*w+j].insert((i-1)*w+j);
			}
		}
	}

	public void dfs(int i) {
		int[] visit = new int[w*h+1];
		dfsCount = 0;
		dfs(i, visit);
	}

	private void dfs(int i, int[] v) {
		v[i] = 1;
		dfsOrder[dfsCount++] = i;

		for (Node n = graph[i].head; n != null; n = n.next) {
			if (v[n.val] == 0)
				dfs(n.val, v);
		}
	}

	public void display() {
		for (int i = 1; i <= s; i++) {
			System.out.print(i + " : ");
			if (graph[i] != null)
				graph[i].display();
			System.out.println();
			if (i % w == 0) {
				System.out.println();
			}
		}
	}
	
	public Point coordinateAt(int node) {
		if (node < 1 || node > s/* || graph[node].isEmpty()*/) 
			return null;
		Point p = new Point();
		p.x = (node-1) % w;
		p.y = (node-1) / w;
		return p;
	}

	public void remove(int node) {
		for (Node adjNode = graph[node].head; adjNode != null; adjNode = adjNode.next) {
			graph[adjNode.val].remove(node);
		}
		graph[node].head = null;
	}

	public int getLeft(int node) {
		if (node > 1 && !graph[node-1].isEmpty() && node % w != 1) 
			return node - 1;
		else
			return -1;
	}

	public int getRight(int node) {
		if (node < s && !graph[node+1].isEmpty() && (node-1) % w != w-1) 
			return node + 1;
		else
			return -1;
	}

	public int getUp(int node) {
		if (node > w && !graph[node-w].isEmpty()) 
			return node - w;
		else
			return -1;
	}

	public int getDown(int node) {
		if (node <= s-w && !graph[node+w].isEmpty()) 
			return node + w;
		else
			return -1;
	}
	
	public int[] getAdj(int n) {
		int[] l = {getLeft(n), getRight(n), getUp(n), getDown(n)};
		return l;
	}

	public static void main(String[] args) {
		Foo f = new Foo(10, 4);	// Put height and width here.
		f.remove(1);
		f.remove(2);
		f.remove(3);
		f.display();
		f.dfs(4);
		System.out.println(Arrays.toString(f.dfsOrder));
	}
}
