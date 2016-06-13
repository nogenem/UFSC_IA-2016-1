import java.applet.Applet;
import javax.swing.JFrame;

/**
 * An applet that holds a robot's sandbox and settings panel
 *
 * @author Ondrej Stanek
 */
public class LineFollowerApplet extends Applet {

	public RobotSandbox sandbox;

	@Override
	public void init() {
		this.setSize(920, 373);
		sandbox = new RobotSandbox();
		this.add(sandbox);
		sandbox.setVisible(true);

		SettingsJPanel settings = new SettingsJPanel(sandbox.robot);
		add(settings);
		settings.UpdateAllSettings();

		sandbox.StartAnimation();

	}

	public static void main(String[] args) {

		// ... Create an initialize the applet.
		LineFollowerApplet applet = new LineFollowerApplet();
		applet.init();

		// ... Create a window (JFrame) and make applet the content pane.
		JFrame window = new JFrame("Line follower simulator");
		window.setContentPane(applet);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack(); // Arrange the components.
		window.setVisible(true); // Make the window visible.

	}

}
