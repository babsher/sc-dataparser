package gmu

import java.io.{File, FileOutputStream}

import akka.actor._
import akka.event.Logging
import akka.routing.FromConfig
import akka.util.ByteString
import com.redis.RedisClient
import com.typesafe.config.ConfigFactory
import scala.pickling._
import scala.pickling.binary._
import scala.pickling.static._
import scala.pickling.Defaults._


object ZergWorker extends App  {
  val config = ConfigFactory.load()
  implicit val system = ActorSystem("Zerg", config)
  val router = system.actorOf(FromConfig.props(ZergWorker.props()), "zergRouter")

  def props() = Props(new ZergWorker(new RedisClient("localhost", 6379)))
}

class ZergWorker(val frame: RedisClient) extends Actor with RedisSerializer {
  val log = Logging(context.system, this)

  def receive = {
    case WakeUp =>
      log.info("Got wake up")
    case msg: ReplayUnit =>
      log.debug("got replay unit {}", msg.id)
      frame.lpush(getKey(msg), msg)
//      frame.lset(getKey(msg), msg.id, msg)
    case msg: ReplayPlayers =>
      log.info("Got replay frame: {} {}/{}", msg.frame.map.mapName, msg.frame.frame, msg.frame.frameCount)
      frame.set(getKey(msg), msg)
    case ReplayDone =>
      log.info("Replay Done")
  }
}