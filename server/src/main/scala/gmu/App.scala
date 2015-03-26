package main.scala.gmu

import akka.actor._
import akka.routing.FromConfig
import com.typesafe.config.ConfigFactory
import gmu.ReplayFrame
import redis.RedisClient
import scala.pickling.Defaults._
import scala.pickling.binary._

object HelloRemote extends App  {
  val config = ConfigFactory.load()
  val system = ActorSystem("Zerg", config)
  val router = system.actorOf(FromConfig.props(Props[ZergWorker]), "zergRouter")
}

class ZergWorker extends Actor {

  val redis = RedisClient()

  def receive = {
    case msg: ReplayFrame =>
      redis.set(getKey(msg), msg.pickle)
  }

  def getKey(frame: ReplayFrame): String = {
    frame.replay + "" + frame.frame + frame.map
  }
}