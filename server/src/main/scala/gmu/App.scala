package gmu

import akka.actor._
import akka.routing.FromConfig
import com.typesafe.config.ConfigFactory
import gmu.Race.Race
import gmu.Unit.UnitType
import redis.RedisClient


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
}