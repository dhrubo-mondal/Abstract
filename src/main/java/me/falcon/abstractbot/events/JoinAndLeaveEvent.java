package me.falcon.abstractbot.events;

import me.falcon.abstractbot.Bot;
import me.falcon.abstractbot.utils.Values;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

public class JoinAndLeaveEvent extends ListenerAdapter {

    @SuppressWarnings("TextBlockMigration")
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        System.out.println("Joined in "+event.getGuild().getName()+" with id " + event.getGuild().getId());
        String[] keys = new String[]{"redirectChannel","prefix","hasRedirect"};
        String[] values = new String[]{"", Bot.prefix,"false"};
        Values.setProperty(keys, values ,event);
        EmbedBuilder message = new EmbedBuilder();
        message.setColor(new Color(3, 13, 217));
        message.addField("Thanks for inviting us ","Press `,help` for help commands",false);
        message.addField("Common Configurations ",
                "`,prefix <new prefix>` to set a new prefix \n"+
                "`,redirect <channel>` to redirect the bot command to a particular channel \n"+
                "`,config` to get the complete configurations for the server \n" , false);
        message.addField("Support Server", "Click here[link] to join our support server",false);
        List<TextChannel> txt = event.getGuild().getTextChannels();
        Objects.requireNonNull(event.getGuild().getTextChannelById(txt.get(0).getId())).sendMessage(message.build()).queue();
        message.clear();
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        System.out.println("Left from " + event.getGuild().getName()+" with id " + event.getGuild().getId());
        File file = new File(".//res//GUILD_FILES//"+event.getGuild().getId()+".properties");
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}