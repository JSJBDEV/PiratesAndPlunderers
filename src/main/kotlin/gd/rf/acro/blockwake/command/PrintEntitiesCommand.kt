package gd.rf.acro.blockwake.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import gd.rf.acro.blockwake.Blockwake
import gd.rf.acro.blockwake.engagement.EngagementManager
import net.minecraft.command.arguments.EntityArgumentType
import net.minecraft.entity.Entity
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.TranslatableText
import java.io.File

object PrintEntitiesCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource?>) {
        dispatcher.register(
                CommandManager.literal("printentities")
                        .requires { src ->
                            return@requires src.hasPermissionLevel(2);
                        }.executes {ctx ->
                    return@executes execute(ctx.source)
                })


    }

    private fun execute(source: ServerCommandSource): Int {
        val f = File("blockwake_debug_entitylist.txt")
        f.appendText("\nCommand Executed by ${source.name} in ${source.world}\n[x, y, z] EntityName EntityAge")
        for(e in source.world.iterateEntities()) {
            f.appendText("[${e.x.toInt()}, ${e.y.toInt()}, ${e.z.toInt()}] ${e.type} ${e.age}\n")
        }
        return 0;
    }
}