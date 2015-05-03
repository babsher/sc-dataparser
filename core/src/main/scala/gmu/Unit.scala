package gmu

object Unit {

  def fromName(name: String): UnitType = {
    values.filter(_.toString.startsWith(name)).head
  }

  sealed trait UnitType
  case object Terran_Marine extends UnitType
  case object Hero_Jim_Raynor_Marine extends UnitType
  case object Terran_Ghost extends UnitType
  case object Hero_Sarah_Kerrigan extends UnitType
  case object Hero_Samir_Duran extends UnitType
  case object Hero_Infested_Duran extends UnitType
  case object Hero_Alexei_Stukov extends UnitType
  case object Terran_Vulture extends UnitType
  case object Hero_Jim_Raynor_Vulture extends UnitType
  case object Terran_Goliath extends UnitType
  case object Hero_Alan_Schezar extends UnitType
  case object Terran_Siege_Tank_Tank_Mode extends UnitType
  case object Hero_Edmund_Duke_Tank_Mode extends UnitType
  case object Terran_SCV extends UnitType
  case object Terran_Wraith extends UnitType
  case object Hero_Tom_Kazansky extends UnitType
  case object Terran_Science_Vessel extends UnitType
  case object Hero_Magellan extends UnitType
  case object Terran_Dropship extends UnitType
  case object Terran_Battlecruiser extends UnitType
  case object Hero_Arcturus_Mengsk extends UnitType
  case object Hero_Hyperion extends UnitType
  case object Hero_Norad_II extends UnitType
  case object Hero_Gerard_DuGalle extends UnitType
  case object Terran_Vulture_Spider_Mine extends UnitType
  case object Terran_Nuclear_Missile extends UnitType
  case object Terran_Siege_Tank_Siege_Mode extends UnitType
  case object Hero_Edmund_Duke_Siege_Mode extends UnitType
  case object Terran_Firebat extends UnitType
  case object Hero_Gui_Montag extends UnitType
  case object Spell_Scanner_Sweep extends UnitType
  case object Terran_Medic extends UnitType
  case object Terran_Civilian extends UnitType
  case object Zerg_Larva extends UnitType
  case object Zerg_Egg extends UnitType
  case object Zerg_Zergling extends UnitType
  case object Hero_Devouring_One extends UnitType
  case object Hero_Infested_Kerrigan extends UnitType
  case object Zerg_Hydralisk extends UnitType
  case object Hero_Hunter_Killer extends UnitType
  case object Zerg_Ultralisk extends UnitType
  case object Hero_Torrasque extends UnitType
  case object Zerg_Broodling extends UnitType
  case object Zerg_Drone extends UnitType
  case object Zerg_Overlord extends UnitType
  case object Hero_Yggdrasill extends UnitType
  case object Zerg_Mutalisk extends UnitType
  case object Hero_Kukulza_Mutalisk extends UnitType
  case object Zerg_Guardian extends UnitType
  case object Hero_Kukulza_Guardian extends UnitType
  case object Zerg_Queen extends UnitType
  case object Hero_Matriarch extends UnitType
  case object Zerg_Defiler extends UnitType
  case object Hero_Unclean_One extends UnitType
  case object Zerg_Scourge extends UnitType
  case object Zerg_Infested_Terran extends UnitType
  case object Terran_Valkyrie extends UnitType
  case object Zerg_Cocoon extends UnitType
  case object Protoss_Corsair extends UnitType
  case object Hero_Raszagal extends UnitType
  case object Protoss_Dark_Templar extends UnitType
  case object Hero_Dark_Templar extends UnitType
  case object Hero_Zeratul extends UnitType
  case object Zerg_Devourer extends UnitType
  case object Protoss_Dark_Archon extends UnitType
  case object Protoss_Probe extends UnitType
  case object Protoss_Zealot extends UnitType
  case object Hero_Fenix_Zealot extends UnitType
  case object Protoss_Dragoon extends UnitType
  case object Hero_Fenix_Dragoon extends UnitType
  case object Protoss_High_Templar extends UnitType
  case object Hero_Tassadar extends UnitType
  case object Hero_Aldaris extends UnitType
  case object Protoss_Archon extends UnitType
  case object Hero_Tassadar_Zeratul_Archon extends UnitType
  case object Protoss_Shuttle extends UnitType
  case object Protoss_Scout extends UnitType
  case object Hero_Mojo extends UnitType
  case object Hero_Artanis extends UnitType
  case object Protoss_Arbiter extends UnitType
  case object Hero_Danimoth extends UnitType
  case object Protoss_Carrier extends UnitType
  case object Hero_Gantrithor extends UnitType
  case object Protoss_Interceptor extends UnitType
  case object Protoss_Reaver extends UnitType
  case object Hero_Warbringer extends UnitType
  case object Protoss_Observer extends UnitType
  case object Protoss_Scarab extends UnitType
  case object Critter_Rhynadon extends UnitType
  case object Critter_Bengalaas extends UnitType
  case object Special_Cargo_Ship extends UnitType
  case object Special_Mercenary_Gunship extends UnitType
  case object Critter_Scantid extends UnitType
  case object Critter_Kakaru extends UnitType
  case object Critter_Ragnasaur extends UnitType
  case object Critter_Ursadon extends UnitType
  case object Zerg_Lurker_Egg extends UnitType
  case object Zerg_Lurker extends UnitType
  case object Spell_Disruption_Web extends UnitType
  case object Terran_Command_Center extends UnitType
  case object Terran_Comsat_Station extends UnitType
  case object Terran_Nuclear_Silo extends UnitType
  case object Terran_Supply_Depot extends UnitType
  case object Terran_Refinery extends UnitType
  case object Terran_Barracks extends UnitType
  case object Terran_Academy extends UnitType
  case object Terran_Factory extends UnitType
  case object Terran_Starport extends UnitType
  case object Terran_Control_Tower extends UnitType
  case object Terran_Science_Facility extends UnitType
  case object Terran_Covert_Ops extends UnitType
  case object Terran_Physics_Lab extends UnitType
  case object Terran_Machine_Shop extends UnitType
  case object Terran_Engineering_Bay extends UnitType
  case object Terran_Armory extends UnitType
  case object Terran_Missile_Turret extends UnitType
  case object Terran_Bunker extends UnitType
  case object Special_Crashed_Norad_II extends UnitType
  case object Special_Ion_Cannon extends UnitType
  case object Zerg_Infested_Command_Center extends UnitType
  case object Zerg_Hatchery extends UnitType
  case object Zerg_Lair extends UnitType
  case object Zerg_Hive extends UnitType
  case object Zerg_Nydus_Canal extends UnitType
  case object Zerg_Hydralisk_Den extends UnitType
  case object Zerg_Defiler_Mound extends UnitType
  case object Zerg_Greater_Spire extends UnitType
  case object Zerg_Queens_Nest extends UnitType
  case object Zerg_Evolution_Chamber extends UnitType
  case object Zerg_Ultralisk_Cavern extends UnitType
  case object Zerg_Spire extends UnitType
  case object Zerg_Spawning_Pool extends UnitType
  case object Zerg_Creep_Colony extends UnitType
  case object Zerg_Spore_Colony extends UnitType
  case object Zerg_Sunken_Colony extends UnitType
  case object Special_Overmind_With_Shell extends UnitType
  case object Special_Overmind extends UnitType
  case object Zerg_Extractor extends UnitType
  case object Special_Mature_Chrysalis extends UnitType
  case object Special_Cerebrate extends UnitType
  case object Special_Cerebrate_Daggoth extends UnitType
  case object Protoss_Nexus extends UnitType
  case object Protoss_Robotics_Facility extends UnitType
  case object Protoss_Pylon extends UnitType
  case object Protoss_Assimilator extends UnitType
  case object Protoss_Observatory extends UnitType
  case object Protoss_Gateway extends UnitType
  case object Protoss_Photon_Cannon extends UnitType
  case object Protoss_Citadel_of_Adun extends UnitType
  case object Protoss_Cybernetics_Core extends UnitType
  case object Protoss_Templar_Archives extends UnitType
  case object Protoss_Forge extends UnitType
  case object Protoss_Stargate extends UnitType
  case object Special_Stasis_Cell_Prison extends UnitType
  case object Protoss_Fleet_Beacon extends UnitType
  case object Protoss_Arbiter_Tribunal extends UnitType
  case object Protoss_Robotics_Support_Bay extends UnitType
  case object Protoss_Shield_Battery extends UnitType
  case object Special_Khaydarin_Crystal_Form extends UnitType
  case object Special_Protoss_Temple extends UnitType
  case object Special_XelNaga_Temple extends UnitType
  case object Resource_Mineral_Field extends UnitType
  case object Resource_Mineral_Field_Type_2 extends UnitType
  case object Resource_Mineral_Field_Type_3 extends UnitType
  case object Special_Independant_Starport extends UnitType
  case object Resource_Vespene_Geyser extends UnitType
  case object Special_Warp_Gate extends UnitType
  case object Special_Psi_Disrupter extends UnitType
  case object Special_Power_Generator extends UnitType
  case object Special_Overmind_Cocoon extends UnitType
  case object Special_Zerg_Beacon extends UnitType
  case object Special_Terran_Beacon extends UnitType
  case object Special_Protoss_Beacon extends UnitType
  case object Special_Zerg_Flag_Beacon extends UnitType
  case object Special_Terran_Flag_Beacon extends UnitType
  case object Special_Protoss_Flag_Beacon extends UnitType
  case object Spell_Dark_Swarm extends UnitType
  case object Powerup_Uraj_Crystal extends UnitType
  case object Powerup_Khalis_Crystal extends UnitType
  case object Powerup_Flag extends UnitType
  case object Powerup_Young_Chrysalis extends UnitType
  case object Powerup_Psi_Emitter extends UnitType
  case object Powerup_Data_Disk extends UnitType
  case object Powerup_Khaydarin_Crystal extends UnitType
  case object Powerup_Mineral_Cluster_Type_1 extends UnitType
  case object Powerup_Mineral_Cluster_Type_2 extends UnitType
  case object Powerup_Protoss_Gas_Orb_Type_1 extends UnitType
  case object Powerup_Protoss_Gas_Orb_Type_2 extends UnitType
  case object Powerup_Zerg_Gas_Sac_Type_1 extends UnitType
  case object Powerup_Zerg_Gas_Sac_Type_2 extends UnitType
  case object Powerup_Terran_Gas_Tank_Type_1 extends UnitType
  case object Powerup_Terran_Gas_Tank_Type_2 extends UnitType
  case object Special_Map_Revealer extends UnitType
  case object Special_Floor_Missile_Trap extends UnitType
  case object Special_Floor_Hatch extends UnitType
  case object Special_Upper_Level_Door extends UnitType
  case object Special_Right_Upper_Level_Door extends UnitType
  case object Special_Pit_Door extends UnitType
  case object Special_Right_Pit_Door extends UnitType
  case object Special_Floor_Gun_Trap extends UnitType
  case object Special_Wall_Missile_Trap extends UnitType
  case object Special_Wall_Flame_Trap extends UnitType
  case object Special_Right_Wall_Missile_Trap extends UnitType
  case object Special_Right_Wall_Flame_Trap extends UnitType
  case object Special_Start_Location extends UnitType
  case object None extends UnitType
  case object AllUnits extends UnitType
  case object Men extends UnitType
  case object Buildings extends UnitType
  case object Factories extends UnitType
  case object Unknown extends UnitType

