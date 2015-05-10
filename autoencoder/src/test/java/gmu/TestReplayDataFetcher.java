package gmu;

import com.mongodb.*;
import org.junit.Test;

public class TestReplayDataFetcher {

  @Test
  public void testFetch() throws Exception {
    MongoClient mongo = new MongoClient("localhost", MongoClientOptions.builder()
            .connectionsPerHost(16)
            .build());

    DB db = mongo.getDB("json");
    DBCollection unitsCol = db.getCollection("units");
    DBCollection playersCol = db.getCollection("players");

    DBObject rawPlayer = playersCol.find(new BasicDBObject("id.replay", 2))
            .sort(new BasicDBObject("id.frame", 1)).next();

    ReplayDataFetcher fetcher = new ReplayDataFetcher();

    fetcher.fetch(1);
  }
}
