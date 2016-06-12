import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegulatorServer implements Runnable {

  private ServerSocket server;
  private Socket client;
  private BufferedReader in;
  private PrintWriter out;

  private volatile double speed;
  private volatile long delay;
//  private volatile double previousLinePosition;
//  private volatile double sumLinePositions;
  private MotorController motorController;
  private Sensor sensor;
  private Thread timer;
  private boolean has_client = false;
  
  private static double accummulator = 0.0;
  private static double mae = 0.0;
  private static long counter = 0;

  /**
   * Initializes the regulator.
   *
   * @param motorCtrl a MotorController that will simulate robot movement
   * @param sensor sensor that will provide line position
   */
  public RegulatorServer(MotorController motorCtrl, Sensor sensor) {
    this.motorController = motorCtrl;
    this.sensor = sensor;

    timer = new Thread(this);
  }

  /**
   * Runs a thread that periodically obtain line position from a sensor and
   * updates motors' speed.
   */
  public void StartRegulation() {
    timer.start();
  }

  /**
   * Set the refresh frequency for PID regulation, e.g. how often the speed of
   * motors is updated.
   *
   * @param freq refresh frequency in Hz
   */
  public void SetFrequency(int freq) {
    delay = 1000 / freq;
  }

  /**
   * Thread that periodicaly updates motors' speed acording to the line position
   * contains the regulator loop itself
   */
  public void run() {
    // Remember the starting time
    long tm = System.currentTimeMillis();

    listenSocket();

    while (Thread.currentThread() == timer) {
// ***************************Versão original********************************      
//      double position = sensor.GetLinePosition();
//
//      if (Math.abs(position) > 1) { // if robot is off line, start turning
//        if (position < 0) {
//          motorController.setSpeed(0, speed);
//        } else {
//          motorController.setSpeed(speed, 0);
//        }
//        sumLinePositions = 0;
//      } else {
//
//        
//        out.println(position + " " + sumLinePositions + " " + previousLinePosition);
//        double drive = -1;
//        try {
//          drive = Double.parseDouble(in.readLine());
//        } catch (Exception ex) {
//          has_client = false;
//          break;
//        }
//        drive = drive * speed;
//
//        double l, r;
//
//        if (drive < 0) {
//          l = Math.min(-drive, speed);
//          r = speed;
//        } else {
//          l = speed;
//          r = Math.min(drive, speed);
//        }
//        motorController.setSpeed(l, r);
//
//        previousLinePosition = position;
//        sumLinePositions += position;
//      }
      
// ***********************************************************************      

      double positionl = sensor.GetLinePosition();
      double positionr = 0.5 - positionl;
      
      System.out.println("Left Sensor "
              + positionl);
      System.out.println("Rigth Sensor "
              + positionr);
      
      out.println(positionl);
      out.println(positionr);
      
      if (Math.abs(positionl)==Double.POSITIVE_INFINITY) {
        accummulator+=1000;
      } else {
          accummulator += Math.abs(positionl - positionr);
      }
      counter += 1;
      mae = accummulator / counter;
      System.out.println("Mean Absolute Error "
              + mae);
      double l, r;
try {
       r = Double.parseDouble(in.readLine());
       l = Double.parseDouble(in.readLine()) ;
       
       

     } catch (Exception ex) {
       has_client = false;
       break;
     }
     motorController.setSpeed(l, r);
      
      
      try {
        tm += delay;
        Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
      } catch (InterruptedException e) {
        break;
      }
    }
    try {
      System.out.println("Finishing simulation...");
      server.close();
    } catch (IOException ex) {
      Logger.getLogger(RegulatorServer.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Sets the maximum speed of the robot.
   *
   * @param speed positive number
   */
  public void setSpeed(double speed) {
    this.speed = speed;
  }

  public void listenSocket() {
    try {
      server = new ServerSocket(4321, 1, new InetSocketAddress(0).getAddress());
      System.out.println("Server is listening on "
              + server.getInetAddress().getHostName() + ":" + 4321);
    } catch (IOException e) {
      System.out.println("Could not listen on port 4321");
      System.exit(-1);
    }
    try {
      client = server.accept();
      System.out.println("Client connected on port " + 4321);
      has_client = true;
    } catch (IOException e) {
      System.out.println("Accept failed: 4321");
      System.exit(-1);
    }

    try {
      in = new BufferedReader(new InputStreamReader(client.getInputStream()));
      out = new PrintWriter(client.getOutputStream(), true);
    } catch (IOException e) {
      System.out.println("Read failed");
      System.exit(-1);
    }
  }

  boolean hasClient() {
    return has_client;
  }
}
