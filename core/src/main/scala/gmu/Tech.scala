package gmu

object Tech {

  def fromName(name: String): TechType = {
    values.filter(_.toString == name).head
  }

  sealed trait TechType
  case object Stim_Packs extends TechType
  case object Lockdown extends TechType
  case object EMP_Shockwave extends TechType
  case object Spider_Mines extends TechType
  case object Scanner_Sweep extends TechType
  case object Tank_Siege_Mode extends TechType
  case object Defensive_Matrix extends TechType
  case object Irradiate extends TechType
  case object Yamato_Gun extends TechType
  case object Cloaking_Field extends TechType
  case object Personnel_Cloaking extends TechType
  case object Burrowing extends TechType
  case object Infestation extends TechType
  case object Spawn_Broodlings extends TechType
  case object Dark_Swarm extends TechType
  case object Plague extends TechType
  case object Consume extends TechType
  case object Ensnare extends TechType
  case object Parasite extends TechType
  case object Psionic_Storm extends TechType
  case object Hallucination extends TechType
  case object Recall extends TechType
  case object Stasis_Field extends TechType
  case object Archon_Warp extends TechType
  case object Restoration extends TechType
  case object Disruption_Web extends TechType
  case object Mind_Control extends TechType
  case object Dark_Archon_Meld extends TechType
  case object Feedback extends TechType
  case object Optical_Flare extends TechType
  case object Maelstrom extends TechType
  case object Lurker_Aspect extends TechType
  case object Healing extends TechType
  case object None extends TechType
  case object Unknown extends TechType
  case object Nuclear_Strike extends TechType

  val values = List(
    Stim_Packs,
    Lockdown,
    EMP_Shockwave,
    Spider_Mines,
    Scanner_Sweep,
    Tank_Siege_Mode,
    Defensive_Matrix,
    Irradiate,
    Yamato_Gun,
    Cloaking_Field,
    Personnel_Cloaking,
    Burrowing,
    Infestation,
    Spawn_Broodlings,
    Dark_Swarm,
    Plague,
    Consume,
    Ensnare,
    Parasite,
    Psionic_Storm,
    Hallucination,
    Recall,
    Stasis_Field,
    Archon_Warp,
    Restoration,
    Disruption_Web,
    Mind_Control,
    Dark_Archon_Meld,
    Feedback,
    Optical_Flare,
    Maelstrom,
    Lurker_Aspect,
    Healing,
    None,
    Unknown,
    Nuclear_Strike
  )
}
