package gameCore.math;

/**
 * Defines a vector with two integer components.
 * 
 * @author Eric Perron (inspired by XNA Framework from Microsoft)
 * 
 */
public class Vector2i
{
	// TODO : Do I keep those private static fields
	private static final Vector2i zeroVector = new Vector2i(0, 0);
	private static final Vector2i unitVector = new Vector2i(1, 1);
	private static final Vector2i unitXVector = new Vector2i(1, 0);
	private static final Vector2i unitYVector = new Vector2i(0, 1);

	/**
	 * Returns a Vector2i with all of its components set to zero.
	 */
	public static final Vector2i ZERO = zeroVector;

	/**
	 * Returns a Vector2i with all of its components set to one.
	 */
	public static final Vector2i ONE = unitVector;

	/**
	 * Returns the unit vector for the x-axis, that is a Vector2i with its x
	 * component set to one and all the other components set to zero (1, 0).
	 */
	public static final Vector2i UNIT_X = unitXVector;

	/**
	 * Returns the unit vector for the y-axis, that is a Vector2i with its y
	 * component set to one and all the other components set to zero (0, 1).
	 */
	public static final Vector2i UNIT_Y = unitYVector;

	/**
	 * The x component of this Vector.
	 */
	private int x;

	/**
	 * The y component of this Vector.
	 */
	private int y;

	/**
	 * Initializes a new instance of Vector2i with both of its component set to
	 * 0.0f.
	 */
	public Vector2i()
	{
		x = y = 0;
	}

	/**
	 * Initializes a new instance of Vector2i with both of its component set to
	 * the specified value.
	 * 
	 * @param value
	 *        The value to initialize both components to.
	 */
	public Vector2i(int value)
	{
		this.x = value;
		this.y = value;
	}

	/**
	 * Initializes a new instance of Vector2i with its component set to the
	 * specified values.
	 * 
	 * @param x
	 *        Initial value for the x-component of the vector.
	 * @param y
	 *        Initial value for the y-component of the vector.
	 */
	public Vector2i(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Initializes a new instance of Vector2i with both of its component set to
	 * the same values as the specified vector.
	 * 
	 * @param vector
	 *        The vector used to set the components of this vector.
	 */
	public Vector2i(Vector2i vector)
	{
		this.x = vector.getX();
		this.y = vector.getY();
	}

	// TODO : Finish comments and add missing methods
	public Vector2i add(Vector2i other)
	{
		this.x += other.x;
		this.y += other.y;
		return this;
	}

	public Vector2i add(int value)
	{
		this.x += value;
		this.y += value;
		return this;
	}

	public Vector2i subtract(Vector2i other)
	{
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}

	public Vector2i subtract(int value)
	{
		this.x -= value;
		this.y -= value;
		return this;
	}

	public Vector2i multiply(Vector2i other)
	{
		this.x *= other.x;
		this.y *= other.y;
		return this;
	}

	public Vector2i multiply(int value)
	{
		this.x *= value;
		this.y *= value;
		return this;
	}

	public Vector2i divide(Vector2i other) throws IllegalArgumentException
	{
		if (other.x == 0 || other.y == 0)
			throw new IllegalArgumentException("Cannot divide by zero");
		this.x /= other.x;
		this.y /= other.y;
		return this;
	}

	public Vector2i divide(int value) throws IllegalArgumentException
	{
		if (value == 0)
			throw new IllegalArgumentException("Cannot divide by zero");
		this.x /= value;
		this.y /= value;
		return this;
	}

	public float length()
	{
		return (float) Math.sqrt(x * x + y * y);
	}

	public float dotProduct(Vector2i other)
	{
		return x * other.getX() + y * other.getY();
	}

	public Vector2i normalize()
	{
		float length = length();
		x /= length;
		y /= length;
		return this;
	}

	public Vector2i rotate(float angle)
	{
		double rad = Math.toRadians(angle);
		double cos = Math.cos(rad);
		double sin = Math.sin(rad);

		return new Vector2i((int) (x * cos - y * sin), (int) (x * sin + y * cos));
	}

	public boolean equals(Object object)
	{
		if (!(object instanceof Vector2i))
			return false;
		Vector2i vec = (Vector2i) object;
		if (vec.getX() == this.getX() && vec.getY() == this.getY())
			return true;
		return false;
	}

	// ++++++++++ Static methods ++++++++++ //

	public static Vector2i add(Vector2i v0, Vector2i v1)
	{
		return new Vector2i(v0.getX() + v1.getX(), v0.getY() + v1.getY());
	}

	public static Vector2i subtract(Vector2i v0, Vector2i v1)
	{
		return new Vector2i(v0.getX() - v1.getX(), v0.getY() - v1.getY());
	}

	public static Vector2i multiply(Vector2i vector, float value)
	{
		return new Vector2i((int) (vector.getX() * value), (int) (vector.getY() * value));
	}

	public static Vector2i divide(Vector2i vector, float value)
	{
		return new Vector2i((int) (vector.getX() / value), (int) (vector.getY() / value));
	}

	// ++++++++++ GETTERS ++++++++++ //

	/**
	 * 
	 * @return
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * 
	 * @return
	 */
	public int getY()
	{
		return y;
	}

	// ++++++++++ SETTERS ++++++++++ //

	/**
	 * 
	 * @param x
	 */
	public void setX(int x)
	{
		this.x = x;
	}

	/**
	 * 
	 * @param y
	 */
	public void setY(int y)
	{
		this.y = y;
	}

	/**
	 * 
	 * @param vector2i
	 */
	public void set(Vector2i vector2i)
	{
		set(vector2i.x, vector2i.y);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void set(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * 
	 */
	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
}
