package com.etec_bot.event_handler

import com.etec_bot.ETECBot
import com.etec_bot.cmd_handler.cmds.Embed
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter


class RoleAdd(private val bot: ETECBot) : ListenerAdapter() {
    private lateinit var guild: Guild
    private lateinit var user: User
    private lateinit var removedRoles: List<Role>
    private lateinit var channelToWelcome: TextChannel
    override fun onGuildMemberRoleAdd(event: GuildMemberRoleAddEvent) {
        guild = event.guild
        user = event.user
        removedRoles = event.roles
        channelToWelcome = guild.getTextChannelById("1115262679939026975")!!

        val removedRole = guild.getRoleById("1115270769841291416")?.let { CheckRole(removedRoles, it) }
        removedRole?.let {
            channelToWelcome.sendMessage("¡Bienvenido ${user.asMention} a la comunidad!").setEmbeds(WelcomeEmbed()).queue()
        }
    }

    private fun CheckRole(userRoles: List<Role>, rolesToChecks: List<Role>): List<Role>? {
        val commonRoles = userRoles.filter { rolesToChecks.contains(it) }
        return commonRoles.ifEmpty { null }
    }
    private fun CheckRole(userRoles: List<Role>, roleToCheck: Role): Role? {
        return userRoles.find { it == roleToCheck }
    }

    private fun WelcomeEmbed(): MessageEmbed {
        val welcomeEmbed = EmbedBuilder()
        val welcomeGifs = arrayListOf<String>(
            "https://media.tenor.com/oC8CSq25wx4AAAAC/baby-yoda-welcome.gif",
            "https://media.tenor.com/zuCAx-5Jg08AAAAC/welcome.gif",
            "https://media.tenor.com/7prGMxIzJv8AAAAC/minions-despicable-me.gif",
            "https://media.giphy.com/media/l4JyOCNEfXvVYEqB2/giphy.gif",
            "https://media.giphy.com/media/xUPGGDNsLvqsBOhuU0/giphy.gif",
            "https://media.giphy.com/media/FQyQEYd0KlYQ/giphy.gif",
            "https://media.giphy.com/media/ASd0Ukj0y3qMM/giphy.gif",
            "https://media.giphy.com/media/Id6dC0GQOOzPMXgcPv/giphy.gif"
        )
        welcomeEmbed.setImage(welcomeGifs[(0 until welcomeGifs.size).random()])
        welcomeEmbed.setFooter(
            "Esperamos que puedas encontrar nuevos amigos en este servidor", "https://plantillasdememes.com/img/plantillas/hombres-musculosos-apretandose-las-manos1.jpg"
        )
        return welcomeEmbed.build()
    }
}