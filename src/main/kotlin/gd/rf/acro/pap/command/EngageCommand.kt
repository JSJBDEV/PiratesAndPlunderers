package gd.rf.acro.pap.command

import com.google.common.collect.ImmutableList
import com.mojang.brigadier.CommandDispatcher
import gd.rf.acro.pap.engagement.EngagementManager
import net.minecraft.command.arguments.EntityArgumentType
import net.minecraft.entity.Entity
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.KillCommand
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.TranslatableText

object EngageCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource?>) {
        dispatcher.register(
                CommandManager.literal("pap").then(CommandManager.literal("engage").requires { src ->
                    return@requires src.hasPermissionLevel(2);
                }.executes {ctx ->
                    return@executes execute(ctx.source as ServerCommandSource, ImmutableList.of((ctx.source as ServerCommandSource).entityOrThrow))
                }.then(CommandManager.argument("targets", EntityArgumentType.entities()).executes { ctx ->
                    return@executes execute(ctx.source, EntityArgumentType.getEntities(ctx, "targets"))
                }))
        )
    }

    private fun execute(source: ServerCommandSource, targets: Collection<Entity>): Int {
        val var2: Iterator<*> = targets.iterator()
        while (var2.hasNext()) {
            val entity = var2.next() as Entity
            EngagementManager.teleportEntityToPirateOcean(entity)
        }
        if (targets.size == 1) {
            source.sendFeedback(TranslatableText("pap.commands.dimtp.success.single", *arrayOf<Any>(targets.iterator().next().displayName)), true)
        } else {
            source.sendFeedback(TranslatableText("pap.commands.dimtp.success.multiple", *arrayOf<Any>(targets.size)), true)
        }
        return targets.size
    }
}