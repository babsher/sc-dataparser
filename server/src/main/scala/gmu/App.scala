package main.scala.gmu

import akka.actor._
import com.typesafe.config.ConfigFactory

object HelloRemote extends App  {
  val system = ActorSystem("Zerg", ConfigFactory.load())
  val remoteActor = system.actorOf(Props[RemoteActor], name = "RemoteActor")
  remoteActor ! "The RemoteActor is alive"
}

class RemoteActor extends Actor {
  def receive = {
    case msg: String =>
      println(s"RemoteActor received message '$msg'")
      sender ! "Hello from the RemoteActor"
  }
}