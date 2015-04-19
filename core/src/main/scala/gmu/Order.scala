package gmu

object Order {
  sealed trait OrderType

  def fromName(name: String): OrderType = {
    values.filter(_.toString eq name).head
  }

  case object Die extends OrderType
  case object Stop extends OrderType
  case object Guard extends OrderType
  case object PlayerGuard extends OrderType
  case object TurretGuard extends OrderType
  case object BunkerGuard extends OrderType
  case object Move extends OrderType
  case object AttackUnit extends OrderType
  case object AttackTile extends OrderType
  case object Hover extends OrderType
  case object AttackMove extends OrderType
  case object InfestedCommandCenter extends OrderType
  case object UnusedNothing extends OrderType
  case object UnusedPowerup extends OrderType
  case object TowerGuard extends OrderType
  case object VultureMine extends OrderType
  case object Nothing extends OrderType
  case object Nothing3 extends OrderType
  case object CastInfestation extends OrderType
  case object InfestingCommandCenter extends OrderType
  case object PlaceBuilding extends OrderType
  case object BuildProtoss2 extends OrderType
  case object ConstructingBuilding extends OrderType
  case object Repair extends OrderType
  case object PlaceAddon extends OrderType
  case object BuildAddon extends OrderType
  case object Train extends OrderType
  case object RallyPointUnit extends OrderType
  case object RallyPointTile extends OrderType
  case object ZergBirth extends OrderType
  case object ZergUnitMorph extends OrderType
  case object ZergBuildingMorph extends OrderType
  case object IncompleteBuilding extends OrderType
  case object BuildNydusExit extends OrderType
  case object EnterNydusCanal extends OrderType
  case object Follow extends OrderType
  case object Carrier extends OrderType
  case object ReaverCarrierMove extends OrderType
  case object CarrierIgnore2 extends OrderType
  case object Reaver extends OrderType
  case object TrainFighter extends OrderType
  case object InterceptorAttack extends OrderType
  case object ScarabAttack extends OrderType
  case object RechargeShieldsUnit extends OrderType
  case object RechargeShieldsBattery extends OrderType
  case object ShieldBattery extends OrderType
  case object InterceptorReturn extends OrderType
  case object BuildingLand extends OrderType
  case object BuildingLiftOff extends OrderType
  case object DroneLiftOff extends OrderType
  case object LiftingOff extends OrderType
  case object ResearchTech extends OrderType
  case object Upgrade extends OrderType
  case object Larva extends OrderType
  case object SpawningLarva extends OrderType
  case object Harvest1 extends OrderType
  case object Harvest2 extends OrderType
  case object MoveToGas extends OrderType
  case object WaitForGas extends OrderType
  case object HarvestGas extends OrderType
  case object ReturnGas extends OrderType
  case object MoveToMinerals extends OrderType
  case object WaitForMinerals extends OrderType
  case object MiningMinerals extends OrderType
  case object Harvest3 extends OrderType
  case object Harvest4 extends OrderType
  case object ReturnMinerals extends OrderType
  case object Interrupted extends OrderType
  case object EnterTransport extends OrderType
  case object PickupIdle extends OrderType
  case object PickupTransport extends OrderType
  case object PickupBunker extends OrderType
  case object Pickup4 extends OrderType
  case object PowerupIdle extends OrderType
  case object Sieging extends OrderType
  case object Unsieging extends OrderType
  case object InitCreepGrowth extends OrderType
  case object SpreadCreep extends OrderType
  case object StoppingCreepGrowth extends OrderType
  case object GuardianAspect extends OrderType
  case object ArchonWarp extends OrderType
  case object CompletingArchonsummon extends OrderType
  case object HoldPosition extends OrderType
  case object Cloak extends OrderType
  case object Decloak extends OrderType
  case object Unload extends OrderType
  case object MoveUnload extends OrderType
  case object FireYamatoGun extends OrderType
  case object CastLockdown extends OrderType
  case object Burrowing extends OrderType
  case object Burrowed extends OrderType
  case object Unburrowing extends OrderType
  case object CastDarkSwarm extends OrderType
  case object CastParasite extends OrderType
  case object CastSpawnBroodlings extends OrderType
  case object CastEMPShockwave extends OrderType
  case object NukeWait extends OrderType
  case object NukeTrain extends OrderType
  case object NukeLaunch extends OrderType
  case object NukePaint extends OrderType
  case object NukeUnit extends OrderType
  case object CastNuclearStrike extends OrderType
  case object NukeTrack extends OrderType
  case object CloakNearbyUnits extends OrderType
  case object PlaceMine extends OrderType
  case object RightClickAction extends OrderType
  case object CastRecall extends OrderType
  case object TeleporttoLocation extends OrderType
  case object CastScannerSweep extends OrderType
  case object Scanner extends OrderType
  case object CastDefensiveMatrix extends OrderType
  case object CastPsionicStorm extends OrderType
  case object CastIrradiate extends OrderType
  case object CastPlague extends OrderType
  case object CastConsume extends OrderType
  case object CastEnsnare extends OrderType
  case object CastStasisField extends OrderType
  case object CastHallucination extends OrderType
  case object Hallucination2 extends OrderType
  case object ResetCollision extends OrderType
  case object Patrol extends OrderType
  case object CTFCOPInit extends OrderType
  case object CTFCOPStarted extends OrderType
  case object CTFCOP2 extends OrderType
  case object ComputerAI extends OrderType
  case object AtkMoveEP extends OrderType
  case object HarassMove extends OrderType
  case object AIPatrol extends OrderType
  case object GuardPost extends OrderType
  case object RescuePassive extends OrderType
  case object Neutral extends OrderType
  case object ComputerReturn extends OrderType
  case object SelfDestrucing extends OrderType
  case object Critter extends OrderType
  case object HiddenGun extends OrderType
  case object OpenDoor extends OrderType
  case object CloseDoor extends OrderType
  case object HideTrap extends OrderType
  case object RevealTrap extends OrderType
  case object Enabledoodad extends OrderType
  case object Disabledoodad extends OrderType
  case object Warpin extends OrderType
  case object Medic extends OrderType
  case object MedicHeal1 extends OrderType
  case object HealMove extends OrderType
  case object MedicHeal2 extends OrderType
  case object CastRestoration extends OrderType
  case object CastDisruptionWeb extends OrderType
  case object CastMindControl extends OrderType
  case object DarkArchonMeld extends OrderType
  case object CastFeedback extends OrderType
  case object CastOpticalFlare extends OrderType
  case object CastMaelstrom extends OrderType
  case object JunkYardDog extends OrderType
  case object Fatal extends OrderType
  case object None extends OrderType
  case object Unknown extends OrderType

