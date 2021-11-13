package me.falcon.abstractbot.commands;

import me.falcon.abstractbot.utils.Values;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class PingCommand extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if(!event.getAuthor().isBot()){
            String prefix = Values.getProperty("prefix",event);
            String[] rawMessage = event.getMessage().getContentRaw().split("\\s");
            if((rawMessage[0].equalsIgnoreCase(prefix+"ping"))) {
                if(Boolean.parseBoolean(Values.getProperty("hasRedirect" ,event))) {
                    if(Values.getProperty("redirectChannel" ,event).equalsIgnoreCase(event.getChannel().getId())){
                        execute(event);
                    }
                }else{
                    execute(event);
                }
            }
        }
    }
    private static void execute(GuildMessageReceivedEvent event){
            event.getChannel().sendMessage("Pong !!").queue(msg -> msg.editMessage("Pong "+event.getJDA().getGatewayPing()+"ms !!").queue());
    }
}
