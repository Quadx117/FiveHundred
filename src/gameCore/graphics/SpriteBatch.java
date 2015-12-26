package gameCore.graphics;

import gameCore.math.Vector2i;

import java.awt.Color;
import java.awt.Rectangle;

public class SpriteBatch {
	// TODO : Add support for the other types of blend states
	/**
	 * The enumeration for the different types of blending states.
	 * 
	 * @author Eric Perron
	 * 
	 */
	public static enum BlendState {
		/**
		 * A built-in state object with settings for additive blend that is
		 * adding the destination data to the source data without using alpha.
		 */
		ADDITIVE,
		/**
		 * A built-in state object with settings for alpha blend that is
		 * blending the source and destination data using alpha.
		 */
		ALPHA_BLEND,
		/**
		 * A built-in state object with settings for blending with
		 * non-premultiplied alpha that is blending source and destination data
		 * by using alpha while assuming the color data contains no alpha
		 * information.
		 */
		NON_PREMULTIPLIED,
		/**
		 * A built-in state object with settings for opaque blend that is
		 * overwriting the destination with the source data.
		 */
		OPAQUE
	}

	// TODO : Add support for the other types of resizing options
	/**
	 * The enumeration for the different interpolation types used when resizing
	 * a sprite.
	 * 
	 * @author Eric Perron
	 * 
	 */
	public static enum InterpolationType {
		/**
		 * A built-in state object with settings for nearest neighbor
		 * interpolation as the resizing method.
		 */
		NEAREST_NEIGHBOR,
		/**
		 * A built-in state object with settings for bilinear
		 * interpolation as the resizing method.
		 */
		BILINEAR,
		/**
		 * A built-in state object with settings for bicubic interpolation as
		 * the resizing method.
		 */
		BICUBIC
	}

	/**
	 * The width and height of this screen.
	 */
	private int width, height;

	/**
	 * The array of pixel used to draw one frame. Since an array is an object in
	 * java, we can leave it public because returning an array effectively
	 * returns a reference to the object.
	 */
	public int[] screenPixels;

	// TODO : Don't know if I want to keep this
	/**
	 * The x and y offset used for rendering. Useful for top down games with
	 * maps bigger than the screen.
	 */
	private int xOffset, yOffset;

	public SpriteBatch(int width, int height) {
		this.width = width;
		this.height = height;
		screenPixels = new int[width * height];
	}

	/**
	 * Clears this sceen's array of pixels to black, making it available to
	 * receive new pixel data to be drawn. This method is the one called before
	 * each frame in the GameCore class.
	 */
	public void clear() {
		clear(0);
	}

	/**
	 * Clears this sceen's array of pixels to the specified color, making it
	 * available to receive new pixel data to be drawn.
	 * 
	 * @param color
	 *            the color to clear the screen to using .
	 */
	public void clear(Color color) {
		clear(color.getRGB());
	}

	/**
	 * Clears this sceen's array of pixels to the specified color, making it
	 * available to receive new pixel data to be drawn.
	 * 
	 * @param color
	 *            the color to clear the screen to specified as an integer
	 *            value.
	 */
	public void clear(int color) {
		for (int i = 0; i < screenPixels.length; ++i) {
			screenPixels[i] = color;
		}
	}

	// TODO : Don't know if I want to keep this.
	/**
	 * Renders the specified sprite at the specified x and y position without
	 * any scaling.
	 * 
	 * @param sprite
	 *            The sprite to draw.
	 * @param xDestPos
	 *            The x destination position (in screen coordinates) at which to
	 *            draw the sprite.
	 * @param yDestPos
	 *            The y destination position (in screen coordinates) at which to
	 *            draw the sprite.
	 * @param fixedPos
	 *            {@code false} if the sprite is fixed to the screen position,
	 *            {@code true} otherwise.
	 */
	/*
	 * public void renderSprite(Sprite sprite, int xDestPos, int yDestPos,
	 * boolean fixedPos) {
	 * if (fixedPos) {
	 * xDestPos -= xOffset;
	 * yDestPos -= yOffset;
	 * }
	 * for (int y = 0; y < sprite.getHeight(); y++) {
	 * int ya = y + yDestPos;
	 * for (int x = 0; x < sprite.getWidth(); x++) {
	 * int xa = x + xDestPos;
	 * if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
	 * int color = sprite.pixels[x + y * sprite.getWidth()];
	 * if (color != alphaColor) screenPixels[xa + ya * width] = color;
	 * }
	 * }
	 * }
	 */

