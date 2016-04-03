package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Gomoku extends JFrame {

	public Gomoku() {
		setTitle("Gomoku");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(0,0));
		
		setVisible(true);
	}

	public static void main(String[] args) {
		new Gomoku();
	}

}
