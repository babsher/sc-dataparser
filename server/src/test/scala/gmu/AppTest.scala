package gmu

import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestKit, TestActorRef}
import org.scalamock.scalatest.MockFactory
import org.scalatest._
import redis.{RedisCommands, RedisClient}
import scala.pickling._
import scala.pickling.binary._
import scala.pickling.static._
import scala.pickling.Defaults._

class AppTest extends  TestKit(ActorSystem("MyActorTest"))  with WordSpecLike with Matchers with MockFactory  {
  val units = Seq(
    ReplayUnit(replay, true, false,  2,  1, RPosition(1,2), Velocity(22.0,1.0), Race.Zerg, 100, 10, 0, Unit.Zerg_Drone, 0, Order.Stop, 2, RPosition(2,4), 2, RPosition(5,5), 0, 0, 0),
    ReplayUnit(replay, true, false, 22, 11, RPosition(12,22), Velocity(224.0,14.0), Race.Zerg, 104, 1, 3, Unit.Zerg_Hydralisk, 3, Order.AIPatrol, 3, RPosition(1,4), 45, RPosition(7,5), 1, 2, 3))

  val players = Seq(
    ReplayPlayer(1, Map(Tech.Cloaking_Field -> false), Map(Upgrade.Chitinous_Plating -> 1)),
    ReplayPlayer(2, Map(Tech.Cloaking_Field -> false), Map(Upgrade.Chitinous_Plating -> 1)))

  val map = ReplayMap("amap", MapSize(128, 128))

  val replay = ReplayFrame(map, 1, 2, 3, players)

  "Replay frame pickel and unpickle" must {
    val redis = stub[RedisCommands]
    val sut = TestActorRef(Props(new ZergWorker(redis, redis)))

    sut ! replay
  }
}
