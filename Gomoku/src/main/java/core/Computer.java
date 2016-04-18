package core;

import gui.GamePanel;

public class Computer implements Runnable {
	
	private GamePanel panel;
	private IA ia;
	
	private boolean running;
	private boolean endGame;
	private Thread t;
	
	public Computer(GamePanel p, boolean iaBegins) {
		this.panel = p;
		this.ia = new IA();
		this.running = iaBegins;
		this.endGame = false;
		
		this.ia.setIaPlayer(iaBegins ? Board.BLACK : Board.WHITE);
		
		this.t = new Thread(this);
		this.t.start();
	}
	
	public void run(){
		while(!endGame){
			while(!running){ Thread.yield(); }
			play();
		}
	}
	
	private void play(){
		Move m = ia.getBestMove( panel.getState().getBoard() );
		if(!this.endGame){
			System.out.println(m);
			panel.iaPerformMove(m);
			running = false;
		}
	}
	
	public void gameOver(){
		this.endGame = true;
	}
	
	public void resume(){
		this.running = true;
	}
	
	public void pause(){
		this.running = false;
	}

}
