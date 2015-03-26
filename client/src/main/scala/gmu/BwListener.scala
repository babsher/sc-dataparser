package gmu

import main.scala.gmu.GameUnit

import scala.collection.JavaConversions._

import akka.actor.ActorRef
import bwapi.{Unit => BwUnit, _}

import scala.collection.mutable

class BwListener(val local: ActorRef, val mirror: Mirror) extends DefaultBWListener {

  val game = mirror.getGame
  val current: mutable.Set[Int] = mutable.Set[Int]()
  val destroyed: mutable.Set[Int] = mutable.Set[Int]()
  var map: ReplayMap = null
  var replayNum: Int = 0

  def getState(unit: BwUnit) = {
    if (current.contains(unit.getID)) {
      if(destroyed.contains(unit.getID)) {
        UnitState.Destoryed
      } else {
        UnitState.Normal
      }
    } else {
      current.add(unit.getID)
      UnitState.Created
    }
  }

  override def onFrame(): Unit = {
    for(unit <- mirror.getGame.getAllUnits) {
      val state = getState(unit)
      local ! GameUnit(state, unit)
    }
    val replayPlayers = game.getPlayers.map(p => {
      val hasTech = techTypes.map(tech => (convert(tech), p.hasResearched(tech))).toMap
      val hasUpgrade = upgradeTypes.map(up => (convert(up), p.getUpgradeLevel(up))).toMap
      ReplayPlayer(p.getID, hasTech, hasUpgrade)
    })
    local ! ReplayFrame(null,
      map,
      replayNum,
      game.getFrameCount,
      game.getReplayFrameCount,
      replayPlayers)
  }

  override def onEnd(b: Boolean): Unit = super.onEnd(b)

  override def onUnitDestroy(unit: BwUnit): Unit = {
    destroyed.add(unit.getID)
  }

  def convert(techType: bwapi.TechType): gmu.Tech.TechType =
    gmu.Tech.withName(techType.toString)

  def convert(upgradeType: UpgradeType): gmu.Upgrade.UpgradeType =
    gmu.Upgrade.withName(upgradeType.toString)

  def mapName: String = {
    mirror.getGame.mapName()
  }

  def mapDimensions: (Int, Int) = {
    (mirror.getGame.mapHeight(), mirror.getGame.mapWidth())
  }

  override def onStart(): Unit = {
    replayNum += 1
    val game = mirror.getGame
    map = ReplayMap(game.mapName(), (game.mapHeight(), game.mapWidth()))
    // getplayers
    // update replay map
    // update replay num
  }

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
