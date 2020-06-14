package gd.rf.acro.pap.engagement

import gd.rf.acro.pap.PiratesAndPlunderers
import gd.rf.acro.pap.dimension.VoidPlacementHandler.enter
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions
import net.minecraft.command.arguments.BlockPosArgumentType.blockPos
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.LiteralText
import gd.rf.acro.pap.PiratesAndPlunderers.logger;
import net.minecraft.util.math.BlockPos


object EngagementManager {

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

}