  val values = List(Terran_Marine,
    Hero_Jim_Raynor_Marine,
    Terran_Ghost,
    Hero_Sarah_Kerrigan,
    Hero_Samir_Duran,
    Hero_Infested_Duran,
    Hero_Alexei_Stukov,
    Terran_Vulture,
    Hero_Jim_Raynor_Vulture,
    Terran_Goliath,
    Hero_Alan_Schezar,
    Terran_Siege_Tank_Tank_Mode,
    Hero_Edmund_Duke_Tank_Mode,
    Terran_SCV,
    Terran_Wraith,
    Hero_Tom_Kazansky,
    Terran_Science_Vessel,
    Hero_Magellan,
    Terran_Dropship,
    Terran_Battlecruiser,
    Hero_Arcturus_Mengsk,
    Hero_Hyperion,
    Hero_Norad_II,
    Hero_Gerard_DuGalle,
    Terran_Vulture_Spider_Mine,
    Terran_Nuclear_Missile,
    Terran_Siege_Tank_Siege_Mode,
    Hero_Edmund_Duke_Siege_Mode,
    Terran_Firebat,
    Hero_Gui_Montag,
    Spell_Scanner_Sweep,
    Terran_Medic,
    Terran_Civilian,
    Zerg_Larva,
    Zerg_Egg,
    Zerg_Zergling,
    Hero_Devouring_One,
    Hero_Infested_Kerrigan,
    Zerg_Hydralisk,
    Hero_Hunter_Killer,
    Zerg_Ultralisk,
    Hero_Torrasque,
    Zerg_Broodling,
    Zerg_Drone,
    Zerg_Overlord,
    Hero_Yggdrasill,
    Zerg_Mutalisk,
    Hero_Kukulza_Mutalisk,
    Zerg_Guardian,
    Hero_Kukulza_Guardian,
    Zerg_Queen,
    Hero_Matriarch,
    Zerg_Defiler,
    Hero_Unclean_One,
    Zerg_Scourge,
    Zerg_Infested_Terran,
    Terran_Valkyrie,
    Zerg_Cocoon,
    Protoss_Corsair,
    Hero_Raszagal,
    Protoss_Dark_Templar,
    Hero_Dark_Templar,
    Hero_Zeratul,
    Zerg_Devourer,
    Protoss_Dark_Archon,
    Protoss_Probe,
    Protoss_Zealot,
    Hero_Fenix_Zealot,
    Protoss_Dragoon,
    Hero_Fenix_Dragoon,
    Protoss_High_Templar,
    Hero_Tassadar,
    Hero_Aldaris,
    Protoss_Archon,
    Hero_Tassadar_Zeratul_Archon,
    Protoss_Shuttle,
    Protoss_Scout,
    Hero_Mojo,
    Hero_Artanis,
    Protoss_Arbiter,
    Hero_Danimoth,
    Protoss_Carrier,
    Hero_Gantrithor,
    Protoss_Interceptor,
    Protoss_Reaver,
    Hero_Warbringer,
    Protoss_Observer,
    Protoss_Scarab,
    Critter_Rhynadon,
    Critter_Bengalaas,
    Special_Cargo_Ship,
    Special_Mercenary_Gunship,
    Critter_Scantid,
    Critter_Kakaru,
    Critter_Ragnasaur,
    Critter_Ursadon,
    Zerg_Lurker_Egg,
    Zerg_Lurker,
    Spell_Disruption_Web,
    Terran_Command_Center,
    Terran_Comsat_Station,
    Terran_Nuclear_Silo,
    Terran_Supply_Depot,
    Terran_Refinery,
    Terran_Barracks,
    Terran_Academy,
    Terran_Factory,
    Terran_Starport,
    Terran_Control_Tower,
    Terran_Science_Facility,
    Terran_Covert_Ops,
    Terran_Physics_Lab,
    Terran_Machine_Shop,
    Terran_Engineering_Bay,
    Terran_Armory,
    Terran_Missile_Turret,
    Terran_Bunker,
    Special_Crashed_Norad_II,
    Special_Ion_Cannon,
    Zerg_Infested_Command_Center,
    Zerg_Hatchery,
    Zerg_Lair,
    Zerg_Hive,
    Zerg_Nydus_Canal,
    Zerg_Hydralisk_Den,
    Zerg_Defiler_Mound,
    Zerg_Greater_Spire,
    Zerg_Queens_Nest,
    Zerg_Evolution_Chamber,
    Zerg_Ultralisk_Cavern,
    Zerg_Spire,
    Zerg_Spawning_Pool,
    Zerg_Creep_Colony,
    Zerg_Spore_Colony,
    Zerg_Sunken_Colony,
    Special_Overmind_With_Shell,
    Special_Overmind,
    Zerg_Extractor,
    Special_Mature_Chrysalis,
    Special_Cerebrate,
    Special_Cerebrate_Daggoth,
    Protoss_Nexus,
    Protoss_Robotics_Facility,
    Protoss_Pylon,
    Protoss_Assimilator,
    Protoss_Observatory,
    Protoss_Gateway,
    Protoss_Photon_Cannon,
    Protoss_Citadel_of_Adun,
    Protoss_Cybernetics_Core,
    Protoss_Templar_Archives,
    Protoss_Forge,
    Protoss_Stargate,
    Special_Stasis_Cell_Prison,
    Protoss_Fleet_Beacon,
    Protoss_Arbiter_Tribunal,
    Protoss_Robotics_Support_Bay,
    Protoss_Shield_Battery,
    Special_Khaydarin_Crystal_Form,
    Special_Protoss_Temple,
    Special_XelNaga_Temple,
    Resource_Mineral_Field,
    Resource_Mineral_Field_Type_2,
    Resource_Mineral_Field_Type_3,
    Special_Independant_Starport,
    Resource_Vespene_Geyser,
    Special_Warp_Gate,
    Special_Psi_Disrupter,
    Special_Power_Generator,
    Special_Overmind_Cocoon,
    Special_Zerg_Beacon,
    Special_Terran_Beacon,
    Special_Protoss_Beacon,
    Special_Zerg_Flag_Beacon,
    Special_Terran_Flag_Beacon,
    Special_Protoss_Flag_Beacon,
    Spell_Dark_Swarm,
    Powerup_Uraj_Crystal,
    Powerup_Khalis_Crystal,
    Powerup_Flag,
    Powerup_Young_Chrysalis,
    Powerup_Psi_Emitter,
    Powerup_Data_Disk,
    Powerup_Khaydarin_Crystal,
    Powerup_Mineral_Cluster_Type_1,
    Powerup_Mineral_Cluster_Type_2,
    Powerup_Protoss_Gas_Orb_Type_1,
    Powerup_Protoss_Gas_Orb_Type_2,
    Powerup_Zerg_Gas_Sac_Type_1,
    Powerup_Zerg_Gas_Sac_Type_2,
    Powerup_Terran_Gas_Tank_Type_1,
    Powerup_Terran_Gas_Tank_Type_2,
    Special_Map_Revealer,
    Special_Floor_Missile_Trap,
    Special_Floor_Hatch,
    Special_Upper_Level_Door,
    Special_Right_Upper_Level_Door,
    Special_Pit_Door,
    Special_Right_Pit_Door,
    Special_Floor_Gun_Trap,
    Special_Wall_Missile_Trap,
    Special_Wall_Flame_Trap,
    Special_Right_Wall_Missile_Trap,
    Special_Right_Wall_Flame_Trap,
    Special_Start_Location,
    None,
    AllUnits,
    Men,
    Buildings,
    Factories,
    Unknown)
}