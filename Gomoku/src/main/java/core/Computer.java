package core;

import gui.GamePanel;

/**
 * Classe que executa as jogadas da IA.
 * 
 * @author Gilney N. Mathias
 */
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
	
	/**
	 * Executa um movimento da IA.
	 */
	private void play(){
		Move m = ia.getBestMove( panel.getState().getBoard() );
		if(!this.endGame){
			panel.iaPerformMove(m);
			running = false;
		}
	}
	
	/**
	 * Termina esta thread.
	 */
	public void gameOver(){
		this.endGame = true;
	}
	
	/**
	 * Ativa esta thread.
	 */
	public void resume(){
		this.running = true;
	}
	
	/**
	 * Pausa esta thread.
	 */
	public void pause(){
		this.running = false;
	}

}
