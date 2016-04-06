package core;

public class IA {
	
	private char iaPlayer;
	private char userPlayer;
	
	public IA() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Atualiza qual é o valor do IA e do USER no tabuleiro.
	 * 
	 * @param val			Valor da IA, <b>Board.BLACK</b> ou <b>Board.WHITE</b>.
	 */
	public void setIaPlayer(char val){
		this.iaPlayer = val;
		this.userPlayer = val==Board.BLACK ? Board.WHITE : Board.BLACK;
	}
	
	/**
	 * Retorna o melhor movimento possível que a IA pode fazer
	 *  no tabuleiro passado como parâmetro.
	 * 
	 * @param board				Tabuleiro atual do jogo
	 * @return					Melhor movimento possivel dado o tabuleiro passado.
	 */
	public Move getBestMove(Board board){
		// Fazer busca em spiral
		Move move = new Move();
		
		return move;
	}

}
