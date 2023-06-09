package com.etec_bot.cmd_handler.cmds

import com.etec_bot.ETECBot
import com.etec_bot.cmd_handler.Command
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class Help(etecBot: ETECBot): Command(etecBot, "help", "Obtiene información sobre el funcionamiento del bot.", null) {
    override fun execute(slashEvent: SlashCommandInteractionEvent) {
        slashEvent.deferReply().queue()
        slashEvent.hook.sendMessage("El comando `/help` estará pronto disponible.").setEphemeral(true).queue()
    }
}