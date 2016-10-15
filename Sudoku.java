/* 
Keegan McGrath
7/12/16
Summer Project
*/

import java.lang.*;
import java.io.*;
import java.util.*;
import java.text.ParseException;


// This class solves sudoku puzzles and either prints the solved puzzle to the console 
// or writes the solved puzzle to a file.
public class Sudoku {
	public static void main(String[] args) {
		if (args.length == 0 || args.length > 3 || (args.length == 3 && !args[2].equals("--debug"))) {
	        System.out.println("usage: java Sudoku <infile> [<outfile>] [--debug]");
	        return;
	    }
		try{
			int [][] gridArray = Sudoku.readGrid(args[0]);
	        SudokuPuzzle grid = Sudoku.getPuzzle(gridArray);
	        grid.solve(grid);

	        if (args.length == 1) {
	        	System.out.println(grid);
	        }

	        else if (args.length == 2) {
				if (args[1] == "--debug") {
					//print to console
					System.out.println(grid);
				}
				else {
					//write to file
					Sudoku.writeGrid(args[1],grid.toArray());
				}
	        }

	        else if (args.length == 3) {
	        	//write to file
	        	Sudoku.writeGrid(args[1],grid.toArray());
	        }
	    }
    	catch (Exception exception) {
    		if (args[args.length - 1].equals("--debug")) {
    			exception.printStackTrace();
    		}
    		else {
    			System.out.println(exception.getMessage());
    		}
    	}
	}

	// Returns a 2D integer array from a file containing a rectangular grid or the numbers 0-9.
	// Accepts the filename as input and may throw a SudokuException if the grid in the file
	// is not rectangular or if the file contains characters that aren't the numbers 0-9.
	public static int[][] readGrid(String fileName) throws SudokuException {
		List<int[]> puzzle = new ArrayList<int[]>();
		try{
			BufferedReader br = null;
			String sCurrentLine;
			br = new BufferedReader(new FileReader(fileName));
	        int i = 0;
	        sCurrentLine = br.readLine();
	        int firstLineLength = sCurrentLine.length();
	        while (sCurrentLine != null) {
	        	if (firstLineLength != sCurrentLine.length()) {
	        		String message = String.format("Differing row lengths (%d, %d) found in file (%s).", firstLineLength, sCurrentLine.length(), fileName);
	        		throw new ParseException(message, i);
	        	}
	        	int[] lineArray = new int[sCurrentLine.length()];
	            for (int a = 0; a < sCurrentLine.length(); a++) {
	                char currentChar = sCurrentLine.charAt(a);
	                String character = Character.toString(currentChar);
	                if (!character.matches(".*\\d.*")) {
	                	String message = String.format("Non-numeric character (%c) found in file (%s).", currentChar, fileName);
	                	throw new CharConversionException(message);
	                }
	                lineArray[a] = Character.getNumericValue(currentChar);
	            }
	            i++;
	            puzzle.add(lineArray);
	            sCurrentLine = br.readLine();
		    }
        	if (br != null) br.close();

        	int[][] grid = new int[puzzle.size()][puzzle.get(0).length];
	    	for(int row = 0; row < puzzle.size(); row++) {
	    		for (int col = 0; col < puzzle.get(0).length; col++) {
	    			grid[row][col] = puzzle.get(row)[col];
	    		}
	    	}
	    	return grid;
    	}
    	catch (Exception exception) {
    		String message = exception.getClass().getSimpleName() + " while reading file (" + fileName + "): " + exception.getMessage();
    		throw new SudokuException(message, exception);
    	}
	}

	// Returns a SudokuPuzzle object from a 2D integer array.  May throw a 
	// SudokuException if the 2D array contains any sudoku conflicts
	public static SudokuPuzzle getPuzzle(int[][] grid) throws SudokuException {
		try{
			SudokuPuzzle puzzle = new SudokuPuzzle(grid);
			return puzzle;
		} 
		catch (Exception exception) {
			String message = exception.getClass().getSimpleName() + " while creating Sudoku puzzle: " + exception.getMessage();
    		throw new SudokuException(message, exception);
		}
	}

	// Takes a 2D integer array and an output file name and writes the 2D array to a new file.
	// May throw a SudokuException.
	public static void writeGrid(String fileName, int[][] grid) throws SudokuException {
		try{
			BufferedWriter bw = null;
	        bw = new BufferedWriter(new FileWriter(fileName));
	        for (int r = 0; r < 9; r++) {
	            for (int c = 0; c < 9; c++) {
	                bw.write(Integer.toString(grid[r][c]));
	            }
	            bw.newLine();
	        }
	        if (bw != null) bw.close();
    	}
        catch (Exception exception) {
    		String message = exception.getClass().getSimpleName() + " while writing file (" + fileName + "): " + exception.getMessage();
    		throw new SudokuException(message, exception);
    	}
	}
}