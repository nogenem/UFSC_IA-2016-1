package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
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
		
		showConfig();
		
		setVisible(true);
	}

	public static void main(String[] args) {
		new Gomoku();
	}
	
	private void showConfig(){
		if(getContentPane().getComponentCount() > 0){
			getContentPane().removeAll();
		}
		
		setSize(this.configDim);
		getContentPane().add(getConfigPanel(), BorderLayout.CENTER);
		getContentPane().add(getBtnsPanel(), BorderLayout.SOUTH);
		
		setLocationRelativeTo(null);
	}
	
	private void showGame(){
		if(getContentPane().getComponentCount() > 0){
			getContentPane().removeAll();
		}
		
		setSize(this.gomokuDim);
		getContentPane().add(getGamePanel(), BorderLayout.CENTER);
		
		setLocationRelativeTo(null);
		
		String whomBegins = getConfigPanel().getWhomBegins();
		String gameType = getConfigPanel().getGameType();
		
		getGamePanel().init(gameType=="Multiplayer", whomBegins=="IA");
	}
	
	public void gameOver(){
		showConfig();
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "Come\u00E7ar"){
			showGame();
		}else if(e.getActionCommand() == "Sair"){
			System.exit(0);
		}
	}
	
	// ------- Getters and Setters ------- \\
	public JPanel getBtnsPanel(){
		if(this.btnsPanel == null){
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			
			JButton btnComecar = new JButton("Come\u00E7ar");
			btnComecar.addActionListener(this);
			panel.add(btnComecar);
			
			JButton btnSair = new JButton("Sair");
			btnSair.addActionListener(this);
			panel.add(btnSair);
			
			this.btnsPanel = panel;
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
