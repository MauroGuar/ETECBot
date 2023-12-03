package com.etec_bot.event_handler

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.entities.channel.Channel
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color
import java.util.concurrent.TimeUnit

class ForumSuggestion : ListenerAdapter() {
    private lateinit var guild: Guild
    private lateinit var user: User
    private lateinit var member: Member
    private lateinit var message: Message
    private lateinit var channel: Channel
    private lateinit var channelUnion: MessageChannelUnion
    private lateinit var reaction: MessageReaction
    override fun onMessageReceived(event: MessageReceivedEvent) {
        try {
            guild = event.guild
        } catch (e: IllegalStateException) {}
        user = event.author
        message = event.message
        channelUnion = event.channel

        forumMessageSuggestion()
        forumSuggestionAdminReaction()
    }

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {
        guild = event.guild
        user = event.user!!
        channel = event.channel
        reaction = event.reaction

        forumSuggestionAdminCheck(event.messageId)
    }

    private fun forumMessageSuggestion() {
        if (channelUnion.type == ChannelType.TEXT) {
            val textChannel = channelUnion.asTextChannel()
            if (guild.getTextChannelById("1115642424493084723")
                    ?.let { checkTextChannel(textChannel, it) } == true && !(user.isBot)
            ) {
                member = guild.getMember(user)!!

                if (checkRole(
                        member.roles,
                        listOf(
                            // Administrator and Bot roles IDs
                            guild.getRoleById("1115247455701848136"),
                            guild.getRoleById("1115249881074897006")
                        )
                    ) == null
                ) {
                    reactMessageCheck(message)
                    deleteMessageTimer(message, 3)
                    sendAdminForumMessage("1115646677450244196", message)
                    privateMemberRecognitionMsg()
                }

            }
        }
    }

    private fun forumSuggestionAdminReaction() {
        if (channelUnion.type == ChannelType.TEXT) {
            val textChannel = channelUnion.asTextChannel()
            if (guild.getTextChannelById("1115646677450244196")
                    ?.let { checkTextChannel(textChannel, it) } == true && user.isBot
            ) {
                member = guild.getMember(user)!!
                if (guild.getRoleById("1149492194139447360")?.let { checkRole(member.roles, it) } != null) {
                    message.addReaction(Emoji.fromCustom("finished", 1149734416432767028, false)).queue()
                }
            }
        }
    }

    private fun forumSuggestionAdminCheck(messageId: String) {
        if (channel.type == ChannelType.TEXT) {
            val textChannel = channel as TextChannel
            if (guild.getTextChannelById("1115646677450244196")
                    ?.let { checkTextChannel(textChannel, it) } == true && !(user.isBot)
            ) {
                member = guild.getMember(user)!!

                if (reaction.emoji == Emoji.fromCustom(Emoji.fromCustom("finished", 1149734416432767028, false))) {
                    TimeUnit.SECONDS.sleep(2)
                    textChannel.deleteMessageById(messageId).queue()
                }
            }
        }
    }

    private fun checkTextChannel(channel: TextChannel, channelToCheck: TextChannel): Boolean {
        return channel == channelToCheck
    }

    private fun checkRole(userRoles: List<Role>, rolesToChecks: List<Role?>): List<Role>? {
        val commonRoles = userRoles.filter { rolesToChecks.contains(it) }
        return commonRoles.ifEmpty { null }
    }

    private fun checkRole(userRoles: List<Role>, roleToCheck: Role): Role? {
        return userRoles.find { it == roleToCheck }
    }

    private fun reactMessageCheck(message: Message) {
        message.addReaction(Emoji.fromCustom("check_mark", 1149711680771735694, false)).queue()
    }

    private fun deleteMessageTimer(message: Message, timeSeconds: Long) {
        TimeUnit.SECONDS.sleep(timeSeconds)
        message.delete().queue()
    }

    private fun makeForumEmbed(message: Message): MessageEmbed {
        val embed = EmbedBuilder()
        embed.setAuthor(message.contentDisplay)
        embed.setColor(Color(0xffe882))
        return embed.build()
    }

    private fun sendAdminForumMessage(textChannelId: String, message: Message) {
        guild.getTextChannelById(textChannelId)?.sendMessageEmbeds(makeForumEmbed(message))?.queue()
            ?: println("\u001b[31m" + "Text Channel Not Found")
    }

    private fun privateMemberRecognitionMsg() {
        user.openPrivateChannel().queue { channel: PrivateChannel ->
            channel.sendMessage(
                "Hey, ¡gracias por la sugerencia! Pronto un administrador se hará cargo de ella."
            ).queue()
        }
    }
}