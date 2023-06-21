package com.etec_bot.cmd_handler

import com.etec_bot.ETECBot
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData

abstract class Cmd(
    val etecBot: ETECBot,
    val name: String,
    val description: String,
    val args: List<OptionData>?,
    val subcommands: LinkedHashMap<SubcommandData, List<OptionData>?>?
) {
    abstract fun execute(slashEvent: SlashCommandInteractionEvent): Unit
    abstract fun autoCompletion(autoCompeteEvent: CommandAutoCompleteInteractionEvent): Unit
}