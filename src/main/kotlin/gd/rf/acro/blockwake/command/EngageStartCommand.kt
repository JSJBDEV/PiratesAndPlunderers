package gd.rf.acro.blockwake.command

import com.google.common.collect.ImmutableList
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import gd.rf.acro.blockwake.Blockwake
import gd.rf.acro.blockwake.engagement.EngagementManager
import gd.rf.acro.blockwake.engagement.ShipHasNoEntitiesException
import gd.rf.acro.blockwake.engagement.ShipIsAlreadyEngagedException
import gd.rf.acro.blockwake.entities.SailingShipEntity
import net.minecraft.command.arguments.EntityArgumentType
import net.minecraft.entity.Entity
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.KillCommand
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.TranslatableText

object EngageStartCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource?>) {
        dispatcher.register(
                CommandManager.literal("blockwake").then(CommandManager.literal("engage").then(CommandManager.literal("teleport").requires { src ->
                    return@requires src.hasPermissionLevel(2);
                }.executes {ctx ->
                    return@executes executeTeleport(ctx.source as ServerCommandSource, ImmutableList.of((ctx.source as ServerCommandSource).entityOrThrow))
                }.then(CommandManager.argument("targets", EntityArgumentType.entities()).executes { ctx ->
                    return@executes executeTeleport(ctx.source, EntityArgumentType.getEntities(ctx, "targets"))
                })))
        )

        dispatcher.register(
        CommandManager.literal("blockwake")
        .then(CommandManager.literal("engage")
        .then(CommandManager.literal("start")
        .requires { src ->
            return@requires src.hasPermissionLevel(2);
        }.then(CommandManager.argument("attacker", EntityArgumentType.entity())
        .then(CommandManager.argument("defender", EntityArgumentType.entity())
        .executes {ctx ->
            return@executes executeEngagementStart(ctx.source, EntityArgumentType.getEntity(ctx, "attacker"), EntityArgumentType.getEntity(ctx, "defender"))
        })))))
    }

    private fun executeEngagementStart(source: ServerCommandSource, attacker: Entity, defender: Entity): Int {
        if ((attacker !is SailingShipEntity) or (defender !is SailingShipEntity)) {
            source.sendFeedback(TranslatableText("blockwake.commands.engagement.nonBoatEntityError"), true)
            return 1;
        }

        try {
            EngagementManager.startEngagementWithShipEntities(attacker as SailingShipEntity, defender as SailingShipEntity)
            source.sendFeedback(TranslatableText("blockwake.commands.engagement.engagementBeginSuccess"), true)
            return 0;
        } catch (e: ShipHasNoEntitiesException) {
            source.sendFeedback(TranslatableText("blockwake.commands.error.ShipHasNoEntitiesException"), true)
        } catch (e: ShipIsAlreadyEngagedException) {
            source.sendFeedback(TranslatableText("blockwake.commands.error.ShipIsAlreadyEngagedException"), true)
        } catch (e: Exception) {
            source.sendFeedback(TranslatableText("blockwake.commands.error.GenericException %s", e.message), true)
        }
        return 1;
    }

    private fun executeTeleport(source: ServerCommandSource, targets: Collection<Entity>): Int {
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