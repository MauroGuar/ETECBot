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

class Todo(etecBot: ETECBot) : Cmd(
    etecBot, "todo", "Comandos para administrar todo-lists.", null, linkedMapOf(
        SubcommandData("crear", "Crear una nueva lista.") to arrayListOf(
            OptionData(
                OptionType.STRING, "nombre", "Nombre de la lista.", true
            ),
            OptionData(OptionType.CHANNEL, "canal", "El canal donde estará la lista", true),
        )
    )
) {
    override fun execute(slashEvent: SlashCommandInteractionEvent) {
        slashEvent.deferReply().setEphemeral(true).queue()
        val todoListManager = JsonManager().TodoListManager()

        when (slashEvent.subcommandName) {
            "crear" -> {
                val name = slashEvent.getOption("nombre")?.asString
                val channel = slashEvent.getOption("canal")?.asChannel

                if (name != null && channel != null && slashEvent.guild != null) {
                    todoListManager.create(TodoList(name, channel.id, slashEvent.guild!!.id, null))
                    slashEvent.hook.sendMessage("¡Lista creada con éxito!").queue()
                } else {
                    slashEvent.hook.sendMessage("¡Falta algún argumento!").queue()
                }
            }
        }
    }

    override fun autoCompletion(autoCompeteEvent: CommandAutoCompleteInteractionEvent) {}
}