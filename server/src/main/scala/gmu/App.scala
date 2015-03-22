package main.scala.gmu

import akka.actor._
import akka.routing.FromConfig
import com.typesafe.config.ConfigFactory

object HelloRemote extends App  {
  val config = ConfigFactory.load()
  val system = ActorSystem("Zerg", config)
  val router = system.actorOf(FromConfig.props(Props[ZergWorker]), "zergRouter")
}

class ZergWorker extends Actor {
  def receive = {
    case msg: String =>
      val name = context.self.path
      println(s"'$name' Got work '$msg'")
  }
}