package me.falcon.abstractbot.commands;

import me.falcon.abstractbot.Bot;
import me.falcon.abstractbot.utils.Values;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class CustomPrefixCommand extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if(!event.getAuthor().isBot()){
            String prefix = Values.getProperty("prefix",event);
            String[] rawMessage = event.getMessage().getContentRaw().split("\\s");
            if(rawMessage[0].equalsIgnoreCase(prefix+"prefix") || rawMessage[0].equalsIgnoreCase(prefix+"prf")){
                if(Boolean.parseBoolean(Values.getProperty("hasRedirect" ,event))) {
                    if(Values.getProperty("redirectChannel" ,event).equalsIgnoreCase(event.getChannel().getId())){
                        execute(event,rawMessage,prefix);
                    }
                }else{
                    execute(event,rawMessage,prefix);
                }
            }
        }
    }

    private static void execute(GuildMessageReceivedEvent event,String[] rawMessage,String prefix){
        if(!(rawMessage.length < 2)){
            if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) || Objects.requireNonNull(event.getMember()).getId().equals(Bot.owner_id)) {
                String setPrefix = rawMessage[1];
                String redirectChannel = Values.getProperty("redirectChannel", event);
                String hasRedirect = Values.getProperty("hasRedirect", event);

                String[] keys = new String[]{"redirectChannel", "prefix", "hasRedirect"};
                String[] values = new String[]{redirectChannel, setPrefix, hasRedirect};
                Values.setProperty(keys, values, event);

                EmbedBuilder finalMessage = new EmbedBuilder();
                finalMessage.setColor(Color.GREEN);
                finalMessage.addField("Operation Completed", "Successfully set the prefix of the server as `" + Values.getProperty("prefix", event) + "`", false);
                event.getChannel().sendMessage(finalMessage.build()).queue();
                finalMessage.clear();
            }else{
                EmbedBuilder noMsg = new EmbedBuilder();
                noMsg.setColor(Color.RED);
                noMsg.setTitle("Permission required ");
                noMsg.setDescription("It seems you don't have the permission `ADMINISTRATOR`");
                event.getChannel().sendMessage(noMsg.build()).queue();
                noMsg.clear();
            }
        }else{
            EmbedBuilder help = new EmbedBuilder();
            help.setColor(new Color(158, 5, 162));
            help.addField("Usage","`"+prefix+"prefix <new prefix>` changes the prefix of the bot for the server",false);
            help.addField("Aliases", "`prefix | prf`",false);
            event.getChannel().sendMessage(help.build()).queue();
            help.clear();
        }
    }
}
