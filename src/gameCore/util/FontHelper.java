package gameCore.util;

import gameCore.math.Vector2i;

import java.awt.Font;
import java.awt.Graphics;

/**
 * Contains helper and convenience methods for commonly used operations on
 * String (for example, measuring the screen length of a String, in other words
 * the space that it will occupy on screen).
 * 
 * @author Eric Perron
 */
public class FontHelper
{
	/**
	 * Returns the width and height of a string as a Vector2i when rendered in
	 * the specified font. The {@code Graphics} is needed to access some of the
	 * methods we use here to get the font metrics.
	 * 
	 * @param text
	 *        The string to measure.
	 * @param font
	 *        The font that this text will be rendered in.
	 * @param g
	 *        The {@code Graphics} object used for rendering.
	 * @return The width and height, in pixels, of text when rendered in the
	 *         specified font.
	 */
	public static Vector2i measureString(String text, Font font, Graphics g)
	{
		Vector2i size = new Vector2i();
		int width = g.getFontMetrics(font).stringWidth(text);
		int height = g.getFontMetrics(font).getHeight();
		size.set(width, height);
		return size;
	}
}
