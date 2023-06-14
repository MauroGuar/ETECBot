package com.etec_bot.cmd_handler.cmds

import com.etec_bot.ETECBot
import com.etec_bot.cmd_handler.Cmd
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class Help(etecBot: ETECBot): Cmd(etecBot, "help", "Obtiene información sobre el funcionamiento del bot.", args = null) {
    override fun execute(slashEvent: SlashCommandInteractionEvent) {
        slashEvent.deferReply().setEphemeral(true).queue()
        slashEvent.hook.sendMessage("El comando `/help` estará pronto disponible.").queue()
    }
}