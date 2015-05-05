package gmu

import akka.actor._
import akka.event.Logging
import akka.routing.FromConfig
import akka.util.ByteString
import com.typesafe.config.ConfigFactory
import redis.{RedisCommands, ByteStringFormatter, ByteStringSerializer, RedisClient}
import scala.pickling._
import scala.pickling.binary._
import scala.pickling.static._
import scala.pickling.Defaults._


object ZergWorker extends App  {
  val config = ConfigFactory.load()
  implicit val system = ActorSystem("Zerg", config)
  val router = system.actorOf(FromConfig.props(ZergWorker.props()), "zergRouter")

  def props() = Props(new ZergWorker(new RedisClient(db = Option(25)), new RedisClient(db = Option(26))))
}

trait RedisSerializer {
  implicit val replayFrameSerializer = new ByteStringFormatter[ReplayFrame] {
    implicit val racePickler = Pickler.generate[gmu.Race.RaceType]
    implicit val raceUnplicker = Unpickler.generate[gmu.Race.RaceType]

    implicit val velPickler = Pickler.generate[gmu.Velocity]
    implicit val velUnpickler = Unpickler.generate[gmu.Velocity]

    implicit val posPickler = Pickler.generate[gmu.RPosition]
    implicit val posUnpickler = Unpickler.generate[gmu.RPosition]

    implicit val unitPickler = Pickler.generate[gmu.ReplayUnit]
    implicit val unitUnpickler = Unpickler.generate[gmu.ReplayUnit]

    implicit val playerPickler = Pickler.generate[gmu.ReplayPlayer]
    implicit val playerUnpickler = Unpickler.generate[gmu.ReplayPlayer]

    implicit val replayFramePickler = Pickler.generate[ReplayFrame]
    implicit val replayFrameUnpickler = Unpickler.generate[ReplayFrame]

    implicit val mapSizePickler = Pickler.generate[MapSize]
    implicit val mapSizeUnpickler = Unpickler.generate[MapSize]

    override def deserialize(bs: ByteString): ReplayFrame = {
      bs.toArray.unpickle[ReplayFrame]
    }

    override def serialize(data: ReplayFrame): ByteString = {
      ByteString(data.pickle.value)
    }
  }

  def getKey(frame: ReplayFrame): String =
    frame.replay + "" + frame.frame + frame.map
}

class ZergWorker(val frame :RedisCommands, val units :RedisCommands) extends Actor with RedisSerializer {
  val log = Logging(context.system, this)

  def receive = {
    case WakeUp =>
      log.debug("Got wake up")
    case msg: ReplayUnit =>
      units.lset(getKey(msg.replay), msg.id, msg)
    case msg: ReplayFrame =>
      log.debug("Got replay frame: {} {}/{}", msg.map.mapName, msg.frame, msg.frameCount)
      frame.set(getKey(msg), msg)
  }
}