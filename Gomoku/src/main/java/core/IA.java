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
	
	public int[] performMove(Board board){
		int move[] = {0, 0};
		
		return move;
	}

}
