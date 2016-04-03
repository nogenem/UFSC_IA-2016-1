package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Gomoku extends JFrame implements ActionListener {
	
	private final Dimension configDim = new Dimension(250, 200);
	private final Dimension gomokuDim = new Dimension(600, 650);
	
	private GamePanel gamePanel;
	private ConfigPanel configPanel;
	private JPanel btnsPanel;
	
	public Gomoku() {
		setTitle("Gomoku");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(0,0));
		
		//soh teste
		showGame();
		
		setVisible(true);
	}

	public static void main(String[] args) {
		new Gomoku();
	}
	
	private void showConfig(){
		
	}
	
	private void showGame(){
		if(getContentPane().getComponentCount() > 0){
			getContentPane().removeAll();
		}
		
		setSize(this.gomokuDim);
		getContentPane().add(getGamePanel(), BorderLayout.CENTER);
		
		setLocationRelativeTo(null);
		
		// soh teste
		getGamePanel().init(true, false);
	}
	
	public void gameOver(){
		
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}
	
	// ------- Getters and Setters ------- \\
	public JPanel getBtnsPanel(){
		if(this.btnsPanel == null){
			
		}
		return this.btnsPanel;
	}
	
	public ConfigPanel getConfigPanel(){
		if(this.configPanel == null)
			this.configPanel = new ConfigPanel();
		return this.configPanel;
	}
	
	public GamePanel getGamePanel(){
		if(this.gamePanel == null)
			this.gamePanel = new GamePanel(this);
		return this.gamePanel;
	}

}
