package core;

import java.util.Arrays;

public class Board {
	
	public static final char NO_VAL = '0';
	public static final char TIE_VAL = '1';
	
	public static final char BLACK = '2';
	public static final char WHITE = '3';
	
	private int size;
	private char board[][];
	
	public Board(int size){
		this.size = size;
	}
	
	public char getValue(int x, int y){
		return this.board[y][x];
	}
	
	public void setValue(int x, int y, char value){
		this.board[y][x] = value;
	}
	
	public void reset(){
		this.board = new char[this.size][this.size];
		cleanBoard();
	}
	
	private void cleanBoard(){
		for(int i = 0; i<this.size; i++)
			Arrays.fill(this.board[i], Board.NO_VAL);
	}
	
	/**
	 * 
	 * 
	 * @param x					Posição x do ultimo movimento
	 * @param y					Posição y do ultimo movimento
	 * @param currentPlayer		Player que efetuou o ultimo movimento
	 * @return
	 */
	public char checkGameState(int x, int y, char currentPlayer){
		String s;
		int x1, y1;
		
		// Check x
		s = new String(this.board[y]);
		
		// Check y
		s += "\n";
		for(y1 = 0; y1<this.size; y1++){
			s += this.board[y1][x];
		}
		
		//Check Diagonal direita 1
		x1 = x+y;
		if(x >= 4 && x1 <= 14){
			s += "\n";
			for(y1 = 0; y1<=x1; y1++){
				int x2 = x1-y1;
				s += this.board[y1][x2];
			}
		}
		
		//Check Diagonal direita 2
		int tmp = this.size - 1;
		x1 = x-(tmp-y);
		if(y < size-4 && x1 >= 1){
			s += "\n";
			for(y1 = tmp; y1>=x1; y1--){
				int x2 = x1+(tmp-y1);
				s += this.board[y1][x2];
			}
		}
		
		//Check diagonal esquerda 1
		x1 = x-y;
		if(x1 >= 0 && x1 < size-4){
			s += "\n";
			for(y1 = 0; y1<=(tmp-x1); y1++){
				int x2 = x1+y1;
				s += this.board[y1][x2];
			}
		}
		
		//Check diagonal esquerda 2
		y1 = y-x;
		if(y1 >= 1 && y1 < size-4){
			s += "\n";
			for(x1 = 0; x1<=(tmp-y1); x1++){
				int y2 = y1+x1;
				s += this.board[y2][x1];
			}
		}
		
		System.out.println(s +"\n");
		String strCheck = new String(new char[5]).replace("\0", ""+currentPlayer);
		if(s.contains(strCheck))
			return currentPlayer;
		
		//Check TIE
		s = "";
		for(y1 = 0; y1<size; y1++){
			s += (new String(this.board[y1]));
		}
		
		if(!s.contains(""+NO_VAL))
			return TIE_VAL;
		
		// Se ninguem venceu, e não deu empate...
		return NO_VAL;
	}
}
