package gmu

import org.scalatest._
import scala.pickling._
import scala.pickling.binary._
import scala.pickling.static._
import scala.pickling.Defaults._

class AppTest extends FlatSpec with Matchers {
  val units = Seq(
    ReplayUnit(true,  false,  2,  1,  RPosition(1,2), Velocity(22.0,1.0), Race.Zerg, 100, 10, 0, Unit.Zerg_Drone, 0, Order.Stop, 2, RPosition(2,4), 2, RPosition(5,5), 0, 0, 0),
    ReplayUnit(true, false, 22, 11, RPosition(12,22), Velocity(224.0,14.0), Race.Zerg, 104, 1, 3, Unit.Zerg_Hydralisk, 3, Order.AIPatrol, 3, RPosition(1,4), 45, RPosition(7,5), 1, 2, 3))

  val players = Seq(
    ReplayPlayer(1, Map(Tech.Cloaking_Field -> false), Map(Upgrade.Chitinous_Plating -> 1)),
    ReplayPlayer(2, Map(Tech.Cloaking_Field -> false), Map(Upgrade.Chitinous_Plating -> 1)))

  val map = ReplayMap("amap", (128, 128))

  val replay = ReplayFrame(units, map, 1, 2, 3, players)

  "Replay frame" should "pickel and unpickle" in {
    val pickled = ZergWorker.replayFrameSerializer.serialize(replay)
    println pickle

    ZergWorker.replayFrameSerializer.deserialize(pickled) should be (replay)
  }
}
