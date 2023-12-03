package com.etec_bot.EDA.discord

import com.etec_bot.EDA.EDA
import com.etec_bot.EDA.schemes.SchoolGrade
import com.etec_bot.ETECBot
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.Event
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal
import net.dv8tion.jda.api.utils.FileUpload

class Form(etecBot: ETECBot) : ListenerAdapter() {
    private val FORM_CHANNEL_ID = "1172165721334227066"
    private val CHECK_CHANNEL_ID = "1151925150535655504"
    private var canSendFile = false
    private lateinit var user_name: String
    private lateinit var user_last_name: String
    private lateinit var grade: String
    private lateinit var subject: String
    private lateinit var fileToCheck: FileUpload
    private val eda: EDA = etecBot.eda
    private fun startForm(guild: Guild, user: User, event: Event, formProcessPart: Int = 1) {
        val channel = guild.getTextChannelById(FORM_CHANNEL_ID)
        if (user.isBot) return
        if (formProcessPart == 1) {
            val subjectModal = createSubjectModal()
            val buttonInteraction = event as ButtonInteractionEvent
            buttonInteraction.replyModal(subjectModal).queue()
        } else if (formProcessPart == 2) {
            val modalInteraction = event as ModalInteractionEvent
            val schoolGrades = SchoolGrade.entries
            val selectGrade = StringSelectMenu.create("select-grade").apply {
                schoolGrades.forEach {
                    addOption(it.value, it.value)
                }
            }.build()
            modalInteraction.reply("Selecciona el año y división").setEphemeral(true).addActionRow(selectGrade).queue()
        } else if (formProcessPart == 3) {
            val stringSelectInteraction = event as StringSelectInteractionEvent
            val schoolGrade = SchoolGrade.entries.find { it.value == grade }
            val grade = eda.grades.find { it.schoolGradeEnum == schoolGrade }
            val subjects = grade?.subjects?.map { it.name }
            val selectSubject = StringSelectMenu.create("select-subject").apply {
                subjects?.forEach {
                    addOption(it, it)
                }
            }.build()
            stringSelectInteraction.reply("Selecciona la materia").setEphemeral(true).addActionRow(selectSubject).queue()
        } else {
            val stringSelectInteraction = event as StringSelectInteractionEvent
            stringSelectInteraction.reply("Ahora sube el archivo").setEphemeral(true).queue()
            canSendFile = true
        }
    }

    fun sendForm(guild: Guild) {
        val check_channel = guild.getTextChannelById(CHECK_CHANNEL_ID)
        if (user_name == "None" || user_last_name == "None") return
        check_channel?.sendMessage("**Alumno**: ${user_name} ${user_last_name}\n**Curso**: ${grade}\n**Materia**: ${subject}")
            ?.addFiles(fileToCheck)?.queue()
    }

    private fun createSubjectModal(): Modal {
        val name = TextInput.create("name-input", "Nombre", TextInputStyle.SHORT).apply {
            placeholder = "Nombre"
            minLength = 3
            maxLength = 24
        }.build()
        val last_name = TextInput.create("last-name-input", "Apellido", TextInputStyle.SHORT).apply {
            placeholder = "Apellido"
            minLength = 3
            maxLength = 24
        }.build()
        val subjectModal = Modal.create("subject-modal", "Nombre y Apellido")
            .addComponents(ActionRow.of(name), ActionRow.of(last_name)).build()
        return subjectModal
    }

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.componentId == "submit") {
            startForm(event.guild!!, event.user, event)
        }
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if (event.modalId == "subject-modal") {
            user_name = event.getValue("name-input")?.asString?.trim() ?: "None"
            user_last_name = event.getValue("last-name-input")?.asString?.trim() ?: "None"
            startForm(event.guild!!, event.user, event, 2)
        }
    }

    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        when (event.componentId) {
            "select-grade" -> {
                grade = event.values[0]
                startForm(event.guild!!, event.user, event, 3)
            }
            "select-subject" -> {
                subject = event.values[0]
                startForm(event.guild!!, event.user, event, 4)
            }
        }
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val channel = event.channel
        if (channel.id != FORM_CHANNEL_ID) return
        val user = event.author
        val message = event.message
        if (user.isBot) return
        if (!canSendFile) {
            message.delete().queue()
            user.openPrivateChannel().queue { privateChannel ->
                privateChannel.sendMessage("Debes completar el formulario para poder enviar el archivo.").queue()
            }
        } else {
            if (message.attachments.isEmpty()) {
                message.delete().queue()
                user.openPrivateChannel().queue { privateChannel ->
                    privateChannel.sendMessage("Debes subir un archivo.").queue()
                }
            } else {
                val messageWithFile = channel.history?.retrievePast(1)?.complete()?.last()
                val attachment = messageWithFile?.attachments?.last()
                val fileToSend = attachment?.proxy?.download()?.get()
                fileToCheck = fileToSend?.let { FileUpload.fromData(it, attachment.fileName) }!!
                user.openPrivateChannel().queue { privateChannel ->
                    privateChannel.sendMessage("Archivo enviado.").queue()
                }
                messageWithFile?.delete()?.queue()
                canSendFile = false
                sendForm(event.guild!!)
            }
        }

    }
}