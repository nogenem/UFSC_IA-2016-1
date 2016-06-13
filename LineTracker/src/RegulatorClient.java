
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sourceforge.jFuzzyLogic.FIS;

/**
 *
 * @author tiago
 */
public class RegulatorClient implements Runnable {

	private volatile double p = 0.42187;
	private volatile double i = 0.09375;
	private volatile double d = 2.36718;

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;

	private final String host;
	private final int port;

	public RegulatorClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void run() {
		// ********************Versão original*********************************
		// listenSocket(this.host, this.port);
		// while (socket.isConnected()) {
		// try {
		// String line = in.readLine();
		// String[] params = line.split(" ");
		// double position = Double.parseDouble(params[0]);
		// double sumLinePositions = Double.parseDouble(params[1]);
		// double previousLinePosition = Double.parseDouble(params[2]);
		// double drive = p * position +
		// i * sumLinePositions + d * (position - previousLinePosition);
		// out.println(drive);
		// } catch (IOException ex) {
		// Logger.getLogger(RegulatorClient.class.getName()).log(Level.SEVERE,
		// null, ex);
		// break;
		// }
		// }
		// ***********************************************************************

		listenSocket(this.host, this.port);
		// Carrega o arquivo fcl
		FIS fis = FIS.load("fuzzy.fcl", true);
		double leftSensor, rightSensor, leftEngine, rightEngine;
		while (socket.isConnected()) {
			try {
				// Lê os valores dos sensores envios pelo server
				leftSensor = Double.parseDouble(in.readLine());
				rightSensor = Double.parseDouble(in.readLine());
				
				// Ajusta os valores max e min dos sensores
				if(leftSensor == Double.POSITIVE_INFINITY)
					leftSensor = 1;// O sensor da esquerda vai no max até 1
				else if(leftSensor == Double.NEGATIVE_INFINITY)
					leftSensor = -1;// O sensor da esquerda vai no min até -1
				
				if(rightSensor == Double.POSITIVE_INFINITY)
					rightSensor = 1.5;// O sensor da direita vai no max até 1.5
				else if(rightSensor == Double.NEGATIVE_INFINITY)
					rightSensor = -0.5;// O sensor da direita vai no min até -0.5
				
				System.out.println("leftSensor: " +leftSensor);
				System.out.println("rightSensor: " +rightSensor);
				
				// Passa o valor da diferença dos sensores para 
				//  a biblioteca fuzzy
				fis.setVariable("diff_sensor", (leftSensor-rightSensor));
				
				// Manda a biblioteca avaliar o novo valor passado
				fis.evaluate();
				
				// Pega os valores dos motores calculados pela biblioteca
				leftEngine = fis.getVariable("left_engine").getLatestDefuzzifiedValue();
				rightEngine = fis.getVariable("right_engine").getLatestDefuzzifiedValue();
	
				System.out.println("leftEngine: " +leftEngine);
				System.out.println("rightEngine: " +rightEngine);
				
				// Envia os valores dos motores para o server
				out.println(rightEngine);
				out.println(leftEngine);
			} catch (IOException ex) {
				Logger.getLogger(RegulatorClient.class.getName()).log(Level.SEVERE, null, ex);
				break;
			}
		}
	}

	public void listenSocket(String host, int port) {
		try {
			socket = new Socket(host, port);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("Connected on " + host + ":" + port);
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: " + host);
			System.exit(1);
		} catch (IOException e) {
			System.out.println("No I/O");
			System.exit(1);
		}
	}

	void finish() {
		try {
			if (socket != null)
				socket.close();
		} catch (IOException ex) {
			Logger.getLogger(RegulatorClient.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
