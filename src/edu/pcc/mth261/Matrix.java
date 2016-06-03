package edu.pcc.mth261;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * An immutable matrix class.
 */
public class Matrix {
	
	/**
	 * The backing data of the matrix.
	 */
	private final double[][] data;
	
	/**
	 * Creates a zero-d square matrix with the given dimension.
	 * @param dimension the dimension of the square matrix.
	 */
	public Matrix(final int dimension) {
		if (dimension <= 0) {
			throw new IllegalArgumentException("Dimension must be positive.");
		}
		this.data = new double[dimension][dimension];
	}
	
	/**
	 * Creates a zero-d matrix with the given number of rows and columns.
	 * @param rows the number of rows of the matrix.
	 * @param columns the number of columns of the matrix.
	 */
	public Matrix(final int rows, final int columns) {
		if (rows <= 0) {
			throw new IllegalArgumentException("Rows must be positive.");
		}
		if (columns <= 0) {
			throw new IllegalArgumentException("Columns must be positive.");
		}
		this.data = new double[rows][columns];
	}
	
	/**
	 * Creates a matrix from the given multi-dimensional data array.
	 * Note: The given data is copied.
	 * @param data the data of the matrix.
	 */
	public Matrix(final double[][] data) {
		final int rows = data.length;
		if (rows <= 0) {
			throw new IllegalArgumentException("Rows must be positive.");
		}
		final int columns = data[0].length;
		if (columns <= 0) {
			throw new IllegalArgumentException("Columns must be positive.");
		}
		// Perform a deep copy on the data
		final double[][] newData = new double[rows][];
		for (int r = 0; r < rows; r++) {
			final double[] oldRow = data[r];
			if (oldRow.length != columns) {
				throw new IllegalArgumentException("Rows must not be jagged.");
			}
			final double[] newRow = new double[columns];
			System.arraycopy(oldRow, 0, newRow, 0, columns);
			newData[r] = newRow;
		}
		this.data = newData;
	}
	
	/**
	 * Copy constructor for matrix.
	 * @param matrix
	 */
	public Matrix(final Matrix matrix) {
		this(matrix.data);
	}
	
	private Matrix(final double[][] data, final boolean unused) {
		this.data = data;
	}
	
	/**
	 * Gets the number of rows in the matrix.
	 */
	public int getRows() {
		return this.data.length;
	}
	
	/**
	 * Gets the number of columns in the matrix.
	 */
	public int getColumns() {
		return this.data[0].length;
	}
	
	/**
	 * Gets a new matrix that is the Row Echelon Form of this matrix.
	 */
	public Matrix getREF() {
		return this.rowReduce(false);
	}
	
	/**
	 * Gets a new matrix that is the Reduced Row Echelon Form of this matrix.
	 */
	public Matrix getRREF() {
		return this.rowReduce(true);
	}
	
	/**
	 * Row reduces the matrix to Row Echelon Form (and optionally to Reduced Row Echelon Form).
	 */
	private Matrix rowReduce(final boolean rref) {
		final Matrix copyMatrix = new Matrix(this);
		final double[][] data = copyMatrix.data;
		final int rows = data.length;
		final int columns = data[0].length;
		
		int row = 0;
		int column = 0;
		// Forward elimination
		for (; row < rows && column < columns; column++) {
			{
				final int pivotRow;
				// Find pivot (non-zero value)
				{
					int pRow = row; // pivot row
					for (; pRow < rows; pRow++) {
						if (data[pRow][column] != 0) {
							// Found pivot, break search loop
							break;
						}
					}
					pivotRow = pRow;
				}
				// Check to see if a pivot row was found
				if (pivotRow >= rows) {
					// No pivot so skip column
					continue;
				}
				if (pivotRow != row) {
					// Pivot is not in the diagonal, interchange pivot to diagonal
					// Interchange operation
					for (int interchangeColumn = column; interchangeColumn < columns; interchangeColumn++) {
						final double oldValue = data[row][interchangeColumn];
						data[row][interchangeColumn] = data[pivotRow][interchangeColumn];
						data[pivotRow][interchangeColumn] = oldValue;
					}
				}
			}
			//
			final double diagValue = data[row][column];
			// Replace operations
			for (int replaceRow = row + 1; replaceRow < rows; replaceRow++) {
				final double replaceValue = data[replaceRow][column];
				if (replaceValue != 0) {
					final double replaceRatio = replaceValue / (-diagValue);
					for (int replaceColumn = column; replaceColumn < columns; replaceColumn++) {
						final double newValue = data[replaceRow][replaceColumn] + data[row][replaceColumn] * replaceRatio;
						data[replaceRow][replaceColumn] = normalizePrecision(newValue);
					}
				}
			}
			// Scale operation
			if (diagValue != 1.0) {
				final double scaleRatio = 1.0 / diagValue;
				for (int scaleColumn = column; scaleColumn < columns; scaleColumn++) {
					final double newValue = data[row][scaleColumn] * scaleRatio;
					data[row][scaleColumn] = normalizePrecision(newValue);
				}
			}
			row++;
		}
		
		if (rref) {
			
			// Find all pivot columns
			final int[] pivotColumns = new int[rows];
			{
				int lastPivot = -1;
				for (int r = 0; r < rows; r++) {
					int pivotColumn = columns;
					for (int c = lastPivot + 1; c < columns; c++) {
						if (data[r][c] != 0) {
							// Found pivot
							pivotColumn = c;
							break;
						}
					}
					lastPivot = pivotColumn;
					pivotColumns[r] = pivotColumn;
				}
			}
			
			// Back substitution
			row--;
			for (; row >= 0; row--) {
				final int pivotColumn = pivotColumns[row];
				if (pivotColumn < columns) {
					// Replace operations
					for (int replaceRow = row - 1; replaceRow >= 0; replaceRow--) {
						final double replaceValue = data[replaceRow][pivotColumn];
						if (replaceValue != 0) {
							for (int replaceColumn = pivotColumn; replaceColumn < columns; replaceColumn++) {
								final double newValue = data[replaceRow][replaceColumn] - data[row][replaceColumn] * replaceValue;
								data[replaceRow][replaceColumn] = normalizePrecision(newValue);
							}
						}
					}
				}
			}
			
		}
		
		return copyMatrix;
	}
	
