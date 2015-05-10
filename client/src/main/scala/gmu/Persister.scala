package gmu

import java.util.concurrent._

import com.mongodb.{BasicDBObject, WriteConcern, MongoClientOptions, MongoClient}
import org.slf4j.LoggerFactory

class Persister(val dbName: String) {
  val mongo = new MongoClient("192.168.1.250", MongoClientOptions.builder()
    .connectionsPerHost(32)
    .writeConcern(WriteConcern.UNACKNOWLEDGED)
    .build())

  val saves = new LinkedBlockingQueue[ToSave](1024 * 2)
  val pool = Executors.newFixedThreadPool(12)
  for(x <- Range(1, 12)) {
    pool.execute(new Mover(this, mongo, dbName))
  }
}

class Mover(val p: Persister, val mongo: MongoClient, val dbName: String) extends Runnable with ReplayPickles with ReplayConversions {
  val log = LoggerFactory.getLogger(classOf[Mover])
  val db = mongo.getDB(dbName)
  val units = db.getCollection("units")
  val players = db.getCollection("players")

  override def run(): Unit = {
    try {
      while (true) {
        val toSave = p.saves.take()
        toSave.players match {
          case Some(player) =>
            players.insert(new BasicDBObject("id", getKey(player)).append("players", pickle(player)))
            log.info("Saves size {}", p.saves.size())
          case None =>
        }
        toSave.unit match {
          case Some(unit) =>
            units.insert(new BasicDBObject("id", getKey(unit)).append("units", pickle(unit)))
          case None =>
        }
      }
    } catch {
      case e :InterruptedException => log.info("Stopping")
    }
  }
}

case class ToSave(unit: Option[Seq[ReplayUnit]], players: Option[ReplayPlayers])