	/**
	 * Renders the specified sprite at the specified x and y position without
	 * any scaling. The specified alpha color will not be rendered, that is, it
	 * will let what is behind it be seen unaltered.
	 * 
	 * @param sprite
	 *            The sprite to draw.
	 * @param xDestPos
	 *            The x destination position (in screen coordinates) at which to
	 *            draw the sprite.
	 * @param yDestPos
	 *            The y destination position (in screen coordinates) at which to
	 *            draw the sprite.
	 * @param tint
	 *            The color to tint a sprite. Use Color.WHITE for full color
	 *            with no tinting.
	 * @param alphaFactor
	 *            alpha value applied to the new color ranging from 0.0f
	 *            (completely dark) to 1.0f.
	 * @param alphaColor
	 *            The color in the ARGB model that will be transparent.
	 * @param blendState
	 *            The type of blending to use for this sprite.
	 * @throws NullPointerException
	 *             If the sprite is null.
	 */
	public void renderSprite(Sprite sprite, int xDestPos, int yDestPos, Color tint, float alphaFactor, int alphaColor,
			BlendState blendState) throws NullPointerException {

		checkValidSprite(sprite);

		// Extract our tint components
		int rTint = tint.getRed();
		int gTint = tint.getGreen();
		int bTint = tint.getBlue();

		// Calculate the new foreground color alpha component
		int foregroundAlpha = (int) (alphaFactor * 255);

		for (int y = 0; y < sprite.getHeight(); y++) {
			int ya = y + yDestPos;
			for (int x = 0; x < sprite.getWidth(); x++) {
				int xa = x + xDestPos;

				// The color of the pixel about to be drawn.
				int foregroundCol = sprite.pixels[x + y * sprite.getWidth()];

				// If current pixel is out of screen bounds or current pixel
				// color equals alphaColor, skip it.
				if (xa < 0 || xa >= width || ya < 0 || ya >= height || foregroundCol == alphaColor) continue;

				// The alpha value of the pixel about to be drawn.
				if (alphaFactor < 0.0f) {
					foregroundAlpha = (foregroundCol >> 24) & 0xff;
				}
				// The color of the pixel already there.
				int backgroundCol = screenPixels[xa + ya * width];
				// The resulting color of the pixel to be drawn
				int color;

				// The components of the pixel about to be drawn.
				int foregroundR = (foregroundCol >> 16) & 0xff;
				int foregroundG = (foregroundCol >> 8) & 0xff;
				int foregroundB = (foregroundCol) & 0xff;

				// Using Color.WHITE would have no effect so skip this
				if (!tint.equals(Color.WHITE)) {
					// Typical tint formula -> original component * tint / 255
					foregroundR = foregroundR * rTint / 255;
					foregroundG = foregroundG * gTint / 255;
					foregroundB = foregroundB * bTint / 255;
				}

				// If alpha is 255 it completely overrides the existing color.
				if (foregroundAlpha == 255 || blendState == BlendState.OPAQUE) {
					color = foregroundAlpha << 24 | foregroundR << 16 | foregroundG << 8 | foregroundB;
				} else if (blendState == BlendState.ALPHA_BLEND) {
					// Do the alpha-blending.
					int backgroundR = (backgroundCol >> 16) & 0xff;
					int backgroundG = (backgroundCol >> 8) & 0xff;
					int backgroundB = (backgroundCol) & 0xff;
					// Typical over blend formula
					int r = (foregroundR * foregroundAlpha / 255) + (backgroundR * (255 - foregroundAlpha) / 255);
					int g = (foregroundG * foregroundAlpha / 255) + (backgroundG * (255 - foregroundAlpha) / 255);
					int b = (foregroundB * foregroundAlpha / 255) + (backgroundB * (255 - foregroundAlpha) / 255);
					color = r << 16 | g << 8 | b;
				} else {
					// Defaults to BlendState.OPAQUE after tinting.
					color = foregroundAlpha << 24 | foregroundR << 16 | foregroundG << 8 | foregroundB;
				}

				screenPixels[xa + ya * width] = color;
			}
		}
	}

	/**
	 * Renders an ARGB sprite without any scaling using the specified blending
	 * option.
	 * 
	 * @param sprite
	 *            The sprite to draw.
	 * @param xDestPos
	 *            The X position at which to begin drawing the sprite.
	 * @param yDestPos
	 *            The Y position at which to begin drawing the sprite.
	 * @param blendState
	 *            The type of blending to use for this sprite.
	 * @throws NullPointerException
	 *             If the sprite is null.
	 */
	public void draw(Sprite sprite, int xDestPos, int yDestPos, BlendState blendState) throws NullPointerException {
		this.draw(sprite, xDestPos, yDestPos, Color.WHITE, -1.0f, blendState);
	}

	/**
	 * Renders an ARGB sprite without any scaling using the specified blending
	 * option.
	 * 
	 * @param sprite
	 *            The sprite to draw.
	 * @param position
	 *            The Vector2i containing the x and y position at which to begin
	 *            drawing the sprite.
	 * @param blendState
	 *            The type of blending to use for this sprite.
	 * @throws NullPointerException
	 *             If the sprite is null.
	 */
	public void draw(Sprite sprite, Vector2i position, BlendState blendState) throws NullPointerException {
		this.draw(sprite, position.getX(), position.getY(), Color.WHITE, -1.0f, blendState);
	}

	/**
	 * Renders an ARGB sprite without any scaling using the specified blending
	 * option.
	 * 
	 * @param sprite
	 *            The sprite to draw.
	 * @param xDestPos
	 *            The X position at which to begin drawing the sprite.
	 * @param yDestPos
	 *            The Y position at which to begin drawing the sprite.
	 * @param tint
	 *            The color to tint a sprite. Use Color.WHITE for full color
	 *            with no tinting.
	 * @param blendState
	 *            The type of blending to use for this sprite.
	 * @throws NullPointerException
	 *             If the sprite is null.
	 */
	public void draw(Sprite sprite, int xDestPos, int yDestPos, Color tint, BlendState blendState)
			throws NullPointerException {

		this.draw(sprite, xDestPos, yDestPos, tint, -1.0f, blendState);
	}

