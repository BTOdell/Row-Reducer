package edu.pcc.mth261;

import java.util.function.Consumer;

/**
 * An immutable matrix class.
 */
public class Matrix {
	
	/**
	 * The backing data of the matrix.
	 */
	private final double[][] data;
	
	/**
	 * Cache value for whether the matrix is in row echelon form (REF).
	 */
	private Boolean ref = null;
	
	/**
	 * Cache value for whether the matrix is in reduced row echelon form (RREF).
	 */
	private Boolean rref = null;
	
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
	 * 
	 * @return
	 */
	public int getRows() {
		return this.data.length;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getColumns() {
		return this.data[0].length;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isSquare() {
		return this.data.length == this.data[0].length;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isREF() {
		if (this.ref == null) {
			// TODO
		}
		return this.ref;
	}
	
	/**
	 * 
	 * @return
	 */
	public Matrix getREF() {
		return this.getREF(null);
	}
	
	/**
	 * 
	 * @param stepConsumer
	 * @return
	 */
	public Matrix getREF(final Consumer<RowReductionStep> stepConsumer) {
		return this.rowReduce(stepConsumer, false);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isRREF() {
		if (this.rref == null) {
			// TODO
		}
		return this.rref;
	}
	
	/**
	 * 
	 * @return
	 */
	public Matrix getRREF() {
		return this.getRREF(null);
	}
	
	/**
	 * 
	 * @param stepConsumer
	 * @return
	 */
	public Matrix getRREF(final Consumer<RowReductionStep> stepConsumer) {
		return this.rowReduce(stepConsumer, true);
	}
	
	/**
	 * 
	 */
	private Matrix rowReduce(final Consumer<RowReductionStep> stepConsumer, final boolean rref) {
		final Matrix copyMatrix = new Matrix(this);
		final double[][] data = copyMatrix.data;
		final int rows = data.length;
		final int columns = data[0].length;
		final boolean hasStepConsumer = stepConsumer != null;
		
		int row = 0;
		int column = 0;
		
		for (; column < columns; column++) {
			
			// Select a pivot position
			
			
		}
		
		if (rref) {
			
			
			
		}
		
		return copyMatrix;
	}
	
	/*
	 * Converts the matrix to a pretty-print string.
	 * ┌──┬──┬──┬──┬──┐
	 * │0 │1 │2 │3 │4 │
	 * ├──┼──┼──┼──┼──┤
	 * │5 │6 │7 │8 │9 │
	 * ├──┼──┼──┼──┼──┤
	 * │10│11│12│13│14│
	 * └──┴──┴──┴──┴──┘
	 */
	public String printToString() {
		int cellWidth = 1;
		for (int i = 0; i < this.data.length; i++) {
			for (int j = 0; j < this.data[0].length; j++) {
				final double n = this.data[i][j];
				cellWidth = Math.max(cellWidth, String.valueOf(n).length());
			}
		}
		int j = 0;
		final int dMax = 1 + this.data[0].length * 2;
		final StringBuilder b = new StringBuilder(this.data.length * dMax * cellWidth);
		for (int d = 0; d < dMax; d++) {
			if (d % 2 == 0) {
				if (d == 0) {
					b.append("┌");
					for (int i = 0; i < this.data.length; i++) {
						for (int c = 0; c < cellWidth; c++) {
							b.append("─");
						}
						if (i < this.data.length - 1) {
							b.append("┬");
						}
					}
					b.append("┐");
					b.append("\n");
				} else if (d == dMax - 1) {
					b.append("└");
					for (int i = 0; i < this.data.length; i++) {
						for (int c = 0; c < cellWidth; c++) {
							b.append("─");
						}
						if (i < this.data.length - 1) {
							b.append("┴");
						}
					}
					b.append("┘");
				} else {
					b.append("├");
					for (int i = 0; i < this.data.length; i++) {
						for (int c = 0; c < cellWidth; c++) {
							b.append("─");
						}
						if (i < this.data.length - 1) {
							b.append("┼");
						}
					}
					b.append("┤");
					b.append("\n");
				}
			} else {
				for (int i = 0; i < this.data.length; i++) {
					b.append("│");
					final double n = this.data[i][j];
					final String s = String.valueOf(n);
					b.append(s);
					final int cellTemp = cellWidth - s.length();
					for (int ct = 0; ct < cellTemp; ct++) {
						b.append(" ");
					}
				}
				b.append("│");
				b.append("\n");
				j++;
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
	 * 
	 * @param dimension
	 *            the dimension of the identity matrix to create.
	 */
	public static Matrix identity(final int dimension) {
		final double[][] data = new double[dimension][dimension];
		for (int rc = 0; rc < dimension; rc++) {
			data[rc][rc] = 1;
		}
		return new Matrix(data, false);
	}
	
	/**
	 * 
	 */
	public static class RowReductionStep {
		
		private final String message;
		private final Matrix before;
		private final Matrix after;
		
		/**
		 * 
		 * @param message
		 * @param before
		 * @param after
		 */
		public RowReductionStep(final String message, final Matrix before, final Matrix after) {
			this.message = message;
			this.before = before;
			this.after = after;
		}
		
		/**
		 * 
		 * @return
		 */
		public String getMessage() {
			return this.message;
		}
		
		/**
		 * 
		 * @return
		 */
		public Matrix getBeforeMatrix() {
			return this.before;
		}
		
		/**
		 * 
		 * @return
		 */
		public Matrix getAfterMatrix() {
			return this.after;
		}
		
	}
	
}
