import java.awt.geom.GeneralPath;
import java.awt.*;
import java.awt.geom.CubicCurve2D;

/**
 * The playground for the robot. Contains the robot and a line. Runs its own
 * thread that does discrete (step-by-step) simulation of the robot movement.
 * Animation framerate is set to 50Hz.
 *
 * @author Ondrej Stanek
 */
public class RobotSandbox extends Canvas implements Runnable {

  // graphic buffer
  private Image dbImage;
  private Graphics dbg;

  public Robot robot;
  private GeneralPath guideline;

  Thread animationThread;
  private final long DELAY = 1000 / 50; // 50 FPS animation

  /**
   * Initializes the robot and a sample guideline.
   */
  public RobotSandbox() {

    guideline = new GeneralPath();

    this.setSize(600, 300);

    guideline.append(new CubicCurve2D.Float(200, 100, 200, 200, 100, 100, 100, 200), false);
    guideline.append(new CubicCurve2D.Float(100, 200, 100, 300, 300, 200, 400, 200), false);
    guideline.append(new CubicCurve2D.Float(400, 200, 600, 200, 200, 0, 200, 100), false);

    robot = new Robot(200, 100, Math.PI / 2, guideline);

    animationThread = new Thread(this);
  }

  /**
   * Starts the animation thread of this object.
   */
  public void StartAnimation() {
    animationThread.start();
  }

  @Override
  public void update(Graphics g) {
    if (dbImage == null) {
      dbImage = createImage(this.getSize().width, this.getSize().height);
      dbg = dbImage.getGraphics();
    }
    // clear screen in background
    dbg.setColor(getBackground());
    dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

    // draw elements in background
    dbg.setColor(getForeground());
    paint(dbg);

    // draw image on the screen
    g.drawImage(dbImage, 0, 0, this);
  }

  @Override
  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

    // draw guideline
    g2.setPaint(Color.black);
    g2.setStroke(new BasicStroke(1));
    g2.draw(guideline);

	// draw the robot
    //g2.setStroke(new BasicStroke(1));
    g2.setPaint(Color.blue);
    g2.draw(robot);

  }

  /**
   * Periodically updates robots position and redraw the canvas
   */
  public void run() {

    robot.regulator.StartRegulation();
    // Remember the starting time
    long tm = System.currentTimeMillis();
    while (Thread.currentThread() == animationThread) {
      
      if (robot.regulator.hasClient()) {

        robot.Move();
        repaint();

        // Delay depending on how far we are behind.
        try {
          tm += DELAY;
          Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
        } catch (InterruptedException e) {
          break;
        }
      } else {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          break;
        }
      }
    }
  }
}
