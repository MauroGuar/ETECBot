package com.etec_bot.cmd_handler.cmds

import com.etec_bot.EDA.schemes.SchoolGrade
import com.etec_bot.ETECBot
import com.etec_bot.cmd_handler.Cmd
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData

class EDACmd(etecBot: ETECBot) : Cmd(
    etecBot, "ade", "Comandos para el manejo del ADE.", null, linkedMapOf(
        SubcommandData("añadir-materia", "Añade una materia.") to arrayListOf(
            OptionData(OptionType.STRING, "nombre", "Nombre de la materia", true),
            OptionData(OptionType.STRING, "grado", "Grado donde añadir la materia.", true, true)
        )
    ), true
) {
    override fun execute(slashEvent: SlashCommandInteractionEvent) {
        when (slashEvent.subcommandName) {
            "añadir-materia" -> {
                val subjectName = slashEvent.getOption("nombre")!!.asString
                val schoolGradeString = slashEvent.getOption("grado")!!.asString
                val schoolGrade = SchoolGrade.entries.find { it.value == schoolGradeString }
                schoolGrade?.let { etecBot.eda.addSubject(subjectName, it) }
                slashEvent.reply("Materia añadida exitosamente.").setEphemeral(true).queue()
            }
        }
    }

    override fun autoCompletion(autoCompleteEvent: CommandAutoCompleteInteractionEvent) {
        when (autoCompleteEvent.subcommandName) {
            "añadir-materia" -> {
                val schoolGrade = SchoolGrade.entries.map { it.value }
                autoCompleteEvent.replyChoiceStrings(schoolGrade.filter { it.startsWith(autoCompleteEvent.focusedOption.value) })
                    .queue()
            }
        }
    }
}