import java.util.Arrays;

public class Vector implements LinearAlgebraObject {
	private final double[] components;
	private final double magnitude;
	private final Vector direction;
	private final int size;

	public Vector(double[] components) {
		this.components = components;
		this.magnitude = calcMagnitude();
		this.size = components.length;

		if (!Operators.doubleEquals(magnitude, 1.0)) {
			this.direction = this.multiply(1.0 / this.magnitude);
		} else {
			this.direction = this;
		}
	}
	public Vector(int size) {
		this(new double[size]);
	}
	public Vector(Vector v) {
		this(v.getComponents());
	}

	public double[] getComponents() {
		return components.clone();
	}

	public double getComponent(int i) {
		return components[i];
	}

	public int getSize() {
		return size;
	}

	public Vector add(Vector v) {
		double[] newComponents = v.getComponents();
		for (int i = 0; i < size; i++) {
			newComponents[i] += components[i];
		}
		return new Vector(newComponents);
	}

	public Vector subtract(Vector v) {
		return add(v.multiply(-1));
	}

	public Vector multiply(double k) {
		double[] newComponents = getComponents();
		for (int i = 0; i < size; i++) {
			newComponents[i] *= k;
		}
		return new Vector(newComponents);
	}

	public double dot(Vector v) {
		double[] otherComponents = v.getComponents();
		double sum = 0.0;
		for (int i = 0; i < size; i++) {
			sum += components[i] * otherComponents[i];
		}
		return sum;
	}

	public double getMagnitude() {
		return magnitude;
	}

	public Vector getDirection() {
		return direction;
	}

	private double calcMagnitude() {
		double sum = 0.0;
		for (double x : components) {
			sum += x * x;
		}
		return Math.sqrt(sum);
	}

	// Projects this vector onto another
	public Vector project(Vector u) {
		// If this is v:
		// proj_u (v) = v * (u/|u|)
		return u.getDirection().multiply(this.dot(u));
	}

	@Override
	public String toString() {
		return Arrays.toString(components);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {return true;}
		if (!(o instanceof Vector)) {return false;}
		Vector v = (Vector)(o);
		if (v.getSize() != size) {return false;}

		double[] otherComponents = v.getComponents();
		for (int i = 0; i < size; i++) {
			if (!Operators.doubleEquals(components[i], otherComponents[i])) {
				return false;
			}
		}
		return true;
	}
}
