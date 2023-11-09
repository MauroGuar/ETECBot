package com.etec_bot.event_handler

import com.etec_bot.ETECBot
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter


class VerifiedRoleWelcome(private val bot: ETECBot) : ListenerAdapter() {
    private lateinit var guild: Guild
    private lateinit var user: User
    private lateinit var addedRoles: List<Role>
    private lateinit var channelToWelcome: TextChannel
    override fun onGuildMemberRoleAdd(event: GuildMemberRoleAddEvent) {
        guild = event.guild
        user = event.user
        addedRoles = event.roles
        channelToWelcome = guild.getTextChannelById("1115262679939026975")!!

        val addedRole = guild.getRoleById("1115270769841291416")?.let { checkRole(addedRoles, it) }
        addedRole?.let {
            channelToWelcome.sendMessage("¡Bienvenido/a ${user.asMention} a la comunidad!").setEmbeds(welcomeEmbed()).queue()
        }
    }

    private fun checkRole(userRoles: List<Role>, rolesToChecks: List<Role>): List<Role>? {
        val commonRoles = userRoles.filter { rolesToChecks.contains(it) }
        return commonRoles.ifEmpty { null }
    }
    private fun checkRole(userRoles: List<Role>, roleToCheck: Role): Role? {
        return userRoles.find { it == roleToCheck }
    }

    private fun welcomeEmbed(): MessageEmbed {
        val welcomeEmbed = EmbedBuilder()
        val welcomeGifs = arrayListOf<String>(
            "https://media.tenor.com/oC8CSq25wx4AAAAC/baby-yoda-welcome.gif",
            "https://media.tenor.com/zuCAx-5Jg08AAAAC/welcome.gif",
            "https://media.tenor.com/7prGMxIzJv8AAAAC/minions-despicable-me.gif",
            "https://media.giphy.com/media/l4JyOCNEfXvVYEqB2/giphy.gif",
            "https://media.giphy.com/media/xUPGGDNsLvqsBOhuU0/giphy.gif",
            "https://media.giphy.com/media/FQyQEYd0KlYQ/giphy.gif",
            "https://media.giphy.com/media/ASd0Ukj0y3qMM/giphy.gif",
            "https://media.giphy.com/media/Id6dC0GQOOzPMXgcPv/giphy.gif",
            "https://media.giphy.com/media/12B39IawiNS7QI/giphy.gif",
            "https://media.giphy.com/media/3ornk57KwDXf81rjWM/giphy.gif",
            "https://media.giphy.com/media/hTruzAjXFLEiTNBbwx/giphy.gif",
            "https://media.tenor.com/AvEF5AMuxLgAAAAC/h1-hello1.gif"
        )
        welcomeEmbed.setImage(welcomeGifs[(0 until welcomeGifs.size).random()])
        welcomeEmbed.setFooter(
            "Esperamos que puedas encontrar nuevos amigos en este servidor", "https://plantillasdememes.com/img/plantillas/hombres-musculosos-apretandose-las-manos1.jpg"
        )
        return welcomeEmbed.build()
    }
}