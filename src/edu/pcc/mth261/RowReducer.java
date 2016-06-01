package edu.pcc.mth261;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

import edu.pcc.mth261.Matrix.RowReductionStep;

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
				switch (line.toLowerCase()) {
					case "ref": {
						final Matrix matrix = readMatrix(scanner);
						if (matrix != null) {
							System.out.println("Row reducing matrix to Row Echelon Form:");
							System.out.println(matrix.printToString());
							final Matrix result = processMatrix(matrix::getREF);
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
							final Matrix result = processMatrix(matrix::getRREF);
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
	
	private static void printHelp() {
		System.out.println("~~~ Help ~~~");
		System.out.println("ref: Row reduces a matrix to Row Echelon Form.");
		System.out.println("rref: Row reduces a matrix to Reduced Row Echelon Form.");
		System.out.println("exit: Terminates application.");
		System.out.println("~~~~~~~~~~~~");
	}
	
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
	
	private static Matrix processMatrix(final Function<Consumer<RowReductionStep>, Matrix> func) {
		class Step {
			public int number;
		};
		final Step s = new Step();
		s.number = 1;
		return func.apply((final RowReductionStep step) -> {
			System.out.println("Step " + s.number + ": " + step.getMessage());
			System.out.println(step.getAfterMatrix().printToString());
			s.number++;
		});
	}
	
}
