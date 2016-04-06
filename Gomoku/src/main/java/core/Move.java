package core;

import java.awt.Point;

public class Move {
	
	private Point pos;
	private char player;
	private int score;
	
	public Move(){
		this(0, 0, Board.NO_VAL, 0);
	}
	
	public Move(int x, int y) {
		this(x, y, Board.NO_VAL);
	}
	
	public Move(int x, int y, char player){
		this(x, y, player, 0);
	}
	
	public Move(int x, int y, char player, int score){
		this.pos = new Point(x, y);
		this.player = player;
		this.score = score;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		if (player != other.player)
			return false;
		if (pos == null && other.pos != null) 
			return false;
		else if (!pos.equals(other.pos))
			return false;
		if (score != other.score)
			return false;
		return true;
	}
	
	public boolean isEqualPos(Point pos){
		if(this.pos == null && pos != null)
			return false;
		else
			return this.pos.equals(pos);
	}
	
	public boolean isEqualPos(int x, int y){
		Point pos = new Point(x, y);
		if(this.pos == null && pos != null)
			return false;
		else
			return this.pos.equals(pos);
	}

	// ------- Getters and Setters ------- \\
	public Point getPos() {
		return pos;
	}

	public void setPos(int x, int y) {
		this.pos = new Point(x, y);
	}

	public char getPlayer() {
		return player;
	}

	public void setPlayer(char player) {
		this.player = player;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	
}
