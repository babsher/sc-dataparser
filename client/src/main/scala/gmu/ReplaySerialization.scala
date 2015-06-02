package gmu

trait ReplaySerialization {

  def pickle(player: ReplayPlayers): Array[Byte] = ???

  def pickle(bwUnitType: Map[Unit.UnitType, BwUnitType]): Array[Byte] = ???

  def unpickleUnitTypes(s: Array[Byte]): Map[Unit.UnitType, BwUnitType] = ???

  def pickle(unit: ReplayUnit): Array[Byte] = ???

  def pickle(units: Seq[ReplayUnit]): Array[Byte] = ???

  def pickle(bwMap: BwMap): Array[Byte] = ???

  def unpickleMap(b: Array[Byte]): BwMap = ???

  def unpickleUnit(b: Array[Byte]): Seq[ReplayUnit] = ???

  def unpicklePlayers(b: Array[Byte]): ReplayPlayers = ???

  def toTuple(pos: RPosition): (Int, Int) = (pos.x / 32, pos.y / 32)
}
