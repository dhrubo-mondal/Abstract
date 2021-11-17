package me.falcon.abstractbot.utils;

import me.falcon.abstractbot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.*;
import java.util.Properties;

public class Values {

    public static String getProperty(String key , GuildMessageReceivedEvent event) {
        File file = new File(".//res//GUILD_FILES//" + event.getGuild().getId() + ".properties");
        String str = " ";
        Properties pr= new Properties();
        try {
            pr.load(new FileInputStream(file));
            str = pr.getProperty(key);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(Color.RED);
            error.setTitle("Sudden Unexpected error occurred");
            error.setDescription("The error should be fixed by the next time you issue a command");
            event.getChannel().sendMessage(error.build()).queue();
            error.clear();
            String[] keys = new String[]{"redirectChannel","prefix","hasRedirect"};
            String[] values = new String[]{"", Bot.prefix,"false"};
            Values.setProperty(keys, values ,event);
        }
        return str;
    }
    public static void setProperty(String[] keys, String[] values , GuildMessageReceivedEvent event) {
        if (keys.length == values.length) {
            File file = new File(".//res//GUILD_FILES//" + event.getGuild().getId() + ".properties");
            Properties pr = new Properties();
            try {
                for (int i = 0; i < keys.length; i++) {
                    pr.setProperty(keys[i], values[i]);
                }
                pr.store(new FileOutputStream(file), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void setProperty(String[] keys, String[] values , GuildJoinEvent event){
        if (keys.length == values.length) {
            File file = new File(".//res//GUILD_FILES//" + event.getGuild().getId() + ".properties");
            Properties pr = new Properties();
            try {
                for (int i = 0; i < keys.length; i++) {
                    pr.setProperty(keys[i], values[i]);
                }
                pr.store(new FileOutputStream(file), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
