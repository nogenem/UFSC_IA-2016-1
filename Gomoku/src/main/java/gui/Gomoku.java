package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import core.Board;

@SuppressWarnings("serial")
/**
 * Classe responsável por criar e manter a GUI do jogo.
 * 
 * @author Gilney N. Mathias
 */
public class Gomoku extends JFrame implements ActionListener {
	
	// Dimensão para o frame quando possuir o painel de configuração
	private final Dimension configDim = new Dimension(250, 200);
	// Dimensão para o frame quando possuir o painel do jogo
	private final Dimension gameDim = new Dimension(600, 650);

	private GamePanel gamePanel;
	private ConfigPanel configPanel;
	private JPanel btnsPanel;
	private JPanel gameStatusPanel;
	private JLabel gameStatusLbl;

	public Gomoku() {
		setTitle("Gomoku");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(0, 0));

		showConfig();

		setVisible(true);
	}
	
	public static void main(String[] args) {
		new Gomoku();
	}
	
	/**
	 * Remove outros painéis do frame, caso tenha algum, e 
	 *  adiciona o painel de configuração do jogo.
	 */
	private void showConfig() {
		// Remove a gamePanel, se ele estiver visivel
		if (getContentPane().getComponentCount() > 0) {
			getContentPane().removeAll();
		}

		setSize(this.configDim);
		getContentPane().add(getConfigPanel(), BorderLayout.CENTER);
		getContentPane().add(getBtnsPanel(), BorderLayout.SOUTH);

		setLocationRelativeTo(null); //centraliza a window
	}
	
	/**
	 * Remove outros painéis do frame, caso tenha algum, e 
	 *  adiciona o painel do jogo.
	 */
	private void showGame() {
		// Remove a configPanel, se ele estiver visivel
		if (getContentPane().getComponentCount() > 0) {
			getContentPane().removeAll();
		}

		setSize(this.gameDim);
		getContentPane().add(getGameStatusPanel(), BorderLayout.NORTH);
		getContentPane().add(getGamePanel(), BorderLayout.CENTER);

		setLocationRelativeTo(null); //centraliza a window

		String whomBegins = getConfigPanel().getWhomBegins();
		String gameMode = getConfigPanel().getGameMode();

		getGamePanel().init(gameMode == "Multiplayer", whomBegins == "IA");
	}
	
	/**
	 * Apenas um wrapper para a função {@link #showConfig()} para ser chamado
	 *  pela classe GamePanel quando o jogo terminar.
	 */
	public void gameOver() {
		showConfig();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Come\u00E7ar") {//Começar
			showGame();
		} else if (e.getActionCommand() == "Sair") {
			System.exit(0);
		} else if (e.getActionCommand() == "Voltar") {
			this.gamePanel.gameOver();
			showConfig();
		}
	}

	// ------- Getters and Setters ------- \\
	/**
	 * Retorna o painel de botões da janela de configuração do jogo.</br>
	 * Caso o painel ainda não tenha sido criado, ele é criado antes de 
	 *  ser retornado.
	 * 
	 * @return				Painel de botões.
	 */
	public JPanel getBtnsPanel() {
		if (this.btnsPanel == null) {
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
	
	/**
	 * Retorna o painel de status do jogo, que contem de quem é a vez e
	 *  um botão de voltar ao menu de configuração.</br>
	 * Caso o painel ainda não tenha sido criado, ele é criado antes de 
	 *  ser retornado.
	 * 
	 * @return				Painel de status do jogo.
	 */
	public JPanel getGameStatusPanel(){
		if(this.gameStatusPanel == null){
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
			
			Component horizontalStrut = Box.createHorizontalStrut(20);
			panel.add(horizontalStrut);
			
			gameStatusLbl = new JLabel("Game Status");
			gameStatusLbl.setFont(new Font("Tahoma", Font.BOLD, 11));
			panel.add(gameStatusLbl);
			
			Component horizontalGlue = Box.createHorizontalGlue();
			panel.add(horizontalGlue);
			
			JButton btnVoltar = new JButton("Voltar");
			btnVoltar.addActionListener(this);
			panel.add(btnVoltar);
			
			Component horizontalStrut_1 = Box.createHorizontalStrut(20);
			panel.add(horizontalStrut_1);
			
			panel.setBackground(new Color(0.925f, 0.670f, 0.34f));
			
			this.gameStatusPanel = panel;
		}
		return this.gameStatusPanel;
	}
	
	/**
	 * Muda o texto da label de próximo jogador do painel 
	 *  de status do jogo.
	 * 
	 * @param txt			Texto que será exibido na label de 
	 * 						 próximo jogador.
	 */
	public void setGameStatus(String txt){
		this.gameStatusLbl.setText(txt);
	}
	
	/**
	 * Retorna o painel de configuração do jogo.</br>
	 * Caso o painel ainda não tenha sido criado, ele é criado antes de 
	 *  ser retornado.
	 * 
	 * @return				Painel de configuração.
	 */
	public ConfigPanel getConfigPanel() {
		if (this.configPanel == null)
			this.configPanel = new ConfigPanel();
		return this.configPanel;
	}
	
	/**
	 * Retorna o painel do jogo.</br>
	 * Caso o painel ainda não tenha sido criado, ele é criado antes de 
	 *  ser retornado.
	 * 
	 * @return				Painel do jogo.
	 */
	public GamePanel getGamePanel() {
		if (this.gamePanel == null)
			this.gamePanel = new GamePanel(this);
		return this.gamePanel;
	}

}
