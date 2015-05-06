package gmu

import java.util.Optional
import java.util.concurrent._
import scala.pickling._
import scala.pickling.binary._
import scala.pickling.static._
import scala.pickling.Defaults._

import com.mongodb.{BasicDBObject, WriteConcern, MongoClientOptions, MongoClient}
import org.slf4j.LoggerFactory

class Persister {
  val mongo = new MongoClient("192.168.1.250", MongoClientOptions.builder()
    .connectionsPerHost(32)
    .writeConcern(WriteConcern.UNACKNOWLEDGED)
    .build())

  val saves = new LinkedBlockingQueue[ToSave](1024 * 75)
  val pool = Executors.newFixedThreadPool(6)
  for(x <- Range(1, 6)) {
    pool.execute(new Mover(this, mongo))
  }
}

class Mover(val p: Persister, val mongo: MongoClient) extends Runnable with ReplayPickles with ReplayConversions {
  val log = LoggerFactory.getLogger(classOf[Mover])
  val db = mongo.getDB("sc")
  val units = db.getCollection("units")
  val players = db.getCollection("players")

  override def run(): Unit = {
    try {
      while (true) {
        val toSave = p.saves.take()
        toSave.players match {
          case Some(player) =>
            players.insert(new BasicDBObject("id", getKey(player)).append("players", player.pickle.value))
            log.info("Saves size {}", p.saves.size())
          case None =>
        }
        toSave.unit match {
          case Some(unit) =>
            units.insert(new BasicDBObject("id", getKey(unit)).append("units", unit.pickle.value))
          case None =>
        }
      }
    } catch {
      case e :InterruptedException => log.info("Stopping")
    }
  }
}

case class ToSave(unit: Option[ReplayUnit], players: Option[ReplayPlayers])