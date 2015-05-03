package gmu;

import org.junit.Assert;
import org.junit.Test;

public class TestRace {

  @Test
  public void testRaceFromString() {
    char[] c = new char[]{'Z', 'e', 'r', 'g'};
    String z = new String(c);
    Race.RaceType r = Race.fromName(z);
    System.out.println(r);
    System.out.println(Race.fromName("None"));
  }
}
