package com.aryan.linearalgebracalculator;

public class Operators {
	// if |a - b| <= threshold, a = b
	public static final double EQUALITYTHRESHOLD = 0.001;

	public static boolean doubleEquals(double a, double b) {
		return Math.abs(a - b) <= EQUALITYTHRESHOLD;
	}

	// Take components of a vector, or vector columns of a matrix
	public static LinearAlgebraObject create(LinearAlgebraObject[] components) {
		if (components[0] instanceof Scalar) {
			double[] doubleComponents = new double[components.length];
			for (int i = 0; i < components.length; i++) {
				Scalar s = (Scalar) (components[i]);
				doubleComponents[i] = s.getValue();
			}
			return new Vector(doubleComponents);
		} if (components[0] instanceof Vector) {
			// Vector columns
			Vector first = (Vector) (components[0]);
			double[][] matrixComponents = new double[components.length][first.getSize()];
			for (int i = 0; i < components.length; i++) {
				Vector v = (Vector) (components[i]);
				matrixComponents[i] = v.getComponents();
			}
			// We must transpose because we flipped rows and columns
			Matrix m = new Matrix(matrixComponents);
			return m.transpose();
		} else {
			throw new UnsupportedOperationException("Cannot create from array of " + components[0].getClass());
		}

	}

	public static boolean SS(LinearAlgebraObject x, LinearAlgebraObject y) {return x instanceof Scalar && y instanceof Scalar;}
	public static boolean VV(LinearAlgebraObject x, LinearAlgebraObject y) {return x instanceof Vector && y instanceof Vector;}
	public static boolean VS(LinearAlgebraObject x, LinearAlgebraObject y) {return x instanceof Vector && y instanceof Scalar;}
	public static boolean MS(LinearAlgebraObject x, LinearAlgebraObject y) {return x instanceof Matrix && y instanceof Scalar;}
	public static boolean MV(LinearAlgebraObject x, LinearAlgebraObject y) {return x instanceof Matrix && y instanceof Vector;}
	public static boolean MM(LinearAlgebraObject x, LinearAlgebraObject y) {return x instanceof Matrix && y instanceof Matrix;}

	// SS, VV, MM
	public static LinearAlgebraObject add(LinearAlgebraObject x, LinearAlgebraObject y) {
		if (SS(x, y)) {
			Scalar a = (Scalar)(x);
			Scalar b = (Scalar)(y);
			return a.add(b);
		} else if (VV(x, y)) {
			Vector a = (Vector)(x);
			Vector b = (Vector)(y);
			return a.add(b);
		} else if (MM(x, y)) {
			Matrix a = (Matrix)(x);
			Matrix b = (Matrix)(y);
			return a.add(b);
		} else {
			throw new UnsupportedOperationException("Cannot add " + x.getClass() + " and " + y.getClass());
		}

	}

	// SS, VV, MM
	public static LinearAlgebraObject subtract(LinearAlgebraObject x, LinearAlgebraObject y) {
		if (SS(x, y)) {
			Scalar a = (Scalar)(x);
			Scalar b = (Scalar)(y);
			return a.subtract(b);
		} else if (VV(x, y)) {
			Vector a = (Vector)(x);
			Vector b = (Vector)(y);
			return a.subtract(b);
		} else if (MM(x, y)) {
			Matrix a = (Matrix)(x);
			Matrix b = (Matrix)(y);
			return a.subtract(b);
		} else {
			throw new UnsupportedOperationException("Cannot subtract " + x.getClass() + " and " + y.getClass());
		}
	}

	// SS, MS
	public static LinearAlgebraObject pow(LinearAlgebraObject x, LinearAlgebraObject y) {
		if (SS(x, y)) {
			Scalar a = (Scalar)(x);
			Scalar b = (Scalar)(y);
			return a.pow(b);
		} else if (MS(x, y)) {
			Matrix a = (Matrix)(x);
			Scalar b = (Scalar)(y);
			return a.pow((int)(b.getValue()));
		} else {
			throw new UnsupportedOperationException("Cannot raise " + x.getClass() + " to " + y.getClass());
		}
	}

