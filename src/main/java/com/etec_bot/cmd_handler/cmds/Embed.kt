package com.etec_bot.cmd_handler.cmds

import com.etec_bot.ETECBot
import com.etec_bot.cmd_handler.Cmd
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.awt.Color

class Embed(etecBot: ETECBot) : Cmd(etecBot, "embed", "Hacer embed.", args = null, null) {
    override fun execute(slashEvent: SlashCommandInteractionEvent) {
        slashEvent.deferReply().setEphemeral(false).queue()
        val eb1 = EmbedBuilder()
        eb1.setDescription("**1.** Sistema de tickets (sugerencias para el foro)")
        eb1.setColor(Color(0xff4444))
        eb1.setAuthor(slashEvent.user.globalName, null, "https://images.emojiterra.com/mozilla/512px/274c.png")

        val eb2 = EmbedBuilder()
//        eb2.setTitle("¡Bienvenido ${slashEvent.user.globalName} a la comunidad!")
        val gifs = arrayListOf<String>(
            "https://media.tenor.com/oC8CSq25wx4AAAAC/baby-yoda-welcome.gif",
            "https://media.tenor.com/zuCAx-5Jg08AAAAC/welcome.gif",
            "https://media.tenor.com/7prGMxIzJv8AAAAC/minions-despicable-me.gif",
            "https://media.giphy.com/media/l4JyOCNEfXvVYEqB2/giphy.gif",
            "https://media.giphy.com/media/xUPGGDNsLvqsBOhuU0/giphy.gif",
            "https://media.giphy.com/media/FQyQEYd0KlYQ/giphy.gif",
            "https://media.giphy.com/media/ASd0Ukj0y3qMM/giphy.gif",
            "https://media.giphy.com/media/Id6dC0GQOOzPMXgcPv/giphy.gif"
        )
        eb2.setImage(gifs[(0 until gifs.size).random()])
        eb2.setFooter(
            "Esperamos que puedas encontrar nuevos amigos en este servidor", "https://plantillasdememes.com/img/plantillas/hombres-musculosos-apretandose-las-manos1.jpg"
        )
        slashEvent.hook.sendMessage("¡Bienvenido ${slashEvent.user.asMention} a la comunidad!").setEmbeds(eb2.build()).queue()
    }

    override fun autoCompletion(autoCompleteEvent: CommandAutoCompleteInteractionEvent) {}
}