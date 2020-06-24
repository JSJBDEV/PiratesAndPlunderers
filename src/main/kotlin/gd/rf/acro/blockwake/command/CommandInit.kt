package gd.rf.acro.blockwake.command

import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.ServerCommandSource


object CommandInit {
    fun registerCommands() {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher: CommandDispatcher<ServerCommandSource?>?, dedicated: Boolean ->
            EngageStartCommand.register(dispatcher!!)
            EngageWorldManageCommand.register(dispatcher)
            PrintEntitiesCommand.register(dispatcher)

            EngagementDisengageCommand.register(dispatcher)
            EngagementDisengageForceCommand.register(dispatcher)
        })
    }
}