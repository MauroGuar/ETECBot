package com.etec_bot.event_handler

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

class EducationalDocumentArchive : ListenerAdapter() {
    private val CHANNEL_ID = "1172165721334227066"
    private fun startForm(guild: Guild, user: User, event: Event, codePart: Int = 1) {
        val channel = guild.getTextChannelById(CHANNEL_ID)

        if (user.isBot) return
        if (codePart == 1) {
            val subjectModal = createSubjectModal()
            val buttonInteraction = event as ButtonInteractionEvent
            buttonInteraction.replyModal(subjectModal).queue()
        } else if (codePart == 2) {
            val modalInteraction = event as ModalInteractionEvent
            val selectGrade = StringSelectMenu.create("select-grade").apply {
                addOption("6to I", "6to I")
                addOption("6to E", "6to E")
            }.build()
            modalInteraction.reply("Selecciona el año y división").setEphemeral(true).addActionRow(selectGrade).queue()
        } else {
            val stringSelectInteraction = event as StringSelectInteractionEvent
            val grade = stringSelectInteraction.values[0]
            stringSelectInteraction.reply("Ahora sube el archivo").setEphemeral(true).queue()
        }
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
            startForm(event.guild!!, event.user, event, 2)
        }
    }

    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        if (event.componentId == "select-grade") {
            startForm(event.guild!!, event.user, event, 3)
        }
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.channel.id == CHANNEL_ID) {
            val channel = event.channel
            val user = event.author
            val message = event.message
            if (message.attachments.isEmpty()) {
                message.delete().queue()
                user.openPrivateChannel().queue { privateChannel ->
                    privateChannel.sendMessage("Debes subir un archivo.").queue()
                }
            }
        }
    }
}