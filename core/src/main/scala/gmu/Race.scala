package gmu

import org.slf4j.LoggerFactory

object Race {
  val log = LoggerFactory.getLogger("gmu.Race")

  sealed trait RaceType
  case object Zerg extends RaceType
  case object Terran extends RaceType
  case object Protoss extends RaceType
  case object Random extends RaceType
  case object Other extends RaceType
  case object None extends RaceType
  case object Unknown extends RaceType

  def fromName(name: String): RaceType = {
    mapping.get(name) match {
      case Some(v) => v
      case scala.None => throw new IllegalArgumentException("Unknown type " + name)
    }
  }

  lazy val mapping = values.map(v => v.toString -> v).toMap

  val values = List(
  Zerg,
  Terran,
  Protoss,
  Random,
  Other,
  None,
  Unknown
  )
}
