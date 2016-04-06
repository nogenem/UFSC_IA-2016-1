package core;

import java.util.Arrays;

// Botar o lastMove no Board? Isso pode gerar problemas depois na recursão da IA...
public class Board {
	
	public static final char NO_VAL = '.';
	public static final char TIE_VAL = 'T';
	
	public static final char BLACK = 'B';
	public static final char WHITE = 'W';
	
	private int size;
	private char board[][];
	
	public Board(int size){
		this.size = size;
		this.board = new char[this.size][this.size];
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
	 * @param lastMove			Ultimo movimento realizado.
	 * @return					<b>Board.BLACK</b> ou <b>Board.WHITE</b> caso houver um vencedor,</br>
	 * 							<b>Board.TIE_VAL</b> caso deu empate e</br>
	 * 							<b>Board.NO_VAL</b> caso contrario.
	 */
	public char checkBoardState(Move lastMove){
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
		if(x1 <= 14){
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
		
		System.out.println(s +"\n");
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
}
