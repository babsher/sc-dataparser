package gmu

import akka.actor._
import akka.routing.FromConfig
import akka.util.ByteString
import com.typesafe.config.ConfigFactory
import redis.{ByteStringFormatter, ByteStringSerializer, RedisClient}
import scala.pickling._
import scala.pickling.binary._
import scala.pickling.static._
import scala.pickling.Defaults.{ pickleOps, unpickleOps }
import scala.pickling.Defaults._


object HelloRemote extends App  {
  val config = ConfigFactory.load()
  val system = ActorSystem("Zerg", config)
  val router = system.actorOf(FromConfig.props(Props[ZergWorker]), "zergRouter")
}

class ZergWorker extends Actor {
  val redis = RedisClient()

  def receive = {
    case msg: ReplayFrame =>
      redis.set(getKey(msg), msg)
  }

  def getKey(frame: ReplayFrame): String = {
    frame.replay + "" + frame.frame + frame.map
  }

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

    override def deserialize(bs: ByteString): ReplayFrame = {
      bs.toArray.unpickle[ReplayFrame]
    }

    override def serialize(data: ReplayFrame): ByteString = {
      ByteString(data.pickle.value)
    }
  }
}