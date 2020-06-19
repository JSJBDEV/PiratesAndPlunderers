package gd.rf.acro.blockwake.engagement

import java.lang.Exception

open class EngagementException(msg: String) : Exception(msg)

class ShipIsAlreadyEngagedException : EngagementException("Cannot engage a ship if it is already engaged with another ship!")

class NoCurrentEngagementException : EngagementException("Cannot disengage from battle when no ship is currently engaged with!")

class ShipHasNoEntitiesException : EngagementException("A ship with no entities cannot engage with another ship")