	/**
	 * Renders an ARGB sprite without any scaling using the specified blending
	 * option.
	 * 
	 * @param sprite
	 *            The sprite to draw.
	 * @param position
	 *            The Vector2i containing the x and y position at which to begin
	 *            drawing the sprite.
	 * @param tint
	 *            The color to tint a sprite. Use Color.WHITE for full color
	 *            with no tinting.
	 * @param blendState
	 *            The type of blending to use for this sprite.
	 * @throws NullPointerException
	 *             If the sprite is null.
	 */
	public void draw(Sprite sprite, Vector2i position, Color tint, BlendState blendState) throws NullPointerException {
		this.draw(sprite, position.getX(), position.getY(), tint, -1.0f, blendState);
	}

	/**
	 * Renders an ARGB sprite using a color to tint the sprite and an alpha
	 * value to blend the resulting color with the background using alpha
	 * blending. The alphaFactor is useful to create fading effects. If this
	 * value is set to -1.0f, the sprites original alpha channel per pixel will
	 * be used.
	 * 
	 * <p>
	 * The formula for used is this :
	 * <p>
	 * <code>tint / 255 * originalColorComponent</code>
	 * <p>
	 * The new alpha channel for the resulting color is calculated like this :
	 * <p>
	 * <code>(int) alphaFactor * 255</code>
	 * <p>
	 * We then do a typical alpha-blending between the background color and the
	 * new color.
	 * 
	 * @param sprite
	 *            The sprite to draw.
	 * @param xDestPos
	 *            The X position at which to begin drawing the sprite.
	 * @param yDestPos
	 *            The Y position at which to begin drawing the sprite.
	 * @param tint
	 *            The color to tint a sprite. Use Color.WHITE for full color
	 *            with no tinting.
	 * @param alphaFactor
	 *            alpha value applied to the new color ranging from 0.0f
	 *            (completely dark) to 1.0f.
	 * @param blendState
	 *            The type of blending to use for this sprite.
	 * @throws NullPointerException
	 *             If the sprite is null.
	 */
	public void draw(Sprite sprite, int xDestPos, int yDestPos, Color tint, float alphaFactor, BlendState blendState)
			throws NullPointerException {

		checkValidSprite(sprite);
		// The pixel about to be drawn is the foreground color and the existing
		// pixel is the background color

		// Extract our tint components
		int rTint = tint.getRed();
		int gTint = tint.getGreen();
		int bTint = tint.getBlue();

		// Calculate the new foreground color alpha component
		int foregroundAlpha = (int) (alphaFactor * 255);

		// Cycle through all the sprites pixels. and apply tint and
		// alpha-blending
		for (int y = 0; y < sprite.getHeight(); y++) {
			// Set our Y position relative to the screen.
			int ya = y + yDestPos;
			for (int x = 0; x < sprite.getWidth(); x++) {
				// Set our X position relative to the screen.
				int xa = x + xDestPos;
				// If this pixel is out of the screen's bound, skip it.
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;

				// The color of the pixel about to be drawn.
				int foregroundCol = sprite.pixels[x + y * sprite.getWidth()];
				// The alpha value of the pixel about to be drawn.
				if (alphaFactor < 0.0f) {
					foregroundAlpha = (foregroundCol >> 24) & 0xff;
				}
				// The color of the pixel already there.
				int backgroundCol = screenPixels[xa + ya * width];
				// The resulting color of the pixel to be drawn
				int col;

				// The components of the pixel about to be drawn.
				int foregroundR = (foregroundCol >> 16) & 0xff;
				int foregroundG = (foregroundCol >> 8) & 0xff;
				int foregroundB = (foregroundCol) & 0xff;

				// Using Color.WHITE would have no effect so skip this
				if (!tint.equals(Color.WHITE)) {
					// Typical tint formula -> original component * tint / 255
					foregroundR = foregroundR * rTint / 255;
					foregroundG = foregroundG * gTint / 255;
					foregroundB = foregroundB * bTint / 255;
				}

				// If alpha is 255 it completely overrides the existing color.
				if (foregroundAlpha == 255 || blendState == BlendState.OPAQUE) {
					col = foregroundAlpha << 24 | foregroundR << 16 | foregroundG << 8 | foregroundB;
				} else if (blendState == BlendState.ALPHA_BLEND) {
					// Do the alpha-blending.
					int backgroundR = (backgroundCol >> 16) & 0xff;
					int backgroundG = (backgroundCol >> 8) & 0xff;
					int backgroundB = (backgroundCol) & 0xff;
					// Typical over blend formula
					int r = (foregroundR * foregroundAlpha / 255) + (backgroundR * (255 - foregroundAlpha) / 255);
					int g = (foregroundG * foregroundAlpha / 255) + (backgroundG * (255 - foregroundAlpha) / 255);
					int b = (foregroundB * foregroundAlpha / 255) + (backgroundB * (255 - foregroundAlpha) / 255);
					col = r << 16 | g << 8 | b;
				} else {
					// Defaults to BlendState.OPAQUE after tinting.
					col = foregroundAlpha << 24 | foregroundR << 16 | foregroundG << 8 | foregroundB;
				}
				screenPixels[xa + ya * width] = col;
			}
		}
	}

