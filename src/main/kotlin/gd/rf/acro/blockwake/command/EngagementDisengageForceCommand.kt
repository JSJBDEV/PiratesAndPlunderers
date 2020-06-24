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

object EngagementDisengageForceCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource?>) {
        dispatcher.register(
                CommandManager.literal("blockwake").then(CommandManager.literal("force").then(CommandManager.literal("disengage").then(
                        CommandManager.argument("entity", EntityArgumentType.entity())
                                .requires { src ->
                                    return@requires src.hasPermissionLevel(2);
                                }.executes {ctx ->
                                    return@executes execute(ctx.source, EntityArgumentType.getEntity(ctx, "entity"))
                                }))))
    }

    private fun execute(source: ServerCommandSource, e: Entity): Int {
        if (e !is SailingShipEntity) {
            source.sendError(LiteralText("Target is not a sailing ship entity!"))
            return 1;
        }
        e.setDisengaged();
        source.sendFeedback(LiteralText("The entity $e was forcibly set to a disengaged state!"), true)
        return 0;
    }
}