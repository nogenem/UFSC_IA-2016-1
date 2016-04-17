package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

enum Direction {
	UP, RIGHT, DOWN, LEFT
}

public class Board {
	
	public static final char NO_VAL = '.';
	public static final char TIE_VAL = 'T';
	
	public static final char BLACK = 'B';
	public static final char WHITE = 'W';
	
	private int size;
	private char board[][];
	private CalcScore cs;
	private Move lastMove;
	
	public Board(int size){
		this.size = size;
		this.board = new char[this.size][this.size];
		this.cs = new CalcScore();
		this.lastMove = new Move();
	}
	
	public int getSize(){
		return this.size;
	}
	
	public Move getLastMove(){
		return this.lastMove;
	}
	
	public void setLastMove(Move m){
		this.lastMove = m;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(int y = 0; y<size; y++)
			s += new String(board[y]) + "\n";
		return s;
	}
	
	// Spiral de Ulam
	public ArrayList<Move> getAllPossibleMoves(char player){
		ArrayList<Move> moves = new ArrayList<>();
		
		Direction dir = Direction.RIGHT;
		int n = size, n2 = n*n;
		int y = n / 2;
		int x = (n % 2 == 0) ? y - 1 : y; //shift left for even n's
		int j = 1;
		ArrayList<Point> v = new ArrayList<>();
		
		while(j <= n2){
			if(getValue(x, y) == Board.NO_VAL)
				moves.add(new Move(x, y, player));
			
			v.add(new Point(x,y));
			
			switch(dir){
			case RIGHT:
				if(x <= (n - 1) && !v.contains(new Point(x, y-1)) && j > 1) dir = Direction.UP; break;
			case UP:
				if(!v.contains(new Point(x-1, y))) dir = Direction.LEFT; break;
			case LEFT:
				if(x == 0 || !v.contains(new Point(x, y+1))) dir = Direction.DOWN; break;
			case DOWN:
				if(!v.contains(new Point(x+1, y))) dir = Direction.RIGHT; break;
			}

			switch(dir){
				case RIGHT:	x++; break;
				case UP: 	y--; break;
				case LEFT:	x--; break;
				case DOWN:	y++; break;
			}
			j++;
		}
		
		return moves;
	}
	
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
		this.board[y][x] = value;
		lastMove.setPos(x, y);
		lastMove.setPlayer(value);
	}
	
	public void setValue(Move m){
		this.board[m.getPos().y][m.getPos().x] = m.getPlayer();
		lastMove = m;
	}
	
	/**
	 * Reseta o tabuleiro limpando ele.
	 */
	public void reset(){
		cleanBoard();
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
	 * Retorna o estado atual do tabuleiro.
	 * 
	 * @return					<b>Board.BLACK</b> ou <b>Board.WHITE</b> caso houver um vencedor,</br>
	 * 							<b>Board.TIE_VAL</b> caso deu empate e</br>
	 * 							<b>Board.NO_VAL</b> caso contrario.
	 */
	public char checkBoardState(){
		int x = lastMove.getPos().x, 
			y = lastMove.getPos().y;
		
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
				.replace("\0", ""+lastMove.getPlayer());
		if(s.contains(strCheck))
			return lastMove.getPlayer();
		
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
	
	public int getBoardValue(char ia, char user){
		String possibilities = getAllPossibilities();
		int value = this.cs.getPossibilitiesSum(possibilities, ia, user);
		
		//System.out.println(possibilities +"\nLast: "+ lastMove.getPlayer() +" - Value: " +value);
		//System.out.println(toString() + "\nValue: " +value);
		return value;
	}
	
	private String getAllPossibilities(){
		String s = getAllRows() + getAllColumns() + 
				getAllLeftDiagonals() + getAllRightDiagonals();
		return s;
	}
	
	private String getAllRows(){
		String s = "";
		for(int y = 0; y<this.size; y++)
			s += new String(this.board[y]) + "\n";
		//s += "\n---------------------\n";
		return s;
	}
	
	private String getAllColumns(){
		String s = "";
		for(int x = 0; x<this.size; x++){
			for(int y = 0; y<this.size; y++){
				s += this.board[y][x];
			}
			s += "\n";
		}
		//s += "\n---------------------\n";
		return s;
	}
	
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
		//s += "\n---------------------\n";
		return s;
	}
	
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
		//s += "\n---------------------\n";
		return s;
	}
	
	public Board copy(){
		Board board = new Board(this.size);
		
		for(int y = 0; y<size; y++){
			for(int x = 0; x<size; x++){
				board.setValue(x, y, this.getValue(x, y));
			}
		}
		
		board.setLastMove(this.lastMove.copy());
		return board;
	}
}