	/**
	 * Rounds the values of the matrix to the given number of decimal places.
	 * @param decimalPlaces the number of decimal places to round the matrix values to.
	 * @return a new matrix with the values rounded.
	 */
	public Matrix round(final int decimalPlaces) {
		final Matrix copyMatrix = new Matrix(this);
		round(copyMatrix.data, decimalPlaces);
		return copyMatrix;
	}
	
	private static void round(final double[][] data, final int decimalPlaces) {
		final int rows = data.length;
		final int columns = data[0].length;
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				data[row][column] = round(data[row][column], decimalPlaces);
			}
		}
	}
	
	private static double round(final double value, final int decimalPlaces) {
		return BigDecimal.valueOf(value).setScale(decimalPlaces, RoundingMode.HALF_UP).doubleValue();
	}
	
	/**
	 * Rounds the values of the matrix to the maximum double float point precision (15 decimal places).
	 */
	public Matrix normalizePrecision() {
		final Matrix copyMatrix = new Matrix(this);
		normalizePrecision(copyMatrix.data);
		return copyMatrix;
	}
	
	private static void normalizePrecision(final double[][] data) {
		// Round to 15 decimals of precision (since this is the precision limit of a double)
		round(data, 15);
	}
	
	private static double normalizePrecision(final double value) {
		return round(value, 15);
	}
	
	/**
	 * Converts the matrix to a pretty-print string.
	 * <pre>
	 * ┌──┬──┬──┬──┬──┐
	 * │0 │1 │2 │3 │4 │
	 * ├──┼──┼──┼──┼──┤
	 * │5 │6 │7 │8 │9 │
	 * ├──┼──┼──┼──┼──┤
	 * │10│11│12│13│14│
	 * └──┴──┴──┴──┴──┘
	 * </pre>
	 */
	public String printToString() {
		final int rows = this.getRows();
		final int columns = this.getColumns();
		// Calculate cell width
		int cellWidth = 1;
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				final double n = this.data[row][column];
				cellWidth = Math.max(cellWidth, String.valueOf(n).length());
			}
		}
		final int tableWidth = 1 + (columns * cellWidth) + columns;
		final int tableHeight = 1 + (rows * 2);
		final StringBuilder b = new StringBuilder(tableWidth * tableHeight);
		int row = 0;
		for (int tableRow = 0; tableRow < tableHeight; tableRow++) {
			if ((tableRow % 2) == 0) {
				// Print separator
				if (tableRow == 0) {
					// Print table top
					b.append("┌");
					for (int column = 0; column < columns; column++) {
						for (int c = 0; c < cellWidth; c++) {
							b.append("─");
						}
						if (column + 1 < columns) {
							b.append("┬");
						}
					}
					b.append("┐");
					b.append("\n");
				} else if ((tableRow + 1) == tableHeight) {
					// Print table bottom
					b.append("└");
					for (int column = 0; column < columns; column++) {
						for (int c = 0; c < cellWidth; c++) {
							b.append("─");
						}
						if (column + 1 < columns) {
							b.append("┴");
						}
					}
					b.append("┘");
				} else {
					// Print table borders
					b.append("├");
					for (int column = 0; column < columns; column++) {
						for (int c = 0; c < cellWidth; c++) {
							b.append("─");
						}
						if (column + 1 < columns) {
							b.append("┼");
						}
					}
					b.append("┤");
					b.append("\n");
				}
			} else {
				// Print value
				for (int column = 0; column < columns; column++) {
					b.append("│");
					final double n = this.data[row][column];
					final String s = String.valueOf(n);
					b.append(s);
					final int cellTemp = cellWidth - s.length();
					for (int ct = 0; ct < cellTemp; ct++) {
						b.append(" ");
					}
				}
				b.append("│");
				b.append("\n");
				row++;
			}
		}
		return b.toString();
	}
	
	@Override
	public String toString() {
		return this.printToString();
	}

	/**
	 * Gets the identity matrix of the given dimension.
	 * @param dimension the dimension of the identity matrix to create.
	 */
	public static Matrix identity(final int dimension) {
		final double[][] data = new double[dimension][dimension];
		for (int rc = 0; rc < dimension; rc++) {
			data[rc][rc] = 1;
		}
		return new Matrix(data, false);
	}
	
}
