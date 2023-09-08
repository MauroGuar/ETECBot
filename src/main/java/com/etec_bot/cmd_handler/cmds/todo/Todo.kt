package com.etec_bot.cmd_handler.cmds.todo

import com.etec_bot.ETECBot
import com.etec_bot.cmd_handler.Cmd
import com.etec_bot.json_handler.JsonManager
import com.etec_bot.json_handler.TodoList
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import kotlin.math.cbrt

class Todo(etecBot: ETECBot) : Cmd(
    etecBot, "todo", "Comandos para administrar todo-lists.", null, linkedMapOf(
        SubcommandData("crear", "Crear una nueva lista.") to arrayListOf(
            OptionData(
                OptionType.STRING, "nombre", "Nombre de la lista.", true
            ),
            OptionData(OptionType.CHANNEL, "canal", "El canal donde estará la lista", true),
        ), SubcommandData("añadir", "Añadir una tarea a una lista.") to arrayListOf(
            OptionData(OptionType.STRING, "tarea", "La tarea en sí.", true)
        )
    )
) {
    override fun execute(slashEvent: SlashCommandInteractionEvent) {
//        slashEvent.deferReply().setEphemeral(true).queue()
        val todoListManager = JsonManager().TodoListManager(etecBot)

        when (slashEvent.subcommandName) {
            "crear" -> {
                val name = slashEvent.getOption("nombre")?.asString
                val channel = slashEvent.getOption("canal")?.asChannel

                if (name != null && channel != null && slashEvent.guild != null) {
                    if(todoListManager.create(TodoList(name, channel.id, slashEvent.guild!!.id, null))){
                        slashEvent.reply("¡Lista creada con éxito!").setEphemeral(true).queue()
                    } else {
                        slashEvent.reply("¡Ya existe una lista en este canal!").setEphemeral(true).queue()
                    }
                } else {
                    slashEvent.reply("¡Falta algún argumento!").setEphemeral(true).queue()
                }
            }

            "añadir" -> {
                val taskContent = slashEvent.getOption("tarea")?.asString
                val guildID = slashEvent.guild?.id
                val channelID = slashEvent.channel.id

                if (taskContent != null && guildID != null) {
                    if (todoListManager.addTask(guildID, channelID, taskContent)) {
                        slashEvent.reply("¡Tarea añadida con éxito!").setEphemeral(true).queue()
                    } else {
                        slashEvent.reply("¡La lista no existe!").setEphemeral(true).queue()
                    }
                } else {
                    slashEvent.reply("¡Falta algún argumento!").setEphemeral(true).queue()
                }
            }
        }
    }

    override fun autoCompletion(autoCompeteEvent: CommandAutoCompleteInteractionEvent) {}
}