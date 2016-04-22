package core;

import java.util.ArrayList;

/**
 * Classe responsável pelas operações da IA do jogo.
 * 
 * @author Gilney N. Mathias
 */
public class IA {
	
	private char iaPlayer;
	private char userPlayer;
	
	//Conta a quantidade de interações do algortimo alphabeta
	private int count = 0;
	//Na real desce maxDepth+1, ja que vai até 0
	private final int maxDepth = 2;
	
	public IA() {}
	
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
		Move bestMove = null;
		int bestScore = Integer.MIN_VALUE;
		board = board.copy();//usa uma cópia para não dar conflito com o repaint
		
		count = 0;
		long inicio = System.currentTimeMillis();
		
		ArrayList<Move> moves = board.getAllPossibleMoves(iaPlayer);
		for(Move m: moves){
			board.setValue(m);
			int alpha = alphaBeta(board, userPlayer, bestScore, Integer.MAX_VALUE, maxDepth);
			board.setValue(m.getPos().x, m.getPos().y, Board.NO_VAL);
			if(alpha > bestScore || bestMove == null){
				bestMove = m;
				bestMove.setScore(alpha);
				bestScore = alpha;
			}
		}
		
		long fim = System.currentTimeMillis();
		System.out.println("Tempo: " +((fim-inicio)/1000) +"s - Interações: " +count);
		System.out.println(bestMove +"\n---------------------------------");
		return bestMove;
	}
	
	/**
	 * Algoritmo de Minimax com poda alpha e beta.
	 * 
	 * @param board				Tabuleiro atual do jogo.
	 * @param player			Player atual.
	 * @param alpha				Valor alpha até agora.
	 * @param beta				Valor beta até agora.
	 * @param depth				Profundidade da recursão.
	 * @return					Melhor alpha ou beta encontrado.
	 */
	private int alphaBeta(Board board, char player, int alpha, int beta, int depth){	
		count++;
		if(depth == 0 || isLeaf(board)){
			int value = board.getBoardValue(iaPlayer, userPlayer); 
				value *= (depth+1);
			return value;
		}
		
		// get all moves
		ArrayList<Move> moves = board.getAllPossibleMoves(player);
		
		if(player == iaPlayer){
			int cAlpha = Integer.MIN_VALUE;
			for(Move m : moves){
				board.setValue(m);
				cAlpha = Math.max(cAlpha, alphaBeta(board, userPlayer, alpha, beta, depth-1));
				board.setValue(m.getPos().x, m.getPos().y, Board.NO_VAL);
				alpha = Math.max(alpha, cAlpha);
				if(alpha >= beta)
					return alpha;
			}
			return cAlpha;
		}else{
			int cBeta = Integer.MAX_VALUE;
			for(Move m : moves){
				board.setValue(m);
				cBeta = Math.min(cBeta, alphaBeta(board, iaPlayer, alpha, beta, depth-1));
				board.setValue(m.getPos().x, m.getPos().y, Board.NO_VAL);
				beta = Math.min(beta, cBeta);
				if(alpha >= beta)
					return beta;
			}
			return cBeta;
		}
	}
	
	/**
	 * Método que verifica se um tabuleiro é folha ou não.
	 *  
	 * @param board				Board, node, a verificação.
	 * @param lastMove			Ultimo movimento realizado no tabuleiro.
	 * @return					<b>TRUE</b> caso o tabuleiro seja folha,</br>
	 * 							<b>FALSE</b> caso contrario.
	 */
	private boolean isLeaf(Board board){
		return board.checkBoardState() != Board.NO_VAL;
	}
}
