/* 
Keegan McGrath
6/27/16
Summer Project
*/





public class SudokuPuzzle {
	private int[][] grid;

    public SudokuPuzzle() {
    	this.grid = new int[9][9];
    }

    // Creates a SudokuPuzzle object from a 2D array.  Thows an IllegalArgumentException
    // if the 2D array doesn't have 9 rows or columns, contains a character that isn't a number 0-9,
    // or if there is a sudoku conflict in the 2D array.
    public SudokuPuzzle(int[][] grid) throws IllegalArgumentException {
    	if (grid.length != 9) {
            String message = String.format("Invalid number of rows (%d). Sudoku puzzle must have 9 rows.", grid.length);
            throw new IllegalArgumentException(message);
        }
        for (int row = 0; row < grid.length; row++) {
            if (grid[row].length != 9) {
                String message = String.format("Invalid number of columns (%d). Sudoku puzzle must have 9 columns.", grid[row].length);
                throw new IllegalArgumentException(message);
            }
        }

        int[][] currentGrid = copyGrid(grid);
    	int currentCell;
    	for (int row = 0; row < 9; row++) {
    		for (int col = 0; col < 9; col++) {
    			currentCell = currentGrid[row][col];

                if (0 > currentCell || 9 < currentCell) {
                    String message = String.format("Value (%d) at (%d, %d) must be between 0 and 9.", currentCell, row, col);
                    throw new IllegalArgumentException(message);
                }

    			if (currentCell != 0) {

	    			//Check if there are any conflicts in the row of the currentCell
	    			for (int nextCols = col + 1; nextCols < 9; nextCols++) {
	    				if (currentCell == currentGrid[row][nextCols]) {
	    					String message = String.format("Value (%d) at (%d, %d) already exists in row %d at (%d, %d).", currentCell, row, nextCols, row, row, col);
	    					throw new IllegalArgumentException(message);
	    				}
	    			}

	    			//Check if there are any conflicts in the column of the currentCell
	    			for(int nextRows = row + 1; nextRows < 9; nextRows++) {
	    				if (currentCell == currentGrid[nextRows][col]) {
                            String message = String.format("Value (%d) at (%d, %d) already exists in column %d at (%d, %d).", currentCell, nextRows, col, col, row, col);
	    					throw new IllegalArgumentException(message);
	    				}
	    			}
	    			
	    			//Check if there are any conflicts in the subgrid of the currentCell
	    			int originalSubRow = (row / 3) * 3;
	    			int originalSubCol = (col / 3) * 3;
	    			for (int subGridRow = originalSubRow; subGridRow < (originalSubRow + 3); subGridRow++) {
	    				for (int subGridCol = originalSubCol; subGridCol < (originalSubCol + 3); subGridCol++) {
	    					if ((subGridRow != row && subGridCol != col) && currentCell == currentGrid[subGridRow][subGridCol]) {
                                String message = String.format("Value (%d) at (%d, %d) already exists in subgrid %d, %d at (%d, %d).", currentCell, subGridRow, subGridCol, row / 3, col / 3, row, col);
	    						throw new IllegalArgumentException(message);
	    					}
	    				}
	    			}
	    		}
    		}
    	}
    	this.grid = currentGrid;
    }

    // Returns the current SudokuPuzzle object's entry at the row r and column c as an integer.
    public int getEntry(int r, int c) throws IllegalArgumentException {
		return this.grid[r][c];
    }

    // Sets the current SudokuPuzzle object's entry at the row r and column c to the integer
    // value if there are no conflicts in the same row, column, and subgrid.  May throw an 
    // IllegalArgumentException if r or c isn't a number 0-8, if value isn't a number 0-9, 
    // or if there is a sudoku conflict in the row, column, or subgrid of the new value.
    public void setEntry(int r, int c, int value) throws IllegalArgumentException {
    	if (value < 0 || value > 9) {
            String message = String.format("Value (%d) at (%d, %d) must be between 0 and 9.", value, r, c);
    		throw new IllegalArgumentException(message);
    	}

        if (value == 0) {
            this.grid[r][c] = value;
            return;
        }

        

        //Check if there are any conflicts in the row in which we want to chang a value.
        for (int nextCols = 0; nextCols < 9; nextCols++) {
            if (value == this.grid[r][nextCols] && nextCols != c) {
                String message = String.format("Value (%d) at (%d, %d) already exists in row %d at (%d, %d).", value, r, c, r, r, nextCols);
                throw new IllegalArgumentException(message);
            }
        }

        //Check if there are any conflicts in the column in which we want to chang a value.
        for(int nextRows = 0; nextRows < 9; nextRows++) {
            if (value == this.grid[nextRows][c] && nextRows != r) {
                String message = String.format("Value (%d) at (%d, %d) already exists in column %d at (%d, %d).", value, r, c, c, nextRows, c);
                throw new IllegalArgumentException(message);
            }
        }
        
        //Check if there are any conflicts in the subgrid in which we want to change a value.
        int originalSubRow = (r / 3) * 3;
        int originalSubCol = (c / 3) * 3;
        for (int subGridRow = originalSubRow; subGridRow < (originalSubRow + 3); subGridRow++) {
            for (int subGridCol = originalSubCol; subGridCol < (originalSubCol + 3); subGridCol++) {
                if ((subGridRow != r && subGridCol != c) && value == this.grid[subGridRow][subGridCol]) {
                    String message = String.format("Value (%d) at (%d, %d) already exists in subgrid %d, %d at (%d, %d).", value, r, c, r / 3, c / 3, subGridRow, subGridCol);
                    throw new IllegalArgumentException(message);
                }
            }
        }
    
		this.grid[r][c] = value;
    }

