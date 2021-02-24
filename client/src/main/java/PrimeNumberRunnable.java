import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.ThreadLocalRandom;


public class PrimeNumberRunnable implements Runnable{

  private static final String URL = "http://52.13.12.164:8080/prime/";
  private static final int LOWER_BOUND = 1;
  private static final int UPPER_BOUND = 9999;
  private int numRequest;
  private final ActiveCount primeCount;
  private final ActiveCount nonPrimeCount;
  private HttpClient client;

  public PrimeNumberRunnable(int numRequest, ActiveCount primeCount, ActiveCount nonPrimeCount) {
    this.numRequest = numRequest;
    this.primeCount = primeCount;
    this.nonPrimeCount = nonPrimeCount;
    this.client = HttpClient.newHttpClient();
  }

  public void run() {
    int localPrimeCount = 0, localNonPrimeCount = 0;
    try {
      for (int i = 0; i < numRequest; i++) {
        // Send the request number to the server for evaluation
        int val = this.generateRandomOddNumber();
        HttpRequest request = HttpRequest.newBuilder(URI.create(URL + val)).GET().build();
        HttpResponse response = client.send(request, BodyHandlers.ofString());

        int statusCode = response.statusCode();
        if (statusCode == 200) {
          localPrimeCount++;
        } else if (statusCode == 401) {
          localNonPrimeCount++;
        }
      }
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // share local prime/non-prime to global
    primeCount.incrementBy(localPrimeCount);
    nonPrimeCount.incrementBy(localNonPrimeCount);

  }

  private int generateRandomOddNumber() {
    int val = ThreadLocalRandom.current().nextInt(LOWER_BOUND, UPPER_BOUND) | 1;
    return val;
  }
}
