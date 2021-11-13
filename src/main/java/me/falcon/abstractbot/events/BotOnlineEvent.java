package me.falcon.abstractbot.events;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotOnlineEvent extends ListenerAdapter{
    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Bot is now online");
        System.out.println("The bot is online in " + event.getGuildTotalCount() + " servers");
    }
}
