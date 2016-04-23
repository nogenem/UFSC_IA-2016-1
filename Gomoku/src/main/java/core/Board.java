package core;

import java.util.ArrayList;
import java.util.Arrays;

enum Direction {
	UP, RIGHT, DOWN, LEFT
}

/**
 * Classe que representa o tabuleiro do jogo.
 * 
 * @author Gilney N. Mathias
 */
public class Board {
	
	public static final char NO_VAL = '.';
	public static final char TIE_VAL = 'T';
	
	public static final char BLACK = 'B';
	public static final char WHITE = 'W';
	
	private int size;
	private char board[][];
	private CalcScore cs;
	private ArrayList<Move> moveList;
	
	public Board(int size){
		this.size = size;
		this.board = new char[this.size][this.size];
		this.cs = new CalcScore();
		this.moveList = new ArrayList<>();
		
		cleanBoard();
	}
	
	/**
	 * O algoritmo passa por todos os movimentos ja feitos no tabuleiro,
	 *  do ultimo ao primeiro movimento, e adiciona a lista as casas
	 *  vazias em volta da posição de cada um destes movimentos. 
	 * 
	 * @param player			De quem é a vez.
	 * @return					Lista dos possíveis movimentos.
	 */
	public ArrayList<Move> getAllPossibleMoves(char player){
		ArrayList<Move> moves = new ArrayList<>();
		int dx[] = {1,  1,  0, -1, -1, -1, 0, 1};
		int dy[] = {0, -1, -1, -1,  0,  1, 1, 1};
		
		int y, x;
		Move move = null, tmp = null;
		
		for(int j = moveList.size()-1; j>=0; j--){
			tmp = moveList.get(j);
			for(int i = 0; i<8; i++){
				x = tmp.getPos().x + dx[i];
				y = tmp.getPos().y + dy[i];
				move = new Move(x, y, player);
				if(x >= 0 && y >= 0 && x < size && y < size && 
						getValue(x, y) == NO_VAL && !moves.contains(move)){
					moves.add(move);
				}
			}
		}
		
		/*y = size / 2;
		x = (size % 2 == 0) ? y - 1 : y; //shift left for even n's
		move = new Move(x,y,player);
		
		// Caso não tenha peça no meio, adiciona ele na lista
		if(getValue(x, y) == NO_VAL && !moves.contains(move))
			moves.add(move);*/
		
		return moves;
	}

	/**
	 * Retorna o estado atual do tabuleiro.
	 * 
	 * @return					<b>Board.BLACK</b> ou <b>Board.WHITE</b> caso houver um vencedor,</br>
	 * 							<b>Board.TIE_VAL</b> caso deu empate ou</br>
	 * 							<b>Board.NO_VAL</b> caso contrario.
	 */
	public char checkBoardState(){
		int x = getLastMove().getPos().x, 
			y = getLastMove().getPos().y;
		
		String s;
		int x1, y1, min, max, tmp, tmp2;
		
		// Check x
		s = new String(this.board[y]);
		
		// Check y
		s += "\n";
		min = Math.max(0, y-4);
		max = Math.min(size, y+5);
		for(y1 = min; y1<max; y1++){
			s += this.board[y1][x];
		}
		
		//Check Diagonal direita superior
		x1 = x+y;
		if(x1 <= this.size-1){
			s += "\n";
			min = Math.max(0, y-4);
			max = Math.min(x1, y+4);
			for(y1 = min; y1<=max; y1++){//0; x1
				int x2 = x1-y1;
				s += this.board[y1][x2];
			}
		}
		
		//Check Diagonal direita inferior
		tmp = this.size - 1;
		x1 = x-(tmp-y);
		if(x1 >= 1){
			s += "\n";
			min = Math.min(tmp, y+4);
			max = Math.max(x1, y-4);
			for(y1 = min; y1>=max; y1--){//tmp; x1
				int x2 = x1+(tmp-y1);
				s += this.board[y1][x2];
			}
		}
		
		//Check diagonal esquerda superior
		x1 = x-y;
		tmp2 = (tmp-x1);
		if(x1 >= 0 && x1 < size-4){
			s += "\n";
			min = Math.max(0, y-4);
			max = Math.min(tmp2, y+4);
			for(y1 = min; y1<=max; y1++){//0; (tmp-x1)
				int x2 = x1+y1;
				s += this.board[y1][x2];
			}
		}
		
		//Check diagonal esquerda inferior
		y1 = y-x;
		tmp2 = (tmp-y1);
		if(y1 >= 1 && y1 < size-4){
			s += "\n";
			min = Math.max(0, x-4);
			max = Math.min(tmp2, x+4);
			for(x1 = min; x1<=max; x1++){//0; (tmp-y1)
				int y2 = y1+x1;
				s += this.board[y2][x1];
			}
		}
		
		//System.out.println(s +"\n");
		String strCheck = new String(new char[5])
				.replace("\0", ""+getLastMove().getPlayer());
		if(s.contains(strCheck))
			return getLastMove().getPlayer();
		
		//Check TIE
		s = "";
		for(y1 = 0; y1<size; y1++){
			s += (new String(this.board[y1]));
		}
		
		if(!s.contains(""+NO_VAL))
			return TIE_VAL;
		
		// Se ninguem venceu e não deu empate...
		return NO_VAL;
	}
	
