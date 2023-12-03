package com.etec_bot.cmd_handler.cmds

import com.etec_bot.ETECBot
import com.etec_bot.cmd_handler.Cmd
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button

class GenerateButton(etecBot: ETECBot) : Cmd(etecBot, "gen-btn", "Genera un botón.", null, null) {
    override fun execute(slashEvent: SlashCommandInteractionEvent) {
        slashEvent.reply("").addActionRow(
           Button.primary("submit", "Submit")
        ).queue()
    }

    override fun autoCompletion(autoCompleteEvent: CommandAutoCompleteInteractionEvent) {}
}