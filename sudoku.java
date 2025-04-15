public class sudoku {
    public boolean isSafe(char[][] board, int row, int col, int number) {
        int sr = 3 * (row / 3);
        int sc = 3 * (col / 3);
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == (char) (number + '0'))
                return false;
            if (board[row][i] == (char) (number + '0'))
                return false;
            if (board[sr + i / 3][sc + i % 3] == (char) (number + '0'))
                return false;
        }
        return true;
    }

    public boolean isValid(char[][] board) {
        // Check all rows
        for (int i = 0; i < 9; i++) {
            if (!isValidUnit(board[i]))
                return false;
        }

        // Check all columns
        for (int i = 0; i < 9; i++) {
            char[] col = new char[9];
            for (int j = 0; j < 9; j++) {
                col[j] = board[j][i];
            }
            if (!isValidUnit(col))
                return false;
        }

        // Check all 3x3 blocks
        for (int r = 0; r < 9; r += 3) {
            for (int c = 0; c < 9; c += 3) {
                char[] block = new char[9];
                int idx = 0;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        block[idx++] = board[r + i][c + j];
                    }
                }
                if (!isValidUnit(block))
                    return false;
            }
        }

        return true;
    }

    private boolean isValidUnit(char[] unit) {
        boolean[] seen = new boolean[9];
        for (char num : unit) {
            if (num != '.') {
                if (seen[num - '1'])
                    return false;
                seen[num - '1'] = true;
            }
        }
        return true;
    }

    public boolean solve(char[][] board, int row, int col) {
        if (row == 9)
            return true;

        int nrow = (col == 8) ? row + 1 : row;
        int ncol = (col == 8) ? 0 : col + 1;

        if (board[row][col] != '.') {
            return solve(board, nrow, ncol);
        }

        for (int i = 1; i <= 9; i++) {
            if (isSafe(board, row, col, i)) {
                board[row][col] = (char) (i + '0');
                if (solve(board, nrow, ncol))
                    return true;
                board[row][col] = '.';
            }
        }
        return false;
    }

    public void fillDiagonalBoxes(char[][] board) {
        for (int i = 0; i < 9; i += 3) {
            fillBox(board, i, i);
        }
    }

    private void fillBox(char[][] board, int row, int col) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                char num;
                do {
                    num = (char) ((int) (Math.random() * 9) + '1');
                } while (!isUnusedInBox(board, row, col, num));
                board[row + i][col + j] = num;
            }
        }
    }

    private boolean isUnusedInBox(char[][] board, int rowStart, int colStart, char num) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[rowStart + i][colStart + j] == num)
                    return false;
        return true;
    }
}
