import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SudokuUI {
    private JFrame frame;
    private JTextField[][] cells = new JTextField[9][9];
    private sudoku solver = new sudoku(); // Solver logic

    public SudokuUI() {
        frame = new JFrame("Sudoku Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(9, 9));

        Font font = new Font("SansSerif", Font.BOLD, 20);
        Color darkBG = new Color(40, 45, 45);
        Color darkFG = new Color(220, 220, 220);

        // Initialize grid
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setFont(font);
                tf.setBackground(darkBG);
                tf.setForeground(darkFG);
                tf.setCaretColor(darkFG);
                tf.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                // Bold border for 3x3 blocks
                int top = (i % 3 == 0) ? 3 : 1;
                int left = (j % 3 == 0) ? 3 : 1;
                int bottom = (i == 8) ? 3 : 1;
                int right = (j == 8) ? 3 : 1;
                tf.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.LIGHT_GRAY));

                // Input restriction: only 1–9
                tf.addKeyListener(new KeyAdapter() {
                    public void keyTyped(KeyEvent e) {
                        char ch = e.getKeyChar();
                        if (!Character.isDigit(ch) || ch == '0') {
                            e.consume();
                        }
                        tf.setText(""); // clear old input
                    }
                });

                cells[i][j] = tf;
                gridPanel.add(tf);
            }
        }

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(darkBG);

        JButton solveButton = new JButton("Solve");
        JButton clearButton = new JButton("Clear");
        // JButton loadSampleButton = new JButton("Load Sample");
        JButton generateButton = new JButton("New Game");
        JButton checkButton = new JButton("Check Solution");

        solveButton.setFocusPainted(false);
        clearButton.setFocusPainted(false);
        // loadSampleButton.setFocusPainted(false);
        generateButton.setFocusPainted(false);
        checkButton.setFocusPainted(false);

        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);
        // buttonPanel.add(loadSampleButton);
        buttonPanel.add(generateButton);
        buttonPanel.add(checkButton);

        solveButton.addActionListener(e -> solveSudoku());
        clearButton.addActionListener(e -> clearBoard());
        // loadSampleButton.addActionListener(e -> loadSampleBoard());
        generateButton.addActionListener(e -> generateRandomBoard());
        checkButton.addActionListener(e -> checkSolution());

        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.getContentPane().setBackground(darkBG);
        frame.setVisible(true);

        // Load sample board when the application starts
        loadSampleBoard(); // Load the sample Sudoku on start
    }

    private void solveSudoku() {
        char[][] board = new char[9][9];

        // Read input
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String text = cells[i][j].getText().trim();
                board[i][j] = text.isEmpty() ? '.' : text.charAt(0);
            }
        }

        if (solver.solve(board, 0, 0)) {
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    cells[i][j].setText(board[i][j] + "");
        } else {
            JOptionPane.showMessageDialog(frame, "No solution found!");
        }
    }

    private void clearBoard() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                cells[i][j].setText("");
    }

    private void loadSampleBoard() {
        String[][] sample = {
                { "5", "3", ".", ".", "7", ".", ".", ".", "." },
                { "6", ".", ".", "1", "9", "5", ".", ".", "." },
                { ".", "9", "8", ".", ".", ".", ".", "6", "." },
                { "8", ".", ".", ".", "6", ".", ".", ".", "3" },
                { "4", ".", ".", "8", ".", "3", ".", ".", "1" },
                { "7", ".", ".", ".", "2", ".", ".", ".", "6" },
                { ".", "6", ".", ".", ".", ".", "2", "8", "." },
                { ".", ".", ".", "4", "1", "9", ".", ".", "5" },
                { ".", ".", ".", ".", "8", ".", ".", "7", "9" }
        };

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                cells[i][j].setText(sample[i][j].equals(".") ? "" : sample[i][j]);
    }

    private void generateRandomBoard() {
        char[][] board = new char[9][9];
        solver.fillDiagonalBoxes(board);
        solver.solve(board, 0, 0);

        // Remove 40 cells
        int removeCount = 40;
        while (removeCount > 0) {
            int i = (int) (Math.random() * 9);
            int j = (int) (Math.random() * 9);
            if (board[i][j] != '.') {
                board[i][j] = '.';
                removeCount--;
            }
        }

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++)
                cells[i][j].setText(board[i][j] == '.' ? "" : board[i][j] + "");
    }

    private void checkSolution() {
        char[][] board = new char[9][9];

        // Read input
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String text = cells[i][j].getText().trim();
                board[i][j] = text.isEmpty() ? '.' : text.charAt(0);
            }
        }

        if (solver.isValid(board)) {
            JOptionPane.showMessageDialog(frame, "The solution is correct!");
        } else {
            JOptionPane.showMessageDialog(frame, "The solution is incorrect!");
        }
    }

    public static void main(String[] args) {
        new SudokuUI();
    }
}
