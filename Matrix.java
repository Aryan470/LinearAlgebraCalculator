import java.util.Arrays;

public class Matrix implements LinearAlgebraObject {
	private final double[][] components;
	private final int[] dimensions;
	private Double determinant;
	private Matrix inverse;

	public Matrix(double[][] components) {
		this.components = components;
		this.dimensions = new int[] {components.length, components[0].length};
		this.determinant = null;
		this.inverse = null;
	}

	// Empty Matrix
	public Matrix(int m, int n) {
		this(new double[m][n]);
	}

	// Identity Matrix
	public Matrix(int n) {
		double[][] newComponents = new double[n][n];
		for (int i = 0; i < n; i++) {
			newComponents[i][i] = 1;
		}
		this.components = newComponents;
		this.dimensions = new int[] {components.length, components[0].length};
		this.determinant = null;
		this.inverse = null;
	}


	// Cloned Matrix
	public Matrix(Matrix A) {
		this(A.getComponents());
	}

	// TODO: find efficient method
	private double calcDeterminant() {
		if (this.dimensions[0] != this.dimensions[1]) {
			throw new UnsupportedOperationException("Cannot take determinant of " + dimensions[0] + "x" + dimensions[1] + " matrix");
		}

		return 0.0;
	}

	public double getDeterminant() {
		if (this.determinant == null) {
			// TODO: change to efficient method
			this.determinant = recursiveDeterminant();
		}
		return this.determinant.doubleValue();
	}

	public Matrix getInverse() {
		if (this.inverse == null) {
			this.inverse = calcInverse();
		}
		return this.inverse;
	}


	private Matrix calcInverse() {
		if (this.dimensions[0] != this.dimensions[1]) {
			throw new UnsupportedOperationException("Cannot take inverse of " + dimensions[0] + "x" + dimensions[1] + " matrix");
		}
		Matrix coMatrix = cofactMatrix();
		coMatrix = coMatrix.transpose();
		return coMatrix.multiply(1.0 / getDeterminant());
	}

	private Matrix cofactMatrix() {
		double[][] dets = new double[dimensions[0]][dimensions[0]];
		for (int i = 0; i < dimensions[0]; i++) {
			for (int j = 0; j < dimensions[1]; j++) {
				dets[i][j] = minor(i, j).getDeterminant();
				if ((i + j) % 2 != 0) {
					dets[i][j] *= -1;
				}
			}
		}
		return new Matrix(dets);
	}

	public double[][] getComponents() {
		double[][] newComponents = new double[dimensions[0]][dimensions[1]];
		for (int i = 0; i < dimensions[0]; i++) {
			newComponents[i] = components[i].clone();
		}
		return newComponents;
	}

	// A * B is a m x n matrix of the dot product of the corresponding row of A and column of B
	/*
		[1 3] * [2 8] = [[1 3]*[2 4] [1 3]*[8 2]]
		[5 7]   [4 2]   [[5 7]*[2 4] [5 7]*[8 2]]
	*/
	public Matrix multiply(Matrix A) {
		int[] otherDimensions = A.getDimensions();
		if (this.dimensions[1] != otherDimensions[0]) {
			throw new IllegalArgumentException("Columns of A must equal rows of B");
		}
		double[][] newComponents = new double[this.dimensions[0]][otherDimensions[1]];
		double[][] Bt = A.transpose().getComponents(); // To access each column B[col]
		for (int i = 0; i < this.dimensions[0]; i++) {
			Vector row = new Vector(components[i]);
			for (int j = 0; j < otherDimensions[1]; j++) {
				Vector column = new Vector(Bt[j]);
				newComponents[i][j] = row.dot(column);
			}
		}
		return new Matrix(newComponents);
	}

