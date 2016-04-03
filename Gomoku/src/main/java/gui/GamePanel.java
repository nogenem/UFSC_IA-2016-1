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
import core.GomokuState;

/**
 *	Arquivo base:
 *  	http://cs.gettysburg.edu/~tneller/cs111/gomoku/gui/Gomoku.java
 *  
 * @author Todd W. Neller [base]
 * @author Gilney Nathanael Mathias [edições]
 */
public class GamePanel extends JPanel {
	
	private final int MARGIN = 5;
    private final double PIECE_FRAC = 0.9;
    private final int size = 15;
    
    private GomokuState state;
    private Gomoku parent;

    private boolean isMultiplayer;
    private boolean iaBegins;
    private boolean canPlayerInteract;
    
	public GamePanel(Gomoku parent) {
		super();
		this.parent = parent;
		this.state = new GomokuState(this.size);
		this.isMultiplayer = false;
		this.iaBegins = false;
		this.canPlayerInteract = false;
		addMouseListener(new GomokuListener());
	}
	
	public void init(boolean isMultiplayer, boolean iaBegins){
    	this.isMultiplayer = isMultiplayer;
		this.iaBegins = iaBegins;
		
		boolean tmp = (!isMultiplayer && iaBegins);
		this.canPlayerInteract = !tmp;

		state.init(tmp);
		if(tmp)
			this.canPlayerInteract = true;
    }
	
	class GomokuListener extends MouseAdapter {
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
		    	if(checkVictory(x, y))
		    		return;
		    	
		    	if(!isMultiplayer){
		    		canPlayerInteract = false;
		    		
		    		int move[] = state.iaMove();
		    		state.playPiece(move[0], move[1]);
		    		repaint();
		    		
		    		if(checkVictory(move[0], move[1]))
		    			return;
		    		canPlayerInteract = true;
		    	}
		    }
		}    
    }
	
	public boolean checkVictory(int x, int y){
		char vic = state.checkVitory(x, y);
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
    		parent.gameOver();
    		return true;
    	}
    	return false;
	}
	
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

}
