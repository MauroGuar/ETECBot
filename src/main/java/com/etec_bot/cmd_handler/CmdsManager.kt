package com.etec_bot.cmd_handler

import com.etec_bot.ETECBot
import com.etec_bot.cmd_handler.cmds.Help
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

class CmdsManager(private val bot: ETECBot) : ListenerAdapter() {
    private val commands: ArrayList<Command> = arrayListOf();
    private val guildId = bot.guilD_ID
    init {
        commands.addAll(arrayListOf(Help(bot)))
    }

    private fun unpackCommandData(): List<CommandData> {
        val commandData = arrayListOf<CommandData>()
        commands.forEach {
            if (it.args == null) {
                val slashCommand: SlashCommandData = Commands.slash(it.name, it.description)
                commandData.add(slashCommand)
            } else {
                val slashCommand: SlashCommandData = Commands.slash(it.name, it.description).addOptions(it.args)
                commandData.add(slashCommand)
            }
        }
        return commandData
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        commands.find {
            it.name == event.name
        }?.execute(event) ?: println("Null slash command")
    }

    override fun onReady(event: ReadyEvent) {
        val cmdsData = unpackCommandData()
        cmdsData.forEach {
            event.jda.getGuildById(guildId)?.upsertCommand(it)?.queue()?: println("\u001B[31m" + "No command found")
        }
    }
}