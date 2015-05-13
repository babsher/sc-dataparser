package gmu

import bwapi.{Unit => BwUnit, _}
import com.google.common.util.concurrent.RateLimiter
import com.mongodb.{MongoClient, MongoClientOptions, WriteConcern}
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.collection.mutable

class BwListener(val mirror: Mirror, var replayNum: Int, val dbName: String, val mapOnly: Boolean)
  extends DefaultBWListener
  with ReplayConversions {
  val log = LoggerFactory.getLogger("gmu.BwListener")

  lazy val game = mirror.getGame
  val current: mutable.Set[Int] = mutable.Set[Int]()
  val destroyed: mutable.Set[Int] = mutable.Set[Int]()
  var map: ReplayMap = null

  val mongo = new MongoClient("192.168.1.250", MongoClientOptions.builder()
    .connectionsPerHost(32)
    .writeConcern(WriteConcern.UNACKNOWLEDGED)
    .build())

  val persister = new Persister(mongo, dbName)
  val rl = RateLimiter.create(2)

  val per = new MongoPersistence(mongo, dbName)

  def getState(unit: BwUnit) = {
    if (current.contains(unit.getID)) {
      if (destroyed.contains(unit.getID)) {
        UnitState.Destroyed
      } else {
        UnitState.Normal
      }
    } else {
      current.add(unit.getID)
      UnitState.Created
    }
  }

  override def onFrame(): Unit = {
    if (!mapOnly) {
      parseFrame()
    }
  }

  def parseFrame(): Unit = {
    val frame = ReplayFrame(
      map,
      replayNum,
      game.getFrameCount,
      game.getReplayFrameCount)

    val units = mirror.getGame.getAllUnits.filter(u => {
      // dont save non player units
      val r = u.getType.getRace
      r == bwapi.Race.Protoss || r == bwapi.Race.Zerg || r == bwapi.Race.Terran
    })
      .map(unit => {
      val state = getState(unit)
      replayUnit(state, unit, frame)
    }).toSeq
    if (rl.tryAcquire()) {
      log.info("Sending units {}/{}", units.size, mirror.getGame.getAllUnits.size())
    }
    persister.saves.put(ToSave(Some(units), None, None))

    val replayPlayers = game.getPlayers.map(p => {
      val hasTech = techTypes.map(tech => (convert(tech), p.hasResearched(tech))).toMap
      val hasUpgrade = upgradeTypes.map(up => convert(up) -> p.getUpgradeLevel(up)).toMap
      ReplayPlayer(p.getID, p.getRace, p.supplyUsed(), p.supplyTotal(), hasTech, hasUpgrade)
    })
    if (rl.tryAcquire()) {
      log.info("Sending replay frame, {}/{}", frame.frame, frame.frameCount)
    }
    val msg = ReplayPlayers(frame, replayPlayers)
    persister.saves.put(ToSave(None, Some(msg), None))
  }

  override def onEnd(b: Boolean): Unit = {
    log.info("Replay Ended")
  }

  override def onUnitDestroy(unit: BwUnit): Unit = {
    destroyed.add(unit.getID)
  }

  def convert(techType: bwapi.TechType): gmu.Tech.TechType =
    gmu.Tech.fromName(techType.toString)

  def convert(upgradeType: UpgradeType): gmu.Upgrade.UpgradeType =
    gmu.Upgrade.fromName(upgradeType.toString)

  def mapName: String = {
    mirror.getGame.mapName()
  }

  def mapDimensions: (Int, Int) = {
    (mirror.getGame.mapHeight(), mirror.getGame.mapWidth())
  }

  override def onStart(): Unit = {
    replayNum += 1
    val game = mirror.getGame
    game.setLocalSpeed(0)
    game.setGUI(false)
    map = ReplayMap(game.mapName(), MapSize(game.mapHeight(), game.mapWidth()))
    if (!per.mapExists(map.mapName)) {
      val mapInfo = for (
        x <- Range(0, map.size.x);
        y <- Range(0, map.size.y)
      ) yield MapCell(x, y, game.getGroundHeight(x, y))
      persister.saves.put(ToSave(None, None, Some(BwMap(map, mapInfo))))
    }
    if (per.unitTypesIsEmpty) {
      val types = unitTypes.map(convertUnitType)
        .map(u => u.untitType -> u)
        .toMap
      per.insert(types)
    }
  }

  def convertTech(t: bwapi.TechType): gmu.Tech.TechType = {
    gmu.Tech.fromName(t.toString)
  }

  def convertUpgrades(u: bwapi.UpgradeType): gmu.Upgrade.UpgradeType = {
    gmu.Upgrade.fromName(u.toString)
  }

  def convertUnitType(t: bwapi.UnitType): gmu.BwUnitType = {
    gmu.BwUnitType(
      gmu.Unit.fromName(t.toString),
      t.getRace,
      t.maxHitPoints(),
      t.maxShields(),
      t.maxEnergy(),
      t.armor(),
      t.mineralPrice(),
      t.gasPrice(),
      t.buildTime(),
      t.buildScore(),
      t.destroyScore(),
      RPosition(t.tileSize().getX, t.tileSize().getY),
      t.width(),
      t.height(),
      t.seekRange(),
      t.sightRange(),
      t.groundWeapon(),
      t.maxGroundHits(),
      t.airWeapon(),
      t.maxAirHits(),
      t.topSpeed(),
      t.acceleration(),
      t.haltDistance(),
      t.turnRadius(),
      t.canProduce(),
      t.canAttack(),
      t.canMove(),
      t.isFlyer(),
      t.regeneratesHP(),
      t.isSpellcaster(),
      t.hasPermanentCloak(),
      t.isDetector(),
      t.isResourceContainer(),
      t.isResourceDepot(),
      t.isRefinery(),
      t.isWorker(),
      t.requiresPsi(),
      t.requiresCreep(),
      t.isTwoUnitsInOneEgg(),
      t.isBurrowable(),
      t.isCloakable(),
      t.isBuilding(),
      t.isAddon(),
      t.isFlyingBuilding(),
      t.isHero(),
      t.isPowerup(),
      t.isBeacon(),
      t.isFlagBeacon(),
      t.isSpecialBuilding(),
      t.isSpell(),
      t.producesLarva(),
      t.isMineralField(),
      t.isCritter(),
      t.canBuildAddon()
    )
  }

  val unitTypes = Seq(
    bwapi.UnitType.Terran_Marine,
      bwapi.UnitType.Terran_Ghost,
      bwapi.UnitType.Terran_Vulture,
      bwapi.UnitType.Terran_Goliath,
      bwapi.UnitType.Terran_Siege_Tank_Tank_Mode,
      bwapi.UnitType.Terran_SCV,
      bwapi.UnitType.Terran_Wraith,
      bwapi.UnitType.Terran_Science_Vessel,
      bwapi.UnitType.Hero_Gui_Montag,
      bwapi.UnitType.Terran_Dropship,
      bwapi.UnitType.Terran_Battlecruiser,
      bwapi.UnitType.Terran_Vulture_Spider_Mine,
      bwapi.UnitType.Terran_Nuclear_Missile,
      bwapi.UnitType.Terran_Civilian,
      bwapi.UnitType.Hero_Sarah_Kerrigan,
      bwapi.UnitType.Hero_Alan_Schezar,
      bwapi.UnitType.Hero_Jim_Raynor_Vulture,
      bwapi.UnitType.Hero_Jim_Raynor_Marine,
      bwapi.UnitType.Hero_Tom_Kazansky,
      bwapi.UnitType.Hero_Magellan,
      bwapi.UnitType.Hero_Edmund_Duke_Tank_Mode,
      bwapi.UnitType.Hero_Edmund_Duke_Siege_Mode,
      bwapi.UnitType.Hero_Arcturus_Mengsk,
      bwapi.UnitType.Hero_Hyperion,
      bwapi.UnitType.Hero_Norad_II,
      bwapi.UnitType.Terran_Siege_Tank_Siege_Mode,
      bwapi.UnitType.Terran_Firebat,
      bwapi.UnitType.Spell_Scanner_Sweep,
      bwapi.UnitType.Terran_Medic,
      bwapi.UnitType.Zerg_Larva,
      bwapi.UnitType.Zerg_Egg,
      bwapi.UnitType.Zerg_Zergling,
      bwapi.UnitType.Zerg_Hydralisk,
      bwapi.UnitType.Zerg_Ultralisk,
      bwapi.UnitType.Zerg_Broodling,
      bwapi.UnitType.Zerg_Drone,
      bwapi.UnitType.Zerg_Overlord,
      bwapi.UnitType.Zerg_Mutalisk,
      bwapi.UnitType.Zerg_Guardian,
      bwapi.UnitType.Zerg_Queen,
      bwapi.UnitType.Zerg_Defiler,
      bwapi.UnitType.Zerg_Scourge,
      bwapi.UnitType.Hero_Torrasque,
      bwapi.UnitType.Hero_Matriarch,
      bwapi.UnitType.Zerg_Infested_Terran,
      bwapi.UnitType.Hero_Infested_Kerrigan,
      bwapi.UnitType.Hero_Unclean_One,
      bwapi.UnitType.Hero_Hunter_Killer,
      bwapi.UnitType.Hero_Devouring_One,
      bwapi.UnitType.Hero_Kukulza_Mutalisk,
      bwapi.UnitType.Hero_Kukulza_Guardian,
      bwapi.UnitType.Hero_Yggdrasill,
      bwapi.UnitType.Terran_Valkyrie,
      bwapi.UnitType.Zerg_Cocoon,
      bwapi.UnitType.Protoss_Corsair,
      bwapi.UnitType.Protoss_Dark_Templar,
      bwapi.UnitType.Zerg_Devourer,
      bwapi.UnitType.Protoss_Dark_Archon,
      bwapi.UnitType.Protoss_Probe,
      bwapi.UnitType.Protoss_Zealot,
      bwapi.UnitType.Protoss_Dragoon,
      bwapi.UnitType.Protoss_High_Templar,
      bwapi.UnitType.Protoss_Archon,
      bwapi.UnitType.Protoss_Shuttle,
      bwapi.UnitType.Protoss_Scout,
      bwapi.UnitType.Protoss_Arbiter,
      bwapi.UnitType.Protoss_Carrier,
      bwapi.UnitType.Protoss_Interceptor,
      bwapi.UnitType.Hero_Dark_Templar,
      bwapi.UnitType.Hero_Zeratul,
      bwapi.UnitType.Hero_Tassadar_Zeratul_Archon,
      bwapi.UnitType.Hero_Fenix_Zealot,
      bwapi.UnitType.Hero_Fenix_Dragoon,
      bwapi.UnitType.Hero_Tassadar,
      bwapi.UnitType.Hero_Mojo,
      bwapi.UnitType.Hero_Warbringer,
      bwapi.UnitType.Hero_Gantrithor,
      bwapi.UnitType.Protoss_Reaver,
      bwapi.UnitType.Protoss_Observer,
      bwapi.UnitType.Protoss_Scarab,
      bwapi.UnitType.Hero_Danimoth,
      bwapi.UnitType.Hero_Aldaris,
      bwapi.UnitType.Hero_Artanis,
      bwapi.UnitType.Critter_Rhynadon,
      bwapi.UnitType.Critter_Bengalaas,
      bwapi.UnitType.Special_Cargo_Ship,
      bwapi.UnitType.Special_Mercenary_Gunship,
      bwapi.UnitType.Critter_Scantid,
      bwapi.UnitType.Critter_Kakaru,
      bwapi.UnitType.Critter_Ragnasaur,
      bwapi.UnitType.Critter_Ursadon,
      bwapi.UnitType.Zerg_Lurker_Egg,
      bwapi.UnitType.Hero_Raszagal,
      bwapi.UnitType.Hero_Samir_Duran,
      bwapi.UnitType.Hero_Alexei_Stukov,
      bwapi.UnitType.Special_Map_Revealer,
      bwapi.UnitType.Hero_Gerard_DuGalle,
      bwapi.UnitType.Zerg_Lurker,
      bwapi.UnitType.Hero_Infested_Duran,
      bwapi.UnitType.Spell_Disruption_Web,
      bwapi.UnitType.Terran_Command_Center,
      bwapi.UnitType.Terran_Comsat_Station,
      bwapi.UnitType.Terran_Nuclear_Silo,
      bwapi.UnitType.Terran_Supply_Depot,
      bwapi.UnitType.Terran_Refinery,
      bwapi.UnitType.Terran_Barracks,
      bwapi.UnitType.Terran_Academy,
      bwapi.UnitType.Terran_Factory,
      bwapi.UnitType.Terran_Starport,
      bwapi.UnitType.Terran_Control_Tower,
      bwapi.UnitType.Terran_Science_Facility,
      bwapi.UnitType.Terran_Covert_Ops,
      bwapi.UnitType.Terran_Physics_Lab,
      bwapi.UnitType.Terran_Machine_Shop,
      bwapi.UnitType.Terran_Engineering_Bay,
      bwapi.UnitType.Terran_Armory,
      bwapi.UnitType.Terran_Missile_Turret,
      bwapi.UnitType.Terran_Bunker,
      bwapi.UnitType.Special_Crashed_Norad_II,
      bwapi.UnitType.Special_Ion_Cannon,
      bwapi.UnitType.Powerup_Uraj_Crystal,
      bwapi.UnitType.Powerup_Khalis_Crystal,
      bwapi.UnitType.Zerg_Infested_Command_Center,
      bwapi.UnitType.Zerg_Hatchery,
      bwapi.UnitType.Zerg_Lair,
      bwapi.UnitType.Zerg_Hive,
      bwapi.UnitType.Zerg_Nydus_Canal,
      bwapi.UnitType.Zerg_Hydralisk_Den,
      bwapi.UnitType.Zerg_Defiler_Mound,
      bwapi.UnitType.Zerg_Greater_Spire,
      bwapi.UnitType.Zerg_Queens_Nest,
      bwapi.UnitType.Zerg_Evolution_Chamber,
      bwapi.UnitType.Zerg_Ultralisk_Cavern,
      bwapi.UnitType.Zerg_Spire,
      bwapi.UnitType.Zerg_Spawning_Pool,
      bwapi.UnitType.Zerg_Creep_Colony,
      bwapi.UnitType.Zerg_Spore_Colony,
      bwapi.UnitType.Zerg_Sunken_Colony,
      bwapi.UnitType.Special_Overmind_With_Shell,
      bwapi.UnitType.Special_Overmind,
      bwapi.UnitType.Zerg_Extractor,
      bwapi.UnitType.Special_Mature_Chrysalis,
      bwapi.UnitType.Special_Cerebrate,
      bwapi.UnitType.Special_Cerebrate_Daggoth,
      bwapi.UnitType.Protoss_Nexus,
      bwapi.UnitType.Protoss_Robotics_Facility,
      bwapi.UnitType.Protoss_Pylon,
      bwapi.UnitType.Protoss_Assimilator,
      bwapi.UnitType.Protoss_Observatory,
      bwapi.UnitType.Protoss_Gateway,
      bwapi.UnitType.Protoss_Photon_Cannon,
      bwapi.UnitType.Protoss_Citadel_of_Adun,
      bwapi.UnitType.Protoss_Cybernetics_Core,
      bwapi.UnitType.Protoss_Templar_Archives,
      bwapi.UnitType.Protoss_Forge,
      bwapi.UnitType.Protoss_Stargate,
      bwapi.UnitType.Special_Stasis_Cell_Prison,
      bwapi.UnitType.Protoss_Fleet_Beacon,
      bwapi.UnitType.Protoss_Arbiter_Tribunal,
      bwapi.UnitType.Protoss_Robotics_Support_Bay,
      bwapi.UnitType.Protoss_Shield_Battery,
      bwapi.UnitType.Special_Khaydarin_Crystal_Form,
      bwapi.UnitType.Special_Protoss_Temple,
      bwapi.UnitType.Special_XelNaga_Temple,
      bwapi.UnitType.Resource_Mineral_Field,
      bwapi.UnitType.Resource_Mineral_Field_Type_2,
      bwapi.UnitType.Resource_Mineral_Field_Type_3,
      bwapi.UnitType.Special_Independant_Starport,
      bwapi.UnitType.Resource_Vespene_Geyser,
      bwapi.UnitType.Special_Warp_Gate,
      bwapi.UnitType.Special_Psi_Disrupter,
      bwapi.UnitType.Special_Zerg_Beacon,
      bwapi.UnitType.Special_Terran_Beacon,
      bwapi.UnitType.Special_Protoss_Beacon,
      bwapi.UnitType.Special_Zerg_Flag_Beacon,
      bwapi.UnitType.Special_Terran_Flag_Beacon,
      bwapi.UnitType.Special_Protoss_Flag_Beacon,
      bwapi.UnitType.Special_Power_Generator,
      bwapi.UnitType.Special_Overmind_Cocoon,
      bwapi.UnitType.Spell_Dark_Swarm,
      bwapi.UnitType.Special_Floor_Missile_Trap,
      bwapi.UnitType.Special_Floor_Hatch,
      bwapi.UnitType.Special_Upper_Level_Door,
      bwapi.UnitType.Special_Right_Upper_Level_Door,
      bwapi.UnitType.Special_Pit_Door,
      bwapi.UnitType.Special_Right_Pit_Door,
      bwapi.UnitType.Special_Floor_Gun_Trap,
      bwapi.UnitType.Special_Wall_Missile_Trap,
      bwapi.UnitType.Special_Wall_Flame_Trap,
      bwapi.UnitType.Special_Right_Wall_Missile_Trap,
      bwapi.UnitType.Special_Right_Wall_Flame_Trap,
      bwapi.UnitType.Special_Start_Location,
      bwapi.UnitType.Powerup_Flag,
      bwapi.UnitType.Powerup_Young_Chrysalis,
      bwapi.UnitType.Powerup_Psi_Emitter,
      bwapi.UnitType.Powerup_Data_Disk,
      bwapi.UnitType.Powerup_Khaydarin_Crystal,
      bwapi.UnitType.Powerup_Mineral_Cluster_Type_1,
      bwapi.UnitType.Powerup_Mineral_Cluster_Type_2,
      bwapi.UnitType.Powerup_Protoss_Gas_Orb_Type_1,
      bwapi.UnitType.Powerup_Protoss_Gas_Orb_Type_2,
      bwapi.UnitType.Powerup_Zerg_Gas_Sac_Type_1,
      bwapi.UnitType.Powerup_Zerg_Gas_Sac_Type_2,
      bwapi.UnitType.Powerup_Terran_Gas_Tank_Type_1,
      bwapi.UnitType.Powerup_Terran_Gas_Tank_Type_2,
      bwapi.UnitType.None,
      bwapi.UnitType.AllUnits,
      bwapi.UnitType.Men,
      bwapi.UnitType.Buildings,
      bwapi.UnitType.Factories,
      bwapi.UnitType.Unknown)

  lazy val techTypes =
    bwapi.TechType.Stim_Packs ::
      bwapi.TechType.Lockdown ::
      bwapi.TechType.EMP_Shockwave ::
      bwapi.TechType.Spider_Mines ::
      bwapi.TechType.Scanner_Sweep ::
      bwapi.TechType.Tank_Siege_Mode ::
      bwapi.TechType.Defensive_Matrix ::
      bwapi.TechType.Irradiate ::
      bwapi.TechType.Yamato_Gun ::
      bwapi.TechType.Cloaking_Field ::
      bwapi.TechType.Personnel_Cloaking ::
      bwapi.TechType.Burrowing ::
      bwapi.TechType.Infestation ::
      bwapi.TechType.Spawn_Broodlings ::
      bwapi.TechType.Dark_Swarm ::
      bwapi.TechType.Plague ::
      bwapi.TechType.Consume ::
      bwapi.TechType.Ensnare ::
      bwapi.TechType.Parasite ::
      bwapi.TechType.Psionic_Storm ::
      bwapi.TechType.Hallucination ::
      bwapi.TechType.Recall ::
      bwapi.TechType.Stasis_Field ::
      bwapi.TechType.Archon_Warp ::
      bwapi.TechType.Restoration ::
      bwapi.TechType.Disruption_Web ::
      bwapi.TechType.Mind_Control ::
      bwapi.TechType.Dark_Archon_Meld ::
      bwapi.TechType.Feedback ::
      bwapi.TechType.Optical_Flare ::
      bwapi.TechType.Maelstrom ::
      bwapi.TechType.Lurker_Aspect ::
      bwapi.TechType.Healing ::
      bwapi.TechType.None ::
      bwapi.TechType.Unknown ::
      bwapi.TechType.Nuclear_Strike :: Nil

  lazy val upgradeTypes =
    bwapi.UpgradeType.Terran_Infantry_Armor ::
      bwapi.UpgradeType.Terran_Vehicle_Plating ::
      bwapi.UpgradeType.Terran_Ship_Plating ::
      bwapi.UpgradeType.Zerg_Carapace ::
      bwapi.UpgradeType.Zerg_Flyer_Carapace ::
      bwapi.UpgradeType.Protoss_Ground_Armor ::
      bwapi.UpgradeType.Protoss_Air_Armor ::
      bwapi.UpgradeType.Terran_Infantry_Weapons ::
      bwapi.UpgradeType.Terran_Vehicle_Weapons ::
      bwapi.UpgradeType.Terran_Ship_Weapons ::
      bwapi.UpgradeType.Zerg_Melee_Attacks ::
      bwapi.UpgradeType.Zerg_Missile_Attacks ::
      bwapi.UpgradeType.Zerg_Flyer_Attacks ::
      bwapi.UpgradeType.Protoss_Ground_Weapons ::
      bwapi.UpgradeType.Protoss_Air_Weapons ::
      bwapi.UpgradeType.Protoss_Plasma_Shields ::
      bwapi.UpgradeType.U_238_Shells ::
      bwapi.UpgradeType.Ion_Thrusters ::
      bwapi.UpgradeType.Titan_Reactor ::
      bwapi.UpgradeType.Ocular_Implants ::
      bwapi.UpgradeType.Moebius_Reactor ::
      bwapi.UpgradeType.Apollo_Reactor ::
      bwapi.UpgradeType.Colossus_Reactor ::
      bwapi.UpgradeType.Ventral_Sacs ::
      bwapi.UpgradeType.Antennae ::
      bwapi.UpgradeType.Pneumatized_Carapace ::
      bwapi.UpgradeType.Metabolic_Boost ::
      bwapi.UpgradeType.Adrenal_Glands ::
      bwapi.UpgradeType.Muscular_Augments ::
      bwapi.UpgradeType.Grooved_Spines ::
      bwapi.UpgradeType.Gamete_Meiosis ::
      bwapi.UpgradeType.Metasynaptic_Node ::
      bwapi.UpgradeType.Singularity_Charge ::
      bwapi.UpgradeType.Leg_Enhancements ::
      bwapi.UpgradeType.Scarab_Damage ::
      bwapi.UpgradeType.Reaver_Capacity ::
      bwapi.UpgradeType.Gravitic_Drive ::
      bwapi.UpgradeType.Sensor_Array ::
      bwapi.UpgradeType.Gravitic_Boosters ::
      bwapi.UpgradeType.Khaydarin_Amulet ::
      bwapi.UpgradeType.Apial_Sensors ::
      bwapi.UpgradeType.Gravitic_Thrusters ::
      bwapi.UpgradeType.Carrier_Capacity ::
      bwapi.UpgradeType.Khaydarin_Core ::
      bwapi.UpgradeType.Argus_Jewel ::
      bwapi.UpgradeType.Argus_Talisman ::
      bwapi.UpgradeType.Caduceus_Reactor ::
      bwapi.UpgradeType.Chitinous_Plating ::
      bwapi.UpgradeType.Anabolic_Synthesis ::
      bwapi.UpgradeType.Charon_Boosters ::
      bwapi.UpgradeType.None ::
      bwapi.UpgradeType.Unknown :: Nil
}
