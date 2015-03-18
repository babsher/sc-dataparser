package main.scala.gmu

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Local extends App {

  implicit val system = ActorSystem("LocalSystem", ConfigFactory.load())
  val localActor = system.actorOf(Props[LocalActor], name = "LocalActor")  // the local actor
  localActor ! "START"                                                     // start the action

}

class BwListener extends

class LocalActor extends Actor {

  // create the remote actor
  val remote = context.actorSelection("akka.tcp://Zerg@127.0.0.1:2552/user/zergRouter")
  var counter = 0

  var a = 0
  for(a <- 1 to 50) {
    remote ! s"work message '$a'"
  }

  def receive = {
    case "START" =>
      remote ! "Hello from the LocalActor"
    case msg: String =>
      println(s"LocalActor received message: '$msg'")
      if (counter < 5) {
        sender ! "Hello back to you"
        counter += 1
      }
  }
}