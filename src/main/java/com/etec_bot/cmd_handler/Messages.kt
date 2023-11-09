package com.etec_bot.cmd_handler

import com.etec_bot.ETECBot
import net.dv8tion.jda.api.entities.GuildWelcomeScreen.Channel
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.utils.FileUpload
import java.io.File

class Messages(etecBot: ETECBot) : Cmd(etecBot, "get-messages", "Get all messages.", null, null) {
    override fun execute(slashEvent: SlashCommandInteractionEvent) {
        val guild = slashEvent.guild
        val channel = slashEvent.channel.asTextChannel()
        val channelToSend = guild?.getTextChannelById("1151925150535655504")
        /*val messageHistory = channel.history?.retrievePast(20)?.complete()
        val messageWithAttachment = messageHistory?.last {
            it.attachments.isNotEmpty()
        }*/
        val messageWithAttachment = channel.history?.retrievePast(1)?.complete()?.firstOrNull {
            it.attachments.isNotEmpty()
        }
        val attachment = messageWithAttachment?.attachments?.last()
        val fileToSend = attachment?.proxy?.download()?.get()
        if (attachment != null) {
            channelToSend?.sendMessage("Archivos de ${channel.name}")
                ?.addFiles(fileToSend?.let { FileUpload.fromData(it, attachment.fileName) })?.queue()
        }
        slashEvent.reply("Archivos enviados.").queue()

    }
    override fun autoCompletion(autoCompeteEvent: CommandAutoCompleteInteractionEvent) {}
}