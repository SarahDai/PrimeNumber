import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;


public class PrimeNumberRunnable implements Runnable{
  public static final int SERVER_PORT = 8080;
  private static final int LOWER_BOUND = 1;
  private static final int UPPER_BOUND = 9999;
  private int numRequest;
  private final ActiveCount primeCount;
  private final ActiveCount nonPrimeCount;

  public PrimeNumberRunnable(int numRequest, ActiveCount primeCount, ActiveCount nonPrimeCount) {
    this.numRequest = numRequest;
    this.primeCount = primeCount;
    this.nonPrimeCount = nonPrimeCount;
  }

  public void run() {
    int localPrimeCount = 0, localNonPrimeCount = 0;
    try {
      InetAddress local = InetAddress.getLocalHost();
      System.out.println("LocalHost: " + local);

      Socket clientSocket = new Socket(local, SERVER_PORT);
      InputStream in = clientSocket.getInputStream();
      OutputStream out = clientSocket.getOutputStream();
      System.out.println("Connected to server " + clientSocket.getInetAddress()
          + " port " + clientSocket.getPort());

      for (int i = 0; i < numRequest; i++) {
        // Send the request expressions to the server for evaluation
        int evalNumber = this.generateRandomOddNumber();
        out.write(evalNumber);
      }



      clientSocket.close();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  private int generateRandomOddNumber() {
    Random random = new Random();
    int val = LOWER_BOUND + random.nextInt(UPPER_BOUND - LOWER_BOUND);
    return val%2 == 0 ? val + 1 : val;
  }
}
