package core;

public class GomokuState {
	
	private Board board;
	private char currentPlayer;
	private IA ia;
	private Move lastMove;
	
	public GomokuState(int size) {
		board = new Board(size);
		ia = new IA();
		lastMove = new Move();
	}
	
	public void init(boolean iaBegins){
		// Peça preta sempre começa
		currentPlayer = Board.BLACK;
		
		this.board.reset();
		
		this.ia.setIaPlayer(iaBegins ? Board.BLACK : Board.WHITE);
		if(iaBegins){
			Move m = this.getIaMove();
			this.playPiece(m);
		}
	}
	
	public Move getIaMove(){
		return ia.getBestMove(this.board);
	}
	
	public char checkVitory(){
		char gameState = this.board.checkBoardState(this.lastMove);
		return gameState;
	}
	
	public char getPiece(int x, int y){
		return this.board.getValue(x, y);
	}
	
	public void playPiece(Move m){
		this.playPiece(m.getPos().x, m.getPos().y);
	}
	
	public void playPiece(int x, int y){
		// Atualização posição
		this.board.setValue(x, y, this.currentPlayer);
		
		// Atualização ultima jogada
		this.lastMove.setPlayer(currentPlayer);
		this.lastMove.setPos(x, y);
		
		// Atualização proximo jogador
		this.currentPlayer = this.currentPlayer == Board.BLACK ? 
				Board.WHITE : Board.BLACK;
	}

}
