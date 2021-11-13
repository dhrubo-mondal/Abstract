package me.falcon.abstractbot.commands;

import me.falcon.abstractbot.Bot;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ShutdownCommand extends ListenerAdapter {
    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        String prefix = Bot.prefix;
        String[] rawMessage = event.getMessage().getContentRaw().split("\\s");
        if(rawMessage[0].equalsIgnoreCase(prefix + "shutdown") && event.getAuthor().getId().equalsIgnoreCase(Bot.owner_id)){
            event.getChannel().sendMessage("Shutting Down the bot !!").complete();
            event.getJDA().shutdown();
            System.exit(0);
        }
    }
}
