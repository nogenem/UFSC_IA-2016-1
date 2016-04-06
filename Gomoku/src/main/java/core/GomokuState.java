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
	
	/**
	 * Inicia um novo jogo setando o player atual e resetando o tabuleiro.</br>
	 * Caso a IA comece jogando, também realiza o seu movimento.
	 * 
	 * @param iaBegins			A IA começa jogando?
	 */
	public void init(boolean iaBegins){
		// Peça preta sempre começa
		currentPlayer = Board.BLACK;
		
		this.board.reset();
		
		this.ia.setIaPlayer(iaBegins ? Board.BLACK : Board.WHITE);
		if(iaBegins)
			this.iaPerformMove();
	}
	
	/**
	 * Utiliza o tabuleiro atual do jogo para realizar 
	 *  o melhor movimento possível para a IA. 
	 */
	public void iaPerformMove(){
		Move m = ia.getBestMove(this.board);
		this.playPiece(m);
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
		char gameState = this.board.checkBoardState(this.lastMove);
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
		
		// Atualização ultima jogada
		this.lastMove.setPlayer(currentPlayer);
		this.lastMove.setPos(x, y);
		
		// Atualização proximo jogador
		this.currentPlayer = this.currentPlayer == Board.BLACK ? 
				Board.WHITE : Board.BLACK;
	}

}
