import java.io.IOException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class PrimeNumberRunnable implements Runnable{

  private static final String URL = "http://localhost:8080/cs6650_primenumber_war_exploded/prime/";
  private static final int LOWER_BOUND = 1;
  private static final int UPPER_BOUND = 9999;
  private int numRequest;
  private final ActiveCount primeCount;
  private HttpClient client;

  public PrimeNumberRunnable(int numRequest, ActiveCount primeCount) {
    this.numRequest = numRequest;
    this.primeCount = primeCount;
    this.client = new HttpClient();  // HttpClient from apache
  }

  public void run() {
    // HttpClient from apache
    int localPrimeCount = 0;
    for (int i = 0; i < numRequest; i++) {
      // Send the request number to the server for evaluation
      int val = this.generateRandomOddNumber();
      System.out.println(URL+val);
      GetMethod method = new GetMethod(URL + val);
      method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
          new DefaultHttpMethodRetryHandler(0, false));

      try {
        int statusCode = client.executeMethod(method);
        System.out.println(statusCode);
        if (statusCode == HttpStatus.SC_OK) {
          localPrimeCount++;
        }

      } catch (HttpException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        method.releaseConnection();
      }
    }

    // share local prime/non-prime to global
    primeCount.incrementBy(localPrimeCount);
  }

  private int generateRandomOddNumber() {
    int val = ThreadLocalRandom.current().nextInt(LOWER_BOUND, UPPER_BOUND) | 1;
    return val;
  }
}
