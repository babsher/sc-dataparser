package gmu

object Weapon {

  def fromName(name: String): WeaponType = {
    mapping.get(name) match {
      case Some(v) => v
      case scala.None => throw new IllegalArgumentException("Unknown type " + name)
    }
  }

  lazy val mapping = values.map(v => v.toString -> v).toMap

  sealed class WeaponType(val targetsAir: Boolean,
                          val targetsGround: Boolean) {
  }
  sealed class AirAndGround extends WeaponType(true, true)
  sealed class GroundWeapon extends WeaponType(false, true)
  sealed class AirWeapon extends WeaponType(false, true)

  case object Gauss_Rifle extends AirAndGround
  case object Gauss_Rifle_Jim_Raynor extends AirAndGround
  case object C_10_Canister_Rifle extends AirAndGround
  case object C_10_Canister_Rifle_Sarah_Kerrigan extends AirAndGround
  case object C_10_Canister_Rifle_Samir_Duran extends AirAndGround
  case object C_10_Canister_Rifle_Infested_Duran extends AirAndGround
  case object C_10_Canister_Rifle_Alexei_Stukov extends AirAndGround
  case object Fragmentation_Grenade extends GroundWeapon
  case object Fragmentation_Grenade_Jim_Raynor extends GroundWeapon
  case object Spider_Mines extends GroundWeapon
  case object Twin_Autocannons extends GroundWeapon
  case object Twin_Autocannons_Alan_Schezar extends GroundWeapon
  case object Hellfire_Missile_Pack extends AirWeapon
  case object Hellfire_Missile_Pack_Alan_Schezar extends AirWeapon
  case object Arclite_Cannon extends GroundWeapon
  case object Arclite_Cannon_Edmund_Duke extends GroundWeapon
  case object Fusion_Cutter extends GroundWeapon
  case object Gemini_Missiles extends AirWeapon
  case object Gemini_Missiles_Tom_Kazansky extends AirWeapon
  case object Burst_Lasers extends GroundWeapon
  case object Burst_Lasers_Tom_Kazansky extends GroundWeapon
  case object ATS_Laser_Battery extends GroundWeapon
  case object ATS_Laser_Battery_Hero extends GroundWeapon
  case object ATS_Laser_Battery_Hyperion extends GroundWeapon
  case object ATA_Laser_Battery extends AirWeapon
  case object ATA_Laser_Battery_Hero extends AirWeapon
  case object ATA_Laser_Battery_Hyperion extends AirWeapon
  case object Flame_Thrower extends GroundWeapon
  case object Flame_Thrower_Gui_Montag extends GroundWeapon
  case object Arclite_Shock_Cannon extends GroundWeapon
  case object Arclite_Shock_Cannon_Edmund_Duke extends GroundWeapon
  case object Longbolt_Missile extends AirWeapon
  case object Claws extends GroundWeapon
  case object Claws_Devouring_One extends GroundWeapon
  case object Claws_Infested_Kerrigan extends GroundWeapon
  case object Needle_Spines extends AirAndGround
  case object Needle_Spines_Hunter_Killer extends AirAndGround
  case object Kaiser_Blades extends GroundWeapon
  case object Kaiser_Blades_Torrasque extends GroundWeapon
  case object Toxic_Spores extends GroundWeapon
  case object Spines extends GroundWeapon
  case object Acid_Spore extends GroundWeapon
  case object Acid_Spore_Kukulza extends GroundWeapon
  case object Glave_Wurm extends AirAndGround
  case object Glave_Wurm_Kukulza extends AirAndGround
  case object Seeker_Spores extends AirWeapon
  case object Subterranean_Tentacle extends GroundWeapon
  case object Suicide_Infested_Terran extends GroundWeapon
  case object Suicide_Scourge extends AirWeapon
  case object Particle_Beam extends GroundWeapon
  case object Psi_Blades extends GroundWeapon
  case object Psi_Blades_Fenix extends GroundWeapon
  case object Phase_Disruptor extends AirAndGround
  case object Phase_Disruptor_Fenix extends AirAndGround
  case object Psi_Assault extends GroundWeapon
  case object Psionic_Shockwave extends AirAndGround
  case object Psionic_Shockwave_TZ_Archon extends AirAndGround
  case object Dual_Photon_Blasters extends GroundWeapon
  case object Dual_Photon_Blasters_Mojo extends GroundWeapon
  case object Dual_Photon_Blasters_Artanis extends GroundWeapon
  case object Anti_Matter_Missiles extends AirWeapon
  case object Anti_Matter_Missiles_Mojo extends AirWeapon
  case object Anti_Matter_Missiles_Artanis extends AirWeapon
  case object Phase_Disruptor_Cannon extends AirAndGround
  case object Phase_Disruptor_Cannon_Danimoth extends AirAndGround
  case object Pulse_Cannon extends AirAndGround
  case object STS_Photon_Cannon extends GroundWeapon
  case object STA_Photon_Cannon extends AirWeapon
  case object Scarab extends GroundWeapon
  case object Neutron_Flare extends AirWeapon
  case object Halo_Rockets extends AirWeapon
  case object Corrosive_Acid extends AirWeapon
  case object Subterranean_Spines extends GroundWeapon
  case object Warp_Blades extends GroundWeapon
  case object Warp_Blades_Hero extends GroundWeapon
  case object Warp_Blades_Zeratul extends GroundWeapon
  case object Independant_Laser_Battery extends AirWeapon
  case object Twin_Autocannons_Floor_Trap extends GroundWeapon
  case object Hellfire_Missile_Pack_Wall_Trap extends GroundWeapon
  case object Flame_Thrower_Wall_Trap extends GroundWeapon
  case object Hellfire_Missile_Pack_Floor_Trap extends GroundWeapon
  case object Yamato_Gun extends AirAndGround
  case object Nuclear_Strike extends AirAndGround
  case object Lockdown extends AirAndGround
  case object EMP_Shockwave extends AirAndGround
  case object Irradiate extends AirAndGround
  case object Parasite extends AirAndGround
  case object Spawn_Broodlings extends GroundWeapon
  case object Ensnare extends AirAndGround
  case object Dark_Swarm extends AirAndGround
  case object Plague extends AirAndGround
  case object Consume extends AirAndGround
  case object Stasis_Field extends AirAndGround
  case object Psionic_Storm extends AirAndGround
  case object Disruption_Web extends GroundWeapon
  case object Restoration extends AirAndGround
  case object Mind_Control extends AirAndGround
  case object Feedback extends AirAndGround
  case object Optical_Flare extends GroundWeapon
  case object Maelstrom extends AirAndGround
  case object None extends WeaponType(false, false)
  case object Unknown extends WeaponType(false, false)

  val values: Seq[WeaponType] = Seq(
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
