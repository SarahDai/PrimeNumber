import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PrimeNumberClient{
  private static final int TOTAL_REQUESTS = 100000;
  private static final int NUM_THREADS = 500;

  public static void main(String[] args) throws InterruptedException {
    int requestPerThread = TOTAL_REQUESTS / NUM_THREADS;
    final ActiveCount primeCount = new ActiveCount();
    final ActiveCount nonPrimeCount = new ActiveCount();

    // Create the thread pool
    ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
    long startTimeStamp = System.currentTimeMillis();

    // Start request
    for (int i = 0; i < NUM_THREADS; i++) {
      Runnable thread = new PrimeNumberRunnable(requestPerThread, primeCount, nonPrimeCount);
      pool.execute(thread);
    }

    // Shut down
    pool.shutdown();
    try {
      // Wait a while for existing tasks to terminate
      if (!pool.awaitTermination(60, TimeUnit.MINUTES)) {
        pool.shutdownNow(); // Cancel currently executing tasks
        // Wait a while for tasks to respond to being cancelled
        if (!pool.awaitTermination(60, TimeUnit.MINUTES))
          System.err.println("Pool did not terminate");
      }
    } catch (InterruptedException ie) {
      // (Re-)Cancel if current thread also interrupted
      pool.shutdownNow();
      // Preserve interrupt status
      Thread.currentThread().interrupt();
      ie.printStackTrace();
    }

    // Evaluation
    long endTimeStamp = System.currentTimeMillis();
    long wallTime = endTimeStamp - startTimeStamp;
    long throughPut = TOTAL_REQUESTS * 1000 / wallTime;

    System.out.println("-----Prime Number------");
    System.out.println("Total requests: " + TOTAL_REQUESTS);
    System.out.println("Total wall time (secs) : " + wallTime / 1000.0);
    System.out.println("Throughput (requests per second): " + throughPut);
    System.out.println("Total number of primes: " + primeCount.getCount());
    System.out.println("Total number of non-primes: " + nonPrimeCount.getCount());
  }

}
