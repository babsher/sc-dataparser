package gmu

object Upgrade {

  def fromName(name: String): UpgradeType = {
    values.filter(_.toString == name).head
  }
  sealed trait UpgradeType
  case object Terran_Infantry_Armor extends UpgradeType
  case object Terran_Vehicle_Plating extends UpgradeType
  case object Terran_Ship_Plating extends UpgradeType
  case object Zerg_Carapace extends UpgradeType
  case object Zerg_Flyer_Carapace extends UpgradeType
  case object Protoss_Ground_Armor extends UpgradeType
  case object Protoss_Air_Armor extends UpgradeType
  case object Terran_Infantry_Weapons extends UpgradeType
  case object Terran_Vehicle_Weapons extends UpgradeType
  case object Terran_Ship_Weapons extends UpgradeType
  case object Zerg_Melee_Attacks extends UpgradeType
  case object Zerg_Missile_Attacks extends UpgradeType
  case object Zerg_Flyer_Attacks extends UpgradeType
  case object Protoss_Ground_Weapons extends UpgradeType
  case object Protoss_Air_Weapons extends UpgradeType
  case object Protoss_Plasma_Shields extends UpgradeType
  case object U_238_Shells extends UpgradeType
  case object Ion_Thrusters extends UpgradeType
  case object Titan_Reactor extends UpgradeType
  case object Ocular_Implants extends UpgradeType
  case object Moebius_Reactor extends UpgradeType
  case object Apollo_Reactor extends UpgradeType
  case object Colossus_Reactor extends UpgradeType
  case object Ventral_Sacs extends UpgradeType
  case object Antennae extends UpgradeType
  case object Pneumatized_Carapace extends UpgradeType
  case object Metabolic_Boost extends UpgradeType
  case object Adrenal_Glands extends UpgradeType
  case object Muscular_Augments extends UpgradeType
  case object Grooved_Spines extends UpgradeType
  case object Gamete_Meiosis extends UpgradeType
  case object Metasynaptic_Node extends UpgradeType
  case object Singularity_Charge extends UpgradeType
  case object Leg_Enhancements extends UpgradeType
  case object Scarab_Damage extends UpgradeType
  case object Reaver_Capacity extends UpgradeType
  case object Gravitic_Drive extends UpgradeType
  case object Sensor_Array extends UpgradeType
  case object Gravitic_Boosters extends UpgradeType
  case object Khaydarin_Amulet extends UpgradeType
  case object Apial_Sensors extends UpgradeType
  case object Gravitic_Thrusters extends UpgradeType
  case object Carrier_Capacity extends UpgradeType
  case object Khaydarin_Core extends UpgradeType
  case object Argus_Jewel extends UpgradeType
  case object Argus_Talisman extends UpgradeType
  case object Caduceus_Reactor extends UpgradeType
  case object Chitinous_Plating extends UpgradeType
  case object Anabolic_Synthesis extends UpgradeType
  case object Charon_Boosters extends UpgradeType
  case object Upgrade_60 extends UpgradeType
  case object None extends UpgradeType
  case object Unknown extends UpgradeType

  val values = List(
    Terran_Infantry_Armor,
  Terran_Vehicle_Plating,
  Terran_Ship_Plating,
  Zerg_Carapace,
  Zerg_Flyer_Carapace,
  Protoss_Ground_Armor,
  Protoss_Air_Armor,
  Terran_Infantry_Weapons,
  Terran_Vehicle_Weapons,
  Terran_Ship_Weapons,
  Zerg_Melee_Attacks,
  Zerg_Missile_Attacks,
  Zerg_Flyer_Attacks,
  Protoss_Ground_Weapons,
  Protoss_Air_Weapons,
  Protoss_Plasma_Shields,
  U_238_Shells,
  Ion_Thrusters,
  Titan_Reactor,
  Ocular_Implants,
  Moebius_Reactor,
  Apollo_Reactor,
  Colossus_Reactor,
  Ventral_Sacs,
  Antennae,
  Pneumatized_Carapace,
  Metabolic_Boost,
  Adrenal_Glands,
  Muscular_Augments,
  Grooved_Spines,
  Gamete_Meiosis,
  Metasynaptic_Node,
  Singularity_Charge,
  Leg_Enhancements,
  Scarab_Damage,
  Reaver_Capacity,
  Gravitic_Drive,
  Sensor_Array,
  Gravitic_Boosters,
  Khaydarin_Amulet,
  Apial_Sensors,
  Gravitic_Thrusters,
  Carrier_Capacity,
  Khaydarin_Core,
  Argus_Jewel,
  Argus_Talisman,
  Caduceus_Reactor,
  Chitinous_Plating,
  Anabolic_Synthesis,
  Charon_Boosters,
  Upgrade_60,
  None,
  Unknown
  )
}