	/**
	 * Renders an ARGB sprite without any scaling using the specified blending
	 * option.
	 * 
	 * @param sprite
	 *            The sprite to draw.
	 * @param position
	 *            The Vector2i containing the x and y position at which to begin
	 *            drawing the sprite.
	 * @param tint
	 *            The color to tint a sprite. Use Color.WHITE for full color
	 *            with no tinting.
	 * @param alphaFactor
	 *            alpha value applied to the new color ranging from 0.0f
	 *            (completely dark) to 1.0f.
	 * @param blendState
	 *            The type of blending to use for this sprite.
	 * @throws NullPointerException
	 *             If the sprite is null.
	 */
	public void draw(Sprite sprite, Vector2i position, Color tint, float alphaFactor, BlendState blendState)
			throws NullPointerException {
		this.draw(sprite, position.getX(), position.getY(), tint, alphaFactor, blendState);
	}

	/**
	 * Renders an ARGB sprite using alpha blending to blend it with the existing
	 * image.
	 * 
	 * @param sprite
	 *            The sprite to draw.
	 * @param destinationRectangle
	 *            The destination's position and size. If the destination's size
	 *            is equal to the sprite's size, no resizing will be done to the
	 *            sprite.
	 * @param interpolationType
	 *            The type of interpolation used when resizing the sprite to
	 *            match the destination's size.
	 * @param blendState
	 *            The type of blending to use for this sprite.
	 * @throws NullPointerException
	 *             If the sprite is null.
	 */
	public void draw(Sprite sprite, Rectangle destinationRectangle, InterpolationType interpolationType,
			BlendState blendState) throws NullPointerException {
		this.draw(sprite, destinationRectangle, interpolationType, Color.WHITE, -1.0f, blendState);
	}

	/**
	 * Renders an ARGB sprite using alpha blending to blend it with the existing
	 * image.
	 * 
	 * @param sprite
	 *            The sprite to draw.
	 * @param destinationRectangle
	 *            The destination's position and size. If the destination's size
	 *            is equal to the sprite's size, no resizing will be done to the
	 *            sprite.
	 * @param interpolationType
	 *            The type of interpolation used when resizing the sprite to
	 *            match the destination's size.
	 * @param tint
	 *            The color to tint a sprite. Use Color.WHITE for full color
	 *            with no tinting.
	 * @param blendState
	 *            The type of blending to use for this sprite.
	 * @throws NullPointerException
	 *             If the sprite is null.
	 */
	public void draw(Sprite sprite, Rectangle destinationRectangle, InterpolationType interpolationType, Color tint,
			BlendState blendState) throws NullPointerException {
		this.draw(sprite, destinationRectangle, interpolationType, tint, -1.0f, blendState);
	}

