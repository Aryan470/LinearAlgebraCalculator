public class Scalar implements LinearAlgebraObject {
	private final double value;
	public Scalar(double value) {
		this.value = value;
	}

	public Scalar() {
		this(0.0);
	}

	public Scalar add(Scalar b) {
		return new Scalar(getValue() + b.getValue());
	}

	public Scalar subtract(Scalar b) {
		return new Scalar(getValue() - b.getValue());
	}

	public Scalar multiply(Scalar b) {
		return new Scalar(getValue() * b.getValue());
	}

	public Scalar divide(Scalar b) {
		return new Scalar(getValue() / b.getValue());
	}

	public double getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {return true;}
		if (!(o instanceof Scalar) && !(o instanceof Double)) {return false;}
		double b;
		if (o instanceof Scalar) {
			Scalar s = (Scalar) (o);
			b = s.getValue();
		} else {
			Double d = (Double) (o);
			b = d.doubleValue();
		}
		return Operators.doubleEquals(getValue(), b);
	}

	@Override
	public String toString() {
		return Double.toString(value);
	}
}
