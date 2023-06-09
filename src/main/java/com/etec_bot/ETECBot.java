package com.etec_bot;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class ETECBot {
//  Discord API Token
    private static String TOKEN;
//  Bot JDA api
    public JDA api;
    public ETECBot() {
//      Loading .env values
        Dotenv dotenv = Dotenv.load();
        TOKEN = dotenv.get("DS_API_TOKEN");

//      Building JDA api
        JDABuilder builder = JDABuilder.createDefault(TOKEN)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES, GatewayIntent.MESSAGE_CONTENT)
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.playing("/help"));
        try {
            api = builder.build().awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ETECBot etecBot = new ETECBot();
    }
}