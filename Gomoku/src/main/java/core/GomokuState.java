package core;

import javax.swing.JOptionPane;

public class GomokuState {
	
	private Board board;
	private char currentPlayer;
	private IA ia;
	
	public GomokuState(int size) {
		board = new Board(size);
		ia = new IA();
	}
	
	public void init(boolean iaBegins){
		// Peça preta sempre começa
		currentPlayer = Board.BLACK;
		
		this.board.reset();
		
		this.ia.setIaPlayer(iaBegins ? Board.BLACK : Board.WHITE);
		if(iaBegins){
			ia.performMove(this.board);
		}
	}
	
	public char checkVitory(int x, int y){
		char gameState = this.board.checkGameState(x, y, currentPlayer);
		this.currentPlayer = this.currentPlayer == Board.BLACK ? 
					Board.WHITE : Board.BLACK;
		return gameState;
	}
	
	public char getPiece(int x, int y){
		return this.board.getValue(x, y);
	}
	
	public void playPiece(int x, int y){
		this.board.setValue(x, y, this.currentPlayer);
	}

}
