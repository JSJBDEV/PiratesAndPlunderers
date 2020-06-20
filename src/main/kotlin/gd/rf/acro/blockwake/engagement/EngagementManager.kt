package gd.rf.acro.blockwake.engagement

import gd.rf.acro.blockwake.Blockwake
import gd.rf.acro.blockwake.Blockwake.logger
import gd.rf.acro.blockwake.Blockwake.config
import gd.rf.acro.blockwake.dimension.PirateOceanChunkGenerator
import gd.rf.acro.blockwake.dimension.PirateOceanPlacementHandler
import gd.rf.acro.blockwake.entities.PirateEntity
import gd.rf.acro.blockwake.entities.SailingShipEntity
import gd.rf.acro.blockwake.lib.VectorHelper
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.LiteralText
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockPos
import java.util.*
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
        val oceanWorld = serverWorld.server.getWorld(Blockwake.PIRATE_OCEAN_WORLD)
        if (oceanWorld == null) {
            if (entity is PlayerEntity) {
                entity.sendMessage(LiteralText("Failed to find pirate ocean world, was it registered?"), false)
            }
            logger.error("Failed to find PIRATE_OCEAN_WORLD, was it registered?")
            return false
        }
        FabricDimensions.teleport(entity, oceanWorld, PirateOceanPlacementHandler.enter(pos ?: BlockPos(0, 64, 0)))
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

        val initialAttackerEntityPos = BlockPos(attacker.blockPos.x, 64, attacker.blockPos.z)
        val initialDefenderEntityPos = BlockPos(defender.blockPos.x, 64, defender.blockPos.z)

        val midPosition = VectorHelper.getMidpoint(initialAttackerEntityPos, initialDefenderEntityPos)
        var attackerOffset = VectorHelper.subtract(midPosition, attacker.blockPos)
        var defenderOffset = VectorHelper.subtract(midPosition, defender.blockPos)

        val offsetNewMagnitude: Int

        if (VectorHelper.findMagnitude(attackerOffset) > config.DimensionMaxSpacing)
            offsetNewMagnitude = config.DimensionMaxSpacing
        else if (VectorHelper.findMagnitude(attackerOffset) < config.DimensionMinSpacing)
            offsetNewMagnitude = config.DimensionMinSpacing
        else
            offsetNewMagnitude = VectorHelper.findMagnitude(attackerOffset).toInt()

        attackerOffset = VectorHelper.multiplyVector(VectorHelper.getUnitVector(attackerOffset), offsetNewMagnitude.toDouble());
        defenderOffset = VectorHelper.multiplyVector(VectorHelper.getUnitVector(defenderOffset), offsetNewMagnitude.toDouble());

        val serverWorld: ServerWorld

        try {
            serverWorld = attackerEntities[0].entityWorld as ServerWorld
        } catch (e: IndexOutOfBoundsException) {
            throw ShipHasNoEntitiesException()
        }
        val oceanWorld = serverWorld.server.getWorld(Blockwake.PIRATE_OCEAN_WORLD)!!

        val attackerBlocks = rotateBlockModel90Clockwise(attacker.modelBlocksForEntity.toTypedArray())
        val defenderBlocks = rotateBlockModel90Clockwise(rotateBlockModel180(defender.modelBlocksForEntity.toTypedArray()))

        val engagementPositionBase = BlockPos(0, 64, 0)

        val initialAttackerPosition = engagementPositionBase.add(attackerOffset)
        val initialDefenderPosition = engagementPositionBase.add(defenderOffset)

        for (blk in attackerBlocks) {
            oceanWorld.setBlockState(initialAttackerPosition.add(blk.second), blk.first, 3)
        }

        for (blk in defenderBlocks) {
            oceanWorld.setBlockState(initialDefenderPosition.add(blk.second), blk.first, 3)
        }

        //TODO: Use entity placers

        for ((entities, pos) in arrayOf(Pair(attackerEntities, initialAttackerPosition), Pair(defenderEntities, initialDefenderPosition))) {
            for (e in entities) {
                if (e is PlayerEntity || e is PirateEntity)
                    FabricDimensions.teleport(e, oceanWorld, PirateOceanPlacementHandler.enter(pos.add(0, 3, 0)));
            }
        }
    }

    private fun rotateBlockModel90Clockwise(blocks: Array<gd.rf.acro.blockwake.lib.Pair<BlockState, BlockPos>>): Array<gd.rf.acro.blockwake.lib.Pair<BlockState, BlockPos>> {
        val newBlocks = arrayOfNulls<gd.rf.acro.blockwake.lib.Pair<BlockState, BlockPos>>(blocks.size)
        for(index in blocks.indices) {
            val pos = blocks[index].second
            val z = -1*pos.x
            val x = pos.z
            val state = blocks[index].first
            state.rotate(BlockRotation.CLOCKWISE_90)
            newBlocks[index] = gd.rf.acro.blockwake.lib.Pair(state, BlockPos(x, pos.y, z))
        }
        return newBlocks as Array<gd.rf.acro.blockwake.lib.Pair<BlockState, BlockPos>>
    }

    private fun rotateBlockModel180(blocks: Array<gd.rf.acro.blockwake.lib.Pair<BlockState, BlockPos>>): Array<gd.rf.acro.blockwake.lib.Pair<BlockState, BlockPos>> {
        return rotateBlockModel90Clockwise(rotateBlockModel90Clockwise(blocks))
    }

    private fun canShipsEngage(attacker: SailingShipEntity, defender: SailingShipEntity): Boolean {
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