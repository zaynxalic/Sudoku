package application;

public class Sudoku {
	public static boolean isEqualsToSudokuSolution(int[][] storeArray) {
		boolean flag = true;
		int[][] sk = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (storeArray[i][j] < 0) {
					sk[i][j] = 0;
				} else {
					sk[i][j] = storeArray[i][j];
				}
			}
		}
		if (solve(sk, 0, 0)) {
			for (int x = 0; x < 9; x++) {
				for (int y = 0; y < 9; y++) {
					if (Math.abs(storeArray[x][y])!= sk[x][y]) {
						flag = false;
					}
				}
			}
		}
		return flag;
	}

	static boolean solve(int[][] sk, int row, int col) {
		if (col > 8) {
			col = 0;
			row++;
		}
		if (row > 8) {
			return true;
		}
		/*If the current box has a solution, 
		then the operation of next box is performed.*/
		if (sk[row][col] > 0) {
			return solve(sk, row, ++col); 			
		}
		F: for (int i = 1; i <= 9; i++) { //start test numbers
			// If the current row and column have duplicate numbers
			for (int temp = 0; temp < 9; temp++) {
				if (sk[row][temp] == i || sk[temp][col] == i) {
					continue F; // went directly to the next loop
				}
			}
			// If the current 3*3 box has duplicate numbers, then the next loop
			int xMax = row / 3 * 3 + 3;
			int yMax = col / 3 * 3 + 3;
			for (int x = row / 3 * 3; x < xMax; x++)
				for (int y = col / 3 * 3; y < yMax; y++)
					if (sk[x][y] == i) {
						continue F;
					}
			// After all the measurements, fill the i value into the array.
			sk[row][col] = i;

			if (solve(sk, row, col + 1)) {
				return true;
			} else {
				sk[row][col] = 0;
			}
			/*If you can go to this step, it means that the next number has failed to fill, 
			so you should reset the current array and continue the loop measurement.*/
		}
		return false;
	}

	public static int countOfSudoku(int[][] sudoku) {
		int count = 0;
		int row = 0;
		int col = 0;
		//initialize count, row, col to be 0
		int[][] sk = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				sk[i][j] = Math.abs(sudoku[i][j]) ;
				//Assign the absolute value of sudoku array to the sk array
			}
		}
		//count is the solution of the countOfSudoku
		F:for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if(sk[i][j]==0) {
					row = i;
					col = j;
				}
				break F;
			}
		}
		//get the 0 value of the button row = i,col = j
		if (row < 9 && col < 9) {
			//recursion of solve of sudoku
			if (solve(sk, row, col)) {
				count++;
			}
			row++;
			if(row==8) {
				col++;
			}
		}
		return count;
	}
}