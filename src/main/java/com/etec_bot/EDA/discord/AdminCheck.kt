package com.etec_bot.EDA.discord

import com.etec_bot.EDA.schemes.SchoolGrade
import com.etec_bot.ETECBot
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageReaction
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.io.File

class AdminCheck(etecBot: ETECBot) : ListenerAdapter() {
    private val eda = etecBot.eda
    private val ADMIN_CHANNEL_ID = "1151925150535655504"
    private var fileExists: Boolean? = false
    private var fileToCheckName: String? = null
    private var fileToCheckExtension: String? = null
    private val filePath = "src/main/resources/temp/"
    private val checkEmoji = Emoji.fromCustom("checkmark", 1118298915687641098, false)
    private val crossEmoji = Emoji.fromCustom("Cross", 1175140265904836769, false)
    override fun onMessageReceived(event: MessageReceivedEvent) {
        val channel = event.channel
        if (channel.id != ADMIN_CHANNEL_ID) return
        if (!event.author.isBot) return
        val message = event.message

        val messageWithFile = channel.history?.retrievePast(1)?.complete()?.last()
        if (messageWithFile?.attachments?.isEmpty() == true) return
        val attachment = messageWithFile?.attachments?.last()
        fileToCheckName = attachment?.fileName?.let { removeExtension(it) }
        fileToCheckExtension = attachment?.fileExtension
        fileExists = attachment?.proxy?.downloadToFile(File("${filePath}${fileToCheckName}.${fileToCheckExtension}"))?.complete(File("${filePath}${fileToCheckName}.${fileToCheckExtension}"))
        addReactions(message)
    }

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        if (event.user?.isBot == true) return
        if (event.channel.id != ADMIN_CHANNEL_ID) return
        val user = event.user
        val message = event.channel.retrieveMessageById(event.messageId).complete()
        val reaction = event.reaction
        val checkReaction = checkReaction(reaction)
        if (checkReaction == false) {
            message.delete().queue()
        } else if (checkReaction == null) {
            user?.let { reaction.removeReaction(it).queue() }
        } else {
            val messageContent = message.contentStripped
            val (name, gradeStr, subjectStr) = getMessageValues(messageContent)
            val schoolGrade = SchoolGrade.entries.find { it.value == gradeStr }
            val grade = eda.grades.find { it.schoolGradeEnum == schoolGrade }
            val subject = grade?.subjects?.find { it.name == subjectStr }
            if (fileExists == true) {
                val fileToUpload = getFile("${filePath}${fileToCheckName}.${fileToCheckExtension}")
                fileToUpload?.let { subject?.uploadFile(it) }
            }
            user?.openPrivateChannel()?.queue { privateChannel ->
                privateChannel.sendMessage("Archivo subido con éxito.").queue()
            }
            message.delete().queue()
        }
    }

    private fun addReactions(message: Message) {
        message.addReaction(checkEmoji).queue()
        message.addReaction(crossEmoji).queue()
    }

    private fun checkReaction(reaction: MessageReaction): Boolean? {
        val emoji = reaction.emoji
        if (emoji == checkEmoji) {
            return true
        } else if (emoji == crossEmoji) {
            return false
        }
        return null
    }

    private fun getMessageValues(messageContent: String): Triple<String, String, String> {
        val regex = Regex("Alumno: (.*)\\nCurso: (\\S+ \\S+)\\nMateria: (\\w+)")
        val matchResult = regex.find(messageContent)
        val (name, grade, subject) = matchResult!!.destructured
        return Triple(name, grade, subject)
    }

    private fun removeExtension(fileName: String): String {
        val dotIndex = fileName.lastIndexOf('.')
        return if (dotIndex > 0) {
            fileName.substring(0, dotIndex)
        } else {
            fileName
        }
    }

    private fun getFile(filePath: String): File? {
        val file = File(filePath)
        return if (file.exists()) file else null
    }
}
