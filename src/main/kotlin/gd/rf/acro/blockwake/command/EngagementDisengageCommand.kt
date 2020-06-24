package gd.rf.acro.blockwake.command

import com.mojang.brigadier.CommandDispatcher
import gd.rf.acro.blockwake.engagement.EngagementManager
import gd.rf.acro.blockwake.entities.SailingShipEntity
import net.minecraft.command.arguments.EntityArgumentType
import net.minecraft.entity.Entity
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralText
import java.io.File
import java.lang.Exception

object EngagementDisengageCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource?>) {
        dispatcher.register(
                CommandManager.literal("blockwake").then(CommandManager.literal("engage").then(CommandManager.literal("stop").then(
                        CommandManager.argument("entity", EntityArgumentType.entity())
                                .requires { src ->
                                    return@requires src.hasPermissionLevel(2);
                                }.executes {ctx ->
                                    return@executes execute(ctx.source, EntityArgumentType.getEntity(ctx, "entity"))
                                }))))
    }

    private fun execute(source: ServerCommandSource, e: Entity): Int {
        try {
            EngagementManager.finishEngagement(e)
        } catch (e: Exception) {
            source.sendError(LiteralText("The command failed! Is the target involved in an engagement?"))
            source.sendError(LiteralText(e.toString()))
            return 1
        }
        source.sendFeedback(LiteralText("The engagement has been ended"), false)
        return 0
    }
}