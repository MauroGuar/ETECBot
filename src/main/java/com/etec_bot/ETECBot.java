package com.etec_bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class ETECBot {
    public static void main(String[] args) {
        JDA api = JDABuilder.createDefault("MTExNjU1NjQyNDk2MTYwOTcyOQ.Guf_32.b0gaNZAs9vTaHePm82dgiZfmXMqCugTFDwPDvs").setActivity(Activity.playing("/help")).build();
    }
}