	// SS, VV, VS, MM, MV, MS
	public static LinearAlgebraObject multiply(LinearAlgebraObject x, LinearAlgebraObject y) {
		if (SS(x, y)) {
			Scalar a = (Scalar)(x);
			Scalar b = (Scalar)(y);
			return a.multiply(b);
		} else if (VV(x, y)) {
			Vector a = (Vector)(x);
			Vector b = (Vector)(y);
			return new Scalar(a.dot(b));
		} else if (MM(x, y)) {
			Matrix a = (Matrix)(x);
			Matrix b = (Matrix)(y);
			return a.multiply(b);
		} else if (VS(x, y)) {
			Vector a = (Vector) (x);
			Scalar b = (Scalar) (y);
			return a.multiply(b.getValue());
		} else if (VS(y, x)) {
			Vector a = (Vector) (y);
			Scalar b = (Scalar) (x);
			return a.multiply(b.getValue());
		} else if (MV(x, y)) {
			Matrix a = (Matrix) (x);
			Vector b = (Vector) (y);
			return a.multiply(b);
		} else if (MV(y, x)) {
			Matrix a = (Matrix) (y);
			Vector b = (Vector) (x);
			return a.multiply(b);
		} else if (MS(x, y)) {
			Matrix a = (Matrix) (x);
			Scalar b = (Scalar) (y);
			return a.multiply(b.getValue());
		} else if (MS(y, x)) {
			Matrix a = (Matrix) (y);
			Scalar b = (Scalar) (x);
			return a.multiply(b.getValue());
		} else {
			throw new UnsupportedOperationException("Cannot multiply " + x.getClass() + " and " + y.getClass());
		}
	}

	public static LinearAlgebraObject divide(LinearAlgebraObject x, LinearAlgebraObject y) {
		if (SS(x, y)) {
			Scalar a = (Scalar)(x);
			Scalar b = (Scalar)(y);
			return a.divide(b);
		} else {
			throw new UnsupportedOperationException("Cannot divide " + x.getClass() + " by " + y.getClass());
		}
	}
	// proj(a, b) (a onto b)
	public static Vector project(LinearAlgebraObject x, LinearAlgebraObject y) {
		if (VV(x, y)) {
			Vector u = (Vector) (x);
			Vector v = (Vector) (y);
			return u.project(v);
		} else {
			throw new UnsupportedOperationException("Cannot project " + x.getClass() + " to " + y.getClass());
		}
	}

	// |a|
	public static Scalar magnitude(LinearAlgebraObject x) {
		if (x instanceof Vector) {
			Vector v = (Vector) (x);
			return new Scalar(v.getMagnitude());
		} else {
			throw new UnsupportedOperationException("Cannot get magnitude of " + x.getClass());
		}
	}

	// det(A)
	public static Scalar determinant(LinearAlgebraObject x) {
		if (x instanceof Matrix) {
			Matrix M = (Matrix) (x);
			return new Scalar(M.getDeterminant());
		} else {
			throw new UnsupportedOperationException("Cannot get determinant of " + x.getClass());
		}
	}

	// inv(A)
	public static Matrix inverse(LinearAlgebraObject x) {
		if (x instanceof Matrix) {
			Matrix M = (Matrix) (x);
			return M.getInverse();
		} else {
			throw new UnsupportedOperationException("Cannot get inverse of " + x.getClass());
		}
	}

	// trans(A)
	public static Matrix transpose(LinearAlgebraObject x) {
		if (x instanceof Matrix) {
			Matrix M = (Matrix) (x);
			return M.transpose();
		} else {
			throw new UnsupportedOperationException("Cannot get transpose of " + x.getClass());
		}
	}
}
