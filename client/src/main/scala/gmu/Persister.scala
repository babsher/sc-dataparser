package gmu

import java.util.concurrent._
import com.google.common.util.concurrent.RateLimiter
import com.mongodb.{WriteConcern, MongoClientOptions, MongoClient}
import org.slf4j.LoggerFactory

class Persister(val mongo: MongoClient, val dbName: String) extends ReplayPickles {

  val rl = RateLimiter.create(1)
  val saves = new LinkedBlockingQueue[ToSave](1024 * 2)
  val pool = Executors.newFixedThreadPool(12)
  for(x <- Range(1, 12)) {
    pool.execute(new Mover(this, mongo, dbName, rl))
  }
}

class Mover(val p: Persister, val mongo: MongoClient, val dbName: String, val rl: RateLimiter) extends Runnable {
  val log = LoggerFactory.getLogger(classOf[Mover])
  val per = new MongoPersistence(mongo, dbName)

  override def run(): Unit = {
    try {
      while (true) {
        val toSave = p.saves.take()
        toSave.players match {
          case Some(player) =>
            per.insert(player)
            if(rl.tryAcquire()) {
              log.info("Saves size {}", p.saves.size())
            }
          case None =>
        }
        toSave.unit match {
          case Some(unit) =>
            per.insert(unit)
          case None =>
        }
        toSave.mapInfo match {
          case Some(m) =>
            per.insert(m)
          case None =>
        }
      }
    } catch {
      case e :InterruptedException => log.info("Stopping")
    }
  }
}

case class ToSave(unit: Option[Seq[ReplayUnit]], players: Option[ReplayPlayers], mapInfo: Option[BwMap])