	/**
	 * Renders an ARGB sprite using a color to tint the sprite and an alpha
	 * value to blend the resulting color with the background using alpha
	 * blending. The alphaFactor is useful to create fading effects. If this
	 * value is set to -1.0f, the sprites original alpha channel per pixel will
	 * be used. If the destination rectangle is bigger or smaller than the
	 * specified sprite, the sprite will be resized to the destination
	 * rectangle's size using the specified resizing method.
	 * 
	 * <p>
	 * The formula for used is this :
	 * <p>
	 * <code>tint / 255 * originalColorComponent</code>
	 * <p>
	 * The new alpha channel for the resulting color is calculated like this :
	 * <p>
	 * <code>(int) alphaFactor * 255</code>
	 * <p>
	 * We then do a typical alpha-blending between the background color and the
	 * new color.
	 * 
	 * @param sprite
	 *            The sprite to draw.
	 * @param destinationRectangle
	 *            The destination's position and size. If the destination's size
	 *            is equal to the sprite's size, no resizing will be done to the
	 *            sprite.
	 * @param interpolationType
	 *            The type of interpolation used when resizing the sprite to
	 *            match the destination's size.
	 * @param tint
	 *            The color to tint a sprite. Use Color.WHITE for full color
	 *            with no tinting.
	 * @param alphaFactor
	 *            alpha value applied to the new color ranging from 0.0f
	 *            (completely dark) to 1.0f.
	 * @param blendState
	 *            The type of blending to use for this sprite.
	 * @throws NullPointerException
	 *             If the sprite is null.
	 */
	public void draw(Sprite sprite, Rectangle destinationRectangle, InterpolationType interpolationType, Color tint,
			float alphaFactor, BlendState blendState) throws NullPointerException {

		checkValidSprite(sprite);

		int[] spritePixels;
		int spriteWidth, spriteHeight;

		// Determine if we need to resize our sprite. If so, we select the
		// interpolation type and set our local variables accordingly.
		// Otherwise, we set our local variables to the original's sprite
		// values.
		if (sprite.getWidth() != destinationRectangle.width || sprite.getHeight() != destinationRectangle.height) {
			spriteWidth = destinationRectangle.width;
			spriteHeight = destinationRectangle.height;
			spritePixels = new int[spriteWidth * spriteHeight];
			switch (interpolationType) {
				case NEAREST_NEIGHBOR:
					spritePixels = resizeNearestNeighbor(sprite, spriteWidth, spriteHeight);
					break;
				case BILINEAR:
					spritePixels = resizeBilinear(sprite, spriteWidth, spriteHeight);
					break;
				case BICUBIC:
					break;
				default:
					// TODO: Do I want a default action or throw an exception.
			}
		} else {
			spriteWidth = sprite.getWidth();
			spriteHeight = sprite.getHeight();
			spritePixels = new int[spriteWidth * spriteHeight];
			spritePixels = sprite.pixels;
		}

		int xDestPos = destinationRectangle.x;
		int yDestPos = destinationRectangle.y;

		// Extract our tint components
		int rTint = tint.getRed();
		int gTint = tint.getGreen();
		int bTint = tint.getBlue();

		// Calculate the new foreground color alpha component
		int foregroundAlpha = (int) (alphaFactor * 255);

		// Cycle through all the sprites pixels.
		for (int y = 0; y < spriteHeight; y++) {
			// Set our Y position relative to the screen.
			int ya = y + yDestPos;
			for (int x = 0; x < spriteWidth; x++) {
				// Set our X position relative to the screen.
				int xa = x + xDestPos;
				// If this pixel is out of the screen's bound, skip it.
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;

				// The color of the pixel about to be drawn.
				int foregroundCol = spritePixels[x + y * spriteWidth];
				// The color of the pixel already there.
				int backgroundCol = screenPixels[xa + ya * width];
				// The alpha value of the pixel about to be drawn.
				if (alphaFactor < 0.0f) {
					foregroundAlpha = (foregroundCol >> 24) & 0xff;
				}
				// The resulting color of the pixel to be drawn
				int col;

				// The components of the pixel about to be drawn.
				int foregroundR = (foregroundCol >> 16) & 0xff;
				int foregroundG = (foregroundCol >> 8) & 0xff;
				int foregroundB = (foregroundCol) & 0xff;

				// Using Color.WHITE would have no effect so skip this
				if (!tint.equals(Color.WHITE)) {
					// Typical tint formula -> original component * tint / 255
					foregroundR = foregroundR * rTint / 255;
					foregroundG = foregroundG * gTint / 255;
					foregroundB = foregroundB * bTint / 255;
				}

				// If alpha is 255 it completely overrides the existing color.
				if (foregroundAlpha == 255 || blendState == BlendState.OPAQUE) {
					col = foregroundAlpha << 24 | foregroundR << 16 | foregroundG << 8 | foregroundB;
				} else if (blendState == BlendState.ALPHA_BLEND) {
					// Do the alpha-blending.
					int backgroundR = (backgroundCol >> 16) & 0xff;
					int backgroundG = (backgroundCol >> 8) & 0xff;
					int backgroundB = (backgroundCol) & 0xff;
					// Typical over blend formula
					int r = (foregroundR * foregroundAlpha / 255) + (backgroundR * (255 - foregroundAlpha) / 255);
					int g = (foregroundG * foregroundAlpha / 255) + (backgroundG * (255 - foregroundAlpha) / 255);
					int b = (foregroundB * foregroundAlpha / 255) + (backgroundB * (255 - foregroundAlpha) / 255);
					col = r << 16 | g << 8 | b;
				} else {
					// Defaults to BlendState.OPAQUE after tinting.
					col = foregroundAlpha << 24 | foregroundR << 16 | foregroundG << 8 | foregroundB;
				}
				screenPixels[xa + ya * width] = col;
			}
		}
	}

