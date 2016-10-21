import java.awt.*;

import javax.swing.*;

public class PacmanGame extends JFrame {
	public int boardWidth;
	public int boardHeight;
	
	public PacmanGame() {
		boardWidth = 22 * Board.grid;
		boardHeight = 17 * Board.grid;

		initUI();
	}
	
	private void initUI() {
		Board canvas = new Board(this);
        add(canvas, BorderLayout.CENTER);

		setTitle("Pacman");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(boardWidth, boardHeight);
		//setSize(getMaximumSize());
		setResizable(false);
		getContentPane().setBackground(Color.black);
		setLocationRelativeTo(null);
		setVisible(true); 

		canvas.createBufferStrategy(2);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					PacmanGame ex = new PacmanGame();
					ex.setVisible(true);
				}
			});
	}
}
