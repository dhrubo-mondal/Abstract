package me.falcon.abstractbot.commands;

import me.falcon.abstractbot.Bot;
import me.falcon.abstractbot.utils.Values;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class KickCommand extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if(!event.getAuthor().isBot()){
            String prefix = Values.getProperty("prefix",event);
            String[] rawMessage = event.getMessage().getContentRaw().split("\\s");
            if((rawMessage[0].equalsIgnoreCase(prefix+"kick")) || (rawMessage[0].equalsIgnoreCase(prefix+"kck"))) {
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
            if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.KICK_MEMBERS) || Objects.requireNonNull(event.getMember()).getId().equals(Bot.owner_id)) {
                try{
                    Member mentionedUser = event.getMessage().getMentionedMembers().get(0);
                    if(event.getGuild().getSelfMember().canInteract(mentionedUser)){
                        event.getGuild().kick(mentionedUser).queue();
                        EmbedBuilder help = new EmbedBuilder();
                        help.setColor(Color.GREEN);
                        help.addField("Operation Successful", "Successfully kicked " + mentionedUser.getEffectiveName(), false);
                        event.getChannel().sendMessage(help.build()).queue();
                        help.clear();
                    }else{
                        EmbedBuilder help = new EmbedBuilder();
                        help.setColor(Color.RED);
                        help.addField("Operation Unsuccessful","It seems that, I don't have permission to interact with the user", false);
                        event.getChannel().sendMessage(help.build()).queue();
                        help.clear();
                    }
                }catch(Exception e){
                    EmbedBuilder help = new EmbedBuilder();
                    help.setColor(Color.GREEN);
                    help.addField("Operation Unsuccessful", "Cannot kick the mentioned user", false);
                    event.getChannel().sendMessage(help.build()).queue();
                    help.clear();
                }
            }else{
                EmbedBuilder help = new EmbedBuilder();
                help.setColor(Color.RED);
                help.addField("Operation Unsuccessful","It seems that, you don't have permission `KICK_MEMBERS`", false);
                event.getChannel().sendMessage(help.build()).queue();
                help.clear();
            }
        }
        else {
            EmbedBuilder help = new EmbedBuilder();
            help.setColor(new Color(158, 5, 162));
            help.addField("Usage", "`" + prefix + "kick <user as mentioned> <message to be send to user>` kick the mentioned user", false);
            help.addField("Aliases", "`kick | kck`", false);
            event.getChannel().sendMessage(help.build()).queue();
            help.clear();
        }
    }
}