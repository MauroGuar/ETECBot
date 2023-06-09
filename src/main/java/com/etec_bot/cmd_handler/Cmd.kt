package com.etec_bot.cmd_handler

import com.etec_bot.ETECBot
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.OptionData

abstract class Cmd(
    val etecBot: ETECBot,
    val name: String,
    val description: String,
    val args: List<OptionData>?
) {
    abstract fun execute(slashEvent: SlashCommandInteractionEvent): Unit
}