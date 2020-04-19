import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class Games2048 extends JFrame {
	private static final int ROWS = 4;
	private static final int COLS = 4;
	private JPanel contentPane;
	private JPanel pnlControl;
	private JPanel pnlGame;
	private JButton btnUp;
	private JButton btnDown;
	private JButton btnLeft;
	private JButton btnRight;
	private JPanel pnlGround;
	private JButton[][] buttons;
	private JLabel lblScore;
	private JButton btnNew;

	// private int[][] numbers = { { 2, 4, 8, 16 }, { 4, 8, 16, 32 }, { 8, 16, 32,
	// 64 }, { 0, 0, 64, 128 } };
	private int[][] numbers = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
	private int score = 0;
	
	public static void main(String[] args) {
		Games2048 games = new Games2048();
		games.setVisible(true);
	}

	public Games2048() {
		setTitle("2048 Version 2");
		setSize(500, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());
		pnlControl = new JPanel();
		contentPane.add(pnlControl, BorderLayout.PAGE_START);
		pnlControl.setLayout(new FlowLayout());
		btnNew = new JButton("New Game");
		lblScore = new JLabel("0");
		pnlControl.add(lblScore);
		pnlControl.add(btnNew);

		pnlGame = new JPanel();
		contentPane.add(pnlGame, BorderLayout.CENTER);

		btnUp = new JButton("Up");
		btnDown = new JButton("Down");
		btnLeft = new JButton("Left");
		btnRight = new JButton("Right");
		pnlGround = new JPanel();

		pnlGame.setLayout(new BorderLayout());

		pnlGame.add(pnlGround, BorderLayout.CENTER);

		pnlGround.setLayout(new GridLayout(ROWS, COLS));

		buttons = new JButton[ROWS][COLS];
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				buttons[i][j] = new JButton();
				buttons[i][j].setEnabled(false);
				pnlGround.add(buttons[i][j]);
			}
		}
		// Táº¡o 2 sá»‘ 2 má»›i
		newNumber();
		newNumber();
		// Update UI
		update();

		setFocusable(true);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					slideLeft();
					break;
				case KeyEvent.VK_RIGHT:
					slideRight();
					break;
				case KeyEvent.VK_DOWN:
					slideDown();
					break;
				case KeyEvent.VK_UP:
					slideUp();
					break;
				}
			}

			private void slideUp() {
				boolean isMoved = false;
				for (int j = 0; j < numbers[0].length; j++) {
					// B1:Dá»“n láº§n 1
					int dem = 0;
					for (int i = 0; i < numbers.length - dem; i++) {
						if (numbers[i][j] == 0) {
							// XÃ³a sá»‘ 0
							for (int k = i + 1; k < numbers.length; k++) {
								numbers[k - 1][j] = numbers[k][j];
								if (numbers[k][j] != 0) {
									isMoved = true;
								}

							}
							// ThÃªm sá»‘ 0 vÃ o cuá»‘i máº£ng
							numbers[3][j] = 0;
							// giá»¯ nguyÃªn vá»‹ trÃ­
							i--;
							// Ä�áº¿m sá»‘ 0
							dem++;
							isMoved = true;
						}
					}
					// B2: GhÃ©p 2 Ã´ cáº¡nh nhau
					for (int i = 0; i < numbers.length - 1; i++) {
						if (numbers[i][j] != 0 && numbers[i][j] == numbers[i + 1][j]) {
							numbers[i][j] += numbers[i + 1][j];
							numbers[i + 1][j] = 0;
							score += numbers[i][j];
						}
					}
					// B3: Dá»“n láº§n 2
					dem = 0;
					for (int i = 0; i < numbers.length - dem; i++) {
						if (numbers[i][j] == 0) {
							for (int k = i + 1; k < numbers.length; k++) {
								numbers[k - 1][j] = numbers[k][j];
							}
							numbers[3][j] = 0;
							i--;
							dem++;
						}
					}
				}
				// new 2
				if (isMoved) {
					newNumber();
					// update
					update();
					if (isGameOver()) {
						JOptionPane.showMessageDialog(Games2048.this, "Game Over");
					}
				}

			}

			private void slideDown() {
				boolean isMoved = false;
				for (int j = 0; j < numbers[0].length; j++) {
					// B1:Dá»“n láº§n 1
					int dem = 0;
					for (int i = numbers.length - 1; i > dem; i--) {

						if (numbers[i][j] == 0) {
							// XÃ³a sá»‘ 0

							for (int k = i - 1; k >= 0; k--) {
								numbers[k + 1][j] = numbers[k][j];
								if (numbers[k][j] != 0) {
									isMoved = true;
								}

							}
							// ThÃªm sá»‘ 0 vÃ o Ä‘áº§u máº£ng
							numbers[0][j] = 0;
							// Giá»¯ nguyÃªn vá»‹ trÃ­
							i++;
							// Ä�áº¿m sá»‘ 0
							dem++;

						}
					}
					// B2: GhÃ©p 2 Ã´ cáº¡nh nhau
					for (int i = numbers.length - 1; i > 0; i--) {
						if (numbers[i][j] != 0 && numbers[i][j] == numbers[i - 1][j]) {
							numbers[i][j] += numbers[i - 1][j];
							numbers[i - 1][j] = 0;
							score += numbers[i][j];
							isMoved = true;
						}
					}
					// B3: Dá»“n láº§n 2
					dem = 0;
					for (int i = numbers.length - 1; i > dem; i--) {
						if (numbers[i][j] == 0) {
							for (int k = i - 1; k >= 0; k--) {
								numbers[k + 1][j] = numbers[k][j];
							}
							numbers[0][j] = 0;
							i++;
							dem++;
						}
					}
				}
				// new 2
				if (isMoved) {
					newNumber();
					// update
					update();
					if (isGameOver()) {
						JOptionPane.showMessageDialog(Games2048.this, "Game Over");
					}
				}

			}

			private void slideRight() {
				boolean isMoved = false;
				for (int i = 0; i < numbers.length; i++) {
					// B1: Dá»“n láº§n 1
					int dem = 0;
					for (int j = numbers[i].length - 1; j > dem; j--) {
						if (numbers[i][j] == 0) {
							// XÃ³a sá»‘ 0
							for (int k = j - 1; k >= 0; k--) {
								numbers[i][k + 1] = numbers[i][k];
								if (numbers[i][j] != 0) {
									isMoved = true;
								}
							}
							// ThÃªm sá»‘ 0 vÃ o Ä‘áº§u máº£ng
							numbers[i][0] = 0;
							j++;
							dem++;
						}
					}
					// B2: GhÃ©p 2 Ã´ cáº¡nh nhau
					for (int j = numbers[i].length - 1; j > 0; j--) {
						if (numbers[i][j] != 0 && numbers[i][j] == numbers[i][j - 1]) {
							numbers[i][j] = numbers[i][j] + numbers[i][j - 1];
							numbers[i][j - 1] = 0;
							score += numbers[i][j];
						}
					}
					// B3: Dá»“n láº§n 2
					dem = 0;
					for (int j = numbers[i].length - 1; j > dem; j--) {
						if (numbers[i][j] == 0) {
							for (int k = j - 1; k >= 0; k--) {
								numbers[i][k + 1] = numbers[i][k];
							}
							numbers[i][0] = 0;
							j++;
							dem++;
						}
					}
				}
				if (isMoved) {
					newNumber();
					update();
					if (isGameOver()) {
						JOptionPane.showMessageDialog(Games2048.this, "Game Over");
					}
				}
			}

			private void slideLeft() {
				boolean isMoved = false;
				for (int i = 0; i < numbers.length; i++) {
					// B1: Dá»“n láº§n 1
					int dem = 0;
					for (int j = 0; j < numbers[i].length - dem; j++) {

						if (numbers[i][j] == 0) {
							// XÃ³a sá»‘ 0
							for (int k = j + 1; k < numbers[i].length; k++) {
								numbers[i][k - 1] = numbers[i][k];
								if (numbers[k][j] != 0) {
									isMoved = true;
								}
							}
							// ThÃªm sá»‘ 0 vÃ o cuá»‘i máº£ng
							numbers[i][3] = 0;
							// Giá»¯ nguyÃªn vá»‹ trÃ­
							j--;
							dem++;
						}
					}
					// B2: GhÃ©p 2 Ã´ cáº¡nh nhau
					for (int j = 0; j < numbers[i].length - 1; j++) {
						if (numbers[i][j] != 0 && numbers[i][j] == numbers[i][j + 1]) {
							numbers[i][j] = numbers[i][j] + numbers[i][j + 1];
							numbers[i][j + 1] = 0;
							score += numbers[i][j];
						}
					}

					// B3: Dá»“n láº§n 2
					dem = 0;
					for (int j = 0; j < numbers[i].length - dem; j++) {
						if (numbers[i][j] == 0) {

							for (int k = j + 1; k < numbers[i].length; k++) {
								numbers[i][k - 1] = numbers[i][k];
							}
							numbers[i][3] = 0;
							j--;
							dem++;
						}
					}
				}
				// new 2
				if (isMoved) {

					newNumber();
					// update
					update();
					if (isGameOver()) {
						JOptionPane.showMessageDialog(Games2048.this, "Game Over");
					}
				}
			}
		});

		btnNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
				newNumber();
				newNumber();
				update();
				Games2048.this.requestFocus();

			}
		});

	}

	// Update UI
	public void update() {
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				if (numbers[i][j] == 0) {
					buttons[i][j].setText(" ");
				} else {
					buttons[i][j].setText(String.valueOf(numbers[i][j]));
				}
			}
		}

		lblScore.setText(String.valueOf(score));
	}

	// Táº¡o 2 sá»‘ 2 á»Ÿ vá»‹ trÃ­ ngáº«u nhiÃªn
	public void newNumber() {
		int r;
		int c;
		Random random = new Random();
		r = random.nextInt(4);
		c = random.nextInt(4);
		while (numbers[r][c] != 0) {
			r = random.nextInt(4);
			c = random.nextInt(4);
		}
		numbers[r][c] = 2;
	}

	public void reset() {
		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				numbers[i][j] = 0;
				score = 0;

			}
		}
	}

	// Check end game
	public boolean isGameOver() {
		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				if (numbers[i][j] == 0) {
					return false;
				}
				if (j != 3 && numbers[i][j] == numbers[i][j + 1]) {
					return false;
				}
				if (i != 3 && numbers[i][j] == numbers[i + 1][j]) {
					return false;
				}
			}
		}
		return true;
	}
}