	/**
	 * Retorna a heurística do tabuleiro.</br>
	 * Caso o tabuleiro seja folha, a heurística equivale ao valor de utilidade.
	 * 
	 * @param ia			Valor que representa a IA neste jogo.
	 * @param user			Valore que representa o usuário neste jogo.
	 * @return				Valor da heurística do tabuleiro.
	 */
	public int getBoardValue(char ia, char user){
		String possibilities = getAllPossibilities();
		int value = this.cs.getPossibilitiesSum(possibilities, ia, user);
		return value;
	}
	
	/**
	 * Retorna uma representação em String de todas as 
	 *  possiveis linhas, colunas e diagonais do tabuleiro.
	 * 
	 * @return			Representação de todas as linhas, colunas e diagonais
	 * 					 do tabuleiro.
	 */
	private String getAllPossibilities(){
		String s = getAllRows() + getAllColumns() + 
				getAllLeftDiagonals() + getAllRightDiagonals();
		return s;
	}
	
	/**
	 * Retorna uma representação em String de todas
	 *  as linhas do tabuleiro.
	 * 
	 * @return			Representação de todas as linhas
	 * 					 do tabuleiro.
	 */
	private String getAllRows(){
		String s = "";
		for(int y = 0; y<this.size; y++)
			s += new String(this.board[y]) + "\n";
		return s;
	}
	
	/**
	 * Retorna uma representação em String de todas
	 *  as colunas do tabuleiro.
	 * 
	 * @return			Representação de todas as colunas
	 * 					 do tabuleiro.
	 */
	private String getAllColumns(){
		String s = "";
		for(int x = 0; x<this.size; x++){
			for(int y = 0; y<this.size; y++){
				s += this.board[y][x];
			}
			s += "\n";
		}
		return s;
	}
	
	/**
	 * Retorna uma representação em String de todas
	 *  as 'diagonais esquerdas' com no mínimo 5 casas.
	 * 
	 * @return			Representação de todas as 'diagonais esquerdas'
	 * 					 do tabuleiro.
	 */
	private String getAllLeftDiagonals(){
		String s = "";
		// diagonal inferior
		for(int y = size-5; y>=0; y--){
			for(int x = 0; x<size-y; x++){
				int y2 = y+x;
				s += this.board[y2][x];
			}
			s += "\n";
		}
		
		// diagonal superior
		for(int x = 1; x<size-4; x++){
			for(int y = 0; y<size-x; y++){
				int x2 = x+y;
				s += this.board[y][x2];
			}
			s += "\n";
		}
		return s;
	}
	
	/**
	 * Retorna uma representação em String de todas
	 *  as 'diagonais direitas' com no mínimo 5 casas.
	 * 
	 * @return			Representação de todas as 'diagonais direitas'
	 * 					 do tabuleiro.
	 */
	private String getAllRightDiagonals(){
		String s = "";
		// diagonal inferior
		for(int y = size-5; y>=0; y--){
			for(int x = this.size-1; x>=y; x--){
				int y2 = y+((this.size-1)-x);
				s += this.board[y2][x];
			}
			s += "\n";
		}
		
		// diagonal superior
		for(int x = this.size-2; x>=4; x--){
			for(int y = 0; y<=x; y++){
				int x2 = x-y;
				s += this.board[y][x2];
			}
			s += "\n";
		}
		return s;
	}

	/**
	 * Reseta o tabuleiro limpando ele.
	 */
	public void reset(){
		cleanBoard();
		moveList.clear();
	}
	
	/**
	 * Limpa o tabuleiro colocando todos os valores
	 *  como <b>Board.NO_VAL</b>.
	 */
	private void cleanBoard(){
		for(int i = 0; i<this.size; i++)
			Arrays.fill(this.board[i], Board.NO_VAL);
	}
	
	/**
	 * Cria uma cópia deste tabuleiro.
	 * 
	 * @return		Uma cópia deste tabuleiro.
	 */
	public Board copy(){
		Board board = new Board(this.size);
		
		//Atualiza o board e o moveList ao mesmo tempo
		for(Move m : this.moveList){
			board.setValue(m);
		}
		
		return board;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(int y = 0; y<size; y++)
			s += new String(board[y]) + "\n";
		return s;
	}
	
	// ------- Getters and Setters ------- \\
	/**
	 * Retorna o valor da posição especificada.
	 * 
	 * @param x			Valor x da posição que se quer o valor.
	 * @param y			Valor y da posição que se quer o valor.
	 * @return			Valor da posição (x, y).
	 */
	public char getValue(int x, int y){
		return this.board[y][x];
	}
	
	/**
	 * Atualiza o valor da posição especificada.
	 * 
	 * @param x			Valor x da posição que se quer atualizar o valor.
	 * @param y			Valor y da posição que se quer atualizar o valor.
	 * @param value		Valor que se quer por na posição (x, y).
	 */
	public void setValue(int x, int y, char value){
		this.setValue(new Move(x, y, value));
	}
	
	/**
	 * Atualiza o valor da posição contida no movimento passado.
	 * 
	 * @param m				Movimento executado.
	 */
	public void setValue(Move m){
		this.board[m.getPos().y][m.getPos().x] = m.getPlayer();
		if(m.getPlayer() == NO_VAL)//esta desfazendo o movimento na recursão do minimax
			moveList.remove(moveList.size()-1);
		else
			moveList.add(m);
	}
	
	public int getSize(){
		return this.size;
	}
	
	public Move getLastMove(){
		return this.moveList.get(moveList.size()-1);
	}
	
	public ArrayList<Move> getMoveList(){
		return this.moveList;
	}
}
