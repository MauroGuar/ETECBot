package com.etec_bot.cmd_handler.cmds

import com.etec_bot.ETECBot
import com.etec_bot.cmd_handler.Cmd
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData

class Help(etecBot: ETECBot) : Cmd(
    etecBot,
    "ayuda",
    "Obtiene información sobre el funcionamiento del bot.",
    args = null,
    linkedMapOf(SubcommandData("tickets", "Ayuda con el funcionamiento de los tickets de soporte.") to null)
) {
    override fun execute(slashEvent: SlashCommandInteractionEvent) {

        when (slashEvent.subcommandName) {
            "tickets" -> {
                slashEvent.reply("Tickets").setEphemeral(true).queue()
            }
            else -> slashEvent.reply("El comando `/ayuda` estará pronto disponible.").setEphemeral(true).queue()
        }

    }

    override fun autoCompletion(autoCompeteEvent: CommandAutoCompleteInteractionEvent) {}
}