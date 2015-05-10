package gmu

object Weapon {

  def fromName(name: String): WeaponType = {
    mapping.get(name) match {
      case Some(v) => v
      case scala.None => throw new IllegalArgumentException("Unknown type " + name)
    }
  }

  lazy val mapping = values.map(v => v.toString -> v).toMap

  sealed trait WeaponType
  case object Gauss_Rifle extends WeaponType
  case object Gauss_Rifle_Jim_Raynor extends WeaponType
  case object C_10_Canister_Rifle extends WeaponType
  case object C_10_Canister_Rifle_Sarah_Kerrigan extends WeaponType
  case object C_10_Canister_Rifle_Samir_Duran extends WeaponType
  case object C_10_Canister_Rifle_Infested_Duran extends WeaponType
  case object C_10_Canister_Rifle_Alexei_Stukov extends WeaponType
  case object Fragmentation_Grenade extends WeaponType
  case object Fragmentation_Grenade_Jim_Raynor extends WeaponType
  case object Spider_Mines extends WeaponType
  case object Twin_Autocannons extends WeaponType
  case object Twin_Autocannons_Alan_Schezar extends WeaponType
  case object Hellfire_Missile_Pack extends WeaponType
  case object Hellfire_Missile_Pack_Alan_Schezar extends WeaponType
  case object Arclite_Cannon extends WeaponType
  case object Arclite_Cannon_Edmund_Duke extends WeaponType
  case object Fusion_Cutter extends WeaponType
  case object Gemini_Missiles extends WeaponType
  case object Gemini_Missiles_Tom_Kazansky extends WeaponType
  case object Burst_Lasers extends WeaponType
  case object Burst_Lasers_Tom_Kazansky extends WeaponType
  case object ATS_Laser_Battery extends WeaponType
  case object ATS_Laser_Battery_Hero extends WeaponType
  case object ATS_Laser_Battery_Hyperion extends WeaponType
  case object ATA_Laser_Battery extends WeaponType
  case object ATA_Laser_Battery_Hero extends WeaponType
  case object ATA_Laser_Battery_Hyperion extends WeaponType
  case object Flame_Thrower extends WeaponType
  case object Flame_Thrower_Gui_Montag extends WeaponType
  case object Arclite_Shock_Cannon extends WeaponType
  case object Arclite_Shock_Cannon_Edmund_Duke extends WeaponType
  case object Longbolt_Missile extends WeaponType
  case object Claws extends WeaponType
  case object Claws_Devouring_One extends WeaponType
  case object Claws_Infested_Kerrigan extends WeaponType
  case object Needle_Spines extends WeaponType
  case object Needle_Spines_Hunter_Killer extends WeaponType
  case object Kaiser_Blades extends WeaponType
  case object Kaiser_Blades_Torrasque extends WeaponType
  case object Toxic_Spores extends WeaponType
  case object Spines extends WeaponType
  case object Acid_Spore extends WeaponType
  case object Acid_Spore_Kukulza extends WeaponType
  case object Glave_Wurm extends WeaponType
  case object Glave_Wurm_Kukulza extends WeaponType
  case object Seeker_Spores extends WeaponType
  case object Subterranean_Tentacle extends WeaponType
  case object Suicide_Infested_Terran extends WeaponType
  case object Suicide_Scourge extends WeaponType
  case object Particle_Beam extends WeaponType
  case object Psi_Blades extends WeaponType
  case object Psi_Blades_Fenix extends WeaponType
  case object Phase_Disruptor extends WeaponType
  case object Phase_Disruptor_Fenix extends WeaponType
  case object Psi_Assault extends WeaponType
  case object Psionic_Shockwave extends WeaponType
  case object Psionic_Shockwave_TZ_Archon extends WeaponType
  case object Dual_Photon_Blasters extends WeaponType
  case object Dual_Photon_Blasters_Mojo extends WeaponType
  case object Dual_Photon_Blasters_Artanis extends WeaponType
  case object Anti_Matter_Missiles extends WeaponType
  case object Anti_Matter_Missiles_Mojo extends WeaponType
  case object Anti_Matter_Missiles_Artanis extends WeaponType
  case object Phase_Disruptor_Cannon extends WeaponType
  case object Phase_Disruptor_Cannon_Danimoth extends WeaponType
  case object Pulse_Cannon extends WeaponType
  case object STS_Photon_Cannon extends WeaponType
  case object STA_Photon_Cannon extends WeaponType
  case object Scarab extends WeaponType
  case object Neutron_Flare extends WeaponType
  case object Halo_Rockets extends WeaponType
  case object Corrosive_Acid extends WeaponType
  case object Subterranean_Spines extends WeaponType
  case object Warp_Blades extends WeaponType
  case object Warp_Blades_Hero extends WeaponType
  case object Warp_Blades_Zeratul extends WeaponType
  case object Independant_Laser_Battery extends WeaponType
  case object Twin_Autocannons_Floor_Trap extends WeaponType
  case object Hellfire_Missile_Pack_Wall_Trap extends WeaponType
  case object Flame_Thrower_Wall_Trap extends WeaponType
  case object Hellfire_Missile_Pack_Floor_Trap extends WeaponType
  case object Yamato_Gun extends WeaponType
  case object Nuclear_Strike extends WeaponType
  case object Lockdown extends WeaponType
  case object EMP_Shockwave extends WeaponType
  case object Irradiate extends WeaponType
  case object Parasite extends WeaponType
  case object Spawn_Broodlings extends WeaponType
  case object Ensnare extends WeaponType
  case object Dark_Swarm extends WeaponType
  case object Plague extends WeaponType
  case object Consume extends WeaponType
  case object Stasis_Field extends WeaponType
  case object Psionic_Storm extends WeaponType
  case object Disruption_Web extends WeaponType
  case object Restoration extends WeaponType
  case object Mind_Control extends WeaponType
  case object Feedback extends WeaponType
  case object Optical_Flare extends WeaponType
  case object Maelstrom extends WeaponType
  case object None extends WeaponType
  case object Unknown extends WeaponType

