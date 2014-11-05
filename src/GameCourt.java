/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.ArrayList;
import java.util.prefs.Preferences;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 * 
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

  
	// the state of the game logic
private Square square; // the Black Square, keyboard control

private int count;
private ArrayList<Poison> poisonBall = new ArrayList<Poison>();

public boolean playing = false; // whether the game is running
private JLabel status; // Current status text (i.e. Running...)

// Game constants
public static final int COURT_WIDTH = 1100;
public static final int COURT_HEIGHT = 700;
public static int SQUARE_VELOCITY = 10;
// Update interval for timer, in milliseconds
public static final int INTERVAL = 25;

public static int point;
public static double timer;
final static String img_file = "back.jpg";
static BufferedImage img;

final static String point_file = "1.png";
static BufferedImage pointimg;

final static String over_file = "GameOver.png";
static BufferedImage overimg;

static int x;
static int y;






Preferences pref2 = Preferences.userRoot();
private int allTime;


public void instructions() {

  playing = false;
  JOptionPane.showMessageDialog(null, "In this game, you are a goalkeeper!"
      + "\n" + "For every ball you catch, you get a point!"
      + "\n" + "For every ball you miss, you lose three points!"
      + "\n" + "You have 120 seconds to create a new high score."

      + "\n" + "If you press up or down, you keep diving till you change directions"

      + "\n" + "However, you can move sideways freely."
      + "\n" + "\n" + "Good luck!"

      );
  requestFocusInWindow();

  playing = true;

}



public void high() {
  // if no high score yet, give 0
  int b = pref2.getInt("b", 0);
  
  // if current high score is bigger than high score, replace it
  if (point > b) {
    pref2.putInt("b", point);
  }
  // update the high score panel
  allTime = pref2.getInt("b", 0);
}


public void initializeBall() {
  count = 0;
  poisonBall.add(new Poison(COURT_WIDTH, COURT_HEIGHT));
  
       ballValues(poisonBall.get(poisonBall.size() - 1));  
}    
  
public void ballValues(Poison a){
  int count = point;
  if (point < 0) count = 0;
  a.pos_x = 1100;
  a.pos_y = (int) (Math.random() * 700);   
  
  int forX = (int) (Math.random() * 8) + count/2;
  int forY = (int) (Math.random() * 2);
  a.v_x = forX + 2;
  if (forY == 0) {
    a.v_y = -8 - count/5;
  }
  
  else {
    a.v_y = 8 + count/5;
  }
  
  forX = 0;
  forY = 0;  
}



public static void back() {
  try {
    img = ImageIO.read(new File(img_file));
  } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
}

public static void over() {
  try {
    overimg = ImageIO.read(new File(over_file));
  } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
}


public static void point(Poison a) {
  try {
    pointimg = ImageIO.read(new File(point_file));
  } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
  
  x = a.pos_x;
  y = a.pos_y;
  
}

public int miss;
public GameCourt(JLabel status) {
  miss = 0;
  allTime = pref2.getInt("b", 0);
  timer = 0.000;
  point = 0;
  back();
  initializeBall();
  over();
	// creates border around the court area, JComponent method
setBorder(BorderFactory.createLineBorder(Color.BLACK));

// The timer is an object which triggers an action periodically
// with the given INTERVAL. One registers an ActionListener with
// this timer, whose actionPerformed() method will be called
// each time the timer triggers. We define a helper method
// called tick() that actually does everything that should
// be done in a single timestep.
Timer timer = new Timer(INTERVAL, new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	  tick();
	}
});
timer.start(); // MAKE SURE TO START THE TIMER!

// Enable keyboard focus on the court area.
// When this component has the keyboard focus, key
// events will be handled by its key listener.
setFocusable(true);

// This key listener allows the square to move as long
// as an arrow key is pressed, by changing the square's
// velocity accordingly. (The tick method below actually
// moves the square.)
	addKeyListener(new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			
			if (e.getKeyCode() == KeyEvent.VK_DOWN)
		
        square.v_y = SQUARE_VELOCITY;
			
			else if (e.getKeyCode() == KeyEvent.VK_UP)
				square.v_y = -SQUARE_VELOCITY;
		}

		public void keyReleased(KeyEvent e) {
		  if (square.pos_y == 600 || square.pos_y == 0) {
		  square.v_y = 0;
		  }
		  square.v_x = 0;
		}
	});

	this.status = status;
}

/**
 * (Re-)set the game to its initial state.
 */
public void reset() {
  timer = 0.000;
  point = 0;
	miss = 0;
  square = new Square(COURT_WIDTH, COURT_HEIGHT);
	poisonBall = new ArrayList<Poison>();
	initializeBall();		
	
	
	playing = true;
	status.setText("Running...");

// Make sure that this component has the keyboard focus
	requestFocusInWindow();
}

/**
 * This method is called every time the timer defined in the constructor
 * triggers.
 */
void tick() {
	if (playing) {
	  
	  timer = timer + .025;
	  count = count + 1;
	  if (count >= 80) {
	    initializeBall();
	    count = point;
	  }
	  
	square.move();
	if (point > 0) {
	SQUARE_VELOCITY = 10 + point/3;
	}
	
	int thisSize = poisonBall.size();
	for (int x = 0; x < thisSize; x++) {
	  if (poisonBall.get(x) != null) {
	  poisonBall.get(x).move();    
	  
	   if (poisonBall.get(x).pos_x <= 5) { 
	     poisonBall.remove(x);
	     thisSize = thisSize - 1;
	     x = x - 1;
	     miss = miss + 1;
       point = point - miss;
	  }

	   else if (square.intersects(poisonBall.get(x))) {
	     poisonBall.get(x).move();
	     poisonBall.get(x).move();
	     
	     point(poisonBall.get(x));
	     poisonBall.remove(x);
  	  thisSize = thisSize - 1;
  	  x = x - 1;
  	  point = point + 1 + (int) timer/20;

	  }
	
	   else if (x < thisSize && poisonBall.get(x) != null) {
	     poisonBall.get(x).bounce(poisonBall.get(x).hitWall());
	     for (int y = 0; y < poisonBall.size(); y++) {
	       if (y!= x) {
	         poisonBall.get(x).bounce(poisonBall.get(x).hitObj(poisonBall.get(y)));
	  }
	}
	}
	  }
	}
	// make the snitch bounce off walls...

// check for the game end conditions



// update the display
			repaint();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
	
		super.paintComponent(g);
		g.drawImage(img, 0, 0 , 1100, 700, null);
		g.drawImage(pointimg, x, y , 100, 100, null);
		square.draw(g);
	  for (int x = 0; x < poisonBall.size(); x++) {	    

	    poisonBall.get(x).draw(g);
	  }
	  g.setFont(new Font("ComicSans", Font.PLAIN, 40)); 
	  g.setColor(Color.WHITE);
    g.drawString("Points: " + point , 10, 35);
    g.drawString("Timer: " + (int) timer , 10, 70);
    g.drawString("Max: " + allTime , 930, 35);
    g.drawString("Miss: " + miss , 930, 70);
    
    if (timer >= 100) {
      high();
      playing = false;

      g.drawImage(overimg, 200, 100 , 600, 400, null);
      
    }
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}
}
