package core;

/**
 * Classe que guarda o estado do jogo, o tabuleiro e quem é o
 *  jogador atual.
 * 
 * @author Gilney N. Mathias
 */
public class GomokuState {
	
	private Board board;
	private char currentPlayer;
	
	public GomokuState(int size) {
		board = new Board(size);
	}
	
	/**
	 * Inicia um novo jogo setando o player atual e resetando o tabuleiro.</br>
	 * Caso a IA comece jogando, também realiza o seu movimento.
	 * 
	 * @param iaBegins			A IA começa jogando?
	 */
	public void init(){
		// Peça preta sempre começa
		currentPlayer = Board.BLACK;
		
		this.board.reset();
	}
	
	/**
	 * Utiliza o ultimo movimento feito para checar 
	 *  se houve um vencedor ou se o jogo empatou.
	 *  
	 * @return			<b>Board.BLACK</b> ou <b>Board.WHITE</b> caso houver um vencedor,</br>
	 * 					<b>Board.TIE_VAL</b> caso deu empate e</br>
	 * 					<b>Board.NO_VAL</b> caso contrario.
	 */
	public char checkVitory(){
		char gameState = this.board.checkBoardState();
		return gameState;
	}
	
	/**
	 * Retorna qual peça esta localizada na posição passada como parâmetro.
	 * 
	 * @param x			Valor x da posição.
	 * @param y			Valor y da posição.
	 * @return			Peça localizada na posição.
	 */
	public char getPiece(int x, int y){
		return this.board.getValue(x, y);
	}
	
	/**
	 * Realiza uma jogada adicionando a peça do jogador atual
	 *  na posição especificada, atualizando o valor da 
	 *  ultima jogada e atualizando o próximo jogador.
	 * 
	 * @param m			Movimento a ser realizado.
	 */
	public void playPiece(Move m){
		this.playPiece(m.getPos().x, m.getPos().y);
	}
	
	/**
	 * Realiza uma jogada adicionando a peça do jogador atual
	 *  na posição especificada, atualizando o valor da 
	 *  ultima jogada e atualizando o jogador atual.
	 * 
	 * @param x			Valor x da posição a ser jogada.
	 * @param y			Valor y da posição a ser jogada.
	 */
	public void playPiece(int x, int y){
		// Atualização posição
		this.board.setValue(x, y, this.currentPlayer);
		
		// Atualização proximo jogador
		this.currentPlayer = this.currentPlayer == Board.BLACK ? 
				Board.WHITE : Board.BLACK;
	}
	
	// ------- Getters and Setters ------- \\
	public Board getBoard(){
		return this.board;
	}
	
	public char getCurrentPlayer(){
		return this.currentPlayer;
	}

}
