package gmu

trait ReplayConversions {
  def replayUnit(state: UnitState.Value, unit: bwapi.Unit, frame: ReplayFrame): ReplayUnit = {
    val orderTarget = unit.getOrderTarget
    val orderTargetId :Int = if(unit.getOrderTarget != null) unit.getOrderTarget.getID else -1

    val targetId :Int = if(unit.getTarget != null) unit.getTarget.getID else -1
    val energy = Option(unit.getEnergy)

    ReplayUnit(
      frame,
      UnitState.Created eq state,
      UnitState.Destroyed eq state,
      unit.getID,
      unit.getPlayer.getID,
      unit.getPosition,
      Velocity(unit.getVelocityX, unit.getVelocityY),
      unit.getType.getRace,
      unit.getInitialHitPoints,
      unit.getHitPoints,
      unit.getShields,
      unit.getType,
      if(energy.isEmpty) -1 else energy.get,
      unit.getOrder,
      orderTargetId,
      unit.getOrderTargetPosition,
      targetId,
      unit.getTargetPosition,
      unit.getGroundWeaponCooldown,
      unit.getAirWeaponCooldown,
      unit.getSpellCooldown
    )
  }

  implicit def convert[T](sq: collection.mutable.Seq[T]): collection.immutable.Seq[T] =
    collection.immutable.Seq[T](sq:_*)

  implicit def convert(race: bwapi.Race): gmu.Race.RaceType =
    gmu.Race.fromName(race.toString)

  implicit def convertToEnum(unitType: bwapi.UnitType): gmu.Unit.UnitType =
    gmu.Unit.fromName(unitType.toString)

  implicit def convert(order: bwapi.Order): gmu.Order.OrderType =
    gmu.Order.fromName(order.toString)

  implicit def convert(pos: bwapi.Position): gmu.RPosition =
    RPosition(pos.getX, pos.getY)

  implicit def convert(unitType: bwapi.UnitType): gmu.UnitTypeAttributes =
    UnitTypeAttributes(
      unitType.armor,
      unitType.isDetector,
      unitType.isFlyer,
      unitType.isMechanical,
      unitType.isOrganic,
      unitType.isRobotic,
      unitType.isWorker,
      unitType.isBuilding,
      unitType.groundWeapon,
      unitType.groundWeapon,
      unitType.airWeapon,
      unitType.airWeapon,
      unitType.destroyScore
    )

  implicit def convert(weaponType: bwapi.WeaponType): gmu.Weapon.WeaponType =
    gmu.Weapon.fromName(weaponType.toString)

  implicit def convertToWeaponTypeInfo(weaponType: bwapi.WeaponType): gmu.WeaponTypeInfo =
    WeaponTypeInfo(weaponType.damageAmount(),
      weaponType.damageCooldown(),
      weaponType.maxRange(),
      weaponType.minRange(),
      weaponType.medianSplashRadius())
}
