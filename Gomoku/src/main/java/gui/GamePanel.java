package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import core.Board;
import core.Computer;
import core.GomokuState;
import core.Move;

/**
 * Classe responsável por criar e gerenciar o painel
 *  que representa o tabuleiro do jogo.</br>
 * 
 *	Arquivo base:
 *  <ul><li><a href="http://cs.gettysburg.edu/~tneller/cs111/gomoku/gui/Gomoku.java">
 *  	http://cs.gettysburg.edu/~tneller/cs111/gomoku/gui/Gomoku.java
 * 	</a></li></ul>
 *  
 * @author Todd W. Neller [base]
 * @author Gilney N. Mathias [edições]
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	private final int MARGIN = 5;
    private final double PIECE_FRAC = 0.9;
    private final int size = 15;		//Tamanho do tabuleiro
    
    private GomokuState state;
    private Computer comp;
    private Gomoku parent;				//JFrame pai deste painel

    private boolean isMultiplayer;		//Jogo é multiplayer? 
    private boolean iaBegins;			//É a IA que começa jogando?
    private boolean canPlayerInteract;	//Player pode adicionar um peça no jogo?
    
	public GamePanel(Gomoku parent) {
		super();
		
		this.parent = parent;
		this.state = new GomokuState(this.size);
		this.comp = null;
		this.isMultiplayer = false;
		this.iaBegins = false;
		this.canPlayerInteract = false;
		addMouseListener(new GomokuListener());
	}
	
	/**
	 * Função que inicia um novo jogo.
	 * 
	 * @param isMultiplayer			O jogo sera multiplayer?
	 * @param iaBegins				A IA vai começar jogando?
	 */
	public void init(boolean isMultiplayer, boolean iaBegins){
    	this.isMultiplayer = isMultiplayer;
		this.iaBegins = (!isMultiplayer && iaBegins);//Ia realmente começa jogando?
		this.canPlayerInteract = !this.iaBegins;
		
		parent.setGameStatus("Vez do player BLACK.");//Player BLACK sempre começa jogando
		
		state.init();
		if(!isMultiplayer)
			comp = new Computer(this, this.iaBegins);
		else
			comp = null;
    }
	
	/**
	 * Classe interna para gerenciar os eventos de mouse
	 *  gerados pelo usuário.
	 */
	class GomokuListener extends MouseAdapter {
		
		/**
		 * Calcula o valor de <b>x</b> e <b>y</b> no tabuleiro e 
		 *  efetua a jogada do usuário nesta posição.</br>
		 * Caso o jogo seja Single Player, destrava a thread que executa a jogada da IA.
		 */
		public void mouseReleased(MouseEvent e) {
			if(!canPlayerInteract) return;

		    double panelWidth = getWidth();
		    double panelHeight = getHeight();
		    double boardWidth = Math.min(panelWidth, panelHeight) - 2 * MARGIN;
		    double squareWidth = boardWidth / size;
		    double xLeft = (panelWidth - boardWidth) / 2 + MARGIN;
		    double yTop = (panelHeight - boardWidth) / 2 + MARGIN;
		    int x = (int) Math.round((e.getX() - xLeft) / squareWidth - 0.5);
		    int y = (int) Math.round((e.getY() - yTop) / squareWidth - 0.5);

		    if (y >= 0 && y < size && x >= 0 && x < size
		    		&& state.getPiece(x, y) == Board.NO_VAL) {
		    	state.playPiece(x, y);
		    	repaint();
		    	
		    	if(checkVictory())
		    		return;
		    	
		    	//Se for singleplayer, destrava a thread da IA
		    	if(!isMultiplayer){
		    		comp.resume();
		    		canPlayerInteract = false;
		    	}
		    }
		    parent.setGameStatus("Vez do player " +(state.getCurrentPlayer()==Board.BLACK?"BLACK":"WHITE")+ ".");
		}    
    }
	
	/**
	 * Função responsável por checar e informar ao usuário se
	 *  houve algum vencedor ou se o jogo empatou.
	 * 
	 * @return				<b>TRUE</b> caso algum player tenha ganho ou tenha terminado em empate,
	 * 						<b>FALSE</b> caso contrário.
	 */
	public boolean checkVictory(){
		char vic = state.checkVitory();
    	if(vic != Board.NO_VAL){
    		switch(vic){
    		case Board.BLACK:
    		case Board.WHITE:
    			JOptionPane.showMessageDialog(parent, "Player "+
    					(vic==Board.WHITE?"Branco":"Preto")+" venceu o jogo!");
    			break;
    		case Board.TIE_VAL:
    			JOptionPane.showMessageDialog(parent, "O jogo empatou!");
    		}
    		this.gameOver();
    		parent.gameOver();
    		return true;
    	}
    	return false;
	}
	
	/**
	 * Pinta o frame com uma cor de madeira, desenha as linhas 
	 *  verticais e horizontais do tabuleiro e desenha todas as 
	 *  peças ja jogadas.
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				    RenderingHints.VALUE_ANTIALIAS_ON);
		
		double panelWidth = getWidth();
		double panelHeight = getHeight();
	
		g2.setColor(new Color(0.925f, 0.670f, 0.34f)); // light wood
		g2.fill(new Rectangle2D.Double(0, 0, panelWidth, panelHeight));
	
		
		double boardWidth = Math.min(panelWidth, panelHeight) - 2 * MARGIN;
		double squareWidth = boardWidth / size;
		double gridWidth = (size - 1) * squareWidth;
		double pieceDiameter = PIECE_FRAC * squareWidth;
		boardWidth -= pieceDiameter;
		double xLeft = (panelWidth - boardWidth) / 2 + MARGIN;
		double yTop = (panelHeight - boardWidth) / 2 + MARGIN;
	
		g2.setColor(Color.BLACK);
		for (int i = 0; i < size; i++) {
		    double offset = i * squareWidth;
		    g2.draw(new Line2D.Double(xLeft, yTop + offset, 
					      xLeft + gridWidth, yTop + offset));
		    g2.draw(new Line2D.Double(xLeft + offset, yTop,
					      xLeft + offset, yTop + gridWidth));
		}
		
		for (int y = 0; y < size; y++) {
		    for (int x = 0; x < size; x++) {
		    	int piece = state.getPiece(x, y);
				if (piece != Board.NO_VAL) {
				    Color c = (piece == Board.BLACK) ? Color.BLACK : Color.WHITE;
				    g2.setColor(c);
				    double xCenter = xLeft + x * squareWidth;
				    double yCenter = yTop + y * squareWidth;
				    Ellipse2D.Double circle
					= new Ellipse2D.Double(xCenter - pieceDiameter / 2,
							       yCenter - pieceDiameter / 2,
							       pieceDiameter,
							       pieceDiameter);
				    g2.fill(circle);
				    g2.setColor(Color.black);
				    g2.draw(circle);
				}
		    }
    	}
	}
	
	/**
	 * Função que executa um movimento da IA.
	 * 
	 * @param m			Movimento a ser realizado.
	 */
	public void iaPerformMove(Move m){
		state.playPiece(m);
		repaint();
		
		if(checkVictory())
			return;
		
		parent.setGameStatus("Vez do player " +(state.getCurrentPlayer()==Board.BLACK?"BLACK":"WHITE")+ ".");
		canPlayerInteract = true;
	}
	
	/**
	 * Caso o jogo seja singleplayer, acaba com a thread da IA.
	 */
	public void gameOver(){
		if(this.comp != null){
			this.comp.gameOver();
			this.comp = null;
		}
	}
	
	// ------- Getters and Setters ------- \\
	public GomokuState getState(){
		return this.state;
	}
}
