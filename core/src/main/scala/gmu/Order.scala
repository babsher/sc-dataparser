package gmu

sealed trait Order extends Order.Order
object Order extends Enumeration {
  type Order = Value
  val
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
  Unknown = Value
}