	/**
	 * Renders an ARGB sprite using a color to tint the sprite and an alpha
	 * value to blend the resulting color with the background using alpha
	 * blending. The alphaFactor is useful to create fading effects. If this
	 * value is set to -1.0f, the sprites original alpha channel per pixel will
	 * be used. If the destination rectangle is bigger or smaller than the
	 * specified sprite, the sprite will be resized to the destination
	 * rectangle's size using the specified resizing method.
	 * 
	 * <p>
	 * The formula for used is this :
	 * <p>
	 * <code>tint / 255 * originalColorComponent</code>
	 * <p>
	 * The new alpha channel for the resulting color is calculated like this :
	 * <p>
	 * <code>(int) alphaFactor * 255</code>
	 * <p>
	 * We then do a typical alpha-blending between the background color and the
	 * new color.
	 * 
	 * @param sprite
	 *            The sprite to draw.
	 * @param sourceRectangle
	 *            The source's position and size. This argument can be null. A
	 *            source's size smaller than the sprite's size means that we
	 *            will be drawing only a portion of the sprite. Useful for
	 *            animation strips.
	 * @param destinationRectangle
	 *            The destination's position and size. If the destination's size
	 *            is equal to the sprite's size, no resizing will be done to the
	 *            sprite.
	 * @param interpolationType
	 *            The type of interpolation used when resizing the sprite to
	 *            match the destination's size.
	 * @param tint
	 *            The color to tint a sprite. Use Color.WHITE for full color
	 *            with no tinting.
	 * @param alphaFactor
	 *            alpha value applied to the new color ranging from 0.0f
	 *            (completely dark) to 1.0f.
	 * @param blendState
	 *            The type of blending to use for this sprite.
	 * @throws NullPointerException
	 *             If the sprite is null.
	 * @throws IllegalArgumentException
	 *             If the sourceRectangle size is bigger than the sprite size.
	 */
	public void draw(Sprite sprite, Rectangle sourceRectangle, Rectangle destinationRectangle,
			InterpolationType interpolationType, Color tint, float alphaFactor, BlendState blendState)
			throws NullPointerException {

		if (sourceRectangle.width > sprite.getWidth() || sourceRectangle.height > sprite.getHeight())
			throw new IllegalArgumentException("sourceRectangle cannot be bigger than sprite size");

		checkValidSprite(sprite);

		int[] spritePixels;
		int spriteWidth, spriteHeight;

		// Determine if we need to resize our sprite. If so, we select the
		// interpolation type and set our local variables accordingly.
		// Otherwise, we set our local variables to the original's sprite
		// values.
		if (sourceRectangle.width != destinationRectangle.width
				|| sourceRectangle.height != destinationRectangle.height) {
			spriteWidth = destinationRectangle.width;
			spriteHeight = destinationRectangle.height;
			spritePixels = new int[spriteWidth * spriteHeight];
			switch (interpolationType) {
				case NEAREST_NEIGHBOR:
					spritePixels = resizeNearestNeighbor(sprite, sourceRectangle.x, sourceRectangle.y, spriteWidth,
							spriteHeight);
					break;
				case BILINEAR:
					spritePixels = resizeBilinear(sprite, sourceRectangle.x, sourceRectangle.y, spriteWidth,
							spriteHeight);
					break;
				case BICUBIC:
					break;
				default:
					// TODO: Do I want a default action or throw an exception.
			}
		} else {
			spriteWidth = sourceRectangle.width;
			spriteHeight = sourceRectangle.height;
			spritePixels = new int[spriteWidth * spriteHeight];
			spritePixels = sprite.pixels;
		}

		int xDestPos = destinationRectangle.x;
		int yDestPos = destinationRectangle.y;

		// Extract our tint components
		int rTint = tint.getRed();
		int gTint = tint.getGreen();
		int bTint = tint.getBlue();

		// Calculate the new foreground color alpha component
		int foregroundAlpha = (int) (alphaFactor * 255);

		// Cycle through all the sprites pixels.
		for (int y = sourceRectangle.y; y < spriteHeight; y++) {
			// Set our Y position relative to the screen.
			int ya = y + yDestPos;
			for (int x = sourceRectangle.x; x < spriteWidth; x++) {
				// Set our X position relative to the screen.
				int xa = x + xDestPos;
				// If this pixel is out of the screen's bound, skip it.
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;

				// The color of the pixel about to be drawn.
				int foregroundCol = spritePixels[x + y * spriteWidth];
				// The color of the pixel already there.
				int backgroundCol = screenPixels[xa + ya * width];
				// The alpha value of the pixel about to be drawn.
				if (alphaFactor < 0.0f) {
					foregroundAlpha = (foregroundCol >> 24) & 0xff;
				}
				// The resulting color of the pixel to be drawn
				int col;

				// The components of the pixel about to be drawn.
				int foregroundR = (foregroundCol >> 16) & 0xff;
				int foregroundG = (foregroundCol >> 8) & 0xff;
				int foregroundB = (foregroundCol) & 0xff;

				// Using Color.WHITE would have no effect so skip this
				if (!tint.equals(Color.WHITE)) {
					// Typical tint formula -> original component * tint / 255
					foregroundR = foregroundR * rTint / 255;
					foregroundG = foregroundG * gTint / 255;
					foregroundB = foregroundB * bTint / 255;
				}

				// If alpha is 255 it completely overrides the existing color.
				if (foregroundAlpha == 255 || blendState == BlendState.OPAQUE) {
					col = foregroundAlpha << 24 | foregroundR << 16 | foregroundG << 8 | foregroundB;
				} else if (blendState == BlendState.ALPHA_BLEND) {
					// Do the alpha-blending.
					int backgroundR = (backgroundCol >> 16) & 0xff;
					int backgroundG = (backgroundCol >> 8) & 0xff;
					int backgroundB = (backgroundCol) & 0xff;
					// Typical over blend formula
					int r = (foregroundR * foregroundAlpha / 255) + (backgroundR * (255 - foregroundAlpha) / 255);
					int g = (foregroundG * foregroundAlpha / 255) + (backgroundG * (255 - foregroundAlpha) / 255);
					int b = (foregroundB * foregroundAlpha / 255) + (backgroundB * (255 - foregroundAlpha) / 255);
					col = r << 16 | g << 8 | b;
				} else {
					// Defaults to BlendState.OPAQUE after tinting.
					col = foregroundAlpha << 24 | foregroundR << 16 | foregroundG << 8 | foregroundB;
				}
				screenPixels[xa + ya * width] = col;
			}
		}
	}

	/**
	 * Renders the specified sprite representing a text character to the buffer.
	 * The specified alpha color will not be rendered, that is, it will let what
	 * is behind it be seen unaltered.
	 * 
	 * @param xDestPos
	 *            The x destination position (in screen coordinates) at which to
	 *            draw the sprite.
	 * @param yDestPos
	 *            The y destination position (in screen coordinates) at which to
	 *            draw the sprite.
	 * @param spriteFont
	 *            The sprite to draw.
	 * @param alphaColor
	 *            The color in the ARGB model that will be transparent.
	 * @param fixedPos
	 *            {@code false} if the sprite is fixed to the screen position,
	 *            {@code true} otherwise.
	 */
	public void drawString(Sprite spriteFont, int xDestPos, int yDestPos, int alphaColor, boolean fixedPos) {
		if (fixedPos) {
			xDestPos -= xOffset;
			yDestPos -= yOffset;
		}
		for (int y = 0; y < spriteFont.getHeight(); y++) {
			int ya = y + yDestPos;
			for (int x = 0; x < spriteFont.getWidth(); x++) {
				int xa = x + xDestPos;
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
				int currentColor = spriteFont.pixels[x + y * spriteFont.getWidth()];
				if (currentColor != alphaColor) screenPixels[xa + ya * width] = alphaColor;
			}
		}
	}

