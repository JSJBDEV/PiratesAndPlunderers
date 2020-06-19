package gd.rf.acro.blockwake.dimension

import net.fabricmc.fabric.api.dimension.v1.EntityPlacer
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.pattern.BlockPattern.TeleportTarget
import net.minecraft.entity.Entity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.Heightmap
import net.minecraft.world.World

@SuppressWarnings("deprecation")
object VoidPlacementHandler {
    fun enter(portalPos: BlockPos): EntityPlacer {
        return EntityPlacer { entity: Entity, destination: ServerWorld, direction: Direction?, v: Double, v1: Double ->
            val pos = enterOcean(entity, destination, portalPos)
            TeleportTarget(Vec3d.of(pos).add(0.5, 0.0, 0.5), Vec3d.ZERO, 0)
        }
    }

    fun leave(portalPos: BlockPos): EntityPlacer {
        return EntityPlacer { entity: Entity, destination: ServerWorld, direction: Direction?, v: Double, v1: Double ->
            val pos = leaveOcean(entity, destination, portalPos)
            TeleportTarget(Vec3d.of(pos).add(0.5, 0.0, 0.5), Vec3d.ZERO, 0)
        }
    }

    private fun enterOcean(entity: Entity, newWorld: ServerWorld, portalPos: BlockPos): BlockPos {
        val spawnPos = BlockPos(portalPos.x, 100, portalPos.z)
        return spawnPos
    }

    private fun leaveOcean(entity: Entity, newWorld: ServerWorld, portalPos: BlockPos): BlockPos {
        return newWorld.getTopPosition(Heightmap.Type.MOTION_BLOCKING, portalPos).up()
    }

}