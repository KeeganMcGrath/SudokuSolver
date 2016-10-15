/* 
Keegan McGrath
7/21/16
Summer Project
Teacher: Garrett
Sudoku2
*/


// Creates an exception class to be used with SudokuPuzzle objects.
public class SudokuException extends Exception {
	public SudokuException() {
		super();
	}

	public SudokuException(String message) {
		super(message);
	}

	public SudokuException(String message, Throwable cause) {
		super(message, cause);
	}
}