    // This returns a boolean, true if the the current grid has nonzero numbers in every cell,
    // and false if any of the cells contain a 0.
    public boolean isSolved() {
    	for (int row = 0; row < 9; row++) {
    		for (int col = 0; col < 9; col++) {
    			if (this.grid[row][col] == 0) {
    				return false;
    			}
    		}
    	}
    	return true;
    }

    // This returns a 2D array of the current SudokuPuzzle object.
    public int[][] toArray() {
        int[][] grid = new int[9][9];
    	grid = copyGrid(this.grid);
        return grid;
    }

    // This returns a separate 2D array with a different pointer.
    public int[][] copyGrid(int[][] grid) {
        int[][] puzzle = new int[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                puzzle[row][col] = grid[row][col];
            }
        }
        return puzzle;
    }

    // This prints the current SudokuPuzzle object in an easy to read format.
    public String toString() {
    	String puzzle;
    	puzzle = "    0 1 2   3 4 5   6 7 8\n";
        puzzle = puzzle + "   -----------------------\n";
        for(int row = 0; row < 9; row++) {
            puzzle = puzzle + ((row) + " | ");
            for (int col = 0; col < 9; col++) {
                if (this.grid[row][col] == 0) {
                    puzzle = puzzle + " ";
                }
                else {
                    puzzle = puzzle + (this.grid[row][col]);
                }
                puzzle = puzzle + (" ");
                if ((col + 1) == 3 || (col + 1) == 6) {
                    puzzle = puzzle + ("| ");
                }
                else if ((col + 1) == 9) {
                	puzzle = puzzle + ("|\n");
                } 
            }
            if ((row + 1) % 3 == 0 && (row + 1) != 9) {
                puzzle = puzzle + ("  |-----------------------|\n");
            }
            
        }
        puzzle = puzzle + ("   -----------------------");
        return puzzle;
    }

    // This method alters a SudokuPuzzle object to its solved puzzle.
    public void solve(SudokuPuzzle grid) {

        // This creates a 2D array to record the positions of the immutable numbers.
        int[][] immutableGrid = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (grid.getEntry(r, c) != 0) {
                    immutableGrid[r][c] = 1;
                }
            }
        }


        System.out.println(grid);
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (r == 9) {
                    break;
                }
                

                // This attempts to set the value of each cell to the numbers 1-9 in order.
                // If the value is valid, it moves to the next changeable cell and repeats.
                // If the value is invalid, setEntry throws an illegalArgumentException.
                // If there are more values to try (values < 9), then it increments values and tries again.
                // If all values have been exhausted (values == 9), the cell is set to 0 and backtracking occurs.
                // It moves to the last changeable cell and attempts to set the value of that cell to its current value + 1
                // This process loops until all cells have a valid value.
                for (int values = grid.getEntry(r, c); values < 10; values++) {
                    
                    try {

                        // If the program has backtracked to a cell with a 9 in it. it must backtrack again
                        // since this cell has no more possible values, so an exception is manually thrown.
                        if (values == grid.getEntry(r, c)) {
                            if (values == 9) {
                                throw new IllegalArgumentException();
                            } else {
                                values = values + 1;
                            }
                        }

                        // If the cell is immutable, we continue to the next cell by breaking the loop.
                        // This handles if the first cell is immutable.
                        if (immutableGrid[r][c] == 1) {
                            break;
                        }

                        grid.setEntry(r, c, values);
                        if (r == 8 && c == 8) {
                            break;
                        }

                        //Going forward, this sets r and c to the next changable cell, accounting for edge cases.
                        boolean immutableBool = false;
                        while (immutableBool == false) {
                            if (c == 8) {
                                if (immutableGrid[r + 1][0] == 1) {
                                    r = r + 1;
                                    c = 0;
                                } else {
                                    r = r + 1;
                                    c = -1;
                                    immutableBool = true;
                                }
                            } else {
                                if (immutableGrid[r][c + 1] == 1) {
                                    c = c + 1;
                                } else {
                                    immutableBool = true;
                                }
                            }
                        }
                        break;
                    }

                    catch (IllegalArgumentException exception) {
                        grid.setEntry(r, c, 0);
                        if (values == 9) {

                            //Going backward, this sets r and c to the last changable cell, accounting for edge cases.
                            boolean immutableBool = false;
                            while (immutableBool == false) {
                                if (c == 0) {
                                    if (immutableGrid[r - 1][8] == 1) {
                                        r = r - 1;
                                        c = 8;
                                    } else {
                                        r = r - 1;
                                        c = 7;
                                        immutableBool = true;
                                    }
                                }
                                else {
                                    if (immutableGrid[r][c - 1] == 1) {
                                        c = c - 1;
                                    } else {
                                        c = c - 2;
                                        immutableBool = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}