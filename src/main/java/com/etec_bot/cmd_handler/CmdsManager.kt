package com.etec_bot.cmd_handler

import com.etec_bot.ETECBot
import com.etec_bot.cmd_handler.cmds.EDACmd
import com.etec_bot.cmd_handler.cmds.Embed
import com.etec_bot.cmd_handler.cmds.GenerateButton
import com.etec_bot.cmd_handler.cmds.Help
import com.etec_bot.cmd_handler.cmds.todo.Todo
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

class CmdsManager(bot: ETECBot) : ListenerAdapter() {
    private val cmds: ArrayList<Cmd> = arrayListOf()
    private val guildId = bot.guilD_ID

    init {
        cmds.addAll(arrayListOf(Help(bot), Todo(bot), Embed(bot), GenerateButton(bot), EDACmd(bot)))
    }

    private fun unpackCommandData(): List<CommandData> {
        val commandData = arrayListOf<CommandData>()
        cmds.forEach {
            if (it.subcommands != null) {
                val slashCommand: SlashCommandData = Commands.slash(it.name, it.description).setGuildOnly(it.guildOnly)
                it.subcommands.entries.forEach { entry ->
                    val subCmd = entry.key
                    val optDataArrList = entry.value
                    optDataArrList?.forEach { optData ->
                        subCmd.addOptions(optData)
                    }
                    slashCommand.addSubcommands(subCmd)
                }
                commandData.add(slashCommand)
            } else if (it.args == null) {
                val slashCommand: SlashCommandData = Commands.slash(it.name, it.description).setGuildOnly(it.guildOnly)
                commandData.add(slashCommand)
            } else {
                val slashCommand: SlashCommandData = Commands.slash(it.name, it.description).addOptions(it.args).setGuildOnly(it.guildOnly)
                commandData.add(slashCommand)
            }
        }
        return commandData
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        cmds.find {
            it.name == event.name
        }?.execute(event) ?: println("Null slash command")
    }

    override fun onCommandAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        cmds.find {
            it.name == event.name
        }?.autoCompletion(event) ?: println("Null slash command")
    }

    override fun onReady(event: ReadyEvent) {
        val cmdsData = unpackCommandData()
        event.jda.getGuildById(guildId)?.updateCommands()?.addCommands(cmdsData)?.queue()
//        event.jda.updateCommands().queue();
    }
}