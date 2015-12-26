package gameCore.math;

/**
 * Defines a vector with two float components.
 * 
 * @author Eric Perron (inspired by XNA Framework from Microsoft)
 * 
 */
public class Vector2f {

	// TODO : Do I keep those private static fields
	private static final Vector2f zeroVector = new Vector2f(0.0f, 0.0f);
	private static final Vector2f unitVector = new Vector2f(1.0f, 1.0f);
	private static final Vector2f unitXVector = new Vector2f(1.0f, 0.0f);
	private static final Vector2f unitYVector = new Vector2f(0.0f, 1.0f);

	/**
	 * Returns a Vector2f with all of its components set to zero.
	 */
	public static final Vector2f ZERO = zeroVector;

	/**
	 * Returns a Vector2f with all of its components set to one.
	 */
	public static final Vector2f ONE = unitVector;

	/**
	 * Returns the unit vector for the x-axis, that is a Vector2f with its x
	 * component set to one and all the other components set to zero (1, 0).
	 */
	public static final Vector2f UNIT_X = unitXVector;

	/**
	 * Returns the unit vector for the y-axis, that is a Vector2f with its y
	 * component set to one and all the other components set to zero (0, 1).
	 */
	public static final Vector2f UNIT_Y = unitYVector;

	/**
	 * The x component of this Vector.
	 */
	private float x;

	/**
	 * The y component of this Vector.
	 */
	private float y;

	/**
	 * Initializes a new instance of Vector2f with both of its component set to
	 * 0.0f.
	 */
	public Vector2f() {
		x = y = 0.0f;
	}

	/**
	 * Initializes a new instance of Vector2f with both of its component set to
	 * the specified value.
	 * 
	 * @param value
	 *            The value to initialize both components to.
	 */
	public Vector2f(float value) {
		this.x = value;
		this.y = value;
	}

	/**
	 * Initializes a new instance of Vector2f with its component set to the
	 * specified values.
	 * 
	 * @param x
	 *            Initial value for the x-component of the vector.
	 * @param y
	 *            Initial value for the y-component of the vector.
	 */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Initializes a new instance of Vector2f with both of its component set to
	 * the same values as the specified vector.
	 * 
	 * @param vector
	 *            The vector used to set the components of this vector.
	 */
	public Vector2f(Vector2f vector) {
		this.x = vector.getX();
		this.y = vector.getY();
	}

	// TODO : Finish comments and add missing methods
	public Vector2f add(Vector2f other) {
		this.x += other.x;
		this.y += other.y;
		return this;
	}

	public Vector2f add(float value) {
		this.x += value;
		this.y += value;
		return this;
	}

	public Vector2f subtract(Vector2f other) {
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}

	public Vector2f subtract(float value) {
		this.x -= value;
		this.y -= value;
		return this;
	}

	public Vector2f multiply(Vector2f other) {
		this.x *= other.x;
		this.y *= other.y;
		return this;
	}

	public Vector2f multiply(float value) {
		this.x *= value;
		this.y *= value;
		return this;
	}

	public Vector2f divide(Vector2f other) throws IllegalArgumentException {
		if (other.x == 0 || other.y == 0) throw new IllegalArgumentException("Cannot divide by zero");
		this.x /= other.x;
		this.y /= other.y;
		return this;
	}

	public Vector2f divide(float value) throws IllegalArgumentException {
		if (value == 0) throw new IllegalArgumentException("Cannot divide by zero");
		this.x /= value;
		this.y /= value;
		return this;
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float dotProduct(Vector2f other) {
		return x * other.getX() + y * other.getY();
	}

	public Vector2f normalize() {
		float length = length();
		x /= length;
		y /= length;
		return this;
	}

	public Vector2f rotate(float angle) {
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);

		return new Vector2f((float) (x * cos - y * sin), (float) (x * sin + y * cos));
	}

	public boolean equals(Object object) {
		if (!(object instanceof Vector2f)) return false;
		Vector2f vec = (Vector2f) object;
		if (vec.getX() == this.getX() && vec.getY() == this.getY()) return true;
		return false;
	}

	// ++++++++++ Static methods ++++++++++ //

	public static Vector2f add(Vector2f v0, Vector2f v1) {
		return new Vector2f(v0.getX() + v1.getX(), v0.getY() + v1.getY());
	}

	public static Vector2f subtract(Vector2f v0, Vector2f v1) {
		return new Vector2f(v0.getX() - v1.getX(), v0.getY() - v1.getY());
	}

	public static Vector2f multiply(Vector2f vector, float value) {
		return new Vector2f((float) (vector.getX() * value), (float) (vector.getY() * value));
	}

	public static Vector2f divide(Vector2f vector, float value) {
		return new Vector2f((float) (vector.getX() / value), (float) (vector.getY() / value));
	}

	// ++++++++++ GETTERS ++++++++++ //

	/**
	 * 
	 * @return
	 */
	public float getX() {
		return x;
	}

	/**
	 * 
	 * @return
	 */
	public float getY() {
		return y;
	}

	// ++++++++++ SETTERS ++++++++++ //

	/**
	 * 
	 * @param x
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * 
	 * @param y
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * 
	 * @param vector2f
	 */
	public void set(Vector2f vector2f) {
		set(vector2f.x, vector2f.y);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
