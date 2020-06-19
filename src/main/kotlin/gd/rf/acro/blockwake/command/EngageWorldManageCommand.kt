package gd.rf.acro.blockwake.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import gd.rf.acro.blockwake.Blockwake.config
import gd.rf.acro.blockwake.engagement.EngagementManager
import net.minecraft.entity.Entity
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.TranslatableText

object EngageWorldManageCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource?>) {
        dispatcher.register(
                CommandManager.literal("blockwake")
                .then(CommandManager.literal("engage")
                .then(CommandManager.literal("manage")
                .requires { src ->
                    return@requires src.hasPermissionLevel(2);
                }.then(CommandManager.literal("resetarea")
                .executes {ctx ->
                    return@executes executeAreaReset(ctx.source, (ctx.source as ServerCommandSource).entityOrThrow)
                }.then(CommandManager.argument("chunkX", IntegerArgumentType.integer())
                .then(CommandManager.argument("chunkY", IntegerArgumentType.integer())
                .executes {ctx ->
                      return@executes executeAreaReset(ctx.source, IntegerArgumentType.getInteger(ctx, "chunkX"), IntegerArgumentType.getInteger(ctx, "chunkY"))
                }
        ))))))

    }

    private fun executeAreaReset(source: ServerCommandSource, target: Entity): Int {
        return executeAreaReset(source, (target.x/config.DimensionEngagementSpacingBlocks).toInt(), (target.z/config.DimensionEngagementSpacingBlocks).toInt())
    }

    private fun executeAreaReset(source: ServerCommandSource, locX: Int, locZ: Int): Int {
        EngagementManager.resetEngagementLocation(source.world, Pair(locX, locZ))
        return 1;
    }

    private fun execute(source: ServerCommandSource, targets: Collection<Entity>): Int {
        val var2: Iterator<*> = targets.iterator()
        while (var2.hasNext()) {
            val entity = var2.next() as Entity
            EngagementManager.teleportEntityToPirateOcean(entity)
        }
        if (targets.size == 1) {
            source.sendFeedback(TranslatableText("blockwake.commands.dimtp.success.single", *arrayOf<Any>(targets.iterator().next().displayName)), true)
        } else {
            source.sendFeedback(TranslatableText("blockwake.commands.dimtp.success.multiple", *arrayOf<Any>(targets.size)), true)
        }
        return targets.size
    }
}