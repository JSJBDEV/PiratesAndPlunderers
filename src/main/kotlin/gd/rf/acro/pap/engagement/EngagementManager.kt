package gd.rf.acro.pap.engagement

import com.google.common.io.Files
import gd.rf.acro.pap.PiratesAndPlunderers
import gd.rf.acro.pap.PiratesAndPlunderers.logger
import gd.rf.acro.pap.PiratesAndPlunderers.config
import gd.rf.acro.pap.dimension.PirateOceanChunkGenerator
import gd.rf.acro.pap.dimension.VoidPlacementHandler.enter
import gd.rf.acro.pap.entities.SailingShipEntity
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerChunkManager
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.LiteralText
import net.minecraft.util.math.BlockPos
import kotlin.math.max
import kotlin.math.sqrt

typealias EngagementLocation = Pair<Int, Int>


object EngagementManager {

    private val CurrentEngagements: List<EngagementLocation> = emptyList()

    private fun getEngagementLocationBlockRange(loc: EngagementLocation): Pair<BlockPos, BlockPos> {
        val bp1 = BlockPos(loc.first* config.DimensionEngagementSpacingBlocks, 256, loc.second* config.DimensionEngagementSpacingBlocks)
        val bp2 = BlockPos((loc.first+1)* config.DimensionEngagementSpacingBlocks, 256, (loc.second+1)* config.DimensionEngagementSpacingBlocks)
        return Pair(bp1, bp2)
    }

    private fun setupNewEngagementLocation(): EngagementLocation {
        val limit = config.DimensionEngagmentLimit
        val width = sqrt(limit.toFloat()).toInt()
        val height = max(limit-(width*width), 1)

        for (i in 0..width) {
            for (j in 0..height) {

            }
        }
        return Pair(0, 0)
    }

    fun resetEngagementLocation(world: ServerWorld, loc: EngagementLocation) {
        val area = getEngagementLocationBlockRange(loc)
        val column = PirateOceanChunkGenerator.getFilledBlockStates();

        for (i in area.first.x..area.second.x) {
            for (j in area.first.z..area.second.z) {
                for (k in area.first.y..area.second.y) {
                    world.setBlockState(BlockPos(i, j, k), column[k]);
                }
            }
        }
    }


    fun teleportEntityToPirateOcean(entity: Entity, pos: BlockPos? = null): Boolean {
        val serverWorld = entity.entityWorld as ServerWorld
        val oceanWorld = serverWorld.server.getWorld(PiratesAndPlunderers.PIRATE_OCEAN_WORLD)
        if (oceanWorld == null) {
            if (entity is PlayerEntity) {
                entity.sendMessage(LiteralText("Failed to find pirate ocean world, was it registered?"), false)
            }
            logger.error("Failed to find PIRATE_OCEAN_WORLD, was it registered?")
            return false
        }
        FabricDimensions.teleport(entity, oceanWorld, enter(pos ?: BlockPos(0, 64, 0)))
        return true
    }

    fun startEngagementWithShipEntities(attacker: SailingShipEntity, defender: SailingShipEntity) {
        logger.info("$attacker is attacking $defender!")
        if (!canShipsEngage(attacker, defender))
            throw ShipIsAlreadyEngagedException()

        attacker.setEngagedWithShipEntity(defender)
        defender.setEngagedWithShipEntity(attacker)

        val attackerEntities = attacker.passengerList
        val defenderEntities = defender.passengerList

        val serverWorld: ServerWorld;

        try {
            serverWorld = attackerEntities[0].entityWorld as ServerWorld
        } catch (e: IndexOutOfBoundsException) {
            throw ShipHasNoEntitiesException()
        }
        val oceanWorld = serverWorld.server.getWorld(PiratesAndPlunderers.PIRATE_OCEAN_WORLD)!!


        val attackerBlocks = attacker.modelBlocksForEntity;
        val defenderBlocks = defender.modelBlocksForEntity;

        val initialPosition = BlockPos(0, 64, 0)

        for (blk in attackerBlocks) {
            oceanWorld.setBlockState(initialPosition.add(blk.right), blk.left, 3)
            logger.info("PLACE BLOCK AT ${initialPosition.add(blk.right)}")
        }
    }

    fun canShipsEngage(attacker: SailingShipEntity, defender: SailingShipEntity): Boolean {
        return !attacker.isEngaged and !defender.isEngaged
    }

    private fun addBlockPos(block1: BlockPos, vararg positions: BlockPos): BlockPos {
        var p1 = block1;
        for (pos in positions) {
            p1 = p1.add(pos);
        }
        return p1;
    }

}