  val values = List(
      Die,
      Stop,
      Guard,
      PlayerGuard,
      TurretGuard,
      BunkerGuard,
      Move,
      AttackUnit,
      AttackTile,
      Hover,
      AttackMove,
      InfestedCommandCenter,
      UnusedNothing,
      UnusedPowerup,
      TowerGuard,
      VultureMine,
      Nothing,
      Nothing3,
      CastInfestation,
      InfestingCommandCenter,
      PlaceBuilding,
      BuildProtoss2,
      ConstructingBuilding,
      Repair,
      PlaceAddon,
      BuildAddon,
      Train,
      RallyPointUnit,
      RallyPointTile,
      ZergBirth,
      ZergUnitMorph,
      ZergBuildingMorph,
      IncompleteBuilding,
      BuildNydusExit,
      EnterNydusCanal,
      Follow,
      Carrier,
      ReaverCarrierMove,
      CarrierIgnore2,
      Reaver,
      TrainFighter,
      InterceptorAttack,
      ScarabAttack,
      RechargeShieldsUnit,
      RechargeShieldsBattery,
      ShieldBattery,
      InterceptorReturn,
      BuildingLand,
      BuildingLiftOff,
      DroneLiftOff,
      LiftingOff,
      ResearchTech,
      Upgrade,
      Larva,
      SpawningLarva,
      Harvest1,
      Harvest2,
      MoveToGas,
      WaitForGas,
      HarvestGas,
      ReturnGas,
      MoveToMinerals,
      WaitForMinerals,
      MiningMinerals,
      Harvest3,
      Harvest4,
      ReturnMinerals,
      Interrupted,
      EnterTransport,
      PickupIdle,
      PickupTransport,
      PickupBunker,
      Pickup4,
      PowerupIdle,
      Sieging,
      Unsieging,
      InitCreepGrowth,
      SpreadCreep,
      StoppingCreepGrowth,
      GuardianAspect,
      ArchonWarp,
      CompletingArchonsummon,
      HoldPosition,
      Cloak,
      Decloak,
      Unload,
      MoveUnload,
      FireYamatoGun,
      CastLockdown,
      Burrowing,
      Burrowed,
      Unburrowing,
      CastDarkSwarm,
      CastParasite,
      CastSpawnBroodlings,
      CastEMPShockwave,
      NukeWait,
      NukeTrain,
      NukeLaunch,
      NukePaint,
      NukeUnit,
      CastNuclearStrike,
      NukeTrack,
      CloakNearbyUnits,
      PlaceMine,
      RightClickAction,
      CastRecall,
      TeleporttoLocation,
      CastScannerSweep,
      Scanner,
      CastDefensiveMatrix,
      CastPsionicStorm,
      CastIrradiate,
      CastPlague,
      CastConsume,
      CastEnsnare,
      CastStasisField,
      CastHallucination,
      Hallucination2,
      ResetCollision,
      Patrol,
      CTFCOPInit,
      CTFCOPStarted,
      CTFCOP2,
      ComputerAI,
      AtkMoveEP,
      HarassMove,
      AIPatrol,
      GuardPost,
      RescuePassive,
      Neutral,
      ComputerReturn,
      SelfDestrucing,
      Critter,
      HiddenGun,
      OpenDoor,
      CloseDoor,
      HideTrap,
      RevealTrap,
      Enabledoodad,
      Disabledoodad,
      Warpin,
      Medic,
      MedicHeal1,
      HealMove,
      MedicHeal2,
      CastRestoration,
      CastDisruptionWeb,
      CastMindControl,
      DarkArchonMeld,
      CastFeedback,
      CastOpticalFlare,
      CastMaelstrom,
      JunkYardDog,
      Fatal,
      None,
      Unknown
  )
}
