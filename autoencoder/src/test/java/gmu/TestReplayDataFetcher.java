package gmu;

import org.junit.Test;

public class TestReplayDataFetcher {

  @Test
  public void testFetch() {
    ReplayDataFetcher fetcher = new ReplayDataFetcher();

    fetcher.fetch(1);
  }
}
