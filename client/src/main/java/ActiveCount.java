/**
 * Thread safe counter.
 */
public class ActiveCount {
  private int activeThreadCount = 0;

  /**
   * Increment count.
   */
  public synchronized void incrementCount() {
    this.activeThreadCount++;
  }

  public synchronized  void incrementBy(int count) {
    this.activeThreadCount += count;
  }

  /**
   * Decrement count.
   */
  public synchronized void decrementCount() {
    this.activeThreadCount--;
  }

  /**
   * Gets count.
   *
   * @return the count
   */
  public synchronized int getCount() {
    return activeThreadCount;
  }
}