  val values = List(
    Gauss_Rifle,
  Gauss_Rifle_Jim_Raynor,
  C_10_Canister_Rifle,
  C_10_Canister_Rifle_Sarah_Kerrigan,
  C_10_Canister_Rifle_Samir_Duran,
  C_10_Canister_Rifle_Infested_Duran,
  C_10_Canister_Rifle_Alexei_Stukov,
  Fragmentation_Grenade,
  Fragmentation_Grenade_Jim_Raynor,
  Spider_Mines,
  Twin_Autocannons,
  Twin_Autocannons_Alan_Schezar,
  Hellfire_Missile_Pack,
  Hellfire_Missile_Pack_Alan_Schezar,
  Arclite_Cannon,
  Arclite_Cannon_Edmund_Duke,
  Fusion_Cutter,
  Gemini_Missiles,
  Gemini_Missiles_Tom_Kazansky,
  Burst_Lasers,
  Burst_Lasers_Tom_Kazansky,
  ATS_Laser_Battery,
  ATS_Laser_Battery_Hero,
  ATS_Laser_Battery_Hyperion,
  ATA_Laser_Battery,
  ATA_Laser_Battery_Hero,
  ATA_Laser_Battery_Hyperion,
  Flame_Thrower,
  Flame_Thrower_Gui_Montag,
  Arclite_Shock_Cannon,
  Arclite_Shock_Cannon_Edmund_Duke,
  Longbolt_Missile,
  Claws,
  Claws_Devouring_One,
  Claws_Infested_Kerrigan,
  Needle_Spines,
  Needle_Spines_Hunter_Killer,
  Kaiser_Blades,
  Kaiser_Blades_Torrasque,
  Toxic_Spores,
  Spines,
  Acid_Spore,
  Acid_Spore_Kukulza,
  Glave_Wurm,
  Glave_Wurm_Kukulza,
  Seeker_Spores,
  Subterranean_Tentacle,
  Suicide_Infested_Terran,
  Suicide_Scourge,
  Particle_Beam,
  Psi_Blades,
  Psi_Blades_Fenix,
  Phase_Disruptor,
  Phase_Disruptor_Fenix,
  Psi_Assault,
  Psionic_Shockwave,
  Psionic_Shockwave_TZ_Archon,
  Dual_Photon_Blasters,
  Dual_Photon_Blasters_Mojo,
  Dual_Photon_Blasters_Artanis,
  Anti_Matter_Missiles,
  Anti_Matter_Missiles_Mojo,
  Anti_Matter_Missiles_Artanis,
  Phase_Disruptor_Cannon,
  Phase_Disruptor_Cannon_Danimoth,
  Pulse_Cannon,
  STS_Photon_Cannon,
  STA_Photon_Cannon,
  Scarab,
  Neutron_Flare,
  Halo_Rockets,
  Corrosive_Acid,
  Subterranean_Spines,
  Warp_Blades,
  Warp_Blades_Hero,
  Warp_Blades_Zeratul,
  Independant_Laser_Battery,
  Twin_Autocannons_Floor_Trap,
  Hellfire_Missile_Pack_Wall_Trap,
  Flame_Thrower_Wall_Trap,
  Hellfire_Missile_Pack_Floor_Trap,
  Yamato_Gun,
  Nuclear_Strike,
  Lockdown,
  EMP_Shockwave,
  Irradiate,
  Parasite,
  Spawn_Broodlings,
  Ensnare,
  Dark_Swarm,
  Plague,
  Consume,
  Stasis_Field,
  Psionic_Storm,
  Disruption_Web,
  Restoration,
  Mind_Control,
  Feedback,
  Optical_Flare,
  Maelstrom,
  None,
  Unknown)
}
