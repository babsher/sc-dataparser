package gmu

import java.io.{File, FileOutputStream}

import akka.actor._
import akka.event.Logging
import akka.routing.FromConfig
import akka.util.ByteString
import com.mongodb._
import com.mongodb.client.model.UpdateOptions
import com.redis.RedisClient
import com.typesafe.config.ConfigFactory
import org.bson.Document
import scala.pickling._
import scala.pickling.binary._
import scala.pickling.static._
import scala.pickling.Defaults._


object ZergWorker extends App  {
  val config = ConfigFactory.load()
  implicit val system = ActorSystem("Zerg", config)
  val router = system.actorOf(FromConfig.props(ZergWorker.props()), "zergRouter")

  def props() = Props(new ZergWorker(new MongoClient()))
}

class ZergWorker(val mongo: MongoClient) extends Actor {
  val log = Logging(context.system, this)
  val db = mongo.getDatabase("sc")
  db.withWriteConcern(WriteConcern.UNACKNOWLEDGED)
  val units = db.getCollection("units")
  val players = db.getCollection("players")

  def receive = {
    case WakeUp =>
      log.info("Got wake up")
    case msg: ReplayUnit =>
      log.debug("got replay unit {}", msg.id)
//      units.updateOne(new BasicDBObject("id", getKey(msg)),
//        new BasicDBObject("$push", new BasicDBObject("units", msg.pickle.value)),
//        new UpdateOptions().upsert(true)
//      )
//      frame.lpush(getKey(msg), msg)
//      frame.lset(getKey(msg), msg.id, msg)
    case msg: ReplayPlayers =>
      log.info("Got replay frame: {} {}/{}", msg.frame.map.mapName, msg.frame.frame, msg.frame.frameCount)
//      players.insertOne(new Document("id", getKey(msg)).append("players", msg.pickle.value))
//      players.save(MongoDBObject("id" -> getKey(msg), ("players" -> msgValue)))
//      frame.set(getKey(msg), msg)
    case ReplayDone =>
      log.info("Replay Done")
  }
}