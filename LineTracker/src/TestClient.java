/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tiago
 */
public class TestClient {
  public static void main(String[] args) {
    final RegulatorClient client = new RegulatorClient("0.0.0.0", 4321);
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
        public void run() {
          System.out.println("Closing connection...");
          client.finish();
        }
    }));
    new Thread(client).start();
  }
}
