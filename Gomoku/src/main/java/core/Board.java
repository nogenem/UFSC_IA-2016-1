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
	
	public char getWinner(){
		return NO_VAL;
	}
}
