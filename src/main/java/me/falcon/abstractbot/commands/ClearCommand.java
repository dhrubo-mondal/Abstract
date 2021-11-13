package me.falcon.abstractbot.commands;

import me.falcon.abstractbot.Bot;
import me.falcon.abstractbot.utils.Values;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ClearCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if(!event.getAuthor().isBot()) {
            String prefix = Values.getProperty("prefix" , event);
            String[] rawMessage = event.getMessage().getContentRaw().split("\\s");
            if ((rawMessage[0].equalsIgnoreCase(prefix + "clear")) || (rawMessage[0].equalsIgnoreCase(prefix + "clr"))) {
                if(!(rawMessage.length < 2)) {
                    if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.MESSAGE_MANAGE) || Objects.requireNonNull(event.getMember()).getId().equals(Bot.owner_id)) {
                        try {
                            List<Message> messages = event.getChannel().getHistory().retrievePast(Integer.parseInt(rawMessage[1])).complete();
                            for (int i = 0; i < messages.size(); i++) {
                                event.getChannel().deleteMessageById(messages.get(i).getId()).complete();
                                if (i == (messages.size() - 1)) {
                                    EmbedBuilder successful = new EmbedBuilder();
                                    successful.setColor(Color.GREEN);
                                    successful.setTitle("Operation successful");
                                    successful.setDescription("Successfully cleared " + Integer.parseInt(rawMessage[1]) + " messages");
                                    event.getChannel().sendMessage(successful.build()).queue(msg -> msg.delete().queueAfter(1000 , TimeUnit.MILLISECONDS));
                                    successful.clear();
                                }
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            String reason;
                            if (e.getMessage().startsWith("Message retrieval limit")) {
                                reason = "Operation Unsuccessful : Amount of messages must be from 1 to 100";
                            } else if(e.getMessage().startsWith("For input string")){
                                reason = "Operation Unsuccessful : Argument must be a number ";
                            }else{
                                reason = "Operation Unsuccessful : Message too old to be deleted";
                            }
                            EmbedBuilder exc = new EmbedBuilder();
                            exc.setColor(Color.RED);
                            exc.setTitle("Operation can't be performed ");
                            exc.setDescription(reason);
                            event.getChannel().sendMessage(exc.build()).queue();
                            exc.clear();
                        }
                    } else {
                        EmbedBuilder noMsg = new EmbedBuilder();
                        noMsg.setColor(Color.RED);
                        noMsg.setTitle("Permission required ");
                        noMsg.setDescription("It seems you don't have the permission `MANAGE_MESSAGE`");
                        event.getChannel().sendMessage(noMsg.build()).queue();
                        noMsg.clear();
                    }
                }else{
                    EmbedBuilder help = new EmbedBuilder();
                    help.setColor(new Color(158, 5, 162));
                    help.addField("Usage","`"+prefix+"clear <amount of messages>` clears the amount of messages",false);
                    help.addField("Aliases", "`clear | clr`",false);
                    event.getChannel().sendMessage(help.build()).queue();
                    help.clear();
                }
            }
        }
    }
}