	// Take the dot product of each row of matrix by vector
	public Vector multiply(Vector A) {
		if (this.dimensions[1] != A.getSize()) {
			throw new IllegalArgumentException("Matrix columns and vector size must be equal for product (" + Arrays.toString(this.dimensions) + ") and " + A.getSize());
		}
		double[] dotComponents = new double[A.getSize()];
		for (int i = 0; i < dimensions[0]; i++) {
			dotComponents[i] = A.dot(new Vector(components[i]));
		}
		return new Vector(dotComponents);
	}

	public Matrix add(Matrix A) {
		if (!Arrays.equals(dimensions, A.getDimensions())) {
			throw new IllegalArgumentException("Matrices must have same dimensions");
		}
		double[][] newComponents = A.getComponents();
		for (int i = 0; i < dimensions[0]; i++) {
			for (int j = 0; j < dimensions[1]; j++) {
				newComponents[i][j] += components[i][j];
			}
		}
		return new Matrix(newComponents);
	}

	public Matrix subtract(Matrix A) {
		return add(A.multiply(-1));
	}

	public int[] getDimensions() {
		return dimensions;
	}

	private Vector[] getRowVectors() {
		Vector[] rows = new Vector[components.length];
		for (int i = 0; i < components.length; i++) {
			rows[i] = new Vector(components[i]);
		}
		return rows;
	}

	private Matrix minor(int row, int col) {
		double[][] newComponents = new double[dimensions[0] - 1][dimensions[1] - 1];
		for (int i = 0; i < dimensions[0]; i++) {
			for (int j = 0; j < dimensions[1]; j++) {
				int a = -1;
				int b = -1;
				if (i < row) {
					a = i;
				} else if (i > row) {
					a = i - 1;
				}
				if (j < col) {
					b = j;
				} else if (j > col) {
					b = j - 1;
				}

				if (a >= 0 && b >= 0) {
					newComponents[a][b] = components[i][j];
				}
			}
		}
		return new Matrix(newComponents);
	}

	private double recursiveDeterminant() {
		int row = 0;
		if (this.dimensions[0] != this.dimensions[1]) {
			throw new UnsupportedOperationException("Cannot take determinant of nonsquare matrix");
		}
		if (this.dimensions[0] == 1 && this.dimensions[1] == 1) {
			return components[0][0];
		} else if (this.dimensions[0] == 2 && this.dimensions[1] == 2) {
			return components[0][0] * components[1][1] - components[0][1] * components[1][0];
		} else {
			double det = 0;
			for (int j = 0; j < this.dimensions[0]; j++) {
				det += Math.pow(-1, row + j) * components[row][j] * minor(row, j).recursiveDeterminant();
			}
			return det;
		}
	}

	public Matrix transpose() {
		double[][] newComponents = new double[dimensions[1]][dimensions[0]];
		for (int i = 0; i < dimensions[0]; i++) {
			for (int j = 0; j < dimensions[1]; j++) {
				newComponents[j][i] = components[i][j];
			}
		}
		return new Matrix(newComponents);
	}

	public Matrix multiply(double k) {
		double[][] newComponents = getComponents();
		for (int i = 0; i < dimensions[0]; i++) {
			for (int j = 0; j < dimensions[1]; j++) {
				newComponents[i][j] *= k;
			}
		}
		return new Matrix(newComponents);
	}

	public Matrix pow(int k) {
		Matrix product = new Matrix(this);
		for (int i = 0; i < k; i++) {
			product = product.multiply(this);
		}
		return product;
	}


	@Override
	public String toString() {
		String out = "[";
		for (double[] component : components) {
			out += Arrays.toString(component) + ",\n";
		}
		out = out.substring(0, out.length() - 2) + "]";
		return out;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {return true;}
		if (!(o instanceof Matrix)) {return false;}
		Matrix M = (Matrix)(o);
		if (!dimensions.equals(M.getDimensions())) {return false;}
		double[][] otherComponents = M.getComponents();
		for (int i = 0; i < dimensions[0]; i++) {
			for (int j = 0; j < dimensions[1]; j++) {
				if (!Operators.doubleEquals(components[i][j], otherComponents[i][j])) {
					return false;
				}
			}
		}
		return true;
	}
}
