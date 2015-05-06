package gmu

import akka.actor.{Actor, ActorSystem, Props}
import akka.event.Logging
import bwapi.Mirror
import com.mongodb.client.model.UpdateOptions
import com.mongodb.{MongoClientOptions, BasicDBObject, MongoClient, WriteConcern}
import com.typesafe.config.ConfigFactory
import gmu.UnitState.UnitState
import org.bson.Document
import scala.pickling._
import scala.pickling.binary._
import scala.pickling.static._
import scala.pickling.Defaults._

import scala.collection.mutable

case class GameUnit(state: UnitState, unit: bwapi.Unit, replayFrame: ReplayFrame)

object Local extends App {

//  implicit val system = ActorSystem("LocalSystem", ConfigFactory.load())
//  val localActor = system.actorOf(FromConfig.props(Props[LocalActor]), "localRouter")

//  val localActor = system.actorOf(Props(new LocalActor()), "localActor")

  val mirror = new Mirror()
  val listener = new BwListener(mirror)
  mirror.getModule.setEventListener(listener)

  mirror.startGame()
}

class LocalActor() extends Actor with ReplayPickles with ReplayConversions {
  val log = Logging(context.system, this)

  val mongo = new MongoClient("192.168.1.250", MongoClientOptions.builder()
    .connectionsPerHost(32)
    .writeConcern(WriteConcern.UNACKNOWLEDGED)
    .build())

  val db = mongo.getDatabase("sc")
  val units = db.getCollection("units")
  val players = db.getCollection("players")

//  val remote = context.actorSelection("akka.tcp://Zerg@192.168.1.250:2552/user/zergRouter")
//  remote ! WakeUp

  def receive = {
    case GameUnit(state, unit, frame) =>
      val msg = replayUnit(state, unit, frame)
      units.insertOne(new Document("id", getKey(msg)).append("units", msg.pickle.value))
//      units.updateOne(new BasicDBObject("id", getKey(msg)),
//        new BasicDBObject("$push", new BasicDBObject("units", msg.pickle.value)),
//        new UpdateOptions().upsert(true)
//      )
//      remote ! replayUnit(state, unit, frame)
    case msg: ReplayPlayers =>
      log.info("Got replay frame, sending {}/{}", msg.frame.frame, msg.frame.frameCount)
      players.insertOne(new Document("id", getKey(msg)).append("players", msg.pickle.value))
//      remote ! msg
    case ReplayDone =>
      log.info("Done")
//      remote ! ReplayDone
  }
}