package edu.pcc.mth261;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * 
 */
public class RowReducer {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		try (final Scanner scanner = new Scanner(System.in)) {
			
			System.out.println("Row Reducer by Bradley Odell");
			printHelp();
			
			while (true) {
				System.out.print(">");
				final String line = scanner.nextLine();
				if (line == null || line.isEmpty()) {
					continue;
				}
				final String lower = line.toLowerCase();
				switch (lower) {
					case "ref": {
						final Matrix matrix = readMatrix(scanner);
						if (matrix != null) {
							System.out.println("Row reducing matrix to Row Echelon Form:");
							System.out.println(matrix.printToString());
							final Matrix result = matrix.getREF().round(4);
							System.out.println("Matrix in Row Echelon Form:");
							System.out.println(result.printToString());
						}
						break;
					}
					case "rref": {
						final Matrix matrix = readMatrix(scanner);
						if (matrix != null) {
							System.out.println("Row reducing matrix to Reduced Row Echelon Form:");
							System.out.println(matrix.printToString());
							final Matrix result = matrix.getRREF().round(4);
							System.out.println("Matrix in Reduced Row Echelon Form:");
							System.out.println(result.printToString());
						}
						break;
					}
					case "exit": {
						System.out.println("Quitting...");
						return;
					}
					default: {
						System.out.println("Unknown command: \"" + line + "\"");
						printHelp();
						break;
					}
				}
				
			}
			
		}
		
	}
	
	/**
	 * 
	 */
	private static void printHelp() {
		System.out.println("~~~ Help ~~~");
		System.out.println("ref: Row reduces a matrix to Row Echelon Form.");
		System.out.println("rref: Row reduces a matrix to Reduced Row Echelon Form.");
		System.out.println("exit: Terminates application.");
		System.out.println("~~~~~~~~~~~~");
	}
	
	/**
	 * 
	 * @param scanner
	 * @return
	 */
	private static Matrix readMatrix(final Scanner scanner) {
		
		Integer columnCount = null;
		final ArrayList<double[]> rows = new ArrayList<>();
		
		System.out.println("Enter the rows of the matrix as comma separated values.");
		System.out.println("Enter an empty line to finish.");
		
		String line;
		while ((line = scanner.nextLine()) != null && !line.isEmpty()) {
			final String[] split = line.split(Pattern.quote(","));
			//
			final ArrayList<Double> valueList = new ArrayList<>(split.length);
			for (final String s : split) {
				if (s == null || s.isEmpty()) {
					continue;
				}
				try {
					valueList.add(Double.parseDouble(s));
				} catch (NumberFormatException nfe) {
					System.out.println("Error: Unable to parse value: \"" + s + "\"");
					return null;
				}
			}
			final int newColumnCount = valueList.size();
			if (columnCount != null) {
				if (columnCount != newColumnCount) {
					if (newColumnCount > columnCount) {
						System.out.println("Error: Too many columns, must have " + columnCount + " columns.");
					} else if (newColumnCount < columnCount) {
						System.out.println("Error: Not enough columns, must have " + columnCount + " columns.");
					}
					return null;
				}
			} else {
				if (newColumnCount < 1) {
					System.out.println("Error: Not enough columns.");
					return null;
				}
			}
			columnCount = newColumnCount;
			final double[] row = new double[newColumnCount];
			for (int i = 0; i < newColumnCount; i++) {
				row[i] = valueList.get(i);
			}
			rows.add(row);
		}
		final int rowCount = rows.size();
		if (rowCount <= 0 || columnCount == null) {
			System.out.println("Error: No rows provided.");
			return null;
		}
		return new Matrix(rows.toArray(new double[rowCount][]));
	}
	
}
