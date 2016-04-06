package core;

public class IA {
	
	private char iaPlayer;
	private char userPlayer;
	
	public IA() {
		// TODO Auto-generated constructor stub
	}
	
	public void setIaPlayer(char val){
		this.iaPlayer = val;
		this.userPlayer = val==Board.BLACK ? Board.WHITE : Board.BLACK;
	}
	
	// Fazer busca em spiral
	public Move getBestMove(Board board){
		Move move = new Move();
		
		return move;
	}

}
