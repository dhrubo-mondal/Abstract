package me.falcon.abstractbot.commands;

import me.falcon.abstractbot.Bot;
import me.falcon.abstractbot.utils.Values;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class RedirectChannelCommand extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if(!event.getAuthor().isBot()){
            String prefix = Values.getProperty("prefix",event);
            String[] rawMessage = event.getMessage().getContentRaw().split("\\s");
            if((rawMessage[0].equalsIgnoreCase(prefix+"redirect")) || (rawMessage[0].equalsIgnoreCase(prefix+"rdt"))) {
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
        if (!(rawMessage.length < 2)) {
            if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR) || Objects.requireNonNull(event.getMember()).getId().equals(Bot.owner_id)) {
                try {
                    TextChannel rdtChannel = event.getMessage().getMentionedChannels().get(0);
                    String[] keys = new String[]{"redirectChannel", "prefix", "hasRedirect"};
                    String[] values = new String[]{rdtChannel.getId(), prefix, "true"};
                    Values.setProperty(keys, values, event);

                    EmbedBuilder finalMessage = new EmbedBuilder();
                    finalMessage.setColor(Color.GREEN);
                    finalMessage.addField("Operation Completed", "Successfully set the redirect channel of the server to `" + Objects.requireNonNull(event.getGuild().getTextChannelById(Values.getProperty("redirectChannel", event))).getName() + "`", false);
                    event.getChannel().sendMessage(finalMessage.build()).queue();
                    finalMessage.clear();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    if(rawMessage[1].equalsIgnoreCase("disable")){
                        String[] keys = new String[]{"redirectChannel", "prefix", "hasRedirect"};
                        String[] values = new String[]{"", prefix, "false"};
                        Values.setProperty(keys, values, event);

                        EmbedBuilder finalMessage = new EmbedBuilder();
                        finalMessage.setColor(Color.GREEN);
                        finalMessage.addField("Operation Completed", "Successfully disabled the redirect channel of the server", false);
                        event.getChannel().sendMessage(finalMessage.build()).queue();
                        finalMessage.clear();
                    }else {
                        EmbedBuilder errorMsg = new EmbedBuilder();
                        errorMsg.setColor(Color.GREEN);
                        errorMsg.addField("Operation Unsuccessful", "No mentioned channel found ", false);
                        event.getChannel().sendMessage(errorMsg.build()).queue();
                        errorMsg.clear();
                    }
                }
            } else {
                EmbedBuilder noMsg = new EmbedBuilder();
                noMsg.setColor(Color.RED);
                noMsg.setTitle("Permission required ");
                noMsg.setDescription("It seems you don't have the permission `ADMINISTRATOR`");
                event.getChannel().sendMessage(noMsg.build()).queue();
                noMsg.clear();
            }
        } else {
            EmbedBuilder help = new EmbedBuilder();
            help.setColor(new Color(158, 5, 162));
            help.addField("Usage", "`" + prefix + "redirect <redirect channel as mentioned>` restrict the bot command to the redirect channel | `" + prefix +"redirect disable` disables redirect", false);
            help.addField("Aliases", "`redirect | rdt`", false);
            event.getChannel().sendMessage(help.build()).queue();
            help.clear();
        }
    }
}