/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A basic game object displayed as a black square, starting in the upper left
 * corner of the game court.
 * 
 */
public class Square extends GameObj {
  public static final String img_file = "glov.png";
	public static final int SIZE = 90;
	public static final int INIT_X = 10;
	public static final int INIT_Y = 300;
	public static final int INIT_VEL_X = 0;
	public static final int INIT_VEL_Y = 0;
  private static BufferedImage img;
	/**
	 * Note that, because we don't need to do anything special when constructing
	 * a Square, we simply use the superclass constructor called with the
	 * correct parameters
	 */
	public Square(int courtWidth, int courtHeight) {
		super(INIT_VEL_X, INIT_VEL_Y, INIT_X, INIT_Y, SIZE, SIZE, courtWidth,
				courtHeight);
		
		try {
      img = ImageIO.read(new File(img_file));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
	}

	@Override
	public void draw(Graphics g) {
	  g.drawImage(img, pos_x, pos_y, width, height, null);
	}

}