	/**
	 * Renders the specified sprite sheet at the specified x and y position
	 * without any scaling. Useful for debugging.
	 * 
	 * @param sheet
	 *            The sprite sheet to draw.
	 * @param xDestPos
	 *            The x destination position (in screen coordinates) at which to
	 *            draw the sprite sheet.
	 * @param yDestPos
	 *            The y destination position (in screen coordinates) at which to
	 *            draw the sprite sheet.
	 */
	public void drawSheet(SpriteSheet sheet, int xDestPos, int yDestPos) {
		for (int y = 0; y < sheet.SPRITE_HEIGHT; y++) {
			int ya = y + yDestPos;
			for (int x = 0; x < sheet.SPRITE_WIDTH; x++) {
				int xa = x + xDestPos;
				if (xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
				screenPixels[xa + ya * width] = sheet.pixels[x + y * sheet.SPRITE_WIDTH];
			}
		}
	}

	/**
	 * Draws the outline of a rectangle with the specified attributes.
	 * 
	 * @param xDestPos
	 *            The x position of the top left corner.
	 * @param yDestPos
	 *            The y position of the top left corner.
	 * @param width
	 *            The width of the rectangle in pixels.
	 * @param height
	 *            The height of the rectangle in pixels.
	 * @param color
	 *            The color from Java's Color class.
	 * @param fixed
	 *            {@code false} if the sprite is fixed to the screen position,
	 *            {@code true} otherwise.
	 */
	public void drawRect(int xDestPos, int yDestPos, int width, int height, Color color, boolean fixed) {
		this.drawRect(xDestPos, yDestPos, width, height, color.getRGB(), fixed);
	}

	/**
	 * Draws the outline of a rectangle with the specified attributes.
	 * 
	 * @param xDestPos
	 *            The x position of the top left corner.
	 * @param yDestPos
	 *            The y position of the top left corner.
	 * @param width
	 *            The width of the rectangle in pixels.
	 * @param height
	 *            The height of the rectangle in pixels.
	 * @param color
	 *            The color in the ARGB format.
	 * @param fixed
	 *            {@code false} if the sprite is fixed to the screen position,
	 *            {@code true} otherwise.
	 */
	public void drawRect(int xDestPos, int yDestPos, int width, int height, int color, boolean fixed) {
		if (fixed) {
			xDestPos -= xOffset;
			yDestPos -= yOffset;
		}
		for (int x = xDestPos; x < xDestPos + width; x++) {
			if (x < 0 || x >= this.width || yDestPos >= this.height) continue;
			if (yDestPos > 0) screenPixels[x + yDestPos * this.width] = color;
			if (yDestPos + height >= this.height) continue;
			if (yDestPos + height > 0) screenPixels[x + (yDestPos + height) * this.width] = color;
		}
		for (int y = yDestPos; y <= yDestPos + height; y++) {
			if (xDestPos >= this.width || y < 0 || y >= this.height) continue;
			if (xDestPos > 0) screenPixels[xDestPos + y * this.width] = color;
			if (xDestPos + width >= this.width) continue;
			if (xDestPos + width > 0) screenPixels[(xDestPos + width) + y * this.width] = color;
		}
	}

	/**
	 * Resizes an ARGB sprite using the bilinear interpolation algorithm. Useful
	 * for a animation strips. The new width or the new height cannot be less
	 * than or equal to 0.
	 * 
	 * @param sprite
	 *            The original sprite to be resized.
	 * @param newWidth
	 *            The new sprite width.
	 * @param newHeight
	 *            The new sprite height.
	 * @return The reference to the array of pixels containing the resized
	 *         sprite's pixels.
	 * @throws IllegalArgumentException
	 *             If the new width or the new height is less than or equal to
	 *             0.
	 */
	public int[] resizeBilinear(Sprite sprite, int newWidth, int newHeight) throws IllegalArgumentException {
		return resizeBilinear(sprite, 0, 0, newWidth, newHeight);
	}

	/**
	 * Resizes a portion of an ARGB sprite using the bilinear interpolation
	 * algorithm. The
	 * new width or the new height cannot be less than or equal to 0.
	 * 
	 * @param sprite
	 *            The original sprite to be resized.
	 * @param startX
	 *            The starting location on the x axis.
	 * @param startY
	 *            The starting location on the y axis
	 * @param newWidth
	 *            The new sprite width.
	 * @param newHeight
	 *            The new sprite height.
	 * @return The reference to the array of pixels containing the resized
	 *         sprite's pixels.
	 * @throws IllegalArgumentException
	 *             If the new width or the new height is less than or equal to
	 *             0.
	 */
	public int[] resizeBilinear(Sprite sprite, int startX, int startY, int newWidth, int newHeight)
			throws IllegalArgumentException {
		if (newWidth <= 0 || newHeight <= 0)
			throw new IllegalArgumentException("new width or new height cannot be less than or equal to 0");
		int[] temp = new int[newWidth * newHeight];
		int orignialWidth = sprite.getWidth();
		int originalHeight = sprite.getHeight();

		int a, b, c, d, x, y, index;
		float x_ratio = ((float) (orignialWidth - 1)) / newWidth;
		float y_ratio = ((float) (originalHeight - 1)) / newHeight;
		float x_diff, y_diff, blue, red, green, alpha;
		int offset = 0;

		for (int i = startY; i < newHeight; i++) {
			for (int j = startX; j < newWidth; j++) {
				x = (int) (x_ratio * j);
				y = (int) (y_ratio * i);
				x_diff = (x_ratio * j) - x;
				y_diff = (y_ratio * i) - y;
				index = (x + y * orignialWidth);
				a = sprite.pixels[index];
				b = sprite.pixels[index + 1];
				c = sprite.pixels[index + orignialWidth];
				d = sprite.pixels[index + orignialWidth + 1];

				// blue element
				// Yb = Ab(1-w)(1-h) + Bb(w)(1-h) + Cb(h)(1-w) + Db(wh)
				blue = (a & 0xff) * (1 - x_diff) * (1 - y_diff) + (b & 0xff) * (x_diff) * (1 - y_diff) + (c & 0xff)
						* (y_diff) * (1 - x_diff) + (d & 0xff) * (x_diff * y_diff);

				// green element
				// Yg = Ag(1-w)(1-h) + Bg(w)(1-h) + Cg(h)(1-w) + Dg(wh)
				green = ((a >> 8) & 0xff) * (1 - x_diff) * (1 - y_diff) + ((b >> 8) & 0xff) * (x_diff) * (1 - y_diff)
						+ ((c >> 8) & 0xff) * (y_diff) * (1 - x_diff) + ((d >> 8) & 0xff) * (x_diff * y_diff);

				// red element
				// Yr = Ar(1-w)(1-h) + Br(w)(1-h) + Cr(h)(1-w) + Dr(wh)
				red = ((a >> 16) & 0xff) * (1 - x_diff) * (1 - y_diff) + ((b >> 16) & 0xff) * (x_diff) * (1 - y_diff)
						+ ((c >> 16) & 0xff) * (y_diff) * (1 - x_diff) + ((d >> 16) & 0xff) * (x_diff * y_diff);

				// alpha element
				// Ya = Aa(1-w)(1-h) + Ba(w)(1-h) + Ca(h)(1-w) + Da(wh)
				alpha = ((a >> 24) & 0xff) * (1 - x_diff) * (1 - y_diff) + ((b >> 24) & 0xff) * (x_diff) * (1 - y_diff)
						+ ((c >> 24) & 0xff) * (y_diff) * (1 - x_diff) + ((d >> 24) & 0xff) * (x_diff * y_diff);

				temp[offset++] = ((((int) alpha) << 24) & 0xff000000) | ((((int) red) << 16) & 0xff0000)
						| ((((int) green) << 8) & 0xff00) | ((int) blue);
			}
		}
		return temp;
	}

	/**
	 * Resizes an ARGB sprite using the nearest neighbor interpolation
	 * algorithm. The new width or the new height cannot be less than or equal
	 * to 0.
	 * 
	 * @param sprite
	 *            The original sprite to be resized.
	 * @param newWidth
	 *            The new sprite width.
	 * @param newHeight
	 *            The new sprite height.
	 * @return The reference array of pixels containing the resized sprite's
	 *         pixels.
	 * @throws IllegalArgumentException
	 *             If the new width or the new height is less than or equal to
	 *             0.
	 */
	public int[] resizeNearestNeighbor(Sprite sprite, int newWidth, int newHeight) {
		return resizeNearestNeighbor(sprite, 0, 0, newWidth, newHeight);
	}

	/**
	 * Resizes a portion of an ARGB sprite using the nearest neighbor
	 * interpolation algorithm. Useful for a animation strips. The new width or
	 * the new height cannot be less than or equal to 0.
	 * 
	 * @param sprite
	 *            The original sprite to be resized.
	 * @param startX
	 *            The starting location on the x axis.
	 * @param startY
	 *            The starting location on the y axis.
	 * @param newWidth
	 *            The new sprite width.
	 * @param newHeight
	 *            The new sprite height.
	 * @return The reference array of pixels containing the resized sprite's
	 *         pixels.
	 * @throws IllegalArgumentException
	 *             If the new width or the new height is less than or equal to
	 *             0.
	 */
	public int[] resizeNearestNeighbor(Sprite sprite, int startX, int startY, int newWidth, int newHeight) {
		if (newWidth <= 0 || newHeight <= 0)
			throw new IllegalArgumentException("new width or new height cannot be less than or equal to 0");
		int[] temp = new int[newWidth * newHeight];
		int oldWidth = sprite.getWidth();
		int oldHeight = sprite.getHeight();
		float xRatio = (float) newWidth / (float) oldWidth;
		float yRatio = (float) newHeight / (float) oldHeight;

		for (int y = startY; y < newHeight; y++) {
			for (int x = startX; x < newWidth; x++) {
				temp[x + y * newWidth] = sprite.pixels[(int) (x / xRatio) + (int) (y / yRatio) * oldWidth];
			}
		}
		return temp;
	}

	private void checkValidSprite(Sprite sprite) throws NullPointerException {
		if (sprite == null) throw new NullPointerException("sprite is null");
	}

	// ++++++++++ GETTERS ++++++++++ //

	/**
	 * Returns the width of the screen.
	 * 
	 * @return The width of the screen.
	 */
	public int getScreenWidth() {
		return width;
	}

	/**
	 * Returns the height of the screen.
	 * 
	 * @return The height of the screen.
	 */
	public int getScreenHeight() {
		return height;
	}
}
