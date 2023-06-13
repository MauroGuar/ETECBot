package com.etec_bot;

import com.etec_bot.cmd_handler.CmdsManager;
import com.etec_bot.json_handler.JsonManager;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class ETECBot {
    //  Discord API Token and Servers Guilds
    private static String TOKEN;
    private String GUILD_ID;
    //  Bot JDA api
    public JDA api;

    public ETECBot() {
//      Loading .env values
        Dotenv dotenv = Dotenv.load();
        TOKEN = dotenv.get("DS_API_TOKEN");
        GUILD_ID = dotenv.get("GUILD_ID");

//      Building JDA api
        JDABuilder builder = JDABuilder.createDefault(TOKEN)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES, GatewayIntent.MESSAGE_CONTENT)
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.playing("/help"))
                .addEventListeners(new CmdsManager(this));
        try {
            api = builder.build().awaitReady();
        } catch (InterruptedException e) {
            System.out.println("\u001B[31m" + "A problem occurred building the JDA api.");
        }
    }
    public String getGUILD_ID() {
        return GUILD_ID;
    }

    public static void main(String[] args) {
        ETECBot etecBot = new ETECBot();
        JsonManager jsonManager = new JsonManager();
    }
}